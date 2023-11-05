package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;
import org.bukkit.World;

import java.nio.file.Path;
import java.util.ArrayList;

public abstract class Campaign {
    private final String name;
    private final CampaignMap[] maps;
    private final String mainDirectory;

    public Campaign(String name, CampaignMap[] maps, String mainDirectory) {
        this.name = name;
        this.maps = maps;
        this.mainDirectory = mainDirectory;
    }

    public String getName() {
        return name;
    }

    public CampaignMap[] getMaps() {
        return maps;
    }

    public String getMainDirectory() {
        return mainDirectory;
    }

    public enum List {
        THE_DEPARTURE {
            @Override
            public Campaign get() {
                return new TheDeparture();
            }
        };
        public abstract Campaign get();
    }
}
