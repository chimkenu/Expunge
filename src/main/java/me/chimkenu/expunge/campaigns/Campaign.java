package me.chimkenu.expunge.campaigns;

import org.bukkit.Material;

public abstract class Campaign {
    private final CampaignMap[] maps;

    public Campaign(CampaignMap[] maps) {
        this.maps = maps;
    }

    public abstract String name();
    public abstract String displayName();
    public abstract String directoryName();
    public abstract String builderNames();
    public abstract Material guiMaterial();
    public CampaignMap[] maps() {
        return maps;
    }
}
