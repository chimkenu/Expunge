package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.Director;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Stats implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        Director director = Expunge.runningDirector;
        if (!Expunge.isGameRunning || director == null) {
            sender.sendMessage(ChatColor.RED + "There is no active game at the moment.");
            return true;
        }
        player = args.length > 0 ? Bukkit.getPlayer(args[0]) != null ? Bukkit.getPlayer(args[0]) : player : player;
        if (player == null || !Expunge.playing.getKeys().contains(player)) {
            sender.sendMessage(ChatColor.RED + "There are no stats to display.");
            return true;
        }

        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        sender.sendMessage(ChatColor.GRAY + "" + ChatColor.BOLD + player.getDisplayName() + "'s Stats:");
        sender.sendMessage(ChatColor.GRAY + "  Kills: " + director.getKills(player));
        sender.sendMessage(ChatColor.GRAY + "  Gun Accuracy: " + decimalFormat.format(director.getAccuracy(player)));
        sender.sendMessage(ChatColor.GRAY + "  Headshot Accuracy: " + decimalFormat.format(director.getHeadshotAccuracy(player)));
        sender.sendMessage(ChatColor.GRAY + "  Pace: " + decimalFormat.format(director.getPace(player)));
        return true;
    }
}
