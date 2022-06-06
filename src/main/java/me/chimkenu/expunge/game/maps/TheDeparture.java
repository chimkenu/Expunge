package me.chimkenu.expunge.game.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.guns.weapons.melees.*;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class TheDeparture extends Map {
    private static ArrayList<Scene> allScenes() {
        ArrayList<Scene> scenes = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        if (world == null) return scenes;

        // scene 0 - office
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

        scenes.add(new Scene(
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
                null,
                null,
                player -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 9 877 minecraft:birch_door[half=lower,facing=south,hinge=left,open=false] destroy");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "/execute in minecraft:overworld run setblock 952 10 877 minecraft:birch_door[half=upper,facing=south,hinge=left,open=false] destroy");
                    for (int i = 0; i < 4; i++) {
                        Director.spawnUtility(world, new Location(world, 957, 10, 878.3), new Medkit());
                    }
                    ArrayList<Melee> meleeWeapons = new ArrayList<>();
                    meleeWeapons.add(new FireAxe());
                    meleeWeapons.add(new Crowbar());
                    meleeWeapons.add(new Nightstick());
                    Director.spawnWeapon(world, new Location(world, 949.5, 10, 878.5), meleeWeapons.get(ThreadLocalRandom.current().nextInt(0, meleeWeapons.size())), true);
                },
                null,
                null,
                null
        ));

        // scene 1 - office
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(853, 76, 921, 845, 76, 940));
        pathRegions.add(new BoundingBox(854, 76, 938, 878, 76, 915));
        pathRegions.add(new BoundingBox(870, 76, 919, 863, 76, 904));
        pathRegions.add(new BoundingBox(866, 72, 897, 869, 72, 897));
        pathRegions.add(new BoundingBox(866, 56, 897, 869, 56, 897));
        pathRegions.add(new BoundingBox(865, 52, 904, 870, 52, 933));
        pathRegions.add(new BoundingBox(862, 52, 933, 852, 52, 914));
        pathRegions.add(new BoundingBox(845, 52, 921, 839, 52, 910));
        pathRegions.add(new BoundingBox(836, 43, 924, 828, 43, 904));
        pathRegions.add(new BoundingBox(838, 42, 904, 847, 42, 922));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 843, 77, 924));
        spawnLocations.add(new Location(world, 858.5, 77, 940.5));
        spawnLocations.add(new Location(world, 875, 77, 949));
        spawnLocations.add(new Location(world, 859, 77, 906));
        spawnLocations.add(new Location(world, 857.5, 77, 930.5));
        spawnLocations.add(new Location(world, 875, 53, 936));
        spawnLocations.add(new Location(world, 860.5, 53, 913.5));
        spawnLocations.add(new Location(world, 849.5, 53, 921.5));
        spawnLocations.add(new Location(world, 849.5, 43, 912.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 843.5, 44, 913.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 862.5, 54, 916.5));
        itemLocations.add(new Location(world, 839.5, 52, 928.5));
        itemLocations.add(new Location(world, 845.5, 54, 927.5));
        itemLocations.add(new Location(world, 834.5, 45, 920.5));
        itemLocations.add(new Location(world, 845, 44, 907.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 844.5, 43, 919.7));
        ammoLocations = new ArrayList<>();

        scenes.add(new Scene(
                new Location(world, 851, 77, 913),
                new BoundingBox(872, 42, 906, 878, 47, 913),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                2,
                weaponLocations,
                ammoLocations,
                new Location(world, 876, 44, 907),
                null,
                null,
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.AIR);
                    Director.spawnWeapon(world, new Location(world, 864.5, 54, 909.5), new FryingPan(), false);
                },
                null,
                new Listener() {
                    @EventHandler
                    public void onEnterVent(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && block.getLocation().toVector().equals(new Vector(846, 45, 925)))) {
                            return;
                        }

                        Scene.playCrescendoEventEffect();
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                Expunge.runningDirector.spawnMob(world, new Location(world, 845.5, 48, 928.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 853.5, 47, 939.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 866.5, 48, 939.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 872.5, 48, 931.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 876.5, 48, 925.5), Zombie.class);
                                i++;
                                if (i >= 5) {
                                    this.cancel();
                                }
                                if (Expunge.currentSceneIndex != 1) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Expunge.instance, 1, 20 * 4);

                        // crescendo events are only meant to happen once
                        // hence it unregisters the event when it executes
                        HandlerList.unregisterAll(this);
                    }
                },
                null
        ));

        // scene 2 - streets
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(870, 42, 908, 864, 42, 921));
        pathRegions.add(new BoundingBox(861, 42, 916, 854, 42, 922));
        pathRegions.add(new BoundingBox(854, 42, 913, 858, 42, 890));
        pathRegions.add(new BoundingBox(863, 41, 889, 1112, 41, 863));
        pathRegions.add(new BoundingBox(1086, 41, 888, 1112, 41, 907));
        pathRegions.add(new BoundingBox(1127, 50, 899, 1129, 50, 920));
        pathRegions.add(new BoundingBox(1129, 56, 920, 1127, 56, 899));
        pathRegions.add(new BoundingBox(1141, 42, 897, 1138, 42, 912));
        spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 820.5, 43, 896.5));
        spawnLocations.add(new Location(world, 871.5, 43, 918.5));
        spawnLocations.add(new Location(world, 857.5, 43, 928.5));
        spawnLocations.add(new Location(world, 894, 43, 855.5));
        spawnLocations.add(new Location(world, 927, 43, 896.5));
        spawnLocations.add(new Location(world, 949.5, 43, 884.5));
        spawnLocations.add(new Location(world, 956.5, 43, 854.5));
        spawnLocations.add(new Location(world, 981.5, 43, 900.5));
        spawnLocations.add(new Location(world, 984.5, 43, 855.5));
        spawnLocations.add(new Location(world, 1025.5, 43, 896.5));
        spawnLocations.add(new Location(world, 1038, 43, 856.5));
        spawnLocations.add(new Location(world, 1084.5, 43, 852));
        spawnLocations.add(new Location(world, 1145, 43, 895.5));
        spawnLocations.add(new Location(world, 1129.5, 43, 922.5));
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1128.5, 56.07, 905.5));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 854.5, 44, 910.5));
        itemLocations.add(new Location(world, 1124.5, 51, 901.5));
        itemLocations.add(new Location(world, 1135.5, 51, 911.5));
        itemLocations.add(new Location(world, 1120.5, 50, 917.5));
        itemLocations.add(new Location(world, 1136.5, 51, 920));
        itemLocations.add(new Location(world, 1136, 56.6, 917));
        itemLocations.add(new Location(world, 1118, 56, 912));
        itemLocations.add(new Location(world, 1134, 57, 911.5));
        itemLocations.add(new Location(world, 1124.5, 56, 905.5));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1129.5, 58, 896.5));
        ammoLocations = new ArrayList<>();
        scenes.add(new Scene(
                new Location(world, 875.5, 43, 911.5),
                new BoundingBox(1136, 42, 918, 1141, 47, 913),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                3,
                weaponLocations,
                ammoLocations,
                new Location(world, 1137, 44, 916),
                null,
                null,
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.AIR);
                    for (int i = 0; i < 4; i++) {
                        Director.spawnUtility(world, new Location(world, 877.5, 44, 910.5), new Medkit());
                    }
                    Director.spawnWeapon(world, new Location(world, 873.5, 44, 907.5), Director.getRandomGun(Weapons.Tier.TIER2), true);
                    Director.spawnWeapon(world, new Location(world, 875.5, 44, 907.5), Director.getRandomGun(Weapons.Tier.TIER2), true);

                    Director.spawnUtility(world, new Location(world, 1135, 44, 919), new Medkit());
                    Director.spawnWeapon(world, new Location(world, 857.5, 44, 910.5), Director.getRandomGun(Weapons.Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 857.5, 44, 907.5), Director.getRandomGun(Weapons.Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 1135, 44, 910), Director.getRandomGun(Weapons.Tier.TIER1), false);
                    Director.spawnWeapon(world, new Location(world, 1135, 44, 907), Director.getRandomMelee(), false);

                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(863, 41, 889, 1112, 41, 863), 30, false);
                },
                null,
                new Listener() {
                    @EventHandler
                    public void onStepInApartmentBuilding(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(1120, 43, 898)) || block.getLocation().toVector().equals(new Vector(1120, 43, 899))))) {
                            return;
                        }

                        Scene.playCrescendoEventEffect();
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;
                            @Override
                            public void run() {
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1121.5, 43, 922.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1118.5, 43, 843), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1076.5, 43, 894.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1134, 43, 898.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1125.5, 50, 899.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1131.5, 50, 909.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1125.5, 50, 910.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1131.5, 50, 920.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1131.5, 50, 920.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1131.5, 56, 920.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1125.5, 56, 910.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1131.5, 56, 909.5), Zombie.class);
                                Expunge.runningDirector.spawnMob(world, new Location(world, 1125.5, 56, 899.5), Zombie.class);
                                i++;
                                if (i >= 3) {
                                    this.cancel();
                                }
                                if (Expunge.currentSceneIndex != 2) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(Expunge.instance, 1, 20 * 4);
                        HandlerList.unregisterAll(this);
                    }
                },
                null
        ));

        // scene 3 - alleyway streets
        pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1143, 42, 916, 1156, 42, 923));
        pathRegions.add(new BoundingBox(1151, 42, 922, 1155, 42, 938));
        pathRegions.add(new BoundingBox(1152, 42, 933, 1168, 42, 948));
        pathRegions.add(new BoundingBox(1151, 42, 934, 1131, 42, 961));
        pathRegions.add(new BoundingBox(1147, 42, 951, 1175, 42, 973));
        pathRegions.add(new BoundingBox(1157, 42, 970, 1153, 42, 988));
        pathRegions.add(new BoundingBox(1153, 42, 988, 1130, 42, 984));
        spawnLocations = new ArrayList<>();
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
        bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1155, 42, 978));
        itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1148, 44, 980.5));
        itemLocations.add(new Location(world, 1166, 44, 965.5));
        itemLocations.add(new Location(world, 1180.5, 44, 965.5));
        itemLocations.add(new Location(world, 1175.5, 44, 949));
        itemLocations.add(new Location(world, 1167, 44, 934.5));
        itemLocations.add(new Location(world, 1158, 44, 948.5));
        itemLocations.add(new Location(world, 1154.5, 44, 944.5));
        itemLocations.add(new Location(world, 1139.5, 44, 955));
        weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1163.5, 45, 922.5));
        weaponLocations.add(new Location(world, 1140, 44, 950.7));
        weaponLocations.add(new Location(world, 1133.5, 43, 943.5));
        weaponLocations.add(new Location(world, 1137.5, 44, 993.5));
        ammoLocations = new ArrayList<>();
        scenes.add(new Scene(
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
                null,
                null,
                player -> {
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1119, 43, 988)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.BEEHIVE);

                    Director.spawnUtility(world, new Location(world, 1140.7, 44, 917), new Pills());
                    Director.spawnUtility(world, new Location(world, 1140.7, 44, 915), new Adrenaline());
                    Director.spawnWeapon(world, new Location(world, 1138, 44, 914.3), Director.getRandomGun(Weapons.Tier.TIER1), true);
                    Director.spawnWeapon(world, new Location(world, 1138, 44, 917.7), Director.getRandomMelee(), false);

                    Expunge.runningDirector.spawnAtRandomLocations(world, new BoundingBox(1140, 42, 973, 1175, 42, 970), 20, false);
                },
                null,
                null,
                null
        ));
        return scenes;
    }

    public TheDeparture() {
        super("The Departure", allScenes(), Bukkit.getWorld("world"));
    }
}
