package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class Alleys extends CampaignMap {
    @Override
    public String directory() {
        return "Alleys";
    }

    @Override
    public Vector startLocation() {
        return new Vector(3.5, 43, -28.5);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(-7, 39, 34, -20, 57, 61);
    }

    @Override
    public BoundingBox[] pathRegions() {
        return new BoundingBox[]{
                new BoundingBox(1143, 42, 916, 1156, 42, 923),
                new BoundingBox(1151, 42, 922, 1155, 42, 938),
                new BoundingBox(1152, 42, 933, 1168, 42, 948),
                new BoundingBox(1151, 42, 934, 1131, 42, 961),
                new BoundingBox(1147, 42, 951, 1175, 42, 973),
                new BoundingBox(1157, 42, 970, 1153, 42, 988),
                new BoundingBox(1153, 42, 988, 1130, 42, 984)
        };
    }

    @Override
    public Vector[] spawnLocations() {
        return new Vector[]{
                new Vector(19.5, 43.0, -29.5),
                new Vector(34.5, 43.0, -20.5),
                new Vector(21, 43.0, -14),
                new Vector(31, 43.0, -7),
                new Vector(18.5, 43.0, 6.5),
                new Vector(40, 43.0, 10),
                new Vector(40, 43.0, 27),
                new Vector(33.5, 42.0, 46.5),
                new Vector(33.5, 52.5, 29.5),
                new Vector(18.5, 43.375, 21.5),
                new Vector(28.5, 44.0, 13.5),
                new Vector(23.5, 43.0, 9.5),
                new Vector(15.5, 43.0, 9.5),
                new Vector(-3.5, 43.0, -1.5),
                new Vector(-6.5, 43.0, 6.5),
                new Vector(8.5, 44.0, 13.5),
                new Vector(1.5, 43.0, 23.5),
                new Vector(3.5, 43.5, 30.5),
                new Vector(13.5, 43.0, 46.5),
                new Vector(5.5, 43.0, 51.5),
                new Vector(-5.5, 43.0, 49.5),
                new Vector(-5.5, 43.0, 28.5),
                new Vector(-9.5, 43.0, 34.5),
                new Vector(12.5, 43.0, -10.5)

        };
    }

    @Override
    public Vector[] bossLocations() {
        return new Vector[]{
                new Vector(1155, 42, 978)
        };
    }

    @Override
    public Vector[] itemLocations() {
        return new Vector[]{
                new Vector(8.5, 44.0, -18.5),
                new Vector(30.5, 44.0, -22.5),
                new Vector(29.5, 44.0, -9.5),
                new Vector(-2.5, 43.0, -0.5),
                new Vector(16.5, 44.0, 24.5),
                new Vector(38.5, 44.0, 38.5),
                new Vector(32.5, 44.0, 37.5),
                new Vector(-10.5, 42.5, 31.5),
                new Vector(-0.5, 44.0, 49.5)
        };
    }

    @Override
    public int baseItemsToSpawn() {
        return 0;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[]{
                new Vector(11.5, 44.0, 36.5),
                new Vector(39.5, 52.375, 38.5),
                new Vector(21.5, 44.0, 4.5),
                new Vector(3.5, 44.0, 10.5),
                new Vector(39.5, 52.375, 36.5)
        };
    }

    @Override
    public Vector[] ammoLocations() {
        return new Vector[0];
    }

    @Override
    public Vector buttonLocation() {
        return new Vector(-12, 44, 43);
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
    public boolean isStartSafeRoom() {
        return true;
    }

    @Override
    public Listener[] happenings(JavaPlugin plugin, LocalGameManager localGameManager) {
        return new Listener[0];
//        return new Listener[]{
//                new Listener() {
//                    @EventHandler
//                    public void alleysOpening(PlayerMoveEvent e) {
//                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;
//
//                        BoundingBox box = new BoundingBox(1157, 42, 933, 1148, 51, 929);
//                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;
//
//                        // playDialogue(DepartureDialogue.ALLEYS_OPENING);
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void alleysPurpleCar(PlayerMoveEvent e) {
//                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;
//
//                        BoundingBox box = new BoundingBox(1147, 43, 935, 1142, 47, 942);
//                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;
//
//                        // DepartureDialogue.PURPLE_CAR.getSolo().displayDialogue(plugin, List.of(e.getPlayer()));
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void alleysSafeHouse(PlayerMoveEvent e) {
//                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;
//
//                        BoundingBox box = new BoundingBox(1147, 42, 983, 1141, 56, 990);
//                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;
//
//                        // playDialogue(DepartureDialogue.ALLEYS_SAFE_HOUSE);
//                        HandlerList.unregisterAll(this);
//                    }
//                },
//                new Listener() {
//                    @EventHandler
//                    public void finaleBegin(PlayerInteractEvent e) {
//                        if (!localGameManager.getPlayers().contains(e.getPlayer())) return;
//                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
//                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON)) return;
//
//                        if (e.getClickedBlock().getLocation().toVector().equals(new Vector(1126, 44, 997))) {
//                            Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
//                        }
//                    }
//                }
//        };
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
}
