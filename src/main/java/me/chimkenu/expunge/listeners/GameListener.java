package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameListener implements Listener {
    protected final JavaPlugin plugin;
    protected final LocalGameManager localGameManager;

    protected GameListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        this.plugin = plugin;
        this.localGameManager = localGameManager;
    }
}
