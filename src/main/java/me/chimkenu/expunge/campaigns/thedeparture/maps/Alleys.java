package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.listeners.game.*;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public class Alleys extends CampaignMap {
    @Override
    public String directory() {
        return "Alleys";
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
                new Vector(1155.5, 43, 914.5),
                new Vector(1150.5, 43, 929),
                new Vector(1167.5, 43, 937),
                new Vector(1176.5, 43, 971),
                new Vector(1129.5, 43, 950.5),
                new Vector(1155.5, 44, 965.5),
                new Vector(1166.5, 43, 961.5),
                new Vector(1150.5, 43, 958.5),
                new Vector(1147.5, 43, 951.5),
                new Vector(1139.5, 44, 974.5),
                new Vector(1149.5, 43, 990.5),
                new Vector(1130.5, 43, 972.5),
                new Vector(1126.5, 43, 978.5)
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
                new Vector(1148, 44, 980.5),
                new Vector(1166, 44, 965.5),
                new Vector(1180.5, 44, 965.5),
                new Vector(1175.5, 44, 949),
                new Vector(1167, 44, 934.5),
                new Vector(1158, 44, 948.5),
                new Vector(1154.5, 44, 944.5),
                new Vector(1139.5, 44, 955),
        };
    }

    @Override
    public int baseItemsToSpawn() {
        return 0;
    }

    @Override
    public Vector[] weaponLocations() {
        return new Vector[]{
                new Vector(1163.5, 45, 922.5),
                new Vector(1140, 44, 950.7),
                new Vector(1133.5, 43, 943.5),
                new Vector(1137.5, 44, 993.5)
        };
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
    public boolean isStartSafeRoom() {
        return false;
    }

    @Override
    public Listener[] happenings(LocalGameManager localGameManager) {
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
                new ShootListener(plugin, localGameManager)
        };
    }
}
