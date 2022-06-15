package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Join implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }
        if (Tutorial.inTutorial.contains(player)) {
            sender.sendMessage(ChatColor.RED + "Please finish the tutorial first.");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("expunge")) {
            sender.sendMessage(ChatColor.GREEN + "you're already here lol");
        }
        if (Expunge.isCountdownRunning) {
            if (Expunge.inQueue.contains(player)) {
                sender.sendMessage(ChatColor.RED + "You're already in the queue.");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Joined the queue. Please wait for the game to begin.");
            Expunge.inQueue.add(player);
            return true;
        }
        if (!Expunge.isGameRunning) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "No game is active, starting countdown...");
            Expunge.startGame(true);
            Expunge.inQueue.add(player);
            return true;
        }
        if (Expunge.playing.getKeys().contains(player)) {
            sender.sendMessage(ChatColor.RED + "You're in the game.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "Joining game...");
        for (Player p : Expunge.playing.getKeys()) {
            if (Expunge.playing.isAlive(p)) {
                player.sendMessage(ChatColor.YELLOW + "Teleporting you to " + ChatColor.GREEN + p.getDisplayName() + ChatColor.YELLOW + ".");
                player.teleport(p.getLocation());
                Expunge.setPlaying(player);
                return true;
            }
        }

        // no player is alive??
        player.sendMessage(ChatColor.RED + "Something went wrong, try the command again?");
        return true;
    }
}
