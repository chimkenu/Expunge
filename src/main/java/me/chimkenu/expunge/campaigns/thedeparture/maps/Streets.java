package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.campaigns.*;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.entities.MobType;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
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
import java.util.function.Consumer;

import static me.chimkenu.expunge.campaigns.Campaign.playCrescendoEventEffect;

public record Streets(
        String name,
        String displayName,

        Vector startLocation,
        BoundingBox startRegion,
        BoundingBox endRegion,

        List<Path> escapePath,

        List<Vector> spawnLocations,
        List<Vector> bossLocations,
        List<Vector> rescueClosetLocations,
        NextMapCondition nextMapCondition,

        List<ItemRandomizer> startItems,
        List<ItemRandomizer> mapItems,
        List<Vector> ammoLocations,

        Consumer<GameManager> runAtStart,
        Consumer<GameManager> runAtEnd
) implements CampaignMap {
    public static Streets instance = new Streets(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    public Streets {
        name = "Streets";
        displayName = ChatUtil.getColor(120, 111, 86) + name();
        startLocation = new Vector(-180.5, 43, -289.5);
        startRegion = new BoundingBox(-178, 42, -286, -184, 48, -294);
        endRegion = new BoundingBox(71, 42, -220, 61, 56, -197);
        escapePath = List.of(
                new PathVector(-189.5, 43, -289.5, 4),
                new PathVector(-189.5, 43.06, -280.5, 6),
                new PathVector(-198.5, 43, -283.5, 4),
                new PathVector(-200, 43, -290, 3),
                new PathVector(-200, 43, -306, 8),
                new PathVector(-189.5, 42, -317.5, 8),
                new PathVector(-169, 42, -323.5, 8),
                new PathVector(-139, 42, -323.5, 8),
                new PathVector(-103, 42, -323.5, 8),
                new PathVector(-73.5, 42, -323.5, 8),
                new PathVector(-41.5, 42, -323.5, 8),
                new PathVector(-9.5, 42, -323.5, 8),
                new PathVector(21, 42, -323.5, 8),
                new PathVector(35.5, 42, -313.5, 8),
                new PathVector(55, 42, -301, 8),
                new PathVector(67, 43, -301, 4),
                new PathVector(72.5, 45, -303, 2),
                new PathVector(79, 50, -303, 3),
                new PathVector(72.5, 50.06, -298.5, 3),
                new PathVector(72.5, 50.06, -291.5, 3),
                new PathVector(72.5, 50.06, -282.5, 3),
                new PathVector(71.5, 52, -277, 2),
                new PathVector(66, 56, -277, 2),
                new PathVector(72.5, 56.06, -280.5, 3),
                new PathVector(72.5, 56.06, -289, 3),
                new PathVector(72.5, 56.06, -299.5, 3),
                new PathVector(80, 56, -302, 3),
                new PathVector(83, 56, -302, 2),
                new PathVector(84, 43, -289, 2),
                new PathRegion(new BoundingBox(85, 42, -287, 81, 46, -282), List.of(new Vector(83.5, 43, -284))),
                new PathVector(85.5, 43, -278, 3),
                new PathVector(92.5, 42.5, -279.5, 4),
                new PathVector(97.5, 42.5, -279.5, 4),
                new PathVector(97.5, 42.5, -270.5, 4),
                new PathVector(97.5, 42.5, -263.5, 3),
                new PathRegion(new BoundingBox(74, 42, -260, 121, 49, -252), List.of(
                        new Vector(107, 42.5, -256),
                        new Vector(112, 42.5, -253),
                        new Vector(117, 42.5, -253),

                        new Vector(94, 42.5, -258),
                        new Vector(91.5, 42.5, -252.5),
                        new Vector(82.5, 42.5, -252.5),
                        new Vector(77, 42.5, -252.5)
                )),
                new PathRegion(new BoundingBox(121, 42, -251, 72, 53, -238), List.of(
                        new Vector(117, 42.5, -239),

                        new Vector(77, 42.5, -246.5),
                        new Vector(77, 42.5, -240),
                        new Vector(86, 42.5, -240)
                )),
                new PathRegion(new BoundingBox(76, 42, -236, 120, 61, -226), List.of(
                        new Vector(86, 42.5, -234.5),
                        new Vector(86, 42.5, -228),
                        new Vector(97, 42.5, -228),

                        new Vector(117, 42.5, -228),
                        new Vector(102, 42.5, -228)
                )),
                new PathVector(99.5, 42.5, -223.5, 6),
                new PathVector(99.5, 42.5, -213.5, 5),
                new PathVector(88.5, 42.5, -213.5, 5),
                new PathVector(79.5, 42.5, -213.5, 5),
                new PathVector(73.5, 43, -213.5, 1)
        );
        spawnLocations = List.of(
                new Vector(-184.5, 43, -281.5),
                new Vector(-192, 43, -268),
                new Vector(-205, 43, -298),
                new Vector(-194.5, 42, -334.5),
                new Vector(-166, 43, -298),
                new Vector(-153.5, 42, -334.5),
                new Vector(-135.5, 43, -302.5),
                new Vector(-124.5, 43, -342.5),
                new Vector(-100, 43, -349),
                new Vector(-96, 43, -305),
                new Vector(-82.5, 43, -303.5),
                new Vector(-80, 43, -342),
                new Vector(-47, 43, -343),
                new Vector(-49, 43, -303),
                new Vector(-30.5, 43, -304),
                new Vector(-18, 43, -343),
                new Vector(0.5, 43, -306),
                new Vector(8, 43, -344),
                new Vector(20.5, 43, -306),
                new Vector(48.5, 42, -340.5),
                new Vector(24.5, 43.5, -293.5),
                new Vector(52.5, 42, -323.5),
                new Vector(79.5, 42, -320.5),
                new Vector(73.5, 43, -281.5),
                new Vector(80, 43, -300),
                new Vector(74.5, 50, -290.5),
                new Vector(67, 50, -279),
                new Vector(74.5, 56, -290.5),
                new Vector(76, 56, -303),

                // Alleys
                new Vector(85, 43, -301),
                new Vector(82.5, 43, -275.5),
                new Vector(99.5, 43, -284.5),
                new Vector(108.5, 43, -276.5),
                new Vector(105.5, 43, -266.5),
                new Vector(92.5, 43, -266.5),
                new Vector(107, 51, -262),
                new Vector(86, 43, -246),
                new Vector(76, 43, -256.5),
                new Vector(108.5, 44, -245.5),
                new Vector(93.5, 44, -242.5),
                new Vector(109, 43, -239),
                new Vector(120, 43, -229),
                new Vector(112, 49.5, -237.5),
                new Vector(83.5, 43.5, -225.5),
                new Vector(81, 43, -234),
                new Vector(94.5, 43, -234.5),
                new Vector(102, 43, -215),
                new Vector(93.5, 43, -210),
                new Vector(69.5, 42.5, -224.5)
        );
        bossLocations = List.of(
                new Vector(79.5, 43.51, -213.5),
                new Vector(22.5, 42, -323.5)
        );
        rescueClosetLocations = List.of();
        nextMapCondition = new Barrier(
                List.of(
                        new Barrier.Block(new Vector(63, 43, -212), Material.BEEHIVE, true),
                        new Barrier.Block(new Vector(63, 44, -212), Material.BARRIER, true),
                        new Barrier.Block(new Vector(71, 43, -214), Material.BEEHIVE, false)
                )
        );
        startItems = List.of();
        mapItems = List.of(
                new ItemRandomizer(-178.5, 44, -289.5, 1, 4, List.of("MEDKIT")),
                new ItemRandomizer(-182, 44, -292.5, 1, 1, true, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-180, 44, -292.5, 1, 1, true, ItemRandomizer.Preset.TIER2_MELEE),
                new ItemRandomizer(-178.5, 44, -291.5, 1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(-196.5, 44, -270.5, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(-198.5, 44, -290, 1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-201.5, 44, -292, 1, 1, ItemRandomizer.Preset.TIER2_MELEE),
                new ItemRandomizer(-201.5, 44, -289.5, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-198.5, 44, -292.5, 0.7, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(-219.5, 43, -305.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-176.5, 44, -303.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(79.5, 44, -292.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(79.5, 44, -290.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(79.5, 44, -282.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(79.5, 44, -280.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(79, 43, -303.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Apartments
                new ItemRandomizer(78.5, 51, -288.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(80, 50.56, -294.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(70.5, 50, -282.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(80.5, 57, -280, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(65.5, 57, -291.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Alleys
                new ItemRandomizer(89.5, 45, -288.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(88.5, 44, -274.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(111, 44, -265.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(102, 44, -251.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(124.5, 44, -234.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(112.5, 44, -218.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(118.5, 44, -217.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(115.5, 51, -219, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(110.5, 51, -216, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(83.5, 44, -245.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(92, 44, -219.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(82, 44, -206.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(68, 42.5, -225, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(114.5, 51, -225.5, 1, 1, List.of("GRENADE_LAUNCHER")),

                // Shed
                new ItemRandomizer(82, 44, -285.5, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(82, 44, -282.5, 1, 1, ItemRandomizer.Preset.TIER2_MELEE),
                new ItemRandomizer(84.5, 44, -285, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY)
        );
        ammoLocations = List.of(
                new Vector(-178.5, 44, -286.5),
                new Vector(84.5, 44, -282.5),
                new Vector(116.5, 51, -225.5)
        );
        runAtStart = manager -> Dialogue.display(manager.getPlugin(), manager.getPlayers(), DepartureDialogue.OFFICE_RADIO.pickRandom(manager.getPlayers().size()));
        runAtEnd = manager -> {};
    }

    @Override
    public List<Listener> happenings(JavaPlugin plugin, GameManager gameManager) {
        return List.of(
                new Listener() {
                    @EventHandler
                    public void streetsOpeningTrigger(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-193, 42, -302, -207, 53, -309);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_OPENING.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(25, 42, -302, 63, 67, -345);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_APARTMENTS.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(16, 43, -110)) || block.getLocation().toVector().equals(new Vector(16, 43, -109))))) {
                                return;
                            }

                            playCrescendoEventEffect(gameManager.getPlayers());
                            spawnHordeAtApartments(gameManager);
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsShedTrigger(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(33, 55, -112, 38, 61, -103);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_SHED.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },

                // Alleys
                new Listener() {
                    @EventHandler
                    public void alleysOpening(PlayerMoveEvent e) {
                       gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                           BoundingBox box = new BoundingBox(34, 41, -89, 43, 48, -84);
                           if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                           Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_OPENING.pickRandom(gameManager.getPlayers().size()));
                           HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysPurpleCar(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(42, 41, -73, 32, 47, -66);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, List.of(e.getPlayer()), DepartureDialogue.PURPLE_CAR.pickRandom(1));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysSafeHouse(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(54, 41, -19, 41, 53, -25);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_SAFE_HOUSE.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void achievement(PlayerInteractEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                            if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON))
                                return;

                            if (e.getClickedBlock().getLocation().toVector().equals(new Vector(22, 44, -11))) {
                                Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
                            }
                        });
                    }
                }
        );
    }

    private void spawnHordeAtApartments(GameManager gameManager) {
        final Vector[] spawnLocations = new Vector[]{
                new Vector(21.5, 50, -108.5),
                new Vector(27.5, 50, -98.5),
                new Vector(21.5, 50, -97.5),
                new Vector(27.5, 50, -87.5),
                new Vector(27.5, 56, -87.5),
                new Vector(21.5, 56, -97.5),
                new Vector(27.5, 56, -98.5),
                new Vector(21.5, 56, -108.5)
        };

        new BukkitRunnable() {
            int t = 4;
            @Override
            public void run() {
                if (!gameManager.isRunning() || !((CampaignGameManager) gameManager).isSpawningActive()) {
                    this.cancel();
                    return;
                }
                if (t < 0) {
                    this.cancel();
                    return;
                }

                t--;
                for (var v : spawnLocations) {
                    gameManager.spawnInfected(MobType.COMMON, v);
                }
            }
        }.runTaskTimer(gameManager.getPlugin(), 1, 10);
    }
}
