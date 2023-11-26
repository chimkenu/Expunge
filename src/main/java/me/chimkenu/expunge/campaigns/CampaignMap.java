package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public interface CampaignMap {
    String directory();

    Vector startLocation();

    BoundingBox endRegion();

    BoundingBox[] pathRegions();

    Vector[] spawnLocations();

    Vector[] bossLocations();

    Vector[] itemLocations();

    int baseItemsToSpawn();

    Vector[] weaponLocations();

    Vector[] ammoLocations();

    Vector buttonLocation();

    Vector[] rescueClosetLocations();

    GameAction runAtStart();

    GameAction runAtEnd();

    Listener[] happenings(JavaPlugin plugin, GameManager gameManager);

    default GameListener[] gameListeners(JavaPlugin plugin, GameManager gameManager) {
        BreakGlassListener breakGlassListener = new BreakGlassListener(plugin, gameManager);
        return new GameListener[]{
                new AmmoPileListener(plugin, gameManager),
                new DeathReviveListener(plugin, gameManager),
                new InventoryListener(plugin, gameManager),
                new MobListener(plugin, gameManager),
                new NextMapListener(plugin, gameManager),
                new PickUpListener(plugin, gameManager),
                new ShootListener(plugin, gameManager, breakGlassListener),
                new ShoveListener(plugin, gameManager, breakGlassListener),
                new SwingListener(plugin, gameManager, breakGlassListener),
                new JoinLeaveListener(plugin, gameManager),
                new UtilityListener(plugin, gameManager),
                breakGlassListener,
                new BreakDoorListener(plugin, gameManager),
                new InteractableListener(plugin, gameManager)
        };
    }
}
