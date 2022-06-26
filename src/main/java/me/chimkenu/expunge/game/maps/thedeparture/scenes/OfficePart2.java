package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.weapons.melees.FryingPan;
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

public class OfficePart2 {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
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

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 843, 77, 924));
        spawnLocations.add(new Location(world, 858.5, 77, 940.5));
        spawnLocations.add(new Location(world, 875, 77, 949));
        spawnLocations.add(new Location(world, 859, 77, 906));
        spawnLocations.add(new Location(world, 857.5, 77, 930.5));
        spawnLocations.add(new Location(world, 875, 53, 936));
        spawnLocations.add(new Location(world, 860.5, 53, 913.5));
        spawnLocations.add(new Location(world, 849.5, 53, 921.5));
        spawnLocations.add(new Location(world, 849.5, 43, 912.5));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 843.5, 44, 913.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 862.5, 54, 916.5));
        itemLocations.add(new Location(world, 839.5, 52, 928.5));
        itemLocations.add(new Location(world, 845.5, 54, 927.5));
        itemLocations.add(new Location(world, 834.5, 45, 920.5));
        itemLocations.add(new Location(world, 845, 44, 907.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 844.5, 43, 919.7));

        ArrayList<Location> ammoLocations = new ArrayList<>();

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void officeJump(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(849, 51, 915, 847, 56, 921);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_JUMP);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void officeVent(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(837, 46, 904, 840, 41, 907);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_VENTS);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void officeSafeRoom(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(874, 45, 924, 876, 47, 922);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.OFFICE_SAFE_ROOM);
                HandlerList.unregisterAll(this);
            }
        });

        return new Scene(
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
                player -> {
                    world.getBlockAt(new Location(world, 872, 43, 910)).setType(Material.BEEHIVE);
                    Director.spawnWeapon(world, new Location(world, 864.5, 54, 909.5), new FryingPan(), false);
                    playDialogue(DepartureDialogue.OFFICE_ELEVATOR);
                },
                player -> playDialogue(DepartureDialogue.OFFICE_RADIO),
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
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 845.5, 48, 928.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 853.5, 47, 939.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 866.5, 48, 939.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 872.5, 48, 931.5)));
                                Expunge.runningDirector.spawnMob(new Horde(world, new Location(world, 876.5, 48, 925.5)));
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
                happenings
        );
    }
}
