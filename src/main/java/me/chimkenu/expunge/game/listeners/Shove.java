package me.chimkenu.expunge.game.listeners;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Shove implements Listener {
    private final HashMap<Player, Long> shove = new HashMap<>();
    private final HashMap<Player, Integer> shoveCount = new HashMap<>();
    private boolean canShove(Player player) {
        shove.putIfAbsent(player, System.currentTimeMillis() - 3001);
        shoveCount.putIfAbsent(player, 0);

        int cooldown = shoveCount.get(player) > 7 ? 3 : 1;

        return ((System.currentTimeMillis() - shove.get(player)) > (cooldown * 1000));
    }

    private boolean onShove(Player attacker) {
        if (!attacker.getGameMode().equals(GameMode.ADVENTURE)) {
            return false;
        }
        if (!canShove(attacker)) {
            return false;
        }

        shoveCount.put(attacker, shoveCount.get(attacker) + 1);
        if (System.currentTimeMillis() - shove.get(attacker) > 6000) {
            shoveCount.put(attacker, 0);
        }
        shove.put(attacker, System.currentTimeMillis());

        int cooldown = shoveCount.get(attacker) > 7 ? 3 : 1;
        attacker.playSound(attacker.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, SoundCategory.PLAYERS, 1f, 1f);
        attacker.playSound(attacker.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.PLAYERS, 0.2f, 1f);
        attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * cooldown, 4, false, false, true));

        Location loc = attacker.getEyeLocation().add(attacker.getLocation().getDirection().multiply(1.5));
        attacker.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 1);
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
                livingEntity.setVelocity(livingEntity.getVelocity().add(attacker.getLocation().getDirection().setY(0).multiply(0.5)));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 3, 9, false, false, false));
                livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 3, 9, false, false, false));
            }
        }
        return true;
    }

    @EventHandler
    public void onShove(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            boolean isSuccessful = onShove(e.getPlayer());
            if (isSuccessful) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onShove(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand armorStand && armorStand.getScoreboardTags().contains("AMMO_PILE")) {
            e.setCancelled(true);
            return;
        }
        boolean isSuccessful = onShove(e.getPlayer());
        if (isSuccessful) e.setCancelled(true);
    }
}
