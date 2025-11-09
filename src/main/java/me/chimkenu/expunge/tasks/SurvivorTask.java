package me.chimkenu.expunge.tasks;

import me.chimkenu.expunge.entities.survivor.Survivor;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class SurvivorTask extends BukkitRunnable {
    private final Survivor survivor;

    public SurvivorTask(Survivor survivor) {
        this.survivor = survivor;
    }

    @Override
    public void run() {
        if (survivor.isDead() || !survivor.isAlive()) {
            cancel();
        }
    }
}
