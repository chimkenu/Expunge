package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.entities.survivor.Survivor;
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
        return manager.getSurvivors().stream().filter(Survivor::isAlive).allMatch(s -> box.contains(s.getLocation().toVector()));
    }

    @Override
    public boolean sideEffect(CampaignGameManager manager, Event event, Map<String, Object> data) {
        return true;
    }
}
