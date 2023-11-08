package me.chimkenu.expunge.campaigns.thedeparture;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.maps.*;
import me.chimkenu.expunge.campaigns.Campaign;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;

public class TheDeparture extends Campaign {
    public TheDeparture() {
        super("The Departure",
                new CampaignMap[]{
                        new Office(),
                        new Streets(),
                        new Subway(),
                        new Stadium()
                },
                "TheDeparture"
        );
    }
}
