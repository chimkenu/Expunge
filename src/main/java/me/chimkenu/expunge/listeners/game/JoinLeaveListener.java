package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinLeaveListener extends GameListener {
    public JoinLeaveListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (gameManager.getPlayers().contains(e.getPlayer())) {
            gameManager.getPlayers().remove(e.getPlayer());

            if (gameManager.getPlayers().size() < 1) {
                Bukkit.broadcastMessage(ChatColor.RED + "All players left, stopping game...");
                gameManager.stop(true);
            }
        }
    }
}
