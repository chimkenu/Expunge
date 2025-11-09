package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Witch extends MobGoal {
    public Witch(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
    }

    @Override
    public boolean canUse() {
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20, 200, false, false));
        return mob.getTarget() != null;
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{1.259921f, 1.334840f, 1.259921f, 1.334840f, 1.498307f, 1.334840f, 1.498307f, 1.587401f, 1.781797f, 1.587401f, 1.781797f, 1.587401f, 1.498307f, 1.587401f, 1.498307f, 1.334840f};
        for (int t = 0; t < 4; t++) {
            for (int i = 0; i < pitches.length; i++) {
                final int finalI = i;
                manager.addTask(
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                mob.getWorld().playSound(mob, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.HOSTILE, 2, pitches[finalI]);
                            }
                        }.runTaskLater(manager.getPlugin(), t * pitches.length * 3 + i * 3)
                );
            }
        }
    }
}
