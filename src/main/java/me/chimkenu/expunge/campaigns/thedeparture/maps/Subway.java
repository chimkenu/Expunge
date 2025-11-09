package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.*;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.campaigns.thedeparture.extras.HighwayCarBoom;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
import java.util.Set;
import java.util.function.Consumer;

public record Subway(
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
    public static Subway instance = new Subway(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    public Subway {
        name = "Subway";
        displayName = ChatUtil.getColor(46, 39, 32) + name();
        startLocation = new Vector(21.5, 43, -35.5);
        startRegion = new BoundingBox(71, 42, -220, 61, 56, -197);
        endRegion = new BoundingBox(-25, 32, 153, -17, 36, 159);
        escapePath = List.of();
        spawnLocations = List.of(
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
                new Vector(-66.5, 36.0, 326.5)
        );
        bossLocations = List.of();
        rescueClosetLocations = List.of();
        nextMapCondition = new Barrier(
                List.of()
        );
        startItems = List.of();
        mapItems = List.of(
                new ItemRandomizer(19.5, 44, -27.5, 1, 4, List.of("MEDKIT")),
                new ItemRandomizer(18.5, 44, -42, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(15.5, 44, -41, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(15.5, 44.5, -30.5, 1, 1, ItemRandomizer.Preset.TIER2_MELEE),
                new ItemRandomizer(22, 50.2, -40.5, 1, 1, List.of("PILLS")),
                new ItemRandomizer(17, 50.2, -40.5, 1, 1, List.of("ADRENALINE")),

                new ItemRandomizer(25.5, 36.0, 215.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-0.5, 36.0, 215.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-70.5, 37.5, 180.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-90.5, 37.0, 203.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-84.5, 37.0, 230.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-75.5, 37.0, 179.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-110.5, 37.0, 248.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-82.0, 37.0, 246.0, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-84.5, 39.0, 280.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-115.5, 37.0, 323.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-75.5, 42.0, 312.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-66.5, 36.0, 326.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-20.5, 16.0, -13.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Highway
                new ItemRandomizer(-20.5, 16.0, -13.5, 0.1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(0.5, 28.0, 48.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(0.5, 28.0, 51.5, 0.1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(82.5, 27.0, 30, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(61.5, 25.0, 171.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(15.5, 26.0, 182.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(20.5, 26.0, 180.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-16.5, 26.0, 181.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-67.5, 37.0, 193.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(3.5, 25.0, 175.5, 0.2, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-113.5, 37.0, 296.5, 0.2, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-79.5, 37.0, 224.5, 0.2, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(88.5, 26.0, 68.5, 0.2, 1, ItemRandomizer.Preset.TIER1_MELEE)
        );
        ammoLocations = List.of(
                new Vector(21.5, 26, 182.5),
                new Vector(19.5, 44, -32.5)
        );
        runAtStart = (manager) -> manager.getWorld().getBlockAt(-95, 31, 223).setType(Material.REDSTONE_BLOCK);
        runAtEnd = (manager) -> {
            // achievements
            boolean hasMrCookie = false;
            for (Player p : manager.getPlayers()) {
                Achievements.SURVIVOR.grant(p);

                 // the departure achievements TODO: bring ts back
                 Achievements.THE_DEPARTURE.grant(p);
                 for (int i = 0; i < 5; i++) {
                     var item = p.getInventory().getItem(i);
                     if (item != null && item.getType() == Material.COOKIE) hasMrCookie = true;
                 }
            }

            if (hasMrCookie) {
                for (Player p : manager.getPlayers()) {
                    Achievements.COOKIE_MONSTER.grant(p);
                }
            }
        };
    }

    @Override
    public List<Listener> happenings(JavaPlugin plugin, GameManager gameManager) {
        return List.of(
                new Listener() {
                    @EventHandler
                    public void subwayOpening(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(13, 41, -30, 10, 47, -45);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.SUBWAY_OPENING.pickRandom(gameManager.getPlayers().size()));
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
                },
                new Listener() {
                    @EventHandler
                    public void subwayPurpleCar(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-30, 19, 2, -35, 28, 5);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, Set.of(e.getPlayer()), DepartureDialogue.PURPLE_CAR.pickRandom(1));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMrCookie(PlayerInteractEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                                var pos = new Vector(-19, 16, -7);
                                if (!e.getClickedBlock().getLocation().toVector().equals(pos))
                                    return;
                                var cookie = Expunge.getItems().toGameItem("MR_COOKIE");
                                cookie.ifPresent(item -> gameManager.spawnItem(item, pos, false));
                                Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.SUBWAY_MR_COOKIE.pickRandom(gameManager.getPlayers().size()));
                                HandlerList.unregisterAll(this);
                            }
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwayMap(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-13, 15, 7, -1, 22, -95);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.SUBWAY_MAP.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void subwaySafeZone(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(38, 23, 171, 24, 31, 184);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.SUBWAY_PASSENGER_TRAIN.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },

                // Highway
                new Listener() {
                    @EventHandler
                    public void highwayManhole(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(11, 24, 178, 3, 31, 184);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.HIGHWAY_MANHOLE.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayOpening(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-3, 35, 184, -9, 42, 190);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.HIGHWAY_OPENING.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayCarBoom(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-74, 34, 221, -119, 49, 247);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            int delay = new HighwayCarBoom().play(gameManager);
                            HandlerList.unregisterAll(this);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.HIGHWAY_CAR_BOOM.pickRandom(gameManager.getPlayers().size()));
                                }
                            }.runTaskLater(plugin, delay - 10);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwaySafeHouse(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-83, 35, 296, -128, 49, 326);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.HIGHWAY_SAFE_HOUSE.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                },
                new Listener() {
                    @EventHandler
                    public void highwayPurpleCar(PlayerMoveEvent e) {
                        gameManager.getSurvivor(e.getPlayer()).ifPresent(s -> {
                            if (!s.isAlive()) return;
                            BoundingBox box = new BoundingBox(-85, 36, 255, -83, 45, 247);
                            if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.HIGHWAY_PURPLE_CAR.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        });
                    }
                });
    }
}
