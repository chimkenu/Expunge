package me.chimkenu.expunge.game.maps.thedeparture.scenes;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.director.ItemHandler;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.guns.utilities.healing.Adrenaline;
import me.chimkenu.expunge.guns.utilities.healing.Medkit;
import me.chimkenu.expunge.guns.utilities.healing.Pills;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;

import static me.chimkenu.expunge.game.maps.thedeparture.DepartureDialogue.playDialogue;

public class Stadium {
    public static Scene getScene() {
        World world = Bukkit.getWorld("world");
        if (world == null) return null;

        ArrayList<BoundingBox> pathRegions = new ArrayList<>();
        pathRegions.add(new BoundingBox(1044, 35, 1363, 1021, 35, 1400));
        pathRegions.add(new BoundingBox(1011, 35, 1403, 997, 35, 1463));
        pathRegions.add(new BoundingBox(1016, 35, 1448, 1074, 35, 1481));
        pathRegions.add(new BoundingBox(1075, 35, 1480, 1107, 35, 1466));
        pathRegions.add(new BoundingBox(1108, 35, 1466, 1264, 35, 1534));
        pathRegions.add(new BoundingBox(1239, 35, 1536, 1133, 35, 1569));
        pathRegions.add(new BoundingBox(1132, 35, 1571, 1252, 35, 1693));

        ArrayList<Location> spawnLocations = new ArrayList<>();
        spawnLocations.add(new Location(world, 1043.5, 36, 1344.5));
        spawnLocations.add(new Location(world, 1042.5, 36, 1378));
        spawnLocations.add(new Location(world, 1005.5, 36, 1398.5));
        spawnLocations.add(new Location(world, 999.5, 36, 1378.5));
        spawnLocations.add(new Location(world, 999.5, 36, 1437.5));
        spawnLocations.add(new Location(world, 1031.5, 37, 1474.5));
        spawnLocations.add(new Location(world, 1023.5, 37, 1476.5));
        spawnLocations.add(new Location(world, 1057.5, 37, 1490.5));
        spawnLocations.add(new Location(world, 1089.5, 36, 1470.5));
        spawnLocations.add(new Location(world, 1229.5, 36, 1573.5));
        spawnLocations.add(new Location(world, 1246.5, 38, 1539.5));
        spawnLocations.add(new Location(world, 1127.5, 38, 1552.5));
        spawnLocations.add(new Location(world, 1098.5, 45, 1458.5));
        spawnLocations.add(new Location(world, 1181, 36, 1594.5));
        spawnLocations.add(new Location(world, 1241.5, 36, 1611.5));
        spawnLocations.add(new Location(world, 1241.5, 36, 1665.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1665.5));
        spawnLocations.add(new Location(world, 1131.5, 36, 1611.5));
        spawnLocations.add(new Location(world, 1245.5, 45, 1642));
        spawnLocations.add(new Location(world, 1127.5, 45, 1635));
        spawnLocations.add(new Location(world, 1184, 36, 1579));
        spawnLocations.add(new Location(world, 1189, 36, 1579));

        ArrayList<Location> bossLocations = new ArrayList<>();
        bossLocations.add(new Location(world, 1236.5, 36, 1662.5));
        bossLocations.add(new Location(world, 1136.5, 36, 1613.5));
        bossLocations.add(new Location(world, 1186.5, 45, 1591.5));

        ArrayList<Location> itemLocations = new ArrayList<>();
        itemLocations.add(new Location(world, 1175.5, 37, 1658.5));
        itemLocations.add(new Location(world, 1192.5, 36.6, 1656.5));
        itemLocations.add(new Location(world, 1163, 37.5, 1575.5));
        itemLocations.add(new Location(world, 1194.5, 37, 1546.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1548.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1604.5));
        itemLocations.add(new Location(world, 1228.5, 37, 1672.5));
        itemLocations.add(new Location(world, 1186.5, 46, 1684.5));
        itemLocations.add(new Location(world, 1134.5, 36, 1665.5));
        itemLocations.add(new Location(world, 1139.5, 37, 1645.5));

        ArrayList<Location> weaponLocations = new ArrayList<>();
        weaponLocations.add(new Location(world, 1209.5, 36.5, 1576.5));
        weaponLocations.add(new Location(world, 1183.5, 37, 1585.5));
        weaponLocations.add(new Location(world, 1192.5, 36, 1599.5));
        weaponLocations.add(new Location(world, 1248, 45, 1637));
        weaponLocations.add(new Location(world, 1128.5, 46.2, 1644.5));

        ArrayList<Location> ammoLocations = new ArrayList<>();
        ammoLocations.add(new Location(world, 1175.5, 37, 1658.5));

        ArrayList<Listener> happenings = new ArrayList<>();
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumParkingLotA(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1034, 34, 1449, 1042, 50, 1500);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1108, 35, 1466, 1170, 35, 1534), 20 + (Expunge.currentDifficulty.ordinal() * 15));
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumParkingLotB(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1106, 34, 1492, 1102, 55, 1455);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                Expunge.runningDirector.bile(e.getPlayer(), 5);
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1179, 35, 1466, 1239, 35, 1569), 20 + (Expunge.currentDifficulty.ordinal() * 10));
                playDialogue(DepartureDialogue.STADIUM_PARKING_LOT);
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void stadiumEnter(PlayerMoveEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer()))
                    return;
                BoundingBox box = new BoundingBox(1182, 34, 1580, 1190, 39, 1587);
                if (!box.contains(e.getPlayer().getLocation().toVector()))
                    return;
                playDialogue(DepartureDialogue.STADIUM_ENTER);
                Expunge.runningDirector.bile(e.getPlayer(), 5);
                Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1219, 35, 1616, 1153, 35, 1660), 30 + (Expunge.currentDifficulty.ordinal() * 10));
                Expunge.runningDirector.forceChillOut = true;
                HandlerList.unregisterAll(this);
            }
        });
        happenings.add(new Listener() {
            @EventHandler
            public void finaleBegin(PlayerInteractEvent e) {
                if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
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
                    Scene.playCrescendoEventEffect();
                    summonHorde();
                    Expunge.runningDirector.forceChillOut = false;
                    new BukkitRunnable() {
                        int i = 20 * 20;
                        @Override
                        public void run() {
                            i--;
                            if (!Expunge.isGameRunning || !Expunge.isSpawningEnabled) this.cancel();
                            if (i <= 0) {
                                summonHorde();
                                Expunge.runningDirector.forceChillOut = true;
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Expunge.instance, 0, 1);

                    // spawn in tank after horde is dead
                    Expunge.instance.getServer().getPluginManager().registerEvents(new Listener() {
                        @EventHandler
                        public void afterHorde(EntityDeathEvent e) {
                            if (Expunge.runningDirector.getActiveMobs() <= 5) {
                                Expunge.runningDirector.mobHandler.spawnTank();
                                HandlerList.unregisterAll(this);
                            }
                        }
                    }, Expunge.instance);
                    // after tank dies
                    Expunge.instance.getServer().getPluginManager().registerEvents(new Listener() {
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
                                            summonHorde();
                                            Expunge.runningDirector.forceChillOut = false;
                                            new BukkitRunnable() {
                                                int i = 20 * 20;
                                                @Override
                                                public void run() {
                                                    i--;
                                                    if (!Expunge.isGameRunning || !Expunge.isSpawningEnabled) this.cancel();
                                                    if (i <= 0) {
                                                        summonHorde();
                                                        Expunge.runningDirector.forceChillOut = true;
                                                        this.cancel();
                                                    }
                                                }
                                            }.runTaskTimer(Expunge.instance, 0, 1);
                                            // spawn in tank after horde is dead
                                            Expunge.instance.getServer().getPluginManager().registerEvents(new Listener() {
                                                @EventHandler
                                                public void afterHorde(EntityDeathEvent e) {
                                                    if (Expunge.runningDirector.getActiveMobs() <= 5) {
                                                        Expunge.runningDirector.mobHandler.spawnTank();
                                                        HandlerList.unregisterAll(this);
                                                    }
                                                }
                                            }, Expunge.instance);
                                            // after tank dies stadium ending
                                            Expunge.instance.getServer().getPluginManager().registerEvents(new Listener() {
                                                @EventHandler
                                                public void afterTank(EntityDeathEvent e) {
                                                    if (e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                                                        playDialogue(DepartureDialogue.STADIUM_ENDING);
                                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock 1168 17 1626 minecraft:redstone_block");
                                                    }
                                                }
                                            }, Expunge.instance);

                                            this.cancel();
                                        }


                                        // timer
                                        if (!Expunge.isGameRunning || !Expunge.isSpawningEnabled) this.cancel();
                                        for (Player p : Expunge.playing.getKeys()) {
                                            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7" + i));
                                        }
                                        i--;
                                    }
                                }.runTaskTimer(Expunge.instance, 0, 20);
                                HandlerList.unregisterAll(this);
                            }
                        }
                    }, Expunge.instance);
                    HandlerList.unregisterAll(this);
                }
            }
        });
        return new Scene(
                new Location(world, 1033.5, 36, 1353.5),
                new BoundingBox(1184, 38, 1640, 1190, 43, 1635),
                pathRegions,
                spawnLocations,
                bossLocations,
                itemLocations,
                10,
                weaponLocations,
                ammoLocations,
                new Location(world, 1185, 40, 1638),
                player -> {
                    world.getBlockAt(new Location(world, 1031, 37, 1353)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1031, 36, 1353)).setType(Material.BEEHIVE);
                    world.getBlockAt(new Location(world, 1033, 36, 1360)).setType(Material.AIR);
                    world.getBlockAt(new Location(world, 1032, 36, 1360)).setType(Material.AIR);

                    for (int i = 0; i < 4; i++) {
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1031.3, 37, 1358), new Medkit());
                        Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1196, 36, 1658), new Medkit());
                    }
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1186.5, 46, 1684.5), new Medkit());
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1180.7, 36, 1655.3), new Medkit());
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1034.5, 37, 1359), ItemHandler.getRandomGun(Tier.TIER2), true);
                    Expunge.runningDirector.itemHandler.spawnWeapon(new Location(world, 1034.5, 37, 1357), ItemHandler.getRandomMelee(Tier.TIER1), false);
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1038.5, 37, 1352.5), new Pills());
                    Expunge.runningDirector.itemHandler.spawnUtility(new Location(world, 1037.5, 36, 1359.5), new Adrenaline());

                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "setblock 1168 17 1627 minecraft:redstone_block");

                    Expunge.runningDirector.mobHandler.spawnAtRandomLocations(new BoundingBox(1011, 35, 1375, 997, 35, 1428), 30 + (Expunge.currentDifficulty.ordinal() * 10));
                },
                null,
                null,
                happenings
        );
    }

    private static void summonHorde() {
        new BukkitRunnable() {
            int i = 30 + (Expunge.currentDifficulty.ordinal() * 15);
            @Override
            public void run() {
                if (i <= 0) this.cancel();
                if (!Expunge.isGameRunning || !Expunge.isSpawningEnabled) this.cancel();
                Expunge.runningDirector.mobHandler.spawnAdditionalMob();
                i--;
            }
        }.runTaskTimer(Expunge.instance, 0, 2);
    }
}
