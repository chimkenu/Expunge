package me.chimkenu.expunge.game.maps;

import me.chimkenu.expunge.Action;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

public record Scene(Location startLocation, BoundingBox endRegion,
                    ArrayList<BoundingBox> pathRegions,
                    ArrayList<Location> spawnLocations,
                    ArrayList<Location> bossLocations,
                    ArrayList<Location> itemLocations,
                    int baseItemsToSpawn,
                    ArrayList<Location> weaponLocations,
                    ArrayList<Location> ammoLocations,
                    Location buttonLocation,
                    Action runAtStart,
                    Action runAtEnd,
                    boolean isStartSafeRoom,
                    ArrayList<Listener> happenings) {

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
    public ArrayList<BoundingBox> pathRegions() {
        return pathRegions;
    }

    @Override
    public ArrayList<Location> spawnLocations() {
        return spawnLocations;
    }

    @Override
    public ArrayList<Location> bossLocations() {
        return bossLocations;
    }

    @Override
    public ArrayList<Location> itemLocations() {
        return itemLocations;
    }

    @Override
    public int baseItemsToSpawn() {
        return baseItemsToSpawn;
    }

    @Override
    public ArrayList<Location> weaponLocations() {
        return weaponLocations;
    }

    @Override
    public ArrayList<Location> ammoLocations() {
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
    public ArrayList<Listener> happenings() {
        return happenings;
    }
}
