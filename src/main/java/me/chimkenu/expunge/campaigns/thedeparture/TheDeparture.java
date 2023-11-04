package me.chimkenu.expunge.campaigns.thedeparture;

import me.chimkenu.expunge.campaigns.GameMap;
import me.chimkenu.expunge.campaigns.thedeparture.maps.*;
import me.chimkenu.expunge.campaigns.Campaign;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;

public class TheDeparture extends Campaign {
    private static ArrayList<GameMap> scenes() {
        ArrayList<GameMap> scenes = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        if (world == null) return scenes;

        scenes.add(OfficePart1.getScene());
        scenes.add(OfficePart2.getScene());
        scenes.add(Streets.getScene());
        scenes.add(Alleys.getScene());
        scenes.add(Subway.getScene());
        scenes.add(Highway.getScene());
        scenes.add(Stadium.getScene());
        return scenes;
    }

    public TheDeparture() {
        super("The Departure", scenes(), Bukkit.getWorld("world"));
    }
}
