package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.mobs.common.Horde;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class Streets {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(870, 42, 908, 864, 42, 921));
        pathRegions.add(new BoundingBox(861, 42, 916, 854, 42, 922));
        pathRegions.add(new BoundingBox(854, 42, 913, 858, 42, 890));
        pathRegions.add(new BoundingBox(863, 41, 889, 1112, 41, 863));
        pathRegions.add(new BoundingBox(1086, 41, 888, 1112, 41, 907));
        pathRegions.add(new BoundingBox(1127, 50, 899, 1129, 50, 920));
        pathRegions.add(new BoundingBox(1129, 56, 920, 1127, 56, 899));
        pathRegions.add(new BoundingBox(1141, 42, 897, 1138, 42, 912));

        ArrayList<Location> spawnLocations = new ArrayList<>();
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

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1128.5, 56.07, 905.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 854.5, 44, 910.5));
        itemLocations.add(new Location(world, 1124.5, 51, 901.5));
        itemLocations.add(new Location(world, 1135.5, 51, 911.5));
        itemLocations.add(new Location(world, 1120.5, 50, 917.5));
        itemLocations.add(new Location(world, 1136.5, 51, 920));
        itemLocations.add(new Location(world, 1136, 56.6, 917));
        itemLocations.add(new Location(world, 1118, 56, 912));
        itemLocations.add(new Location(world, 1134, 57, 911.5));
        itemLocations.add(new Location(world, 1124.5, 56, 905.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1129.5, 58, 896.5));

        ArrayList<Location> ammoLocations = new ArrayList<>();

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void streetsOpeningTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(861, 42, 899, 851, 48, 903);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_OPENING);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1084, 40, 899, 1079, 57, 851);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_APARTMENTS);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void streetsShedTrigger(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1138, 56, 896, 1140, 60, 906);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STREETS_SHED);
                HandlerList.unregisterAll(this);
            }
        });

        return new Scene(
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
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1139, 43, 914)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1139, 43, 918)).setType(Material.BEEHIVE);
                    for (int i = 0; i < 4; i++) {
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 877.5, 44, 910.5), new Medkit());
                    }
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 873.5, 44, 907.5), ItemHandler.getRandomGun(Tier.TIER2), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 875.5, 44, 907.5), ItemHandler.getRandomGun(Tier.TIER2), true);

                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1135, 44, 919), new Medkit());
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 857.5, 44, 910.5), ItemHandler.getRandomGun(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 857.5, 44, 907.5), ItemHandler.getRandomGun(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1135, 44, 910), ItemHandler.getRandomGun(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1135, 44, 907), ItemHandler.getRandomMelee(Tier.TIER1), false);

                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(873, 41, 889, 932, 41, 863), 30);
                },
                null,
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
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
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1121.5, 43, 922.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1118.5, 43, 843)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1076.5, 43, 894.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1134, 43, 898.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 899.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 909.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 910.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 920.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 910.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 909.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 899.5)));
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
                happenings
        );
    }
}
