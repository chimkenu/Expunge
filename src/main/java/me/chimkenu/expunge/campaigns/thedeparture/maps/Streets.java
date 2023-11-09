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
import me.chimkenu.expunge.items.utilities.healing.Medkit;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import me.chimkenu.expunge.mobs.common.Horde;
import org.bukkit.Material;
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

public class Streets extends CampaignMap {
    @Override
    public String directory() {
        return "Streets";
    }

    @Override
    public Vector startLocation() {
        return new Vector(-228.5, 43, -96.5);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(25, 41, -30, 13, 57, -3);
    }

    @Override
    public BoundingBox[] pathRegions() {
        return new BoundingBox[0];
    }

    @Override
    public Vector[] spawnLocations() {
        return new Vector[]{
                new Vector(-232.5, 43.0, -89.5),
                new Vector(-240.5, 43.0, -76.5),
                new Vector(-269.5, 42.0, -123.5),
                new Vector(-261.5, 42.0, -136.5),
                new Vector(-233.5, 42.0, -142.5),
                new Vector(-214.5, 43.0, -106.5),
                new Vector(-193.5, 43.0, -151.5),
                new Vector(-177, 43.0, -112),
                new Vector(-160.5, 43.0, -151.5),
                new Vector(-154.5, 43.0, -123.5),
                new Vector(-141.5, 43.0, -151.5),
                new Vector(-123.5, 43.0, -105.5),
                new Vector(-88.5, 43.0, -153.5),
                new Vector(-78.5, 43.0, -111.5),
                new Vector(-20.5, 43.0, -153.5),
                new Vector(25.5, 43.0, -87.5),
                new Vector(-23.5, 43.0, -102.5),
                new Vector(36.5, 43.0, -112.5),
                new Vector(14, 43.0, -165),
                new Vector(60, 48.0, -157),
                new Vector(30, 43.0, -109),
                new Vector(21.5, 50.0, -108.5),
                new Vector(28.5, 50.0, -98.5),
                new Vector(27.5, 50.0, -87.5),
                new Vector(27.5, 56.0, -87.5),
                new Vector(21.5, 56.0, -97.5),
                new Vector(20.5, 56.0, -108.5),

                // Alleys
                new Vector(29.5, 43, -64.5),
                new Vector(51.5, 43.0, -93.5),
                new Vector(66.49, 43.0, -84.5),
                new Vector(53.0, 43.0, -78),
                new Vector(63.0, 43.0, -71),
                new Vector(60.5, 44.0, -50.5),
                new Vector(62.5, 43.0, -46.5),
                new Vector(40.5, 44.0, -50.5),
                new Vector(33.5, 43.0, -40.5),
                new Vector(25.5, 43.0, -57.5),
                new Vector(47.5, 43.0, -54.5),
                new Vector(67.5, 42.0, -19.5),
                new Vector(45.5, 43.0, -17.5),
                new Vector(37.5, 43.0, -12.5),
                new Vector(26.5, 43.0, -35.5),
                new Vector(37.5, 43.0, -28.5)


        };
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[]{
        };
    }

    @Override
    public Vector[] itemLocations() {
        return new Vector[]{
                new Vector(31.00834111591309, 44.0, -100.994369777641),
                new Vector(30.985948095776852, 44.0, -91.029574195448),
                new Vector(16.469980824413817, 50.0, -102.52488851090),
                new Vector(32.4445397510778, 51.0, -98.51532243998328),
                new Vector(18.456129545524625, 50.0, -93.499485523880),
                new Vector(32.524940724847276, 56.0, -93.532509254534),
                new Vector(13.500104322620455, 56.0, -96.531607514342),
                new Vector(28.486186448310615, 56.0, -104.45339679608),
                new Vector(16.568158626096178, 57.0, -107.49615572235),

                // Alleys
                new Vector(27.5, 44.0, -26.5),
                new Vector(31.5, 44.0, -14.5),
                new Vector(19.5, 42.5, -33.5),
                new Vector(43.5, 44.0, -27.5),
                new Vector(35.5, 44.0, -52.5),
                new Vector(52.5, 44.0, -56.5),
                new Vector(65.5, 44.0, -86.5),
                new Vector(40.5, 44.0, -82.5),

                // Shed (put stuff here)
                new Vector(34, 44.0, -93.5), // weapon
                new Vector(34, 44.0, -90.5), // weapon
                new Vector(36.5, 44.0, -93)  // medkit probably
        };
    }

    @Override
    public int baseItemsToSpawn() {
        return 4;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[]{
                new Vector(30.99230923775718, 44.0, -98.0190331933698),
                new Vector(30.949658934244066, 44.0, -89.006112607621),
                new Vector(31.461896720786562, 51.0, -85.514974223341),
                new Vector(27.46263912587844, 43.0, -110.508794793466)
        };
    }

    @Override
    public Vector[] ammoLocations() {
        return new Vector[]{
                new Vector(-230.5, 44, -100.5),
                new Vector(36.5, 44.0, -90.5)
        };
    }

    @Override
    public Vector buttonLocation() {
        return new Vector(20, 44, -21);
    }

    @Override
    public GameAction runAtStart() {
        return (plugin, director, player) -> {
            for (int i = 0; i < 4; i++) {
                director.getItemHandler().spawnUtility(new Vector(-226.5, 44, -97), new Medkit(), false);
            }

            director.getItemHandler().spawnWeapon(new Vector(-230.5, 44, -100.5), ItemHandler.getRandomGun(Tier.TIER2), true);
            director.getItemHandler().spawnWeapon(new Vector(-226.5, 44, -95), ItemHandler.getRandomGun(Tier.TIER2), true);

            Dialogue.display(plugin, director.getLocalGameManager().getPlayers(), DepartureDialogue.OFFICE_RADIO.pickRandom(director.getLocalGameManager().getPlayers().size()));
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
                    public void streetsOpeningTrigger(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-243, 41, -105, -254, 52, -115);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STREETS_OPENING.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-10, 40, -105, -32, 52, -157);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STREETS_APARTMENTS.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(16, 43, -110)) || block.getLocation().toVector().equals(new Vector(16, 43, -109))))) {
                            return;
                        }

                        Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1121.5, 43, 922.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1118.5, 43, 843), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1076.5, 43, 894.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1134, 43, 898.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 50, 899.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 909.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 50, 910.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 920.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 920.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 56, 920.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 56, 910.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 56, 909.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 56, 899.5), localGameManager.getDifficulty()));
                                i++;
                                if (i >= 3) {
                                    this.cancel();
                                }
                                if (!localGameManager.isRunning() || !localGameManager.getDirector().getMobHandler().isSpawningEnabled()) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 1, 20 * 4);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsShedTrigger(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(33, 55, -112, 38, 61, -103);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STREETS_SHED.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },

                // Alleys
                new Listener() {
                    @EventHandler
                    public void alleysOpening(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(34, 41, -89, 43, 48, -84);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        // playDialogue(DepartureDialogue.ALLEYS_OPENING);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysPurpleCar(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(42, 41, -73, 32, 47, -66);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        // DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(plugin, List.of(e.getPlayer()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysSafeHouse(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(54, 41, -19, 41, 53, -25);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        // playDialogue(DepartureDialogue.ALLEYS_SAFE_HOUSE);
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
