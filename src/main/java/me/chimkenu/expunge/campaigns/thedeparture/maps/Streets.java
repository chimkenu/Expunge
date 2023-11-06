package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Streets extends CampaignMap {
    @Override
    public String directory() {
        return "Streets";
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
        return new Listener[0];
//        return new Listener[]{
//                new Listener() {
//                    @EventHandler
//                    public void streetsOpeningTrigger(PlayerMoveEvent e) {
//                        if (!Expunge.playing.getKeys().contains(e.getPlayer()))
//                            return;
//                        BoundingBox box = new BoundingBox(861, 42, 899, 851, 48, 903);
//                        if (!box.contains(e.getPlayer().getLocation().toVector()))
//                            return;
//                        playDialogue(DepartureDialogue.STREETS_OPENING);
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void streetsApartmentsTrigger(PlayerMoveEvent e) {
//                        if (!Expunge.playing.getKeys().contains(e.getPlayer()))
//                            return;
//                        BoundingBox box = new BoundingBox(1084, 40, 899, 1079, 57, 851);
//                        if (!box.contains(e.getPlayer().getLocation().toVector()))
//                            return;
//                        playDialogue(DepartureDialogue.STREETS_APARTMENTS);
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void crescendoEventApartments(PlayerInteractEvent e) {
//                        Block block = e.getClickedBlock();
//                        if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
//                            return;
//                        }
//                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(1120, 43, 898)) || block.getLocation().toVector().equals(new Vector(1120, 43, 899))))) {
//                            return;
//                        }
//
//                        CampaignMap.playCrescendoEventEffect();
//                        World world = e.getPlayer().getWorld();
//                        new BukkitRunnable() {
//                            int i = 0;
//
//                            @Override
//                            public void run() {
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1121.5, 43, 922.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1118.5, 43, 843)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1076.5, 43, 894.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1134, 43, 898.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 899.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 909.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 50, 910.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 50, 920.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 920.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 910.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1131.5, 56, 909.5)));
//                                Expunge.runningDirector.mobHandler.spawnMob(new Horde(world, new Location(world, 1125.5, 56, 899.5)));
//                                i++;
//                                if (i >= 3) {
//                                    this.cancel();
//                                }
//                                if (Expunge.currentSceneIndex != 2) {
//                                    this.cancel();
//                                }
//                            }
//                        }.runTaskTimer(Expunge.instance, 1, 20 * 4);
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void streetsShedTrigger(PlayerMoveEvent e) {
//                        if (!Expunge.playing.getKeys().contains(e.getPlayer()))
//                            return;
//                        BoundingBox box = new BoundingBox(1138, 56, 896, 1140, 60, 906);
//                        if (!box.contains(e.getPlayer().getLocation().toVector()))
//                            return;
//                        playDialogue(DepartureDialogue.STREETS_SHED);
//                        HandlerList.unregisterAll(this);
//                    }
//                }
//        };
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
                new ShootListener(plugin, localGameManager)
        };
    }
}
