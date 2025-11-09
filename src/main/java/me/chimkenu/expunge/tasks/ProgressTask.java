package me.chimkenu.expunge.tasks;

import org.bukkit.scheduler.BukkitRunnable;

public abstract class ProgressTask extends BukkitRunnable {
    public abstract void cancel(boolean success);
}
