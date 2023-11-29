package me.chimkenu.expunge.mobs.special;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Spewer extends Special {
    public Spewer(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Creeper.class, mob -> {
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer(world));
            else if (mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 4 * 4) {
                mob.damage(2);
            }
        });
        Creeper creeper = (Creeper) getMob();
        creeper.setPowered(true);
        creeper.setMaxFuseTicks(20 * 60);
        creeper.setExplosionRadius(1);
        creeper.setHealth(1);
        getMob().addScoreboardTag("SPECIAL");
        getMob().addScoreboardTag("BOOMER");
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.5f, 0.529732f, 0.561231f, 0.707107f, 0.667420f};
        for (int i = 0; i < pitches.length; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, SoundCategory.HOSTILE, 1, pitches[finalI]);
                }
            }.runTaskLater(plugin, 4 * i);
        }
    }
}
