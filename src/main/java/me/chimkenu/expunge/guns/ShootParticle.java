package me.chimkenu.expunge.guns;

import me.chimkenu.expunge.enums.Achievements;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
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
        boolean hasSpoken = false; // boolean for achievement - so that it does not trigger more than once per shot
        for (int i = 0; i < positions.size(); i++) {
            Vector v = positions.get(i);
            // so the particle does not obscure vision
            if (i > 4) world.spawnParticle(particle, v.toLocation(world), 1, 0, 0, 0, 0);

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
                    if (livingEntity instanceof ArmorStand armorStand) {
                        if (!hasSpoken && armorStand.getScoreboardTags().contains("ZOEY")) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Zoey &8» &fWatch where you're shooting!"));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (!hasSpoken && armorStand.getScoreboardTags().contains("FRANCIS")) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Francis &8» &fWill you knock it off!"));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (!hasSpoken && armorStand.getScoreboardTags().contains("BILL")) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Bill &8» &fI'm getting too old for this horse shit."));
                            hasSpoken = true;
                            Achievements.THE_BIG_LEAGUES.grant(shooter);
                        }
                        else if (!hasSpoken && armorStand.getScoreboardTags().contains("LOUIS")) {
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Louis &8» &fHey man, that hurt!"));
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
                        }
                        else
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

        HashMap<LivingEntity, Boolean> hitEntities = new HashMap<>();
        for (LivingEntity e : entities) {
            double newDMG = (decreaseDamage && e.getLocation().distance(shooter.getLocation()) > 7) ? damage * 0.5 : damage;
            Vector vec = e.getVelocity();
            boolean isHeadshot = HeadshotCalculator.isHeadshot(ray, e, range);
            if (isHeadshot) {
                e.damage(newDMG + (newDMG * 0.5), shooter);
                shooter.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§6Headshot!"));
            } else
                e.damage(newDMG, shooter);
            hitEntities.put(e, isHeadshot);
            e.getWorld().spawnParticle(Particle.BLOCK_CRACK, e.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
            e.setNoDamageTicks(0);
            e.setVelocity(vec);
        }
        ShootEvent shootEvent = new ShootEvent(shooter, hitEntities);
        Bukkit.getPluginManager().callEvent(shootEvent);
    }
}
