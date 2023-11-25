package me.chimkenu.expunge.game.director;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class StatsHandler {
    private final Director director;
    private final HashMap<Player, Stats> playerStats;

    public StatsHandler(Director director) {
        this.director = director;
        this.playerStats = new HashMap<>();
    }

    public Component displayStats() {
        Component stats = Component.newline();
        for (Player p : director.getPlayers()) {
            stats = stats.append(Component.text("    "))
                    .append(p.displayName())
                    .append(Component.text(" killed " + getCommonKills(p) + " common infected and " + getSpecialKills(p) + " special infected. ", NamedTextColor.GRAY))
                    .append(Component.text("        Accuracy: " + getAccuracy(p) + "% (" + getHeadshotAccuracy(p) + "% headshot)", NamedTextColor.GRAY))
                    .appendNewline();
        }

        return Component.text("Game Stats:").append(stats);
    }

    public int getCommonKills(Player player) {
        if (!playerStats.containsKey(player)) return 0;
        return playerStats.get(player).commonInfectedKills;
    }

    public void addCommonKill(Player player) {
        playerStats.putIfAbsent(player, new Stats());
        playerStats.get(player).commonInfectedKills += 1;

    }

    public int getSpecialKills(Player player) {
        if (!playerStats.containsKey(player)) return 0;
        return playerStats.get(player).specialInfectedKills;
    }

    public void addSpecialKill(Player player) {
        playerStats.putIfAbsent(player, new Stats());
        playerStats.get(player).specialInfectedKills += 1;
    }

    public double getAccuracy(Player player) {
        Stats stats = playerStats.get(player);
        if (stats == null) return 1;
        return (double) stats.shots / stats.shotsHit;
    }

    public double getHeadshotAccuracy(Player player) {
        Stats stats = playerStats.get(player);
        if (stats == null) return 1;
        return (double) stats.headshots / stats.shotsHit;
    }

    public void addShot(Player player, boolean shotHit, boolean headshot) {
        playerStats.putIfAbsent(player, new Stats());
        Stats stats = playerStats.get(player);
        stats.shots += 1;
        stats.shotsHit += shotHit ? 1 : 0;
        stats.headshots += headshot ? 1 : 0;
    }

    public double calculateRating(Player player) {
        double rating = 0;

        rating += getAccuracy(player) * 0.60;
        rating += getHeadshotAccuracy(player) * 0.40;

        return rating;
    }

    private static class Stats {
        int commonInfectedKills;
        int specialInfectedKills;
        int shots;
        int shotsHit;
        int headshots;

        private Stats() {
            commonInfectedKills = 0;
            specialInfectedKills = 0;
            shots = 0;
            shotsHit = 0;
            headshots = 0;
        }
    }
}
