package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;

import java.util.List;

public class Campaigns {
    public static final Campaign THE_DEPARTURE = new TheDeparture();

    public static List<Campaign> values() {
        return List.of(THE_DEPARTURE);
    }

    public static Campaign valueOf(String name) {
        for (Campaign campaign : values()) {
            if (name.equals(campaign.directoryName())) {
                return campaign;
            }
        }
        throw new IllegalArgumentException("Unknown campaign: " + name);
    }
}
