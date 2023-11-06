package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.campaigns.thedeparture.cutscenes.HighwayCarBoom;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.guns.weapons.melees.MrCookie;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Subway extends CampaignMap {
    @Override
    public String directory() {
        return "Subway";
    }

    @Override
    public Vector startLocation() {
        return new Vector(21.5, 43, -35.5);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(24, 24, 184, 11, 31, 178);
    }

    @Override
    public BoundingBox[] pathRegions() {
        return new BoundingBox[0];
    }

    @Override
    public Vector[] spawnLocations() {
        return new Vector[0];
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[0];
    }

    @Override
    public Vector[] itemLocations() {
        return new Vector[0];
    }

    @Override
    public int baseItemsToSpawn() {
        return 0;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[0];
    }

    @Override
    public Vector[] ammoLocations() {
        return new Vector[0];
    }

    @Override
    public Vector buttonLocation() {
        return new Vector(18, 27, 180);
    }

    @Override
    public GameAction runAtStart() {
        return null;
    }

    @Override
    public GameAction runAtEnd() {
        return null;
    }

    @Override
    public Listener[] happenings(JavaPlugin plugin, LocalGameManager localGameManager) {
        return new Listener[]{
                new Listener() {
                    @EventHandler
                    public void subwayOpening(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1118, 42, 990, 1113, 46, 986);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.SUBWAY_OPENING);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void finaleBegin(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
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
                },
                new Listener() {
                    @EventHandler
                    public void subwayPurpleCar(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1074, 19, 1025, 1069, 29, 1030);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(plugin, List.of(e.getPlayer()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwaySpawnZombies(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1069, 32, 1008, 1064, 38, 1001);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1103, 15, 1030, 1078, 15, 1016), 30);
                        localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1104, 26, 1042, 1090, 26, 1048), 15);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMrCookie(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                            if (!e.getClickedBlock().getLocation().toVector().equals(new Vector(1085, 16, 1017)))
                                return;
                            localGameManager.getDirector().spawnWeapon(new Location(localGameManager.getWorld(), 1084.5, 16.3, 1017.5), new MrCookie(), false);
                            playDialogue(DepartureDialogue.SUBWAY_MR_COOKIE);
                            HandlerList.unregisterAll(this);
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMap(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1091, 14, 1031, 1104, 23, 1015);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.SUBWAY_MAP);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwaySafeZone(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1129, 24, 1201, 1151, 31, 1197);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.SUBWAY_SAFE_ZONE);
                        HandlerList.unregisterAll(this);
                    }
                },

                // Highway
                new Listener() {
                    @EventHandler
                    public void highwayManhole(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1114, 24, 1209, 1108, 33, 1201);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.HIGHWAY_MANHOLE);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayOpening(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1100, 36, 1209, 1096, 39, 1213);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.HIGHWAY_OPENING);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwaySafeHouse(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1028, 34, 1258, 988, 56, 1233);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        // new HighwayCarBoom(localGameManager.getPlayers(), localGameManager.getDirector().getActiveMobs()).play();
                        HandlerList.unregisterAll(this);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                playDialogue(DepartureDialogue.HIGHWAY_CAR_BOOM);
                            }
                        }.runTaskLater(plugin, 20 * 5);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                                playDialogue(DepartureDialogue.HIGHWAY_SAFE_HOUSE);
                                localGameManager.getDirector().bile(plugin, e.getPlayer(), 30);
                                localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(997, 35, 1362, 1040, 35, 1343), 30 + (10 * localGameManager.getDifficulty().ordinal()));
                            }
                        }.runTaskLater(plugin, 20 * 6);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayPurpleCar(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1019, 39, 1276, 1021, 43, 1273);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.HIGHWAY_PURPLE_CAR);
                        HandlerList.unregisterAll(this);
                    }
                }
        };
    }

    @Override
    public GameListener[] gameListeners(JavaPlugin plugin, LocalGameManager localGameManager) {
        return new GameListener[]{
                new AmmoPileListener(plugin, localGameManager),
                new DeathReviveListener(plugin, localGameManager),
                new InventoryListener(plugin, localGameManager),
                new MobListener(plugin, localGameManager),
                new NextMapListener(plugin, localGameManager),
                new PickUpListener(plugin, localGameManager),
                new ShootListener(plugin, localGameManager),
                new ShoveListener(plugin, localGameManager),
                new SwingListener(plugin, localGameManager)
        };
    }
}
