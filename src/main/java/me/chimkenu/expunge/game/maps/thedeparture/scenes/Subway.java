package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
import me.chimkenu.expunge.guns.weapons.melees.MrCookie;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class Subway {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1112, 41, 969, 1086, 41, 1019));
        pathRegions.add(new BoundingBox(1068, 32, 1001, 1064, 32, 1008));
        pathRegions.add(new BoundingBox(1064, 24, 1016, 1069, 42, 1020));
        pathRegions.add(new BoundingBox(1078, 15, 1016, 1089, 15, 1030));
        pathRegions.add(new BoundingBox(1089, 15, 1030, 1103, 15, 1016));
        pathRegions.add(new BoundingBox(1090, 26, 1042, 1104, 26, 1049));
        pathRegions.add(new BoundingBox(1087, 24, 1050, 1133, 24, 1056));
        pathRegions.add(new BoundingBox(1133, 42, 1046, 1145, 24, 1070));
        pathRegions.add(new BoundingBox(1145, 24, 1070, 1163, 24, 1046));
        pathRegions.add(new BoundingBox(1163, 24, 1046, 1177, 24, 1072));
        pathRegions.add(new BoundingBox(1179, 24, 1073, 1198, 24, 1054));
        pathRegions.add(new BoundingBox(1182, 24, 1078, 1206, 24, 1101));
        pathRegions.add(new BoundingBox(1202, 24, 1103, 1186, 24, 1127));
        pathRegions.add(new BoundingBox(1183, 24, 1131, 1195, 24, 1143));
        pathRegions.add(new BoundingBox(1181, 24, 1133, 1169, 24, 1151));
        pathRegions.add(new BoundingBox(1165, 24, 1155, 1147, 24, 1161));
        pathRegions.add(new BoundingBox(1144, 24, 1161, 1168, 24, 1179));
        pathRegions.add(new BoundingBox(1154, 24, 1179, 1142, 24, 1196));
        pathRegions.add(new BoundingBox(1141, 24, 1197, 1129, 24, 1208));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1080.5, 43, 977));
        spawnLocations.add(new Location(world, 1082.5, 43, 1017));
        spawnLocations.add(new Location(world, 1093, 21, 1011.5));
        spawnLocations.add(new Location(world, 1085.5, 25, 1053.5));
        spawnLocations.add(new Location(world, 1107.5, 25, 1060.5));
        spawnLocations.add(new Location(world, 1100.5, 21, 1077));
        spawnLocations.add(new Location(world, 1107.5, 25, 1066.5));
        spawnLocations.add(new Location(world, 1131.5, 25, 1060.5));
        spawnLocations.add(new Location(world, 1133.5, 25, 1047.5));
        spawnLocations.add(new Location(world, 1133.5, 25, 1069.5));
        spawnLocations.add(new Location(world, 1161.5, 26, 1076.5));
        spawnLocations.add(new Location(world, 1179.5, 26, 1087.5));
        spawnLocations.add(new Location(world, 1137.5, 26, 1063.5));
        spawnLocations.add(new Location(world, 1183.5, 26, 1052.5));
        spawnLocations.add(new Location(world, 1177.5, 25, 1072.5));
        spawnLocations.add(new Location(world, 1205.5, 25, 1101.5));
        spawnLocations.add(new Location(world, 1193.5, 26, 1089.5));
        spawnLocations.add(new Location(world, 1191.5, 25, 1101.5));
        spawnLocations.add(new Location(world, 1183.5, 26, 1141.5));
        spawnLocations.add(new Location(world, 1156.5, 25, 1162.5));
        spawnLocations.add(new Location(world, 1145.5, 25, 1161.5));
        spawnLocations.add(new Location(world, 1167.5, 25, 1179.5));
        spawnLocations.add(new Location(world, 1151.5, 26, 1164.5));
        spawnLocations.add(new Location(world, 1151.5, 26, 1176.5));
        spawnLocations.add(new Location(world, 1127.5, 25, 1208.5));
        spawnLocations.add(new Location(world, 1127.5, 25, 1202.5));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1189.5, 25, 1063.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1191.5, 27, 1051.2));
        itemLocations.add(new Location(world, 1090.3, 28, 1074));
        itemLocations.add(new Location(world, 1165.5, 25, 1195.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1155.5, 25, 1172));
        weaponLocations.add(new Location(world, 1195, 27, 1094.5));
        weaponLocations.add(new Location(world, 1187, 27, 1053.7));
        weaponLocations.add(new Location(world, 1104.7, 28, 1075));

        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1122.5, 50, 998.5));

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void subwayOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1118, 42, 990, 1113, 46, 986);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1074, 19, 1025, 1069, 29, 1030);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(List.of(e.getPlayer()));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwaySpawnZombies(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1069, 32, 1008, 1064, 38, 1001);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1103, 15, 1030, 1078, 15, 1016), 30);
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1104, 26, 1042, 1090, 26, 1048), 15);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayMrCookie(PlayerInteractEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                    if (!e.getClickedBlock().getLocation().toVector().equals(new Vector(1085, 16, 1017)))
                        return;
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1084.5, 16.3, 1017.5), new MrCookie(), false);
                    playDialogue(DepartureDialogue.SUBWAY_MR_COOKIE);
                    HandlerList.unregisterAll(this);
                }
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwayMap(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1091, 14, 1031, 1104, 23, 1015);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_MAP);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void subwaySafeZone(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1129, 24, 1201, 1151, 31, 1197);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.SUBWAY_SAFE_ZONE);
                HandlerList.unregisterAll(this);
            }
        });

        return new Scene(
                new Location(world, 1124.5, 43, 986.5),
                new BoundingBox(1128, 25, 1203, 1116, 30, 1207),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1122, 27, 1204),
                player -> {
                    world.getBlockAt(new Location(world, 1127, 43, 986)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1119, 43, 988)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1117, 26, 1205)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1127, 26, 1205)).setType(Material.AIR);
                    for (int i = 0; i < 4; i++) {
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1123.5, 44, 996.5), new Medkit());
                    }
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1119.3, 44, 983), ItemHandler.getRandomGun(Tier.TIER2), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1122.7, 44, 981), ItemHandler.getRandomGun(Tier.TIER2), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1119.3, 44.5, 994.5), ItemHandler.getRandomMelee(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1121, 50.2, 984), new Pills());
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1126, 50.2, 983), new Adrenaline());

                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1112, 41, 994, 1086, 41, 1011), 45);
                },
                null,
                null,
                happenings
        );
    }
}
