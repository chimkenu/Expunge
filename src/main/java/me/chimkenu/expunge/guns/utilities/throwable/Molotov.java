package me.chimkenu.expunge.guns.utilities.throwable;

import me.chimkenu.expunge.Expunge;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Molotov extends Throwable {

    public Molotov() {
        super(20, Material.LANTERN, "&6Molotov", "THROWABLE_MOLOTOV");
    }

    @Override
    public void use(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = player.launchProjectile(Snowball.class);
        ball.addScoreboardTag("THROWABLE_GRENADE");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_MOLOTOV] run data merge entity @s {Item:{id:\"minecraft:lantern\",Count:1b}}");
    }

    @Override
    public void onLand(World world, Location loc, Entity shooter) {
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
                            livingEntity.damage(1);
                            livingEntity.setVelocity(vec);
                            livingEntity.setNoDamageTicks(0);
                            if (livingEntity instanceof Player) {
                                livingEntity.setFireTicks(40);
                            } else {
                                livingEntity.setFireTicks(20 * 60);
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(Expunge.instance, 0, 1);
    }
}
