package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class StatsHandler {
    private final Director director;
    public final HashMap<Player, Integer> kills = new HashMap<>();
    public final HashMap<Player, Integer[]> shots = new HashMap<>();

    public StatsHandler(Director director) {
        this.director = director;
    }

    public Component displayStats() {
        return Component.text("lol u suck");
    }

    private int getPlayerProgress(Player player) {
        CampaignMap map = director.getMap();
        Vector v = player.getLocation().toVector();
        int nearest = 0;
        double nearestDistance = v.distanceSquared(map.pathRegions()[nearest].getCenter());
        for (int i = 0; i < map.pathRegions().length; i++) {
            BoundingBox b = map.pathRegions()[i];
            double distance = v.distanceSquared(b.getCenter());
            if (distance < nearestDistance) {
                nearest = i;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    public int getTotalKills() {
        int total = 0;
        for (Player player : kills.keySet()) {
            total += kills.get(player);
        }
        return total;
    }

    public int getKills(Player player) {
        if (kills.get(player) == null) return 0;
        return kills.get(player);
    }

    public double getAccuracy(Player player) {
        Integer[] shot = shots.get(player);
        if (shot == null) return 1;
        return (double) shot[1] / shot[0];
    }

    public double getHeadshotAccuracy(Player player) {
        Integer[] shot = shots.get(player);
        if (shot == null) return 1;
        return (double) shot[2] / shot[1];
    }

    public double getPace(Player player) {
        if (director.getPlayers().size() > 1) {
            double progressAverage = 0;
            for (Player p : director.getPlayers()) {
                progressAverage += getPlayerProgress(p);
            }
            progressAverage = progressAverage / (director.getPlayers().size());
            return getPlayerProgress(player) - progressAverage;
        }
        return 0;
    }

    public double calculateRating(Player player) {
        double rating = 0;

        // accuracy & headshot accuracy
        rating += getAccuracy(player) * 0.25;
        rating += getHeadshotAccuracy(player) * 0.25;

        // progress : are you rushing or are you dragging?
        // double diff = Math.abs(getPace(player));
        // diff = (1 - (1 / (1 + Math.exp(-diff)))) + 0.5;
        // rating += diff * 0.5;

        return rating;
    }


}
