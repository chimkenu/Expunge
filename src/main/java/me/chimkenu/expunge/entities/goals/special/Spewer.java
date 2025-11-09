package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Mob;
import org.bukkit.scheduler.BukkitRunnable;

public class Spewer extends MobGoal {
    public Spewer(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);

        if (mob instanceof Creeper creeper) {
            creeper.setPowered(true);
            creeper.setMaxFuseTicks(20 * 60);
            creeper.setExplosionRadius(1);
            creeper.setHealth(1);
        }
    }

    @Override
    public boolean canUse() {
        var target = mob.getTarget();
        if (target != null && mob.getLocation().distanceSquared(target.getLocation()) < 4 * 4) {
            mob.damage(2);
        }
        return false;
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.5f, 0.529732f, 0.561231f, 0.707107f, 0.667420f};
        for (int i = 0; i < pitches.length; i++) {
            int finalI = i;
            manager.addTask(
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.HOSTILE, 2, pitches[finalI]);
                        }
                    }.runTaskLater(manager.getPlugin(), 6 * i)
            );
        }
    }
}
