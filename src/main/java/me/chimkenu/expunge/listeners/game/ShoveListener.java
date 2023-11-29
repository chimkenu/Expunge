package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShoveListener extends GameListener {
    private final BreakGlassListener breakGlassListener;

    public ShoveListener(JavaPlugin plugin, GameManager gameManager, BreakGlassListener breakGlassListener) {
        super(plugin, gameManager);
        this.breakGlassListener = breakGlassListener;
    }

    private boolean canShove(Player player) {
        int cooldown = gameManager.getPlayerStat(player).getNumberOfRecentShoves() > 7 ? 3 : 1;
        return ((System.currentTimeMillis() - gameManager.getPlayerStat(player).getTimeSinceLastShove()) > (cooldown * 1000));
    }

    private boolean onShove(Player attacker) {
        if (!attacker.getGameMode().equals(GameMode.ADVENTURE)) {
            return false;
        }
        if (!canShove(attacker)) {
            return false;
        }

        PlayerStats playerStats = gameManager.getPlayerStat(attacker);

        playerStats.setNumberOfRecentShoves(playerStats.getNumberOfRecentShoves() + 1);
        if (System.currentTimeMillis() - playerStats.getTimeSinceLastShove() > 6000) {
            playerStats.setNumberOfRecentShoves(0);
        }
        playerStats.setTimeSinceLastShove(System.currentTimeMillis());

        attacker.setCooldown(attacker.getInventory().getItemInMainHand().getType(), 1);
        int cooldown = playerStats.getNumberOfRecentShoves() > 7 ? 3 : 1;
        attacker.playSound(attacker.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 0.2f, 1f);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * cooldown, 4, false, false, true));

        Location loc = attacker.getEyeLocation().add(attacker.getLocation().getDirection().multiply(1.5));
        attacker.getWorld().spawnParticle(Particle.SWEEP_ATTACK, loc, 1);

        // check for glass to break
        breakGlassListener.breakGlass(loc.getBlock());

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
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 9, false, false, false));
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
}
