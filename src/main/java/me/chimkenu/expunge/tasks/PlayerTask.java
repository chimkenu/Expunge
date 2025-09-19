package me.chimkenu.expunge.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class PlayerTask extends BukkitRunnable {
    final Player player;

    public PlayerTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (!player.isOnline() || player.isDead()) {
            cancel();
        }
    }
}
