package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.events.ShootEvent;
import me.chimkenu.expunge.utils.RayTrace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ShootParticle {
    private static final double ACCURACY_PARTICLES = 0.3;
    private static final double ACCURACY_TRUE = 0.1;

    public static Set<Block> shoot(Particle particle, int range, double damage, Player shooter, int entitiesToHit, double offset, boolean decreaseDamage) {
        Vector direction = shooter.getEyeLocation().getDirection();
        if (offset > 0) direction = direction.add(new Vector(ThreadLocalRandom.current().nextDouble(-offset, offset),ThreadLocalRandom.current().nextDouble(-offset, offset), ThreadLocalRandom.current().nextDouble(-offset, offset)));
        RayTrace ray = new RayTrace(shooter.getEyeLocation().toVector(), direction);
        ArrayList<Vector> positions = ray.traverse(range, ACCURACY_PARTICLES);
        ArrayList<LivingEntity> entities = new ArrayList<>();
        World world = shooter.getWorld();
        int wallsThrough = ThreadLocalRandom.current().nextInt(1, 4);
        boolean hasSpoken = false; // boolean for achievement - so that it does not trigger more than once per shot

        Set<Block> blocks = new HashSet<>();

        for (int i = 0; i < positions.size(); i++) {
            Vector v = positions.get(i);
            // so the particle does not obscure vision
            if (i > 4) world.spawnParticle(particle, v.toLocation(world), 1, 0, 0, 0, 0);

            // check for glass to break
            blocks.add(v.toLocation(world).getBlock());

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

                    // achievement
                    if (livingEntity instanceof ArmorStand armorStand && !hasSpoken) {
                        if (armorStand.getScoreboardTags().contains("ZOEY")) {
                            world.getPlayers().forEach(player -> player.sendMessage(Component.text("Zoey").append(Component.text(" » ", NamedTextColor.DARK_GRAY)).append(Component.text("Watch where you're shooting!"))));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (armorStand.getScoreboardTags().contains("FRANCIS")) {
                            world.getPlayers().forEach(player -> player.sendMessage(Component.text("Francis").append(Component.text(" » ", NamedTextColor.GRAY)).append(Component.text("Will you knock it off!"))));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (armorStand.getScoreboardTags().contains("BILL")) {
                            world.getPlayers().forEach(player -> player.sendMessage(Component.text("Bill").append(Component.text(" » ", NamedTextColor.GRAY)).append(Component.text("I'm getting too old for this horse shit."))));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (armorStand.getScoreboardTags().contains("LOUIS")) {
                            world.getPlayers().forEach(player -> player.sendMessage(Component.text("Louis").append(Component.text(" » ", NamedTextColor.GRAY)).append(Component.text("Hey man, that hurt!"))));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        continue;
                    }

                    Vector intersection = ray.intersectsAt(e.getBoundingBox(), range, ACCURACY_TRUE);
                    if (intersection != null) {
                        if (livingEntity.getScoreboardTags().contains("ROBOT")) {
                            livingEntity.getWorld().playSound(livingEntity.getLocation(), Sound.BLOCK_ANVIL_PLACE, SoundCategory.HOSTILE, 1f, 2f);
                            livingEntity.getWorld().spawnParticle(Particle.SMOKE_NORMAL, intersection.toLocation(livingEntity.getWorld()), 10, 0, 0, 0, 0.05);
                        } else {
                            entities.add(livingEntity);
                        }
                    }
                }
            }

            if (entities.size() >= entitiesToHit) break;
            if (ray.intersects(world.getBlockAt(v.toLocation(world)).getBoundingBox(), range, ACCURACY_TRUE)) {
                wallsThrough--;
                if (wallsThrough < 0) break;
            }
        }

        HashMap<LivingEntity, Boolean> hitEntities = new HashMap<>();
        for (LivingEntity e : entities) {
            double newDMG = (decreaseDamage && e.getLocation().distance(shooter.getLocation()) > 7) ? damage * 0.5 : damage;
            Vector vec = e.getVelocity();
            boolean isHeadshot = HeadshotCalculator.isHeadshot(ray, e, range);
            if (isHeadshot) {
                e.damage(newDMG + (newDMG * 0.5), shooter);
                shooter.sendActionBar(Component.text("Headshot!", NamedTextColor.GOLD));
            } else
                e.damage(newDMG, shooter);
            hitEntities.put(e, isHeadshot);
            e.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
            e.setNoDamageTicks(0);
            e.setVelocity(vec);
        }
        ShootEvent shootEvent = new ShootEvent(shooter, hitEntities);
        Bukkit.getPluginManager().callEvent(shootEvent);

        return blocks;
    }
}
