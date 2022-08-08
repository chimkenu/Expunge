package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class Stadium {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1044, 35, 1363, 1021, 35, 1400));
        pathRegions.add(new BoundingBox(1011, 35, 1403, 997, 35, 1463));
        pathRegions.add(new BoundingBox(1016, 35, 1448, 1074, 35, 1481));
        pathRegions.add(new BoundingBox(1075, 35, 1480, 1107, 35, 1466));
        pathRegions.add(new BoundingBox(1108, 35, 1466, 1264, 35, 1534));
        pathRegions.add(new BoundingBox(1239, 35, 1536, 1133, 35, 1569));
        pathRegions.add(new BoundingBox(1132, 35, 1571, 1252, 35, 1693));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1043.5, 36, 1344.5));
        spawnLocations.add(new Location(world, 1042.5, 36, 1378));
        spawnLocations.add(new Location(world, 1005.5, 36, 1398.5));
        spawnLocations.add(new Location(world, 999.5, 36, 1378.5));
        spawnLocations.add(new Location(world, 999.5, 36, 1437.5));
        spawnLocations.add(new Location(world, 1031.5, 37, 1474.5));
        spawnLocations.add(new Location(world, 1023.5, 37, 1476.5));
        spawnLocations.add(new Location(world, 1057.5, 37, 1490.5));
        spawnLocations.add(new Location(world, 1089.5, 36, 1470.5));
        spawnLocations.add(new Location(world, 1229.5, 36, 1573.5));
        spawnLocations.add(new Location(world, 1246.5, 38, 1539.5));
        spawnLocations.add(new Location(world, 1127.5, 38, 1552.5));
        spawnLocations.add(new Location(world, 1098.5, 45, 1458.5));
        spawnLocations.add(new Location(world, 1181, 36, 1594.5));
        spawnLocations.add(new Location(world, 1241.5, 36, 1611.5));
        spawnLocations.add(new Location(world, 1241.5, 36, 1665.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1665.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1611.5));
        spawnLocations.add(new Location(world, 1245.5, 45, 1642));
        spawnLocations.add(new Location(world, 1127.5, 45, 1635));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1236.5, 36, 1662.5));
        bossLocations.add(new Location(world, 1136.5, 36, 1613.5));
        bossLocations.add(new Location(world, 1186.5, 45, 1591.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1175.5, 37, 1658.5));
        itemLocations.add(new Location(world, 1192.5, 36.6, 1656.5));
        itemLocations.add(new Location(world, 1163, 37.5, 1575.5));
        itemLocations.add(new Location(world, 1194.5, 37, 1546.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1548.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1604.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1672.5));
        itemLocations.add(new Location(world, 1186.5, 46, 1684.5));
        itemLocations.add(new Location(world, 1134.5, 36, 1665.5));
        itemLocations.add(new Location(world, 1139.5, 37, 1645.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1209.5, 36.5, 1576.5));
        weaponLocations.add(new Location(world, 1183.5, 37, 1585.5));
        weaponLocations.add(new Location(world, 1192.5, 36, 1599.5));
        weaponLocations.add(new Location(world, 1248, 45, 1637));
        weaponLocations.add(new Location(world, 1128.5, 46.2, 1644.5));

        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1175.5, 37, 1658.5));

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumParkingLotA(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1034, 34, 1449, 1042, 50, 1500);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1108, 35, 1466, 1170, 35, 1534), 20 + (Expunge.currentDifficulty.ordinal() * 15));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumParkingLotB(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1106, 34, 1492, 1102, 55, 1455);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.bile(e.getPlayer(), 5);
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1179, 35, 1466, 1239, 35, 1569), 20 + (Expunge.currentDifficulty.ordinal() * 10));
                playDialogue(DepartureDialogue.STADIUM_PARKING_LOT);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumEnter(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1182, 34, 1580, 1190, 39, 1587);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STADIUM_ENTER);
                Expunge.runningDirector.bile(e.getPlayer(), 5);
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1219, 35, 1616, 1153, 35, 1660), 30 + (Expunge.currentDifficulty.ordinal() * 10));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumEnding(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1114, 24, 1209, 1108, 33, 1201);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STADIUM_ENDING);
                HandlerList.unregisterAll(this);
            }
        });

        return new Scene(
                new Location(world, 1033.5, 36, 1353.5),
                new BoundingBox(1210, 34, 1635, 1204, 41, 1641),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                10,
                weaponLocations,
                ammoLocations,
                new Location(world, 1033, 37, 1354),
                player -> {
                    world.getBlockAt(new Location(world, 1031, 37, 1353)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1031, 36, 1353)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1033, 36, 1360)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1032, 36, 1360)).setType(Material.AIR);

                    for (int i = 0; i < 4; i++) {
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1031.3, 37, 1358), new Medkit());
                    }
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1034.5, 37, 1359), ItemHandler.getRandomGun(Tier.TIER2), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1034.5, 37, 1357), ItemHandler.getRandomMelee(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1038.5, 37, 1352.5), new Pills());
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1037.5, 36, 1359.5), new Adrenaline());

                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1011, 35, 1375, 997, 35, 1428), 30 + (Expunge.currentDifficulty.ordinal() * 10));
                },
                null,
                null,
                happenings
        );
    }
}