package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.common.Horde;
import me.chimkenu.expunge.mobs.special.Boomer;
import me.chimkenu.expunge.mobs.special.Charger;
import me.chimkenu.expunge.mobs.special.Jockey;
import me.chimkenu.expunge.mobs.special.Tank;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command.");
            return true;
        }
        if (!player.isOp()) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Insufficient arguments. " + ChatColor.GRAY + "Usage: /spawn (mob) [number]");
            return true;
        }

        // get number to spawn
        int numToSpawn = 1;
        try {
            if (args.length > 1)
                numToSpawn = Integer.parseInt(args[1]);
        } catch (NumberFormatException ignored) {}

        for (int i = 0; i < numToSpawn; i++) {
            // get mob to spawn
            String mobName = args[0].toLowerCase();
            GameMob mobToSpawn = switch (mobName) {
                case "zombie" -> new Horde(player.getWorld(), player.getLocation());
                case "creeper" -> new Boomer(player.getWorld(), player.getLocation());
                case "spider" -> new Jockey(player.getWorld(), player.getLocation());
                case "iron_golem" -> new Tank(player.getWorld(), player.getLocation());
                case "zoglin" -> new Charger(player.getWorld(), player.getLocation());
                default -> null;
            };
            if (mobToSpawn == null) {
                sender.sendMessage(ChatColor.RED + "Unknown mob '" + ChatColor.WHITE + mobName + ChatColor.RED + "' " + ChatColor.GRAY + "Mobs: zombie, creeper, spider, iron_golem, zoglin");
                return true;
            }
        }

        sender.sendMessage(ChatColor.YELLOW + "Spawned.");
        return true;
    }
}
