package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.utilities.throwable.Spit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Spitter extends Special {
    public Spitter(JavaPlugin plugin, GameManager gameManager, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, ZombieVillager.class, mob -> {
            double distance = 0;
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer(world));
            else distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
            if (!mob.hasPotionEffect(PotionEffectType.CONFUSION) && distance < 10 * 10) {
                // spit at player direction
                mob.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0, true, true, false));
                new Spit().use(plugin, gameManager, mob);

                // TODO: run away
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false));
        });

        getMob().addScoreboardTag("SPITTER");
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{0.594604f, 0.629961f, 0.561231f, 0.594604f};
        for (int i = 0; i < pitches.length; i++) {
            final int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_BELL, SoundCategory.HOSTILE, 2, pitches[finalI]);
                }
            }.runTaskLater(plugin, i * 7);
        }
    }
}
