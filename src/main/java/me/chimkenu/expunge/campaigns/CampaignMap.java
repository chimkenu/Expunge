package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public abstract class CampaignMap {
    public abstract String directory();

    public abstract Vector startLocation();

    public abstract BoundingBox endRegion();

    public abstract BoundingBox[] pathRegions();

    public abstract Vector[] spawnLocations();

    public abstract Vector[] bossLocations();

    public abstract Vector[] itemLocations();

    public abstract int baseItemsToSpawn();

    public abstract Vector[] weaponLocations();

    public abstract Vector[] ammoLocations();

    public abstract Vector buttonLocation();

    public abstract GameAction runAtStart();

    public abstract GameAction runAtEnd();

    public abstract boolean isStartSafeRoom();

    public abstract Listener[] happenings(LocalGameManager localGameManager);

    public abstract GameListener[] gameListeners(JavaPlugin plugin, LocalGameManager localGameManager);
}
