package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.enums.Difficulty;
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
        Difficulty vote = null;
        if (args.length > 0 && args[0].equalsIgnoreCase("expunge")) {
            sender.sendMessage(ChatColor.GREEN + "you're already here lol");
            return true;
        } else if (args.length > 0) {
            String difficulty = args[0].toLowerCase();
            switch (difficulty) {
                case "easy" -> vote = Difficulty.EASY;
                case "hard" -> vote = Difficulty.HARD;
                case "suffering" -> vote = Difficulty.SUFFERING;
                default -> vote = Difficulty.NORMAL;
            }
        } else {
            sender.sendMessage(ChatColor.YELLOW + "You can also vote to change the difficulty of the game! " + ChatColor.translateAlternateColorCodes('&', "&7/join &aeasy &7| /join &enormal &7| /join &chard &7| /join &4suffering"));
        }
        if (Expunge.isCountdownRunning) {
            if (Queue.contains(player)) {
                sender.sendMessage(ChatColor.RED + "You're already in the queue.");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "Joined the queue. Please wait for the game to begin.");
            Queue.add(player, vote);
            return true;
        }
        if (!Expunge.isGameRunning) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "No game is active, starting countdown...");
            Expunge.startGame();
            Queue.add(player, vote);
            return true;
        }
        if (Expunge.playing.getKeys().contains(player)) {
            sender.sendMessage(ChatColor.RED + "You're in the game.");
            return true;
        }

        player.sendMessage(ChatColor.YELLOW + "There is already an active game, joining...");
        if (vote != Expunge.currentDifficulty) player.sendMessage(ChatColor.YELLOW + "The difficulty in this current game is " + Expunge.currentDifficulty.string());
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
