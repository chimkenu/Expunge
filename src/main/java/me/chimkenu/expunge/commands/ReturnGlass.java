package me.chimkenu.expunge.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReturnGlass implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
//        if (!sender.isOp()) {
//            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
//            return true;
//        }
//        BreakGlass.returnGlass();
//        sender.sendMessage(ChatColor.GREEN + "Returned all the broken glass.");
          return true;
    }
}
