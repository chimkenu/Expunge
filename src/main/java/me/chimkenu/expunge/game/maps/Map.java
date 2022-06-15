package me.chimkenu.expunge.game.maps;

import org.bukkit.World;

import java.util.ArrayList;

public abstract class Map {
    private final String name;
    private final ArrayList<Scene> scenes;
    private final World world;

    public Map(String name, ArrayList<Scene> scenes, World world) {
        this.name = name;
        this.scenes = scenes;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Scene> getScenes() {
        return scenes;
    }

    public World getWorld() {
        return world;
    }
}
