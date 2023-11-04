package me.chimkenu.expunge.campaigns;

import org.bukkit.World;

import java.util.ArrayList;

public abstract class Campaign {
    private final String name;
    private final ArrayList<CampaignMap> scenes;
    private final World world;

    public Campaign(String name, ArrayList<CampaignMap> scenes, World world) {
        this.name = name;
        this.scenes = scenes;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public ArrayList<CampaignMap> getScenes() {
        return scenes;
    }

    public World getWorld() {
        return world;
    }
}
