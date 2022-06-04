package me.chimkenu.expunge.game.listeners;

import me.chimkenu.expunge.commands.Tutorial;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Chat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Tutorial.inTutorial.contains(e.getPlayer())) {
            e.setCancelled(true);
            return;
        }
        // hide chat for players in tutorial
        Tutorial.inTutorial.forEach(e.getRecipients()::remove);
    }
}
