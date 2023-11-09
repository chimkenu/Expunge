package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import me.chimkenu.expunge.mobs.common.Horde;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Office extends CampaignMap {
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
    public Vector[] itemLocations() {
        return new Vector[]{
                new Vector(1.5, 10.0, -22.5),
                new Vector(-18.5, 10.0, -22.5),
                new Vector(14.5, 10.0, 9.5),
                new Vector(-12.5, 10.0, 16.5),
                new Vector(-4.5, 9.0, 0.5),

                // Part 2
                new Vector(13.5, 78.0, -55.5),
                new Vector(28.5, 54.0, -89.5),
                new Vector(-3.5, 54.0, -65.5),
                new Vector(-8.5, 52.0, -79.5),
                new Vector(-2.5, 44.0, -101.5)
        };
    }

    @Override
    public int baseItemsToSpawn() {
        return 0;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[]{
                new Vector(-33.5, 10.0, 3.5),
                new Vector(-3.5, 43, -87.5)
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
    public GameAction runAtStart() {
        return null;
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
                    public void onElevatorPress(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
                            if (!localGameManager.isRunning()) {
                                return;
                            }
                            Vector buttonLoc = new Vector(-31, 10, 13);
                            Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                            if (!buttonLoc.equals(clickedLoc)) {
                                return;
                            }

                            BoundingBox elevator = new BoundingBox(-36, 7, 15, -30, 14, 9);
                            for (Player p : localGameManager.getPlayers()) {
                                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                                    Location pLoc = p.getLocation();
                                    if (!elevator.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                                        // a player is still not in the end zone
                                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cNot all alive players are in the elevator!"));
                                        return;
                                    }
                                }
                            }

                            localGameManager.getWorld().getBlockAt(-2, 81, -94).setType(Material.REDSTONE_BLOCK);

                            for (Player p : localGameManager.getPlayers()) {
                                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 5, false, true));
                                p.teleport(new Location(localGameManager.getWorld(), 3, 77, -94, 0, 0));
                            }
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeJump(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(-7, 51, -91, -13, 58, -104);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.OFFICE_JUMP.pickRandom(localGameManager.getPlayers().size()));
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
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && block.getLocation().toVector().equals(new Vector(-2, 45, -84)))) {
                            return;
                        }

                        Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                        World world = localGameManager.getWorld();
                        new BukkitRunnable() {
                            int i = 0;

                            @Override
                            public void run() {
                                localGameManager.getDirector().spawnMob(new Horde(plugin, world, new Vector(-2.5, 49, -79.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().spawnMob(new Horde(plugin, world, new Vector(-5.5, 48, -68.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().spawnMob(new Horde(plugin, world, new Vector(18.5, 49, -68.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().spawnMob(new Horde(plugin, world, new Vector(24.5, 49, -76.5), localGameManager.getDifficulty()));
                                localGameManager.getDirector().spawnMob(new Horde(plugin, world, new Vector(28.5, 49, -82.5), localGameManager.getDifficulty()));
                                i++;
                                if (i >= 5) {
                                    this.cancel();
                                }
                                if (localGameManager.getCampaignMapIndex() != 1) {
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
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(0, 41, -95, -10, 48, -86);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.OFFICE_VENTS.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void officeSafeRoom(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(29, 43, -93, 24, 47, -85);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.OFFICE_SAFE_ROOM.pickRandom(localGameManager.getPlayers().size()));
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
