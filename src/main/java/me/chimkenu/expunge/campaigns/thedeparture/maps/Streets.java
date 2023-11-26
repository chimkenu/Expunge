package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
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

import java.util.List;

public class Streets implements CampaignMap {
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
                new Vector(-8.5, 42, -125.5),
                new Vector(51.5, 43, -34.5)
        };
    }

    @Override
    public ItemRandomizer[] randomizedGameItems() {
        return new ItemRandomizer[]{
                new ItemRandomizer(-226.5, 44, -97, 1, 4, List.of(GameItems.MEDKIT)),
                new ItemRandomizer(-230.5, 44, -100.5, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(-226.5, 44, -95, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),

                new ItemRandomizer(31, 44, -101, 1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(31, 44, -91, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(31, 44, -98, 1, 1, ItemRandomizer.Preset.MELEE),
                new ItemRandomizer(31, 44, -89, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(31.5, 51, -85.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(27.5, 43, -110.5, 0.7, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(16.5, 50, -102.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(32.5, 51, -98.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(18.5, 50, -93.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(32.5, 56, -93.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(13.5, 56, -96.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(28.5, 56, -104.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(16.5, 57, -107.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Alleys
                new ItemRandomizer(27.5, 44.0, -26.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(31.5, 44.0, -14.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(19.5, 42.5, -33.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(43.5, 44.0, -27.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(35.5, 44.0, -52.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(52.5, 44.0, -56.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(65.5, 44.0, -86.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(40.5, 44.0, -82.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Shed
                new ItemRandomizer(34, 44.0, -93.5, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(34, 44.0, -90.5, 1, 1, ItemRandomizer.Preset.MELEE),
                new ItemRandomizer(36.5, 44.0, -93, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY)
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
    public Vector[] rescueClosetLocations() {
        return new Vector[0];
    }

    @Override
    public GameAction runAtStart() {
        return (plugin, gameManager, player) -> Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_RADIO.pickRandom(gameManager.getPlayers().size()));
    }

    @Override
    public GameAction runAtEnd() {
        return null;
    }

    @Override
    public Listener[] happenings(JavaPlugin plugin, GameManager gameManager) {
        return new Listener[]{
                new Listener() {
                    @EventHandler
                    public void streetsOpeningTrigger(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-243, 41, -105, -254, 52, -115);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_OPENING.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-10, 40, -105, -32, 52, -157);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_APARTMENTS.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!gameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(16, 43, -110)) || block.getLocation().toVector().equals(new Vector(16, 43, -109))))) {
                            return;
                        }

                        Campaign.playCrescendoEventEffect(gameManager.getPlayers());
                        World world = e.getPlayer().getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1121.5, 43, 922.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1118.5, 43, 843), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1076.5, 43, 894.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1134, 43, 898.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 50, 899.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 909.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 50, 910.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 920.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 50, 920.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 56, 920.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 56, 910.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1131.5, 56, 909.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(1125.5, 56, 899.5), gameManager.getDifficulty()));
                                i++;
                                if (i >= 3) {
                                    this.cancel();
                                }
                                if (!gameManager.isRunning() || !gameManager.getDirector().getMobHandler().isSpawningEnabled()) {
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
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(33, 55, -112, 38, 61, -103);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_SHED.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },

                // Alleys
                new Listener() {
                    @EventHandler
                    public void alleysOpening(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(34, 41, -89, 43, 48, -84);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_OPENING.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysPurpleCar(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(42, 41, -73, 32, 47, -66);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, List.of(e.getPlayer()), DepartureDialogue.PURPLE_CAR.pickRandom(1));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysSafeHouse(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;

                        BoundingBox box = new BoundingBox(54, 41, -19, 41, 53, -25);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_SAFE_HOUSE.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void achievement(PlayerInteractEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON)) return;

                        if (e.getClickedBlock().getLocation().toVector().equals(new Vector(22, 44, -11))) {
                            Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
                        }
                    }
                }
        };
    }

}
