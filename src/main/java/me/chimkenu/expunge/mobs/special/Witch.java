package me.chimkenu.expunge.mobs.special;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enderman;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Witch extends Special {
    public Witch(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Enderman.class, mob -> {
            if (mob.getTarget() != null) {
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 200, false, false, false));
            }
        });
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(39);
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1000);
        getMob().setHealth(1000);
    }

    @Override
    protected void playJingle() {
        final float[] pitches = new float[]{1.259921f, 1.334840f, 1.259921f, 1.334840f, 1.498307f, 1.334840f, 1.498307f, 1.587401f, 1.781797f, 1.587401f, 1.781797f, 1.587401f, 1.498307f, 1.587401f, 1.498307f, 1.334840f};
        for (int t = 0; t < 4; t++) {
            for (int i = 0; i < pitches.length; i++) {
                final int finalI = i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.HOSTILE, 1, pitches[finalI]);
                    }
                }.runTaskLater(plugin, t * pitches.length * 2 + i * 2);
            }
        }
    }
}
