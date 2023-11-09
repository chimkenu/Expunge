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
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Stadium extends CampaignMap {
    @Override
    public String directory() {
        return "Stadium";
    }

    @Override
    public Vector startLocation() {
        return new Vector(-103, 36, -148);
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
        return new Vector[]{
                 new Vector(-142.16, 37.0, -123.),
                 new Vector(-136.25, 36.0, -123.),
                 new Vector(-118.53, 36.0, -136.),
                 new Vector(-92.49, 36.0, -152.5),
                 new Vector(-136.13, 36.0, -67.1),
                 new Vector(-115.95, 37.0, -30.5),
                 new Vector(-110.09, 37.0, -30.5),
                 new Vector(-96.53, 39.0, -10.55),
                 new Vector(-79.52, 37.0, -13.56),
                 new Vector(-43.58, 38.0, -43.44),
                 new Vector(-22.44, 36.0, 33.325),
                 new Vector(4.551, 36.0, 49.498),
                 new Vector(96.49, 36.0, 59.5264),
                 new Vector(118.53, 45.0, 128.95),
                 new Vector(85.53, 36.0, 172.534),
                 new Vector(97.44, 36.0, 136.456),
                 new Vector(111.54, 36.0, 138.46),
                 new Vector(98.50, 36.0, 127.501),
                 new Vector(101.5, 36.0, 104.572),
                 new Vector(44.496, 36.0, 95.537),
                 new Vector(57.396, 36.0, 92.514),
                 new Vector(15.55, 36.0, 164.403),
                 new Vector(2.4682, 36.0, 138.46),
                 new Vector(-10.545, 36.0, 135.4),
                 new Vector(-10.536, 36.0, 130.5),
                 new Vector(2.4294, 36.0, 130.52),
                 new Vector(7.442, 36.0, 104.414)
        };
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[0];
    }

    @Override
    public Vector[] itemLocations() {
        return new Vector[]{
                new Vector(7.5, 37.0, 38.5),
                new Vector(35.5, 37.0, 69.5),
                new Vector(65.5, 37.0, 66.5),
                new Vector(48.5, 36.0, 94.5),
                new Vector(58.5, 37.0, 114.5),
                new Vector(39.5, 37.0, 154.5),
                new Vector(42.5, 37.0, 114.5),
                new Vector(39.5, 37.0, 114.5),
                new Vector(91.5, 37.0, 168.5),
                new Vector(97.5, 36.0, 131.5),
                new Vector(97.5, 37.0, 141.5),
                new Vector(95.5, 37.0, 100.5),
                new Vector(8.5, 37.0, 100.5),
                new Vector(4.5, 37.0, 132.5),
                new Vector(-15.5, 37.0, 135.5),
                new Vector(9.5, 37.0, 168.5),
                new Vector(50.5, 46.0, 180.5)
        };
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
        return new Vector(49, 40, 134);
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
                    public void stadiumParkingLotA(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1034, 34, 1449, 1042, 50, 1500);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1108, 35, 1466, 1170, 35, 1534), 20 + (localGameManager.getDifficulty().ordinal() * 15));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void stadiumParkingLotB(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1106, 34, 1492, 1102, 55, 1455);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        localGameManager.getDirector().bile(plugin, e.getPlayer(), 5);
                        localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1179, 35, 1466, 1239, 35, 1569), 20 + (localGameManager.getDifficulty().ordinal() * 10));
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STADIUM_PARKING_LOT.pickRandom(localGameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void stadiumEnter(PlayerMoveEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer()))
                            return;
                        BoundingBox box = new BoundingBox(1182, 34, 1580, 1190, 39, 1587);
                        if (!box.contains(e.getPlayer().getLocation().toVector()))
                            return;
                        Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STADIUM_ENTER.pickRandom(localGameManager.getPlayers().size()));
                        localGameManager.getDirector().bile(plugin, e.getPlayer(), 5);
                        localGameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1219, 35, 1616, 1153, 35, 1660), 30 + (localGameManager.getDifficulty().ordinal() * 10));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void finaleBegin(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            return;
                        }
                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.LEVER)) {
                            return;
                        }
                        Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                        if (clickedLoc.equals(new Vector(1185, 46, 1684)) || clickedLoc.equals(new Vector(1187, 46, 1684))) {
                            Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                            summonHorde(plugin, localGameManager);
                            new BukkitRunnable() {
                                int i = 20 * 40;
                                @Override
                                public void run() {
                                    i--;
                                    if (!localGameManager.isRunning() || !localGameManager.getDirector().isSpawningEnabled()) this.cancel();
                                    if (i <= 0) {
                                        summonHorde(plugin, localGameManager);
                                        this.cancel();
                                    }
                                }
                            }.runTaskTimer(plugin, 0, 1);

                            // spawn in tank after horde is dead
                            plugin.getServer().getPluginManager().registerEvents(new Listener() {
                                @EventHandler
                                public void afterHorde(EntityDeathEvent e) {
                                    if (localGameManager.getDirector().getActiveMobs() <= 5) {
                                        localGameManager.getDirector().spawnTank();
                                        HandlerList.unregisterAll(this);
                                    }
                                }
                            }, plugin);
                            // after tank dies
                            plugin.getServer().getPluginManager().registerEvents(new Listener() {
                                @EventHandler
                                public void afterTank(EntityDeathEvent e) {
                                    if (e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                                        // 30-second timer
                                        new BukkitRunnable() {
                                            int i = 30;
                                            @Override
                                            public void run() {


                                                if (i <= 0) {
                                                    // do the same thing
                                                    summonHorde(plugin, localGameManager);
                                                    Campaign.playCrescendoEventEffect(localGameManager.getPlayers());
                                                    new BukkitRunnable() {
                                                        int i = 20 * 40;
                                                        @Override
                                                        public void run() {
                                                            i--;
                                                            if (!localGameManager.isRunning() || !localGameManager.getDirector().isSpawningEnabled()) this.cancel();
                                                            if (i <= 0) {
                                                                summonHorde(plugin, localGameManager);
                                                                this.cancel();
                                                            }
                                                        }
                                                    }.runTaskTimer(plugin, 0, 1);
                                                    // spawn in tank after horde is dead
                                                    plugin.getServer().getPluginManager().registerEvents(new Listener() {
                                                        @EventHandler
                                                        public void afterHorde(EntityDeathEvent e) {
                                                            if (localGameManager.getDirector().getActiveMobs() <= 5) {
                                                                localGameManager.getDirector().spawnTank();
                                                                HandlerList.unregisterAll(this);
                                                            }
                                                        }
                                                    }, plugin);
                                                    // after tank dies stadium ending
                                                    plugin.getServer().getPluginManager().registerEvents(new Listener() {
                                                        @EventHandler
                                                        public void afterTank(EntityDeathEvent e) {
                                                            if (e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                                                                Dialogue.display(plugin, localGameManager.getPlayers(), DepartureDialogue.STADIUM_ENDING.pickRandom(localGameManager.getPlayers().size()));
                                                                // TODO: Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock 1168 17 1626 minecraft:redstone_block");
                                                            }
                                                        }
                                                    }, plugin);

                                                    this.cancel();
                                                }


                                                // timer
                                                if (!localGameManager.isRunning() || !localGameManager.getDirector().isSpawningEnabled()) this.cancel();
                                                for (Player p : localGameManager.getPlayers()) {
                                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7" + i));
                                                }
                                                i--;
                                            }
                                        }.runTaskTimer(plugin, 0, 20);
                                        HandlerList.unregisterAll(this);
                                    }
                                }
                            }, plugin);
                            HandlerList.unregisterAll(this);
                        }
                    }
                },
                new Listener() {
                    @EventHandler
                    public void finaleBegin(PlayerInteractEvent e) {
                        if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                            return;
                        }
                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            return;
                        }
                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON)) {
                            return;
                        }
                        Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                        if (clickedLoc.equals(new Vector(1185, 39, 1642)) || clickedLoc.equals(new Vector(1185, 39, 1634))) {
                            Achievements.HEY_DONT_TOUCH_THAT.grant(e.getPlayer());
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

    private void summonHorde(JavaPlugin plugin, LocalGameManager localGameManager) {
        new BukkitRunnable() {
            int i = 6 + (localGameManager.getDifficulty().ordinal() * 4);
            @Override
            public void run() {
                if (i <= 0) this.cancel();
                if (!localGameManager.isRunning() || !localGameManager.getDirector().isSpawningEnabled()) this.cancel();
                for (int j = 0; j < 5; j++) {
                    localGameManager.getDirector().spawnAdditionalMob();
                }
                i--;
            }
        }.runTaskTimer(plugin, 0, 20 * 5);
    }
}
