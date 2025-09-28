package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;

import java.util.Map;

public record Movement(
        BoundingBox box
) implements NextMapCondition {
    @Override
    public boolean init(CampaignGameManager manager, Event event, Map<String, Object> data) {
        return true;
    }

    @Override
    public boolean check(CampaignGameManager manager, Event event, Map<String, Object> data) {
        if (!(event instanceof PlayerMoveEvent e)) {
            return false;
        }
        if (!manager.getPlayers().contains(e.getPlayer())) {
            return false;
        }
        return manager.getDirector().getAlivePlayers().allMatch(p -> box.contains(p.getLocation().toVector()));
    }

    @Override
    public boolean sideEffect(CampaignGameManager manager, Event event, Map<String, Object> data) {
        return true;
    }
}
