package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.commands.Tutorial;
import me.chimkenu.expunge.enums.Achievements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {
    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent e) {
        if (e.getName().contains(".")) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Please get Java Edition!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().getInventory().clear();
        e.getPlayer().sendMessage(ChatColor.GREEN + "New? Do " + ChatColor.YELLOW + "/tutorial" + ChatColor.GREEN + " to learn the mechanics!");
        Achievements.WELCOME_TO_EXPUNGE.grant(e.getPlayer());
        Expunge.setSpectator(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        if (Expunge.playing.getKeys().contains(e.getPlayer())) {
            Expunge.playing.remove(e.getPlayer());

            if (Expunge.playing.getKeys().size() < 1) {
                Bukkit.broadcastMessage(ChatColor.RED + "All players left, stopping game...");
                Expunge.stopGame();
            }
        }
        Expunge.spectators.remove(e.getPlayer());
        Queue.remove(e.getPlayer());
        Tutorial.inTutorial.remove(e.getPlayer());
    }

    @EventHandler
    public void onFlowerPotInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block != null && (block.getType().name().startsWith("POTTED_") || block.getType() == Material.FLOWER_POT)) {
            if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                e.setCancelled(true);
            }
        }
    }
}
