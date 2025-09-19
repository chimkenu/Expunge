package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import org.bukkit.event.Listener;

public abstract class GameListener implements Listener {
    protected final Expunge plugin;
    protected final GameManager gameManager;

    protected GameListener(Expunge plugin, GameManager gameManager) {
        this.plugin = plugin;
        this.gameManager = gameManager;
    }
}
