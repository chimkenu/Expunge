package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
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

import static me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue.playDialogue;

public class Stadium extends CampaignMap {
    @Override
    public String directory() {
        return "Stadium";
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
                        playDialogue(DepartureDialogue.STADIUM_PARKING_LOT);
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
                        playDialogue(DepartureDialogue.STADIUM_ENTER);
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
                                                                playDialogue(DepartureDialogue.STADIUM_ENDING);
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
                new SwingListener(plugin, localGameManager)
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
