package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Action;
import me.chimkenu.expunge.Expunge;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Dialogue {
    private final ArrayList<String> dialogue;
    private final ArrayList<String> speakers;
    private final Action actionAtEnd;

    public Dialogue(ArrayList<String> dialogue, ArrayList<String> speakers, Action actionAtEnd) {
        if (dialogue.size() != speakers.size()) throw new IllegalArgumentException("arraylists must match in size.");
        this.dialogue = dialogue;
        this.speakers = speakers;
        this.actionAtEnd = actionAtEnd;
    }

    public Dialogue(Action actionAtEnd, String... strings) {
        ArrayList<String> dialogue = new ArrayList<>();
        ArrayList<String> speakers = new ArrayList<>();
        for (String s : strings) {
            String[] split = s.split("»", 2);
            if (split.length < 2) throw new IllegalArgumentException("string \"" + s + "\" does not contain the divider '»' to indicate a speaker and the dialogue.");
            speakers.add(split[0]);
            dialogue.add(split[1]);
        }
        this.dialogue = dialogue;
        this.speakers = speakers;
        this.actionAtEnd = actionAtEnd;
    }

    public void displayDialogue(ArrayList<Player> players) {
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
        String speakerA = players.get(0).getDisplayName();
        String speakerB = players.get(Math.min(1, players.size() - 1)).getDisplayName();
        for (int i = 0; i < dialogue.size(); i++) {
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
            }.runTaskLater(Expunge.instance, i * 20 * 2L);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                actionAtEnd.run(players.get(0));
            }
        }.runTaskLater(Expunge.instance, ((dialogue.size() - 1) * 20 * 2L) + 1);
    }

    public void displayDialogue() {
        displayDialogue(new ArrayList<>(Bukkit.getOnlinePlayers()));
    }
}
