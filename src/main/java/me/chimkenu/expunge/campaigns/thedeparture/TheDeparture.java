package me.chimkenu.expunge.campaigns.thedeparture;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.maps.Office;
import me.chimkenu.expunge.campaigns.thedeparture.maps.Stadium;
import me.chimkenu.expunge.campaigns.thedeparture.maps.Streets;
import me.chimkenu.expunge.campaigns.thedeparture.maps.Subway;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;

import java.awt.*;

public class TheDeparture extends Campaign {
    public TheDeparture() {
        super(new CampaignMap[]{
                new Office(),
                new Streets(),
                new Subway(),
                new Stadium()
        });
    }

    @Override
    public String name() {
        return "The Departure";
    }

    @Override
    public String displayName() {
        return ChatUtil.gradient(name(), new Color(255, 92, 51), new Color(102, 0, 0));
    }

    @Override
    public String directoryName() {
        return "TheDeparture";
    }

    @Override
    public String builderNames() {
        return ChatUtil.getColor(134, 0, 179) + "Built by SirSunlight & Pagkain";
    }

    @Override
    public Material guiMaterial() {
        return Material.WOODEN_HOE;
    }
}
