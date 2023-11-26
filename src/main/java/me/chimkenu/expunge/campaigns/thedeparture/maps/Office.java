package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignIntro;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.items.utilities.healing.Medkit;
import me.chimkenu.expunge.items.weapons.melees.Crowbar;
import me.chimkenu.expunge.items.weapons.melees.FireAxe;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import me.chimkenu.expunge.items.weapons.melees.Nightstick;
import me.chimkenu.expunge.mobs.common.Horde;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Office implements CampaignMap, CampaignIntro {
    @Override
    public String directory() {
        return "Office";
    }

    @Override
    public Vector startLocation() {
        return new Vector(-7.5, 9, -23.5);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(24, 42, -102, 30, 47, -94);
    }

    @Override
    public BoundingBox[] pathRegions() {
        return new BoundingBox[0];
    }

    @Override
    public Vector[] spawnLocations() {
        return new Vector[]{
                new Vector(12, 9, -18),
                new Vector(-20.5, 9.0, -18.5),
                new Vector(-31, 10.0, -7),
                new Vector(4.5, 9.0, 6.5),
                new Vector(-31.0, 10.0, 2),
                new Vector(16.5, 9.0, 5.5),
                new Vector(-3, 9.0, 24),
                new Vector(-13.5, 9.0, 19.5),
                new Vector(-13.5, 9.0, 7.5),

                // Part 2
                new Vector(-5.5, 77.0, -84.5),
                new Vector(-3.5, 77.0, -78.5),
                new Vector(-9.5, 77.0, -73.5),
                new Vector(10.5, 77.0, -67.5),
                new Vector(15.5, 77.0, -53.5),
                new Vector(27, 77.0, -62),
                new Vector(28.5, 77.0, -72.5),
                new Vector(11.5, 77.0, -78.5),
                new Vector(7.5, 77.0, -86.5),
                new Vector(11, 77.0, -100),
                new Vector(32, 78.0, -99),
                new Vector(23.5, 53.0, -91.5),
                new Vector(27, 53.0, -76),
                new Vector(2.5, 53.0, -65.5),
                new Vector(-8.5, 52.0, -81.5),
                new Vector(1.5, 43.0, -95.5)
        };
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[]{
                new Vector(-5.5, 9.0, 13.5),
                new Vector(-36.5, 42, -131.5) // Part 2
        };
    }

    @Override
    public ItemRandomizer[] randomizedGameItems() {
        return new ItemRandomizer[]{
                new ItemRandomizer(-3, 10, -17.5, 1, 4, List.of(GameItems.MEDKIT)),
                new ItemRandomizer(-12, 10, -17.5, 1, 1, List.of(GameItems.CROWBAR, GameItems.FIRE_AXE, GameItems.NIGHTSTICK)),

                new ItemRandomizer(1.5, 10, -22.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-18.5, 10, -22.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(14.5, 10, 9.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-12.5, 10, 16.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-4.5, 9, 0.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(13.5, 78, -55.5, 0.1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(28.5, 54, -89.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-3.5, 54, -65.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-8.5, 52, -79.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-2.5, 44, -101.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(-33.5, 10, 3.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(-3.5, 43, -87.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS)
        };
    }

    @Override
    public Vector[] ammoLocations() {
        return new Vector[0];
    }

    @Override
    public Vector buttonLocation() {
        return new Vector(28, 44, -101);
    }

    @Override
    public Vector[] rescueClosetLocations() {
        return new Vector[0];
    }

    @Override
    public GameAction runAtStart() {
        return (plugin, gameManager, player) -> Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_OPENING.pickRandom(gameManager.getPlayers().size()));
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
                    public void onElevatorPress(PlayerInteractEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                            if (!gameManager.isRunning()) {
                                return;
                            }
                            Vector buttonLoc = new Vector(-31, 10, 13);
                            Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                            if (!buttonLoc.equals(clickedLoc)) {
                                return;
                            }

                            BoundingBox elevator = new BoundingBox(-36, 7, 15, -30, 14, 9);
                            for (Player p : gameManager.getPlayers()) {
                                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    Location pLoc = p.getLocation();
                                    if (!elevator.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                                        // a player is still not in the end zone
                                        e.getPlayer().sendActionBar(Component.text("Not all alive players are in the elevator!", NamedTextColor.RED));
                                        return;
                                    }
                                }
                            }

                            for (Player p : gameManager.getPlayers()) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 5, false, true));
                                p.teleport(new Location(gameManager.getWorld(), 3, 77, -94, 0, 0));
                            }

                            Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_ELEVATOR.pickRandom(gameManager.getPlayers().size()));
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeRubble(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(14, 75, -75, 24, 82, -85);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_RUBBLE.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeJump(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-7, 51, -91, -13, 58, -104);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_JUMP.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void onEnterDevelopersRoom(PlayerMoveEvent e) {
                        if (new BoundingBox(-10, 41, -84, -7, 48, -80).contains(e.getPlayer().getLocation().toVector()))
                            Achievements.THE_DEVELOPERS_ROOM.grant(e.getPlayer());
                    }
                },
                new Listener() {
                    @EventHandler
                    public void onEnterVent(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!gameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && block.getLocation().toVector().equals(new Vector(-2, 45, -83)))) {
                            return;
                        }

                        Campaign.playCrescendoEventEffect(gameManager.getPlayers());
                        World world = gameManager.getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(-2.5, 49, -79.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(5.5, 48, -68.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(18.5, 49, -68.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(24.5, 49, -76.5), gameManager.getDifficulty()));
                                gameManager.getDirector().getMobHandler().spawnMob(new Horde(plugin, world, new Vector(28.5, 49, -82.5), gameManager.getDifficulty()));
                                i++;
                                if (i >= 5) {
                                    this.cancel();
                                }
                                if (!gameManager.isRunning() || !gameManager.getDirector().getMobHandler().isSpawningEnabled()) {
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
                    public void officeVent(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(0, 41, -95, -10, 48, -86);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_VENTS.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeSafeRoom(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(29, 43, -93, 24, 47, -85);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.OFFICE_SAFE_ROOM.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                }
        };
    }

    @Override
    public int play(GameManager gameManager) {
        final Location[] points = new Location[]{
                new Location(gameManager.getWorld(), -6.25, 10.5, -1.7, -45, 28),
                new Location(gameManager.getWorld(), -6.25, 10.5, -1.7, -45, 28),
                new Location(gameManager.getWorld(), -7.6, 9.75, -5.85, -30.5f, 9.5f),
                new Location(gameManager.getWorld(), -7.5, 9.4, -9.5, 0, 0),
                new Location(gameManager.getWorld(), -7.5, 9, -16.5, 0, 0),
                new Location(gameManager.getWorld(), -7.5, 9, -21.5, 0, 0)
        };
        final String main = "The Departure";
        final Component sub = Component.text("Built by SirSunlight & Pagkain").color(TextColor.color(134, 0, 179));

        return play(gameManager, points, main, sub, new Color(255, 92, 51), new Color(102, 0, 0), 3, 0.1, 10);
    }
}
