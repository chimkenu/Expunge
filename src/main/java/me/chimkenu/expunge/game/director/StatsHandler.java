package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.GameMap;
import me.chimkenu.expunge.campaigns.Campaign;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class StatsHandler {
    private final Campaign map;
    private int sceneIndex;
    public final HashMap<Player, Integer> kills = new HashMap<>();
    public final HashMap<Player, Integer[]> shots = new HashMap<>();

    public StatsHandler(Campaign map) {
        this.map = map;
        sceneIndex = 0;
    }

    public void updateSceneIndex() {
        sceneIndex++;
    }

    private int getPlayerProgress(Player player) {
        GameMap scene = map.getScenes().get(sceneIndex);
        Vector v = player.getLocation().toVector();
        int nearest = 0;
        double nearestDistance = v.distanceSquared(scene.pathRegions()[nearest].getCenter());
        for (int i = 0; i < scene.pathRegions().length; i++) {
            BoundingBox b = scene.pathRegions()[i];
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
        if (Expunge.playing.getKeys().size() > 1) {
            double progressAverage = 0;
            for (Player p : Expunge.playing.getKeys()) {
                progressAverage += getPlayerProgress(p);
            }
            progressAverage = progressAverage / (Expunge.playing.getKeys().size());
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
        double diff = Math.abs(getPace(player));
        diff = (1 - (1 / (1 + Math.exp(-diff)))) + 0.5;
        rating += diff * 0.5;

        return rating;
    }
}
