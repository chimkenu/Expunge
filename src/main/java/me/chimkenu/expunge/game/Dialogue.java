package me.chimkenu.expunge.game;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.Expunge;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Dialogue {
    private final List<String> dialogue;
    private final List<String> speakers;
    private final GameAction gameActionAtEnd;

    public Dialogue(GameAction gameActionAtEnd, String... strings) {
        List<String> dialogue = new ArrayList<>();
        List<String> speakers = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split("»", 2);
            if (split.length < 2) throw new IllegalArgumentException("string \"" + s + "\" does not contain the divider '»' to indicate a speaker and the dialogue.");
            speakers.add(split[0]);
            dialogue.add(split[1]);
        }
        this.dialogue = dialogue;
        this.speakers = speakers;
        this.gameActionAtEnd = gameActionAtEnd;
    }

    public void displayDialogue(List<Player> players) {
        if (players.size() < 1) return; // no players to show dialogue to
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
            }.runTaskLater(Expunge.instance, totalTime);
            if (i < dialogue.size()) totalTime += delay;
        }
        if (gameActionAtEnd != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    gameActionAtEnd.run(players.get(0));
                }
            }.runTaskLater(Expunge.instance, totalTime + 1);
        }
    }
}
