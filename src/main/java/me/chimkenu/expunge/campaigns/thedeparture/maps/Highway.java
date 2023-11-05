package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.campaigns.thedeparture.cutscenes.HighwayCarBoom;
import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Highway extends CampaignMap {
    @Override
    public Vector startLocation() {
        return null;
    }

    @Override
    public BoundingBox endRegion() {
        return null;
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
        return null;
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
    public boolean isStartSafeRoom() {
        return false;
    }

    @Override
    public Listener[] happenings(LocalGameManager localGameManager) {
        return new Listener[]{
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
                        new HighwayCarBoom(Expunge.playing.getKeys(), Expunge.runningDirector.mobHandler.getActiveMobs().stream().toList()).play();
                        HandlerList.unregisterAll(this);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                playDialogue(DepartureDialogue.HIGHWAY_CAR_BOOM);
                            }
                        }.runTaskLater(Expunge.instance, 20 * 5);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                CampaignMap.playCrescendoEventEffect();
                                playDialogue(DepartureDialogue.HIGHWAY_SAFE_HOUSE);
                                Expunge.runningDirector.bile(e.getPlayer(), 30);
                                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(997, 35, 1362, 1040, 35, 1343), 30 + (10 * Expunge.currentDifficulty.ordinal()));
                            }
                        }.runTaskLater(Expunge.instance, 20 * 6);
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
}
