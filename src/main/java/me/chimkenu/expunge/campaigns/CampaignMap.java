package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public interface CampaignMap {
    String name();
    String displayName();

    Vector startLocation();
    BoundingBox endRegion();

    List<Path> escapePath();

    List<Vector> spawnLocations();
    List<Vector> bossLocations();
    List<Vector> rescueClosetLocations();
    NextMapCondition nextMapCondition();

    List<ItemRandomizer> startItems();
    List<ItemRandomizer> mapItems();
    List<Vector> ammoLocations();

    GameAction runAtStart();
    GameAction runAtEnd();
    List<Listener> happenings(Expunge plugin, GameManager gameManager);

    default GameListener[] gameListeners(Expunge plugin, GameManager gameManager) {
        BreakGlassListener breakGlassListener = new BreakGlassListener(plugin, gameManager);
        return new GameListener[]{
                new AmmoPileListener(plugin, gameManager),
                new DeathReviveListener(plugin, gameManager),
                new InventoryListener(plugin, gameManager),
                new MobListener(plugin, gameManager),
                new NextMapListener(plugin, (CampaignGameManager) gameManager),
                new PickUpListener(plugin, gameManager),
                new ShootListener(plugin, gameManager, breakGlassListener),
                new ShoveListener(plugin, gameManager, breakGlassListener),
                new SwingListener(plugin, gameManager),
                new JoinLeaveListener(plugin, gameManager),
                new UtilityListener(plugin, gameManager),
                breakGlassListener,
                new BreakDoorListener(plugin, gameManager),
                new InteractableListener(plugin, gameManager)
        };
    }
}
