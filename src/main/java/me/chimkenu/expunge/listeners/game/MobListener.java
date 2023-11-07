package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.guns.utilities.throwable.FreshAir;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

public class MobListener extends GameListener {
    public MobListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    @EventHandler(priority = EventPriority.HIGH)
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
            if (e.getEntity().getLocation().distanceSquared(target.getLocation()) < 7 * 7)
                e.setCancelled(true);
            else
                e.getEntity().removeScoreboardTag("WANDERER");
        }
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
                localGameManager.getDirector().bile(plugin, player, 25);
            }
        }
        explode.remove();
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e) {
        if (e.getEntity().getScoreboardTags().contains("BOOMER")) {
            boom(e.getEntity());
        }
        else if (e.getEntity().getScoreboardTags().contains("ROBOT")) {
            Item item = e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new FreshAir().getUtility());
            item.addScoreboardTag("ITEM");
        }

        // achievement
        else if (e.getEntity().getKiller() != null && e.getEntity().getScoreboardTags().contains("JOCKEY") && e.getEntity().getVehicle() instanceof Player) {
            Achievements.PEST_CONTROL.grant(e.getEntity().getKiller());
        }
    }

    // Tank takes more damage on fire
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity().getScoreboardTags().contains("TANK") && e.getEntity().getFireTicks() > 0) {
            e.setDamage(e.getDamage() * 5);
        }
    }

    // Stop mobs from getting set on fire by the sun
    @EventHandler(priority = EventPriority.LOW)
    public void onCombust(EntityCombustEvent e) {
        if (e.getEntity() instanceof Mob) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onSwitch(PlayerItemHeldEvent e) {
        if (e.getPlayer().getPassengers().size() > 0) {
            e.setCancelled(true); // This stops you from switching between items while you're being disabled by a mob
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        // Can't deal damage to mobs that are disabling you
        if ((e.getDamager() instanceof Player) && e.getEntity().getPassengers().contains(e.getDamager()) || e.getDamager().getPassengers().contains(e.getEntity())) {
            e.setCancelled(true);
            return;
        }

        if (!(e.getEntity() instanceof Player player) || !(e.getDamager() instanceof LivingEntity damager)) {
            return;
        }
        if (e.getDamage() <= 0 || e.isCancelled()) {
            return;
        }

        // Hit delay:
        player.setNoDamageTicks(30);
        new BukkitRunnable() {
            int i = 10;
            final Vector v = new Vector();
            @Override
            public void run() {
                player.setVelocity(v);
                if (i <= 0) this.cancel();
                i--;
            }
        }.runTaskTimer(plugin, 0, 1);

        // charger knocks down player
        if (damager.getScoreboardTags().contains("CHARGER")) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 2, false, false, true));
            if (damager.hasPotionEffect(PotionEffectType.CONFUSION) && player.getVehicle() == null) {
                disable(player);
                damager.removePotionEffect(PotionEffectType.CONFUSION);
            }
        }
    }

    public static void disable(Player player) {
        Location loc = player.getLocation();
        while (loc.getBlock().getType().equals(Material.AIR)) {
            loc.subtract(0, 0.25, 0);
        }

        loc.setY(loc.getBlock().getBoundingBox().getMaxY() - 0.95);
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
        if ((player.getNoDamageTicks() < 1 && player.getPassengers().size() < 1) || player.getGameMode() != GameMode.ADVENTURE) {
            armorStand.remove();
            player.teleport(player.getLocation().add(0, 1, 0));
        } else {
            e.setCancelled(true);
        }
    }
}