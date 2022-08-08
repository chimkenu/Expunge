package me.chimkenu.expunge.game.maps.thedeparture;

import me.chimkenu.expunge.game.maps.Map;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.scenes.*;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;

public class TheDeparture extends Map {
    private static ArrayList<Scene> scenes() {
        ArrayList<Scene> scenes = new ArrayList<>();
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
