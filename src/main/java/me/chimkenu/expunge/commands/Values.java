package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Values implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + "Shove Stats:");
        sender.sendMessage(ChatColor.GRAY + "  isGameRunning: " + Expunge.isGameRunning);
        sender.sendMessage(ChatColor.GRAY + "  isSpawningEnabled: " + Expunge.isSpawningEnabled);
        sender.sendMessage(ChatColor.GRAY + "  players: " + Expunge.playing.getKeys().size());
        sender.sendMessage(ChatColor.GRAY + "  spectators: " + Expunge.spectators.size());
        if (Expunge.isGameRunning) {
            sender.sendMessage(ChatColor.GRAY + "  currentMap: " + Expunge.currentMap.getName());
            sender.sendMessage(ChatColor.GRAY + "  currentSceneIndex: " + Expunge.currentSceneIndex);
            sender.sendMessage(ChatColor.GRAY + "  difficulty: " + Expunge.difficulty);
            sender.sendMessage(ChatColor.GRAY + "  gameTime: " + Expunge.runningDirector.getGameTime());
            sender.sendMessage(ChatColor.GRAY + "  sceneTime: " + Expunge.runningDirector.getSceneTime());
            sender.sendMessage(ChatColor.GRAY + "  sceneAttempts: " + Expunge.runningDirector.getSceneAttempts());
            sender.sendMessage(ChatColor.GRAY + "  activeMobs: " + Expunge.runningDirector.getActiveMobs().size());
        } else {
            sender.sendMessage(ChatColor.RED + "There is no active game at the moment.");
        }
        return true;
    }
}
