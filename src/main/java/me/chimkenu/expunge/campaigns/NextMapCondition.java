package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import org.bukkit.event.Event;

import java.util.Map;

public interface NextMapCondition {
    boolean init(CampaignGameManager manager, Event event, Map<String, Object> data);
    boolean check(CampaignGameManager manager, Event event, Map<String, Object> data);
    boolean sideEffect(CampaignGameManager manager, Event event, Map<String, Object> data);
}
