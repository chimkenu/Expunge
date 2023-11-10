package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.items.utilities.healing.Adrenaline;
import me.chimkenu.expunge.items.utilities.healing.Medkit;
import me.chimkenu.expunge.items.utilities.healing.Pills;
import me.chimkenu.expunge.items.weapons.melees.MrCookie;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
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

import java.util.Set;

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
        return new BoundingBox(-75, 34, 326, -62, 43, 339);
    }

    @Override
    public BoundingBox[] pathRegions() {
        return new BoundingBox[0];
    }

    @Override
    public Vector[] spawnLocations() {
        return new Vector[]{
                new Vector(15.5, 43.5, -20.5),
                new Vector(-23.5, 43.0, -3.5),
                new Vector(-24, 43.0, -81),
                new Vector(15, 43.0, -66),
                new Vector(-33.5, 24.0, 4.5),
                new Vector(-10.5, 21.0, -12.5),
                new Vector(-21.5, 16.0, -16.5),
                new Vector(-6.5, 27.0, 46.5),
                new Vector(27.5, 25.0, 36.5),
                new Vector(30, 25.0, 46),
                new Vector(78.5, 25.0, 24.5),
                new Vector(101.5, 25.0, 77.5),
                new Vector(79.5, 25.0, 77.5),
                new Vector(79.5, 26.0, 117.5),
                new Vector(63.5, 25.0, 137.5),
                new Vector(41.5, 25.0, 137.5),
                new Vector(52.5, 25.0, 154.5),
                new Vector(23.5, 25.0, 184.5),
                new Vector(23.5, 25.0, 178.5),
                new Vector(16.5, 36.0, 193.5),// after
                new Vector(25.5, 36.0, 215.5),
                new Vector(-0.5, 36.0, 215.5),
                new Vector(-70.5, 37.5, 180.5),
                new Vector(-90.5, 37.0, 203.5),
                new Vector(-84.5, 37.0, 230.5),
                new Vector(-79.5, 37.0, 224.5),
                new Vector(-75.5, 37.0, 179.5),
                new Vector(-110.5, 37.0, 248.5),
                new Vector(-82, 37.0, 246),
                new Vector(-84.5, 39.0, 280.5),
                new Vector(-113.5, 37.0, 296.5),
                new Vector(-115.5, 37.0, 323.5),
                new Vector(-75.5, 42.0, 312.5),
                new Vector(-66.5, 36.0, 326.5),

        };
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[0];
    }

    @Override
    public Vector[] itemLocations() {
        return new Vector[]{
                new Vector(25.5, 36.0, 215.5),
                new Vector(-0.5, 36.0, 215.5),
                new Vector(-70.5, 37.5, 180.5),
                new Vector(-90.5, 37.0, 203.5),
                new Vector(-84.5, 37.0, 230.5),
                new Vector(-75.5, 37.0, 179.5),
                new Vector(-110.5, 37.0, 248.5),
                new Vector(-82, 37.0, 246),
                new Vector(-84.5, 39.0, 280.5),
                new Vector(-115.5, 37.0, 323.5),
                new Vector(-75.5, 42.0, 312.5),
                new Vector(-66.5, 36.0, 326.5),
                new Vector(-20.5, 16.0, -13.5),

                new Vector(-20.5, 16.0, -13.5), // Highway
                new Vector(0.5, 28.0, 48.5),
                new Vector(0.5, 28.0, 51.5),
                new Vector(82.5, 27.0, 30),
                new Vector(61.5, 25.0, 171.5),
                new Vector(15.5, 26.0, 182.5),
                new Vector(20.5, 26.0, 180.5),
                new Vector(-16.5, 26.0, 181.5),
                new Vector(-67.5, 37.0, 193.5)
        };
    }

    @Override
    public int baseItemsToSpawn() {
        return 4;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[]{
                new Vector(3.5, 25.0, 175.5),
                new Vector(-113.5, 37.0, 296.5),
                new Vector(-79.5, 37.0, 224.5),
                new Vector(88.5, 26.0, 68.5),
        };
    }

    @Override
    public Vector[] ammoLocations() {
        return new Vector[]{
                new Vector(21.5, 26, 182.5),
                new Vector(19.5, 44, -32.5)
        };
    }

    @Override
    public Vector buttonLocation() {
        return new Vector(-71, 37, 330);
    }

    @Override
    public GameAction runAtStart() {
        return (plugin, gameManager, player) -> {
            for (int i = 0; i < 4; i++) {
                gameManager.getDirector().getItemHandler().spawnUtility(new Vector(19.5, 44, -27.5), new Medkit(), false);
            }
            gameManager.getDirector().getItemHandler().spawnWeapon(new Vector(18.5, 44, -42), ItemHandler.getRandomGun(Tier.TIER2), true);
            gameManager.getDirector().getItemHandler().spawnWeapon(new Vector(15.5, 44, -41), ItemHandler.getRandomGun(Tier.TIER2), true);
            gameManager.getDirector().getItemHandler().spawnWeapon(new Vector(15.5, 44.5, -30.5), ItemHandler.getRandomMelee(Tier.TIER1), false);
            gameManager.getDirector().getItemHandler().spawnUtility(new Vector(22, 50.2, -40.5), new Pills(), false);
            gameManager.getDirector().getItemHandler().spawnUtility(new Vector(17, 50.2, -40.5), new Adrenaline(), false);
        };
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
                        BoundingBox box = new BoundingBox(13, 41, -30, 10, 47, -45);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.SUBWAY_OPENING.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void achievement(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON)) return;

                        if (e.getClickedBlock().getLocation().toVector().equals(new Vector(22, 44, -11))) {
                            Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayPurpleCar(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-30, 19, 2, -35, 28, 5);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, Set.of(e.getPlayer()), DepartureDialogue.PURPLE_CAR.pickRandom(1));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMrCookie(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                            if (!e.getClickedBlock().getLocation().toVector().equals(new Vector(-19, 16, -7)))
                                return;
                            localGameManager.getDirector().getItemHandler().spawnWeapon(new Vector(-19, 16, -7), new MrCookie(), false);
                            Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.SUBWAY_MR_COOKIE.pickRandom(localGameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMap(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-13, 15, 7, -1, 22, -95);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.SUBWAY_MAP.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwaySafeZone(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(38, 23, 171, 24, 31, 184);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.SUBWAY_PASSENGER_TRAIN.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },

                // Highway
                new Listener() {
                    @EventHandler
                    public void highwayManhole(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(11, 24, 178, 3, 31, 184);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.HIGHWAY_MANHOLE.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayOpening(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-3, 35, 184, -9, 42, 190);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.HIGHWAY_OPENING.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwaySafeHouse(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-116, 35, 304, -63, 42, 326);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        // new HighwayCarBoom(localGameManager.getPlayers(), localGameManager.getDirector().getActiveMobs()).play();
                        HandlerList.unregisterAll(this);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.HIGHWAY_CAR_BOOM.pickRandom(localGameManager.getPlayers().size()));
                            }
                        }.runTaskLater(plugin, 20 * 5);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                                Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.HIGHWAY_SAFE_HOUSE.pickRandom(localGameManager.getPlayers().size()));
                                localGameManager.getDirector().bile(plugin, e.getPlayer(), 30);
                                localGameManager.getDirector().getMobHandler().spawnAtRandomLocations(new BoundingBox(997, 35, 1362, 1040, 35, 1343), 30 + (10 * localGameManager.getDifficulty().ordinal()));
                            }
                        }.runTaskLater(plugin, 20 * 6);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayPurpleCar(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-85, 36, 255, -83, 45, 247);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.HIGHWAY_PURPLE_CAR.pickRandom(localGameManager.getPlayers().size()));
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
                new SwingListener(plugin, localGameManager),
                new JoinLeaveListener(plugin, localGameManager),
                new UtilityListener(plugin, localGameManager)
        };
    }
}
