package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.MobSettings;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Spider;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Rider extends Pouncer {
    private int tickCounter = 0;

    public Rider(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
        if (mob instanceof Spider spider) {
            Objects.requireNonNull(spider.getAttribute(Attribute.SCALE)).setBaseValue(0.7);
        }
    }

    @Override
    public boolean canUse() {
        var target = mob.getTarget();
        if (target == null) {
            return false;
        }

        super.canUse();

        tickCounter++;
        if (tickCounter == 20) {
            mob.getWorld().playSound(mob, Objects.requireNonNull(mob.getHurtSound()), 2, 1);
        } else if (tickCounter >= 30) {
            pounce(mob, target);
            tickCounter = 0;
        }

        return false;
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.529732f, 0.561231f, 0.529732f, 0.561231f, 0.529732f, 0.561231f, 0.529732f, 0.561231f, 0.529732f, 0.594604f, 0.529732f, 0.594604f, 0.529732f, 0.561231f, 0.529732f, 0.561231f};
        for (int i = 0; i < pitches.length / 2; i++) {
            final int finalI = i;
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_HARP, 2, pitches[finalI * 2]);
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_HARP, 2, pitches[finalI * 2 + 1]);
                        }
                    }.runTaskLater(manager.getPlugin(), i * 4)
            );
        }
    }

    @Override
    public void pounce(LivingEntity source, LivingEntity target) {
        var dir = target.getEyeLocation().subtract(source.getLocation()).toVector().normalize();
        source.setVelocity(dir.multiply(0.5));
        manager.addTask(
                new BukkitRunnable() {
                    int t = 10;
                    @Override
                    public void run() {
                        if (t <= 0) {
                            cancel();
                            return;
                        }
                        source.getWorld().spawnParticle(Particle.ENTITY_EFFECT, source.getLocation(), 5, 0.25, 0.2, 0.25, 0, Color.fromRGB(5, 161, 13));
                        source.getNearbyEntities(1, 1, 1).forEach(e -> {
                            if (e == target) {
                                target.addPassenger(source);
                                cancel();
                            }
                        });
                        t--;
                    }
                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
        );
    }
}
