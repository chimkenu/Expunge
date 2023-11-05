package me.chimkenu.expunge.campaigns.thedeparture;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.maps.*;
import me.chimkenu.expunge.campaigns.Campaign;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;

public class TheDeparture extends Campaign {
    private static ArrayList<CampaignMap> scenes() {
        ArrayList<CampaignMap> scenes = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        if (world == null) return scenes;

        scenes.add(new Office());
        scenes.add(new Streets());
        scenes.add(new Alleys());
        scenes.add(new Subway());
        scenes.add(new Highway());
        scenes.add(new Stadium());
        return scenes;
    }

    public TheDeparture() {
        super("The Departure",
                new CampaignMap[]{
                        new Office(),
                        new Streets(),
                        new Alleys(),
                        new Subway(),
                        new Highway(),
                        new Stadium()
                },
                "Maps/TheDeparture"
        );
    }
}
