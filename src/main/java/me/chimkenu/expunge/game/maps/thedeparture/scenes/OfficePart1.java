package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.weapons.melees.Crowbar;
import me.chimkenu.expunge.guns.weapons.melees.FireAxe;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import me.chimkenu.expunge.guns.weapons.melees.Nightstick;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class OfficePart1 {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(950, 8, 891, 963, 8, 901));
        pathRegions.add(new BoundingBox(963, 8, 903, 955, 8, 915));
        pathRegions.add(new BoundingBox(953, 8, 903, 936, 8, 915));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 972, 9, 876));
        spawnLocations.add(new Location(world, 939.5, 9, 877.5));
        spawnLocations.add(new Location(world, 934, 10, 898));
        spawnLocations.add(new Location(world, 964.5, 9, 902.5));
        spawnLocations.add(new Location(world, 956, 9, 920));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 951.5, 9, 909.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 956, 10, 878.5));
        itemLocations.add(new Location(world, 958, 10, 878.5));
        itemLocations.add(new Location(world, 950, 10, 890));
        itemLocations.add(new Location(world, 941.5, 10, 871.5));
        itemLocations.add(new Location(world, 964.5, 9, 873.5));
        itemLocations.add(new Location(world, 974.5, 10, 907.5));
        itemLocations.add(new Location(world, 978.5, 9, 902.5));
        itemLocations.add(new Location(world, 947.5, 10, 914.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();

        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 953.5, 10, 873.5));

        return new Scene(
                new Location(world, 952.5, 9, 872.5),
                new BoundingBox(930, 8, 905, 925, 13, 910),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 929, 10, 909),
                player -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 9 877 minecraft:birch_door[half=lower,facing=south,hinge=left,open=false] destroy");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 10 877 minecraft:birch_door[half=upper,facing=south,hinge=left,open=false] destroy");
                    for (int i = 0; i < 4; i++) {
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 957, 10, 878.3), new Medkit());
                    }
                    ArrayList<Melee> meleeWeapons = new ArrayList<>();
                    meleeWeapons.add(new FireAxe());
                    meleeWeapons.add(new Crowbar());
                    meleeWeapons.add(new Nightstick());
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 949.5, 10, 878.5), meleeWeapons.get(ThreadLocalRandom.current().nextInt(0, meleeWeapons.size())), true);

                    playDialogue(DepartureDialogue.OFFICE_OPENING);
                },
                null,
                false,
                null
        );
    }
}
