package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.Action;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

public abstract class CampaignMap() {

    public static void playCrescendoEventEffect(Player... players) {
        for (Player p : players) {
            p.sendRichMessage("<Yellow>Here they come...");
            p.playSound(p, Sound.AMBIENT_CAVE, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    public Location startLocation() {
        return startLocation;
    }

    public BoundingBox endRegion() {
        return endRegion;
    }

    public BoundingBox[] pathRegions() {
        return pathRegions;
    }

    public Location[] spawnLocations() {
        return spawnLocations;
    }

    public Location[] bossLocations() {
        return bossLocations;
    }

    public Location[] itemLocations() {
        return itemLocations;
    }

    public int baseItemsToSpawn() {
        return baseItemsToSpawn;
    }

    public Location[] weaponLocations() {
        return weaponLocations;
    }

    public Location[] ammoLocations() {
        return ammoLocations;
    }

    public Location buttonLocation() {
        return buttonLocation;
    }

    public Action runAtStart() {
        return runAtStart;
    }

    public Action runAtEnd() {
        return runAtEnd;
    }

    public boolean isStartSafeRoom() {
        return isStartSafeRoom;
    }

    public Listener[] happenings() {
        return happenings;
    }
}
