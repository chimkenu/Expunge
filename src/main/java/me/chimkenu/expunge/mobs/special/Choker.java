package me.chimkenu.expunge.mobs.special;

import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Choker extends Special {
    private Player victim;

    public Choker(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Husk.class, mob -> {

        });
        getMob().addScoreboardTag("SPECIAL");
    }

    @Override
    protected void playJingle() {
        getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 0.561231f);
        getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 0.594604f);
        new BukkitRunnable() {
            @Override
            public void run() {
                getMob().getWorld().playSound(getMob(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 1, 0.5f);
            }
        }.runTaskLater(plugin, 11);
    }
}
