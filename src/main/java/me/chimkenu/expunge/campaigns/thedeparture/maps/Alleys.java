package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
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

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Alleys {
    public static CampaignMap getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1143, 42, 916, 1156, 42, 923));
        pathRegions.add(new BoundingBox(1151, 42, 922, 1155, 42, 938));
        pathRegions.add(new BoundingBox(1152, 42, 933, 1168, 42, 948));
        pathRegions.add(new BoundingBox(1151, 42, 934, 1131, 42, 961));
        pathRegions.add(new BoundingBox(1147, 42, 951, 1175, 42, 973));
        pathRegions.add(new BoundingBox(1157, 42, 970, 1153, 42, 988));
        pathRegions.add(new BoundingBox(1153, 42, 988, 1130, 42, 984));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1155.5, 43, 914.5));
        spawnLocations.add(new Location(world, 1150.5, 43, 929));
        spawnLocations.add(new Location(world, 1167.5, 43, 937));
        spawnLocations.add(new Location(world, 1176.5, 43, 971));
        spawnLocations.add(new Location(world, 1129.5, 43, 950.5));
        spawnLocations.add(new Location(world, 1155.5, 44, 965.5));
        spawnLocations.add(new Location(world, 1166.5, 43, 961.5));
        spawnLocations.add(new Location(world, 1150.5, 43, 958.5));
        spawnLocations.add(new Location(world, 1147.5, 43, 951.5));
        spawnLocations.add(new Location(world, 1139.5, 44, 974.5));
        spawnLocations.add(new Location(world, 1149.5, 43, 990.5));
        spawnLocations.add(new Location(world, 1130.5, 43, 972.5));
        spawnLocations.add(new Location(world, 1126.5, 43, 978.5));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1155, 42, 978));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1148, 44, 980.5));
        itemLocations.add(new Location(world, 1166, 44, 965.5));
        itemLocations.add(new Location(world, 1180.5, 44, 965.5));
        itemLocations.add(new Location(world, 1175.5, 44, 949));
        itemLocations.add(new Location(world, 1167, 44, 934.5));
        itemLocations.add(new Location(world, 1158, 44, 948.5));
        itemLocations.add(new Location(world, 1154.5, 44, 944.5));
        itemLocations.add(new Location(world, 1139.5, 44, 955));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1163.5, 45, 922.5));
        weaponLocations.add(new Location(world, 1140, 44, 950.7));
        weaponLocations.add(new Location(world, 1133.5, 43, 943.5));
        weaponLocations.add(new Location(world, 1137.5, 44, 993.5));

        ArrayList<Location> ammoLocations = new ArrayList<>();

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void alleysOpening(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1157, 42, 933, 1148, 51, 929);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.ALLEYS_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void alleysPurpleCar(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1147, 43, 935, 1142, 47, 942);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(List.of(e.getPlayer()));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void alleysSafeHouse(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1147, 42, 983, 1141, 56, 990);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.ALLEYS_SAFE_HOUSE);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void finaleBegin(PlayerInteractEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                    return;
                }
                if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    return;
                }
                if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON)) {
                    return;
                }
                if (e.getClickedBlock().getLocation().toVector().equals(new Vector(1126, 44, 997))) {
                    Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
                }
            }
        });

        return new CampaignMap(
                new Location(world, 1139, 43, 916),
                new BoundingBox(1128, 43, 979, 1118, 53, 1002),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 1124, 44, 987),
                player -> {
                    world.getBlockAt(new Location(world, 1119, 43, 988)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1127, 43, 986)).setType(Material.AIR);

                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1140.7, 44, 917), new Pills());
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1140.7, 44, 915), new Adrenaline());
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1138, 44, 914.3), ItemHandler.getRandomGun(Tier.TIER1), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1138, 44, 917.7), ItemHandler.getRandomMelee(Tier.TIER1), false);

                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1174, 42, 945, 1124, 42, 977), 30);
                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1157, 42, 973, 1129, 42, 989), 20);
                },
                null,
                false,
                happenings
        );
    }
}
