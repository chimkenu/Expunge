package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Molotov implements Throwable {
    @Override
    public void use(JavaPlugin plugin, LocalGameManager localGameManager, LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag(getTag());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_MOLOTOV] run data merge entity @s {Item:{id:\"minecraft:lantern\",Count:1b}}");
    }

    @Override
    public void onLand(JavaPlugin plugin, World world, Location loc, Entity shooter) {
        world.playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 0);
        new BukkitRunnable() {
            int t = 0;
            @Override
            public void run() {
                world.spawnParticle(Particle.FLAME, loc, 20, 0.8, 0.8, 0.8, 0, null, true);
                t++;
                if (t > 7 * 20) this.cancel();

                // damage nearby
                if (t % 4 == 0) {
                    for (Entity entity : world.getNearbyEntities(loc, 2, 2, 2)) {
                        if (entity instanceof LivingEntity livingEntity) {
                            Vector vec = livingEntity.getVelocity();
                            livingEntity.setVelocity(vec);
                            livingEntity.setNoDamageTicks(0);
                            if (livingEntity instanceof Player) {
                                livingEntity.damage(1);
                                livingEntity.setFireTicks(40);
                            } else {
                                livingEntity.damage(10, shooter);
                                livingEntity.setFireTicks(20 * 60);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public Material getMaterial() {
        return Material.LANTERN;
    }

    @Override
    public String getName() {
        return "&6Molotov";
    }

    @Override
    public String getTag() {
        return "THROWABLE_MOLOTOV";
    }
}
