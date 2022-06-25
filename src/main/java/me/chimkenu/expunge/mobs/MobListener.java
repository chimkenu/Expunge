package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.Expunge;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class MobListener implements Listener {
    @EventHandler
    public void onMobTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player target)) {
            e.setCancelled(true);
            return;
        }
        if (target.getGameMode() != GameMode.ADVENTURE) {
            e.setCancelled(true);
            return;
        }
        // common infected tags
        Entity entity = e.getEntity();
        if (entity.getScoreboardTags().contains("WANDERER")) {
            if (e.getEntity().getLocation().distanceSquared(target.getLocation()) < 6 * 6)
                e.setCancelled(true);
            else
                e.getEntity().removeScoreboardTag("WANDERER");
        } else if (entity.getScoreboardTags().contains("WANDERER?")) e.getEntity().removeScoreboardTag("WANDERER?");
    }

    // BOOMER
    private void boom(Entity explode) {
        World world = explode.getWorld();
        world.spawnParticle(Particle.EXPLOSION_HUGE, explode.getLocation(), 1);
        world.playSound(explode.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.HOSTILE, 1, 1);
        // damage players
        for (Entity entity : world.getNearbyEntities(explode.getLocation(), 4, 4, 4)) {
            if (!(entity instanceof LivingEntity livingEntity)) {
                continue;
            }
            if (livingEntity instanceof ArmorStand) {
                continue;
            }
            if (livingEntity instanceof Player player && player.getGameMode() == GameMode.ADVENTURE) {
                player.damage(2, explode);
                player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 15, 0, false, true, false));
                world.spawnParticle(Particle.BLOCK_CRACK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
                if (Expunge.runningDirector != null) Expunge.runningDirector.bile(player, 25);
            }
        }
        explode.remove();
    }
    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getScoreboardTags().contains("BOOMER")) {
            boom(e.getEntity());
        }
    }

    // ANY MOB THAT DISABLES
    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {
        if (e.getPlayer().getPassengers().size() > 0) {
            e.setCancelled(true);
        }
    }

    // MOB EFFECTS
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player) || !(e.getDamager() instanceof LivingEntity damager) || e.getDamage() < 0.25) {
            return;
        }
        if (damager.getScoreboardTags().contains("CHARGER")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 2, false, false, true));
            if (damager.hasPotionEffect(PotionEffectType.CONFUSION) && player.getVehicle() == null) {
                disable(player);
                damager.removePotionEffect(PotionEffectType.CONFUSION);
            }
        }
    }

    // GETTING DISABLED
    public static void disable(Player player) {
        Location loc = player.getLocation();
        while (loc.getBlock().getType().equals(Material.AIR)) {
            loc.subtract(0, 0.25, 0);
        }

        loc.setY(loc.getBlock().getBoundingBox().getMaxY());
        ArmorStand armorStand = player.getWorld().spawn(loc, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.addScoreboardTag("KNOCKED");
        armorStand.addPassenger(player);
    }

    @EventHandler
    public void onPlayerTryToGetUp(EntityDismountEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (!(e.getDismounted() instanceof ArmorStand armorStand)) {
            return;
        }
        if (!armorStand.getScoreboardTags().contains("KNOCKED")) {
            return;
        }
        if (player.getNoDamageTicks() < 1 || player.getGameMode() != GameMode.ADVENTURE) {
            armorStand.remove();
            player.teleport(player.getLocation().add(0, 1, 0));
        } else {
            e.setCancelled(true);
        }
    }
}
