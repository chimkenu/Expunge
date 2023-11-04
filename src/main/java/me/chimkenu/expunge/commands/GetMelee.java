package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetMelee implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (!player.isOp()) {
            sender.sendMessage(ChatColor.RED + "Insufficient permissions.");
            return true;
        }

        for (Melee melee : Utils.getMelees()) {
            player.getInventory().addItem(melee.getWeapon());
        }
        player.sendMessage(ChatColor.GREEN + "Here you go.");
        return true;
    }
}
