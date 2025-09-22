package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import org.bukkit.event.Event;

public interface NextMapCondition {
    boolean check(CampaignGameManager manager, Event event);
}
