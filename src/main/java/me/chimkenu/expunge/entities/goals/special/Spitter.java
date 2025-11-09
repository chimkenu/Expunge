package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Throwable;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class Spitter extends MobGoal {
    public Spitter(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
    }

    @Override
    public boolean canUse() {
        var target = mob.getTarget();
        if (target == null) {
            return false;
        }

        double distance = mob.getLocation().distanceSquared(target.getLocation());
        if (distance > 10 * 10) {
            return false;
        }

        return super.canUse();
    }

    @Override
    public void start() {
        var target = mob.getTarget();
        if (target == null) {
            return;
        }

        attackFrameTicks = 20;
        executionTick = 10;
        pendingAttack = () -> ((Throwable) Expunge.getItems().toGameItem("SPIT").get()).use(manager, manager.getEntity(mob).get());
        playTellAnimation("");
    }

    @Override
    public void playTellAnimation(String type) {
        mob.getWorld().playSound(mob, Objects.requireNonNull(mob.getHurtSound()), 2, 0.5f);
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.594604f, 0.629961f, 0.561231f, 0.594604f};
        for (int i = 0; i < pitches.length; i++) {
            final int finalI = i;
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[finalI]);
                        }
                    }.runTaskLater(manager.getPlugin(), i * 7)
            );
        }
    }
}
