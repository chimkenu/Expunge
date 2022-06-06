package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.guns.weapons.guns.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GetGun implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!sender.isOp())  {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            for (Gun gun : Utils.getGuns()) {
                player.getInventory().addItem(gun.getWeapon());
            }
            player.sendMessage(ChatColor.GREEN + "Here you go.");
        } else { sender.sendMessage(ChatColor.RED + "Only players can execute this command."); }
        return true;
    }
}
