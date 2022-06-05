package me.chimkenu.expunge.guns.shoot;

import me.chimkenu.expunge.guns.HeadshotCalculator;
import me.chimkenu.expunge.guns.RayTrace;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ShootParticle {

    private static final double ACCURACY_PARTICLES = 0.3;
    private static final double ACCURACY_TRUE = 0.1;

    public static void shoot(Particle particle, int range, double damage, Player shooter, int entitiesToHit, double offset, boolean decreaseDamage) {
        Vector direction = shooter.getEyeLocation().getDirection();
        direction = direction.add(new Vector(ThreadLocalRandom.current().nextDouble(-offset, offset),ThreadLocalRandom.current().nextDouble(-offset, offset), ThreadLocalRandom.current().nextDouble(-offset, offset)));
        RayTrace ray = new RayTrace(shooter.getEyeLocation().toVector(), direction);
        ArrayList<Vector> positions = ray.traverse(range, ACCURACY_PARTICLES);
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = shooter.getWorld();
        int wallsThrough = ThreadLocalRandom.current().nextInt(1, 4);
        for (Vector v : positions) {
            world.spawnParticle(particle, v.toLocation(world), 1, 0, 0, 0, 0);

            for (Entity e : world.getNearbyEntities(v.toLocation(world), ACCURACY_TRUE, ACCURACY_TRUE, ACCURACY_TRUE)) {
                if (e instanceof LivingEntity livingEntity) {
                    if (shooter == livingEntity) {
                        continue;
                    }
                    if (entities.contains(livingEntity)) {
                        continue;
                    }
                    if (livingEntity instanceof Player player && player.getGameMode() != GameMode.ADVENTURE) {
                        continue;
                    }
                    if (ray.intersects(e.getBoundingBox(), range, ACCURACY_TRUE) && !e.getType().equals(EntityType.ARMOR_STAND)) {
                        entities.add(livingEntity);
                    }
                }
            }

            if (entities.size() >= entitiesToHit) break;
            if (ray.intersects(world.getBlockAt(v.toLocation(world)).getBoundingBox(), range, ACCURACY_TRUE)) {
                wallsThrough--;
                if (wallsThrough <= 0) break;
            }
        }

        for (LivingEntity e : entities) {
            double newDMG = (decreaseDamage && e.getLocation().distance(shooter.getLocation()) > 7) ? damage * 0.5 : damage;
            Vector vec = e.getVelocity();
            if (HeadshotCalculator.isHeadshot(shooter, e, range)) {
                e.damage(newDMG + (newDMG * 0.5), shooter);
                shooter.sendMessage(ChatColor.GOLD + "Headshot!");
            } else {
                e.damage(newDMG, shooter);
            }
            e.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
            e.setNoDamageTicks(0);
            e.setVelocity(vec);
        }
    }
}
