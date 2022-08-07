package me.chimkenu.expunge;

import me.chimkenu.expunge.enums.Difficulty;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Queue {
    private static final HashMap<Player, Difficulty> inQueue = new HashMap<>();

    public static void add(Player player, Difficulty difficulty) {
        inQueue.put(player, difficulty);
    }

    public static void remove(Player player) {
        inQueue.remove(player);
    }

    public static boolean contains(Player player) {
        return inQueue.containsKey(player);
    }

    public static void clear() {
        inQueue.clear();
    }

    public static Set<Player> list() {
        return inQueue.keySet();
    }

    public static Difficulty result() {
        Iterable<Difficulty> iterable = inQueue.values();
        Map<Difficulty, Integer> freqMap = new HashMap<>();
        Difficulty mostFreq = null;
        int mostFreqCount = -1;
        for (Difficulty e : iterable) {
            Integer count = freqMap.get(e);
            freqMap.put(e, count = (count == null ? 1 : count + 1));
            // maintain the most frequent in a single pass.
            if (count > mostFreqCount) {
                mostFreq = e;
                mostFreqCount = count;
            }
        }
        return mostFreq;
    }
}
