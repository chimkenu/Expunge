package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.mobs.common.Horde;
import org.bukkit.Location;
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

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Office extends CampaignMap {
    @Override
    public String directory() {
        return "Office";
    }

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
                    public void officeJump(PlayerMoveEvent e) {
                        if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(849, 51, 915, 847, 56, 921);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        playDialogue(DepartureDialogue.OFFICE_JUMP);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void onEnterDevelopersRoom(PlayerMoveEvent e) {
                        if (new BoundingBox(837, 42, 924, 842, 47, 928).contains(e.getPlayer().getLocation().toVector()))
                            Achievements.THE_DEVELOPERS_ROOM.grant(e.getPlayer());
                    }
                },
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

                        CampaignMap.playCrescendoEventEffect();
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 845.5, 48, 928.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 853.5, 47, 939.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 866.5, 48, 939.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 872.5, 48, 931.5)));
                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 876.5, 48, 925.5)));
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
                new Listener() {
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
                },
                new Listener() {
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
                }
        };
    }
}
