package me.chimkenu.expunge.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class ChatUtil {
    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', "&r" + s);
    }

    public static void sendFormatted(CommandSender recipient, String s) {
        recipient.sendMessage(format(s));
    }

    public static void sendError(CommandSender recipient, String s) {
        sendFormatted(recipient, "&a" + s);
    }

    public static void sendInfo(CommandSender recipient, String s) {
        sendFormatted(recipient, "&e" + s);
    }
}
