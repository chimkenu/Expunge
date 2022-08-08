package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.game.maps.thedeparture.cutscenes.HighwayHelicopterCrash;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class Highway {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1115, 24, 1202, 1089, 24, 1212));
        pathRegions.add(new BoundingBox(1099, 35, 1210, 1075, 35, 1221));
        pathRegions.add(new BoundingBox(1069, 35, 1227, 1045, 35, 1241));
        pathRegions.add(new BoundingBox(1045, 35, 1227, 1013, 35, 1255));
        pathRegions.add(new BoundingBox(1012, 35, 1255, 997, 35, 1286));
        pathRegions.add(new BoundingBox(997, 35, 1288, 1011, 35, 1328));
        pathRegions.add(new BoundingBox(1009, 35, 1330, 1028, 35, 1350));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 988.5, 39, 1218.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1237.5));
        spawnLocations.add(new Location(world, 1130.5, 37, 1205.5));
        spawnLocations.add(new Location(world, 1087.5, 36, 1233.5));
        spawnLocations.add(new Location(world, 995.5, 37, 1247.5));
        spawnLocations.add(new Location(world, 1016.5, 37, 1261.5));
        spawnLocations.add(new Location(world, 990.5, 37, 1266.5));
        spawnLocations.add(new Location(world, 1020.5, 37, 1284.5));
        spawnLocations.add(new Location(world, 1020.5, 40, 1304.5));
        spawnLocations.add(new Location(world, 987.5, 37, 1354.5));
        spawnLocations.add(new Location(world, 989.5, 41, 1334.5));
        spawnLocations.add(new Location(world, 1037.5, 37, 1340.5));
        spawnLocations.add(new Location(world, 1009.5, 36, 1363.5));
        spawnLocations.add(new Location(world, 990.5, 37, 1264.5));
        spawnLocations.add(new Location(world, 1028.5, 37, 1201.5));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1004.5, 36, 1316.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1087.3, 27, 1206));
        itemLocations.add(new Location(world, 1107, 25, 1199));
        itemLocations.add(new Location(world, 1038.5, 37, 1217.5));
        itemLocations.add(new Location(world, 1036.5, 37, 1216.5));
        itemLocations.add(new Location(world, 1125.5, 26, 1203.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1036.5, 37, 1217.5));
        weaponLocations.add(new Location(world, 1120, 26, 1206.5));
        weaponLocations.add(new Location(world, 1122.5, 27, 1207));

        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1120.5, 26, 1204.5));

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void highwayManhole(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1114, 24, 1209, 1108, 33, 1201);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_MANHOLE);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwayOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1100, 36, 1209, 1096, 39, 1213);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwaySafeHouse(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(990, 34, 1291, 1021, 56, 1297);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                new HighwayHelicopterCrash(Expunge.playing.getKeys(), Expunge.runningDirector.mobHandler.getActiveMobs().stream().toList()).play();
                playDialogue(DepartureDialogue.HIGHWAY_SAFE_HOUSE);
                HandlerList.unregisterAll(this);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Scene.playCrescendoEventEffect();
                        Expunge.runningDirector.bile(e.getPlayer(), 30);
                        Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(997, 35, 1362, 1040, 35, 1343), 30 + (10 * Expunge.currentDifficulty.ordinal()));
                    }
                }.runTaskLater(Expunge.instance, 20 * 5);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void highwayPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1019, 39, 1276, 1021, 43, 1273);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.HIGHWAY_PURPLE_CAR);
                HandlerList.unregisterAll(this);
            }
        });

        return new Scene(
                new Location(world, 1122.5, 26, 1205.5),
                new BoundingBox(1031, 34, 1352, 1039, 40, 1360),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1033, 37, 1354),
                player -> {
                    world.getBlockAt(new Location(world, 1127, 26, 1205)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1033, 36, 1360)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1032, 36, 1360)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1117, 26, 1205)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1031, 37, 1353)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1031, 36, 1353)).setType(Material.AIR);
                },
                null,
                null,
                happenings
        );
    }
}
