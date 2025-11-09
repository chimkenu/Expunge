package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Charger extends MobGoal {
    public Charger(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() == null) {
            return false;
        }

        var distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
        if (!mob.hasPotionEffect(PotionEffectType.NAUSEA) && distance > 5 * 5 && distance < 10 * 10) {
            // charge at player
            mob.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 150, 0, true, true, false));
            new BukkitRunnable() {
                final LivingEntity target = mob.getTarget();
                int time = 0;
                final Vector direction = mob.getLocation().getDirection().setY(0).normalize().multiply(1.5);

                @Override
                public void run() {
                    if (mob.isDead()) {
                        this.cancel();
                        return;
                    }
                    if (target == null) {
                        this.cancel();
                        return;
                    }
                    if (mob.getLocation().distanceSquared(target.getLocation()) < 2 * 2) {
                        this.cancel();
                        return;
                    }
                    if (time >= 60) {
                        this.cancel();
                        return;
                    } else if (!mob.hasPotionEffect(PotionEffectType.NAUSEA)) {
                        this.cancel();
                        return;
                    }

                    mob.setVelocity(mob.getVelocity().add(direction));
                    time++;
                }
            }.runTaskTimer(manager.getPlugin(), 0, 1);

            // stun
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!mob.isDead() && mob.getVehicle() == null) {
                        mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 5, 200, false, false, false));
                        mob.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 200, false, false, false));
                    }
                }
            }.runTaskLater(manager.getPlugin(), 61);
        }
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false));

        return false;
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.594604f, 0.561231f, 0.594604f, 0.629961f, 0.529732f, 0.561231f};
        for (int i = 0; i < pitches.length; i++) {
            final int finalI = i;
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.HOSTILE, 2, pitches[finalI]);
                        }
                    }.runTaskLater(manager.getPlugin(), 3 * i)
            );
        }
    }
}
