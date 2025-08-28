package me.chimkenu.expunge.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public final class ChatUtil {
    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', "&r" + s);
    }

    public static void sendFormatted(CommandSender recipient, String s) {
        recipient.sendMessage(format(s));
    }

    public static void sendError(CommandSender recipient, String s) {
        sendFormatted(recipient, "&c" + s);
    }

    public static void sendInfo(CommandSender recipient, String s) {
        sendFormatted(recipient, "&e" + s);
    }

    public static void sendActionBar(Player recipient, String s) {
        recipient.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(format(s)));
    }

    public static String getColor(int r, int g, int b) {
        return net.md_5.bungee.api.ChatColor.of(new Color(r, g, b)).toString();
    }

    public static String gradient(String s, Color color1, Color color2) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            double a = i / (s.length() - 1d);
            int r = (int) ((1 - a) * color1.getRed() + a * color2.getRed());
            int g = (int) ((1 - a) * color1.getGreen() + a * color2.getGreen());
            int b = (int) ((1 - a) * color1.getBlue() + a * color2.getBlue());
            sb.append(getColor(r, g, b)).append(s.charAt(i));
        }
        return sb.toString();
    }

    public static String progressBar(int useTime, int curTime) {
        double percentage = (double) curTime / useTime;
        int progress = (int) (percentage * 10);
        percentage = (int) (percentage * 100);

        // create progress bar
        String progressBarComplete = "&a|".repeat(Math.max(0, progress));
        String progressBarIncomplete = "&7|".repeat(Math.max(0, 10 - progress));
        return format("&eProgress: &7[" + progressBarComplete + progressBarIncomplete + "&7] &8[" + percentage + "%]");
    }
}
