package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class JoinLeaveListener extends GameListener {
    public JoinLeaveListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (gameManager.getPlayers().contains(e.getPlayer())) {
            gameManager.getPlayers().remove(e.getPlayer());

            if (gameManager.getPlayers().isEmpty()) {
                Bukkit.broadcastMessage(ChatUtil.format("&7All players left. Stopping game..."));
                gameManager.stop(true);
            }
        }
    }
}
