package me.chimkenu.expunge.campaigns;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Dialogue {
    public static void display(JavaPlugin plugin, Set<Player> playerSet, String[] strings) {
        List<Player> players = playerSet.stream().toList();
        List<String> dialogue = new ArrayList<>();
        List<String> speakers = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split("»", 2);
            if (split.length < 2) throw new IllegalArgumentException("string \"" + s + "\" does not contain the divider '»' to indicate a speaker and the dialogue.");
            speakers.add(split[0]);
            dialogue.add(split[1]);
        }

        if (players.isEmpty()) return; // no players to show dialogue to
        if (players.size() > 3) {
            for (int i = players.size() - 1; i > 0; i--) {
                int index = ThreadLocalRandom.current().nextInt(i + 1);
                // swap
                Player p = players.get(index);
                players.set(index, players.get(i));
                players.set(i, p);
            }
        }
        String speakerA = players.get(0).displayName() + " ";
        String speakerB = players.get(Math.min(1, players.size() - 1)).displayName() + " ";
        int totalTime = 0;
        double wordsPerSecond = 2.5;
        for (int i = 0; i < dialogue.size(); i++) {
            int words = dialogue.get(i).split(" ").length;
            long delay = (long) (words / wordsPerSecond) * 20;
            int index = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : players) {
                        String speaker = speakers.get(index);
                        if (speaker.trim().equalsIgnoreCase("a")) speaker = speakerA;
                        else if (speaker.trim().equalsIgnoreCase("b")) speaker = speakerB;
                        String message = ChatColor.translateAlternateColorCodes('&', "&r" + speaker + "&8»&r" + dialogue.get(index));
                        p.sendMessage(message);
                    }
                }
            }.runTaskLater(plugin, totalTime);
            if (i < dialogue.size()) totalTime += (int) delay;
        }
    }
}
