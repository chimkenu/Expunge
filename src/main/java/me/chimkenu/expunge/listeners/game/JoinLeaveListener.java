package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class JoinLeaveListener extends GameListener {
    public JoinLeaveListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (gameManager.getPlayers().contains(e.getPlayer())) {
            gameManager.getPlayers().remove(e.getPlayer());

            if (gameManager.getPlayers().isEmpty()) {
                Bukkit.getLogger().log(Level.INFO, "All players at " + gameManager.getUUID() + " left. Stopping game...");
                gameManager.stop(true);
            }
        }
    }
}
