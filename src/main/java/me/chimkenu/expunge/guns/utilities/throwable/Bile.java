package me.chimkenu.expunge.guns.utilities.throwable;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Slot;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Bile extends Throwable {
    public Bile() {
        super(20, Material.LIME_CANDLE, "&aBile", Slot.TERTIARY, "THROWABLE_BILE");
    }

    @Override
    public void use(LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag(getTag());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_BILE] run data merge entity @s {Item:{id:\"minecraft:lime_candle\",Count:1b}}");
    }

    @Override
    public void onLand(World world, Location loc, Entity shooter) {
        world.spawnParticle(Particle.SPELL_MOB, loc, 200, 2, 0.5, 2);
        world.playSound(loc, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0);
        world.playSound(loc, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, SoundCategory.PLAYERS, 1, 2);
        ArmorStand target = world.spawn(loc, ArmorStand.class);
        target.setInvisible(true);
        target.setGravity(false);
        target.addScoreboardTag("THROWN_BILE");
        Mob targetEntity = null;
        for (Entity e : world.getNearbyEntities(loc, 3, 3, 3)) {
            if (e instanceof Mob mob) {
                targetEntity = mob;
            }
        }
        Mob finalTargetEntity = targetEntity;
        new BukkitRunnable() {
            int t = 40;
            @Override
            public void run() {
                world.spawnParticle(Particle.SNEEZE, loc, 25, 0.5, 0.5, 0.5, 0.1);
                t--;
                if (t <= 0) {
                    target.remove();
                    this.cancel();
                } else if (target.isDead()) {
                    this.cancel();
                }

                // attract nearby mobs
                for (Entity e : world.getNearbyEntities(loc, 30, 30, 30)) {
                    if (e instanceof Mob mob && mob != finalTargetEntity) {
                        mob.setTarget(Objects.requireNonNullElse(finalTargetEntity, target));
                    }
                }
            }
        }.runTaskTimer(Expunge.instance, 0, 10);
    }
}