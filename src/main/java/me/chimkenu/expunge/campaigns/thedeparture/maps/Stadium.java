package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Barrier;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.campaigns.thedeparture.extras.StadiumFinale;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public class Stadium implements CampaignMap {
    @Override
    public String name() {
        return "Stadium";
    }

    @Override
    public String displayName() {
        return ChatUtil.getColor(66, 154, 255) + name();
    }

    @Override
    public Vector startLocation() {
        return new Vector(-103, 36, -148);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(52, 37, 132, 48, 42, 136);
    }

    @Override
    public List<Vector> escapePath() {
        return List.of();
    }

    @Override
    public List<Vector> spawnLocations() {
        return List.of(
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
                new Vector(7.442, 36.0, 104.414));
    }

    @Override
    public List<Vector> bossLocations() {
        return List.of(
                new Vector(50.5, 45, 87.5)
        );
    }

    @Override
    public List<Vector> rescueClosetLocations() {
        return List.of();
    }

    @Override
    public List<Barrier> barrierLocations() {
        return List.of();
    }

    @Override
    public List<ItemRandomizer> startItems() {
        return List.of();
    }

    @Override
    public List<ItemRandomizer> mapItems() {
        return List.of(
                new ItemRandomizer(-104.5, 37, -146, 1, 4, List.of("MEDKIT")),
                new ItemRandomizer(61.5, 37, 154.5, 1, 4, List.of("MEDKIT")),

                new ItemRandomizer(63.5, 37, 64.5, 0.5, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(50.5, 46, 180.5, 0.5, 2, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(-101.5, 37, -147, 1, 1, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(-101.5, 37, -145, 1, 1, ItemRandomizer.Preset.MELEE),
                new ItemRandomizer(-97.5, 36, -151.5, 1, 1, List.of("PILLS")),
                new ItemRandomizer(-97.5, 36, -144.5, 1, 1, List.of("ADRENALINE")),

                new ItemRandomizer(7.5, 37.0, 38.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(35.5, 37.0, 69.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(65.5, 37.0, 66.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(48.5, 36.0, 94.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(58.5, 37.0, 114.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(39.5, 37.0, 154.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(42.5, 37.0, 114.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(39.5, 37.0, 114.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(91.5, 37.0, 168.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(97.5, 36.0, 131.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(97.5, 37.0, 141.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(95.5, 37.0, 100.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(8.5, 37.0, 100.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(4.5, 37.0, 132.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(-15.5, 37.0, 135.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(9.5, 37.0, 168.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(50.5, 46.0, 180.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY));
    }

    @Override
    public List<Vector> ammoLocations() {
        return List.of(
                new Vector(42.5, 37, 154.5),
                new Vector(-100.5, 37, -151.5)
        );
    }

    @Override
    public GameAction runAtStart() {
        return (gameManager, player) -> {
            Dialogue.display(gameManager.getPlugin(), gameManager.getPlayers(), DepartureDialogue.STADIUM_OPENING.pickRandom(gameManager.getPlayers().size()));
            gameManager.getWorld().getBlockAt(32, 17, 123).setType(Material.REDSTONE_BLOCK);
        };
    }

    @Override
    public GameAction runAtEnd() {
        return null;
    }

    @Override
    public List<Listener> happenings(Expunge plugin, GameManager gameManager) {
        return List.of(
                new Listener() {
                    @EventHandler
                    public void stadiumParkingLotB(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-32, 34, -17, 7, 52, -47);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        gameManager.getDirector().bile(e.getPlayer(), 5);
                        // TODO: gameManager.getDirector().spawnAtRandomLocations(new BoundingBox(1179, 35, 1466, 1239, 35, 1569), 20 + (gameManager.getDifficulty().ordinal() * 10));
                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STADIUM_PARKING_LOT.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void stadiumEnter(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(45, 35, 75, 55, 39, 82);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STADIUM_ENTER.pickRandom(gameManager.getPlayers().size()));
                        gameManager.getDirector().bile(e.getPlayer(), 5);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void achievement(PlayerInteractEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;

                        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
                        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.STONE_BUTTON))
                            return;

                        Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
                        if (clickedLoc.equals(new Vector(49, 39, 130)) || clickedLoc.equals(new Vector(49, 39, 138))) {
                            Achievements.HEY_DONT_TOUCH_THAT.grant(e.getPlayer());
                        }
                    }
                },
                new StadiumFinale(gameManager)
        );
    }

}
