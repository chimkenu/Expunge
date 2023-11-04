package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.Action;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

public record CampaignMap(Location startLocation, BoundingBox endRegion,
                          BoundingBox[] pathRegions,
                          Location[] spawnLocations,
                          Location[] bossLocations,
                          Location[] itemLocations,
                          int baseItemsToSpawn,
                          Location[] weaponLocations,
                          Location[] ammoLocations,
                          Location buttonLocation,
                          Action runAtStart,
                          Action runAtEnd,
                          boolean isStartSafeRoom,
                          Listener[] happenings) {

    public static void playCrescendoEventEffect() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.YELLOW + "Here they come...");
            p.playSound(p, Sound.AMBIENT_CAVE, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }

    @Override
    public Location startLocation() {
        return startLocation;
    }

    @Override
    public BoundingBox endRegion() {
        return endRegion;
    }

    @Override
    public BoundingBox[] pathRegions() {
        return pathRegions;
    }

    @Override
    public Location[] spawnLocations() {
        return spawnLocations;
    }

    @Override
    public Location[] bossLocations() {
        return bossLocations;
    }

    @Override
    public Location[] itemLocations() {
        return itemLocations;
    }

    @Override
    public int baseItemsToSpawn() {
        return baseItemsToSpawn;
    }

    @Override
    public Location[] weaponLocations() {
        return weaponLocations;
    }

    @Override
    public Location[] ammoLocations() {
        return ammoLocations;
    }

    @Override
    public Location buttonLocation() {
        return buttonLocation;
    }

    @Override
    public Action runAtStart() {
        return runAtStart;
    }

    @Override
    public Action runAtEnd() {
        return runAtEnd;
    }

    @Override
    public boolean isStartSafeRoom() {
        return isStartSafeRoom;
    }

    @Override
    public Listener[] happenings() {
        return happenings;
    }
}
