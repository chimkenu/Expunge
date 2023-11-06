package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.game.BreakGlass;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.game.PlayerStats;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class ShoveListener extends GameListener {

    public ShoveListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    private boolean canShove(Player player) {
        int cooldown = localGameManager.getPlayerStat(player).getNumberOfRecentShoves() > 7 ? 3 : 1;
        return ((System.currentTimeMillis() - localGameManager.getPlayerStat(player).getTimeSinceLastShove()) > (cooldown * 1000));
    }

    private boolean onShove(Player attacker) {
        if (!attacker.getGameMode().equals(GameMode.ADVENTURE)) {
            return false;
        }
        if (!canShove(attacker)) {
            return false;
        }

        PlayerStats playerStats = localGameManager.getPlayerStat(attacker);

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
        BreakGlass.breakGlass(loc.getBlock());

        for (Entity entity : attacker.getWorld().getNearbyEntities(loc, 1.5, 1.5, 1.5)) {
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity instanceof Player) {
                    continue;
                }
                if (livingEntity instanceof ArmorStand) {
                    continue;
                }

                // is a shove-able creature
                livingEntity.damage(2, attacker);
                if (livingEntity.getVehicle() != attacker) livingEntity.leaveVehicle();
                livingEntity.setVelocity(livingEntity.getVelocity().add(attacker.getLocation().getDirection().setY(0).multiply(0.5)));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 3, 9, false, false, false));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 9, false, false, false));
            }
        }
        return true;
    }

    @EventHandler
    public void onShove(PlayerInteractEvent e) {
        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_AIR) {
            return;
        }
        boolean isSuccessful = onShove(e.getPlayer());
        if (isSuccessful) e.setCancelled(true);
    }

    @EventHandler
    public void onShove(PlayerInteractEntityEvent e) {
        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (e.getRightClicked() instanceof FallingBlock fallingBlock && fallingBlock.getScoreboardTags().contains("AMMO_PILE")) {
            e.setCancelled(true);
            return;
        }
        if (e.getPlayer().getPassengers().size() > 0) {
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
