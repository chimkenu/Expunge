package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.*;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.game.campaign.CampaignDirector;
import me.chimkenu.expunge.mobs.MobType;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.List;

public record Office(
        String name,
        String displayName,

        Vector startLocation,
        BoundingBox endRegion,

        List<Path> escapePath,

        List<Vector> spawnLocations,
        List<Vector> bossLocations,
        List<Vector> rescueClosetLocations,
        NextMapCondition nextMapCondition,

        List<ItemRandomizer> startItems,
        List<ItemRandomizer> mapItems,
        List<Vector> ammoLocations,

        GameAction runAtStart,
        GameAction runAtEnd
) implements CampaignMap, CampaignIntro {
    public static final Office instance = new Office(null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    public Office {
        name = "Office";
        displayName = ChatUtil.getColor(160, 75, 0) + name;
        startLocation = new Vector(-103.5, 9, -327.5);
        endRegion = new BoundingBox(-178, 42, -286, -184, 48, -294);
        escapePath = List.of(
                new PathVector(-103.5, 9, -316.5),
                new PathVector(-103.5, 9, -309.5),
                new PathVector(-99.5, 9, -304.5),
                new PathVector(-96.5, 9, -297.5),
                new PathVector( -97.5, 9, -290.5),
                new PathVector(-104.5, 9, -290.5),
                new PathVector(-112.5, 9, -290.5),
                new PathVector(-120.5, 9, -291.5),
                new PathVector(-128, 9, -292),
                new PathVector(-205, 77, -287),
                new PathVector(-205, 77, -274),
                new PathVector(-205.5, 77, -263.5),
                new PathVector(-191.5, 77, -263.5),
                new PathVector(-185.5, 77, -273.5),
                new PathVector(-180.5, 78.17, -277.5),
                new PathVector(-187.5, 77, -282.5),
                new PathVector(-189, 77, -296),
                new PathVector(-188, 73, -302.5),
                new PathVector(-188, 69, -297.5),
                new PathVector(-188, 65, -302.5),
                new PathVector(-188, 61, -297.5),
                new PathVector(-188, 57, -302.5),
                new PathVector(-189, 53, -297),
                new PathVector(-188.5, 53, -285.5),
                new PathVector(-189.5, 53, -272.5),
                new PathVector(-192.5, 53, -268.5),
                new PathVector(-198.5, 54, -275.5),
                new PathVector(-204.5, 53, -281.5),
                new PathVector(-213.5, 53, -285.5),
                new PathVector(-221.5, 45, -287.5),
                new PathVector(-219, 44, -294),
                new PathVector(-212.5, 43.06, -286.5),
                new PathVector(-209.5, 46, -277.5)
        );
        spawnLocations = List.of(
                new Vector(-84, 9, -321),
                new Vector(-82, 9, -299),
                new Vector(-91.5, 9, -297.5),
                new Vector(-118.5, 9, -301.5),
                new Vector(-116.5, 9, -322.5),
                new Vector(-98, 9, -280),
                new Vector(-118.5, 9, -284.5),
                new Vector(-109.5, 9, -296.5),

                // Part 2
                // elevator: new Vector(-213.5, 77, -276.5),
                new Vector(-211.5, 77, -270.5),
                new Vector(-197.5, 77, -259.5),
                new Vector(-181, 77, -253.5),
                new Vector(-194.5, 77, -249),
                new Vector(-197.5, 77.38, -274.5),
                new Vector(-197, 77, -292),
                new Vector(-181, 53, -268),
                new Vector(-205, 53, -257),
                new Vector(-195.5, 53, -286.5),
                new Vector(-206.5, 53, -278.5),
                new Vector(-212, 53.5, -270.5),
                new Vector(-206.5, 43, -287.5),
                new Vector(-217.5, 43, -275.5),
                new Vector(-214, 61, -287),
                new Vector(-189.5, 49, -302.5),
                new Vector(-186.5, 81, -302.5)
        );
        bossLocations = List.of(
                new Vector(-101.5, 9, -290.5),
                new Vector(-212.5, 43.06, -286.5)
        );
        rescueClosetLocations = List.of();
        nextMapCondition = new Barrier(List.of(
                new Barrier.Block(new Vector(-184, 43, -290), Material.BEEHIVE, true),
                new Barrier.Block(new Vector(-184, 44, -290), Material.BARRIER, true),
                new Barrier.Block(new Vector(-182, 43, -287), Material.BEEHIVE, false)
        ));
        startItems = List.of(
                new ItemRandomizer(-99, 10, -321.5, 1, ItemRandomizer.MATCH_PLAYER_COUNT, List.of("MEDKIT")),
                new ItemRandomizer(-108, 10, -321.5, 1, 1, true, List.of("CROWBAR", "FIRE_AXE", "NIGHTSTICK")),
                new ItemRandomizer(-191.5, 54.19, -291.5, 1, 1, List.of("FRYING_PAN"))
        );
        mapItems = List.of(
                new ItemRandomizer(-99.5, 9, -304.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-96.5, 10, -327.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-112.5, 10, -326.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-123.5, 10, -311, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-81.5, 10, -296.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-108.5, 10, -287.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(-196.5, 78, -245.5, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(-213, 77, -276, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-205.5, 78.44, -254.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-173.5, 78, -292, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-179.5, 53.25, -283.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-210.5, 44, -293.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(-210.5, 43, -281.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-216.5, 52, -271.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-201.5, 53, -259.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS)
        );
        ammoLocations = List.of();
        runAtStart = (gameManager, player) -> {
            gameManager.getWorld().setBlockData(new Location(gameManager.getWorld(), -132, 16, -296), Material.REDSTONE_BLOCK.createBlockData());
            gameManager.getWorld().setBlockData(new Location(gameManager.getWorld(), -208, 85, -285), Material.REDSTONE_BLOCK.createBlockData());
            Dialogue.display(gameManager.getPlugin(), gameManager.getPlayers(), DepartureDialogue.OFFICE_OPENING.pickRandom(gameManager.getPlayers().size()));
        };
        runAtEnd = null;
    }

    @Override
    public List<Listener> happenings(Expunge plugin, GameManager gameManager) {
        return List.of(
                new Listener() {
                    @EventHandler
                    public void onElevatorPress(PlayerInteractEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                            if (!gameManager.isRunning()) {
                                return;
                            }
                            Vector buttonLoc = new Vector(-127, 10, -291);
                            Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                            if (!buttonLoc.equals(clickedLoc)) {
                                return;
                            }

                            BoundingBox elevator = new BoundingBox(-131, 7, -295, -126, 13, -289);
                            for (Player p : gameManager.getPlayers()) {
                                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    Location pLoc = p.getLocation();
                                    if (!elevator.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                                        // a player is still not in the end zone
                                        ChatUtil.sendActionBar(e.getPlayer(), "&aNot all alive players are in the elevator!");
                                        return;
                                    }
                                }
                            }

                            gameManager.getWorld().setBlockData(new Location(gameManager.getWorld(), -132, 19, -296), Material.REDSTONE_BLOCK.createBlockData());
                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_ELEVATOR.pickRandom(gameManager.getPlayers().size()));
                            HandlerList.unregisterAll(this);
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeRubble(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-194, 76, -268, -185, 81, -260);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_RUBBLE.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeJump(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-208, 52, -285, -209, 57, -279);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_JUMP.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void onEnterDevelopersRoom(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        if (new BoundingBox(-218, 42, -276, -214, 47, -272).contains(e.getPlayer().getLocation().toVector()))
                            Achievements.THE_DEVELOPERS_ROOM.grant(e.getPlayer());
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeVent(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-218, 42, -278, -207, 47, -297);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_VENTS.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void onEnterVent(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && block.getLocation().toVector().equals(new Vector(-210, 45, -275)))) {
                            return;
                        }

                        CampaignDirector.playCrescendoEventEffect(gameManager.getPlayers());
                        World world = gameManager.getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                gameManager.getDirector().spawnMob(MobType.COMMON, 1, new Vector(-210.5, 49, -271.5), true);
                                gameManager.getDirector().spawnMob(MobType.COMMON, 1, new Vector(-202.5, 48, -260.5), true);
                                gameManager.getDirector().spawnMob(MobType.COMMON, 1, new Vector(-189.5, 49, -260.5), true);
                                gameManager.getDirector().spawnMob(MobType.COMMON, 1, new Vector(-183.5, 49, -268.5), true);
                                gameManager.getDirector().spawnMob(MobType.COMMON, 1, new Vector(-179.5, 49, -274.5), true);
                                i++;
                                if (i >= 5) {
                                    this.cancel();
                                }
                                if (!gameManager.isRunning() || gameManager.getDirector().getPhase() == Director.Phase.DISABLED) {
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 1, 20 * 4);

                        // crescendo events are only meant to happen once
                        // hence it unregisters the event when it executes
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeSafeRoom(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-183, 42, -282, -179, 48, -275);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_SAFE_ROOM.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                }
        );
    }

    @Override
    public int play(GameManager gameManager) {
        CampaignIntro.super.play(gameManager);
        final Location[] points = new Location[]{
                new Location(gameManager.getWorld(), -102.25, 10.5, -305.7, -45, 28),
                new Location(gameManager.getWorld(), -102.25, 10.5, -305.7, -45, 28),
                new Location(gameManager.getWorld(), -103.6, 9.75, -313.85, -30.5f, 9.5f),
                new Location(gameManager.getWorld(), -103.5, 9.4, -315.5, 0, 0),
                new Location(gameManager.getWorld(), -103.5, 9, -322.5, 0, 0),
                new Location(gameManager.getWorld(), -103.5, 9, -327.5, 0, 0)
        };
        final String main = "The Departure";
        final String sub = ChatUtil.getColor(134, 0, 179) + "Built by SirSunlight & Pagkain";

        int time = play(gameManager, points, main, sub, new Color(255, 92, 51), new Color(102, 0, 0), 3, 0.1, 10);
        new BukkitRunnable() {
            @Override
            public void run() {
                end(gameManager);
            }
        }.runTaskLater(gameManager.getPlugin(), time);
        return time;
    }
}
