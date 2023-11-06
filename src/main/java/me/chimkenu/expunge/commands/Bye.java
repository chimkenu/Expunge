package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Bye implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        if (!sender.isOp()) {
//            sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
//            return true;
//        }
//
//        if (Expunge.isGameRunning) {
//            Expunge.stopGame();
//            Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + ChatColor.YELLOW + " ended the game.");
//        } else {
//            sender.sendMessage(ChatColor.RED + "There is no game to stop.");
//        }
        return true;
    }
}
