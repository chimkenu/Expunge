package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.game.MobListener;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Pouncer extends MobGoal {
    public Pouncer(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
        Objects.requireNonNull(mob.getEquipment()).clear();
    }

    @Override
    public boolean canUse() {
        if (mob.getVehicle() instanceof Player target) {
            target.getInventory().setHeldItemSlot(5);
            target.setVelocity(target.getVelocity().add(mob.getEyeLocation().getDirection()));
            mob.attack(target);
            return false;
        }

        if (mob.getTarget() instanceof Player target) {
            if (!target.getPassengers().isEmpty()) {
                mob.setTarget(null);
                return false;
            }

            double distance = mob.getLocation().distanceSquared(target.getLocation());
            int speed = 1;
            if (distance > 10 * 10) speed += 2;
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
        }

        return super.canUse() &&                // cooldown check
                mob.getTarget() != null &&      // has target check
                mob.getVehicle() == null &&     // is not already pouncing someone
                mob.getLocation().distance(mob.getTarget().getLocation()) < 12;
    }

    @Override
    public void start() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        attackFrameTicks = 40;
        executionTick = 20;
        pendingAttack = () -> pounce(mob, target);
        playTellAnimation("pounce");
    }

    @Override
    protected void playTellAnimation(String type) {
        mob.getWorld().playSound(mob, Objects.requireNonNull(mob.getHurtSound()), 2, 0.5f);
        manager.addTask(
                new BukkitRunnable() {
                    int t = 20;
                    @Override
                    public void run() {
                        if (t <= 0) {
                            cancel();
                            return;
                        }
                        mob.getWorld().spawnParticle(Particle.LARGE_SMOKE, mob.getLocation(), 20, 0.25, 0.2, 0.25, 0);
                        t--;
                    }
                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
        );
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{1.781797f, 1.887749f, 1.414214f, 1.587401f, 1.498307f, 1.587401f};
        for (int i = 0; i < pitches.length / 2; i++) {
            final int finalI = i;
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[2 * finalI]);
                        }
                    }.runTaskLater(manager.getPlugin(), i * 10)
            );
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[2 * finalI + 1]);
                        }
                    }.runTaskLater(manager.getPlugin(), i * 10 + 2)
            );
        }
    }

    public void pounce(LivingEntity source, LivingEntity target) {
        var dir = target.getEyeLocation().subtract(source.getLocation()).toVector().normalize();
        source.setVelocity(dir.multiply(3));
        manager.addTask(
                new BukkitRunnable() {
                    int t = 20;
                    @Override
                    public void run() {
                        if (t <= 0) {
                            cancel();
                            return;
                        }
                        source.getWorld().spawnParticle(Particle.LARGE_SMOKE, source.getLocation(), 5, 0.25, 0.2, 0.25, 0);
                        source.getNearbyEntities(1, 1, 1).forEach(e -> {
                            if (e == target) {
                                target.addPassenger(source);
                                if (target instanceof Player player) {
                                    MobListener.disable(player, manager);
                                }
                                cancel();
                            }
                        });
                        t--;
                    }
                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
        );
    }
}
