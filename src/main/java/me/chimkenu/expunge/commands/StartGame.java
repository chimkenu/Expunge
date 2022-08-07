package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Difficulty;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartGame implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command");
            return true;
        }
        if (!player.isOp()) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
            return true;
        }
        if (Expunge.isCountdownRunning) {
            sender.sendMessage(ChatColor.RED + "There's a countdown active. Consider doing " + ChatColor.GRAY + "/join" + ChatColor.RED + " to join it.");
            return true;
        }
        if (Expunge.isGameRunning) {
            sender.sendMessage(ChatColor.RED + "There's a game active. Consider doing " + ChatColor.GRAY + "/bye" + ChatColor.RED + " first then executing this command again.");
            return true;
        }
        Difficulty difficulty = Difficulty.NORMAL;
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "easy" -> difficulty = Difficulty.EASY;
                case "hard" -> difficulty = Difficulty.HARD;
                case "suffering" -> difficulty = Difficulty.SUFFERING;
            }
        }
        Bukkit.broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.YELLOW + " started a game with " + difficulty.string() + ChatColor.YELLOW + " difficulty.");
        Expunge.setPlaying(player);
        Expunge.startGame(difficulty);
        return true;
    }
}
