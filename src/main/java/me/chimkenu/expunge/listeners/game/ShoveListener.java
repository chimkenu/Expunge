package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class ShoveListener extends GameListener {
    private final int MAX_SHOVES = 7;
    private final int SHOVE_COOLDOWN_SHORT = 1000;
    private final int SHOVE_COOLDOWN_LONG = 3000;
    private final int SHOVE_RESET_TIME = 6000;

    private final BreakGlassListener breakGlassListener;
    private final Map<Player, ShoveData> shoveData;

    public ShoveListener(Expunge plugin, GameManager gameManager, BreakGlassListener breakGlassListener) {
        super(plugin, gameManager);
        this.breakGlassListener = breakGlassListener;
        this.shoveData = new HashMap<>();
    }

    private boolean canShove(Player player) {
        shoveData.putIfAbsent(player, new ShoveData());
        var data = shoveData.get(player);
        int cooldown = data.numberOfRecentShoves > MAX_SHOVES ? SHOVE_COOLDOWN_LONG : SHOVE_COOLDOWN_SHORT;
        return (System.currentTimeMillis() - data.timeSinceLastShove) > cooldown;
    }

    private boolean onShove(Player attacker) {
        if (!attacker.getGameMode().equals(GameMode.ADVENTURE)) {
            return false;
        }
        if (!canShove(attacker)) {
            return false;
        }

        var data = shoveData.getOrDefault(attacker, new ShoveData());
        data.numberOfRecentShoves++;
        if (System.currentTimeMillis() - data.timeSinceLastShove > SHOVE_RESET_TIME) {
            data.numberOfRecentShoves = 0;
        }
        data.timeSinceLastShove = System.currentTimeMillis();
        shoveData.put(attacker, data);

        int cooldown = data.numberOfRecentShoves > MAX_SHOVES ? SHOVE_COOLDOWN_LONG : SHOVE_COOLDOWN_SHORT;
        attacker.setCooldown(attacker.getInventory().getItemInMainHand().getType(), 1);
        attacker.playSound(attacker.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 0.2f, 1f);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * cooldown / 1000, 4, false, false, true));

        Location loc = attacker.getEyeLocation().add(attacker.getLocation().getDirection().multiply(1.5));
        attacker.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);

        // check for glass to break
        var block = attacker.getTargetBlockExact(4);
        if (block != null) {
            breakGlassListener.breakGlass(block);
        }

        for (Entity entity : attacker.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity instanceof Player player) {
                    for (Entity e : player.getPassengers()) {
                        if (e.getVehicle() != attacker) e.leaveVehicle();
                    }
                    continue;
                }
                if (livingEntity instanceof ArmorStand) {
                    continue;
                }

                boolean knockBack = true;
                double damage = 8;

                // is a shove-able creature (with a few exceptions)
                if (livingEntity.getScoreboardTags().contains("BOOMER")) {
                    damage = 0;
                }
                if (livingEntity.getScoreboardTags().contains("TANK") || livingEntity.getScoreboardTags().contains("CHARGER") || livingEntity.getScoreboardTags().contains("WITCH")) {
                    damage = 0;
                    knockBack = false;
                }

                if (damage > 0) livingEntity.damage(damage, attacker);
                if (knockBack) {
                    livingEntity.setVelocity(livingEntity.getVelocity().add(attacker.getLocation().getDirection().setY(0).multiply(0.6)));
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 3, 9, false, false, false));
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 3, 9, false, false, false));
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onShove(PlayerInteractEvent e) {
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("GLASS")))) {
            return;
        }
        boolean isSuccessful = onShove(e.getPlayer());
        if (isSuccessful) e.setCancelled(true);
    }

    @EventHandler
    public void onShove(PlayerInteractEntityEvent e) {
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (e.getRightClicked() instanceof FallingBlock fallingBlock && fallingBlock.getScoreboardTags().contains("AMMO_PILE")) {
            e.setCancelled(true);
            return;
        }
        if (!e.getPlayer().getPassengers().isEmpty()) {
            e.setCancelled(true);
            return;
        }
        if (e.getPlayer().getVehicle() != null) {
            e.setCancelled(true);
            return;
        }
        boolean isSuccessful = onShove(e.getPlayer());
        if (isSuccessful) e.setCancelled(true);
    }

    private static class ShoveData {
        public long timeSinceLastShove;
        public int numberOfRecentShoves;
        public ShoveData() {
            timeSinceLastShove = 0;
            numberOfRecentShoves = 0;
        }
    }
}
