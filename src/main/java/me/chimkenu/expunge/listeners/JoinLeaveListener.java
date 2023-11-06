package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class JoinLeaveListener extends GameListener {
    public JoinLeaveListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (localGameManager.getPlayers().contains(e.getPlayer())) {
            localGameManager.getPlayers().remove(e.getPlayer());

            if (localGameManager.getPlayers().size() < 1) {
                Bukkit.broadcastMessage(ChatColor.RED + "All players left, stopping game...");
                localGameManager.stop(true);
            }
        }
    }
}
