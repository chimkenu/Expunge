package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameListener implements Listener {
    protected final JavaPlugin plugin;
    protected final GameManager gameManager;

    protected GameListener(JavaPlugin plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }
}
