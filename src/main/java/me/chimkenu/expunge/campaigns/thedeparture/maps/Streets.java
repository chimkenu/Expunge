package me.chimkenu.expunge.campaigns.thedeparture.maps;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.GameAction;
import me.chimkenu.expunge.campaigns.Barrier;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.game.campaign.CampaignDirector;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public class Streets implements CampaignMap {
    @Override
    public String name() {
        return "Streets";
    }

    @Override
    public String displayName() {
        return ChatUtil.getColor(120, 111, 86) + name();
    }

    @Override
    public Vector startLocation() {
        return new Vector(-180.5, 43, -289.5);
    }

    @Override
    public BoundingBox endRegion() {
        return new BoundingBox(62, 42, -221, 72, 53, -198);
    }

    @Override
    public List<Vector> escapePath() {
        return List.of();
    }

    @Override
    public List<Vector> spawnLocations() {
        return List.of(
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),

                // Alleys
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector(),
                new Vector()
        );
    }

    @Override
    public List<Vector> bossLocations() {
        return List.of(
                new Vector(),
                new Vector()
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
                new ItemRandomizer(-178.5, 44, -289.5, 1, 4, List.of("MEDKIT")),
                new ItemRandomizer(-182, 44, -292.5, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(-180, 44, -292.5, 1, 1, true, ItemRandomizer.Preset.TIER2_GUNS),
                new ItemRandomizer(-178.5, 44, -291.5, 1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(31, 44, -101, 1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(31, 44, -91, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY),
                new ItemRandomizer(31, 44, -98, 1, 1, ItemRandomizer.Preset.MELEE),
                new ItemRandomizer(31, 44, -89, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(31.5, 51, -85.5, 0.5, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(27.5, 43, -110.5, 0.7, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                new ItemRandomizer(16.5, 50, -102.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(32.5, 51, -98.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(18.5, 50, -93.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(32.5, 56, -93.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(13.5, 56, -96.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(28.5, 56, -104.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(16.5, 57, -107.5, 0.25, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Alleys
                new ItemRandomizer(27.5, 44.0, -26.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(31.5, 44.0, -14.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(19.5, 42.5, -33.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(43.5, 44.0, -27.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(35.5, 44.0, -52.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(52.5, 44.0, -56.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(65.5, 44.0, -86.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),
                new ItemRandomizer(40.5, 44.0, -82.5, 0.1, 1, ItemRandomizer.Preset.TIER1_UTILITY),

                // Shed
                new ItemRandomizer(34, 44.0, -93.5, 1, 1, ItemRandomizer.Preset.TIER1_GUNS),
                new ItemRandomizer(34, 44.0, -90.5, 1, 1, ItemRandomizer.Preset.MELEE),
                new ItemRandomizer(36.5, 44.0, -93, 1, 1, ItemRandomizer.Preset.TIER2_UTILITY));
    }

    @Override
    public List<Vector> ammoLocations() {
        return List.of(
                new Vector(-178.5, 44, -286.5),
                new Vector(84.5, 44, -282.5),
                new Vector(116.5, 51, -225.5)
        );
    }

    @Override
    public GameAction runAtStart() {
        return (gameManager, player) -> Dialogue.display(gameManager.getPlugin(), gameManager.getPlayers(), DepartureDialogue.OFFICE_RADIO.pickRandom(gameManager.getPlayers().size()));
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
                    public void streetsOpeningTrigger(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-243, 41, -105, -254, 52, -115);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_OPENING.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsApartmentsTrigger(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(-10, 40, -105, -32, 52, -157);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_APARTMENTS.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void crescendoEventApartments(PlayerInteractEvent e) {
                        Block block = e.getClickedBlock();
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        if (block == null || !(e.getAction().equals(Action.PHYSICAL) && (block.getLocation().toVector().equals(new Vector(16, 43, -110)) || block.getLocation().toVector().equals(new Vector(16, 43, -109))))) {
                            return;
                        }

                        CampaignDirector.playCrescendoEventEffect(gameManager.getPlayers());
                        spawnHordeAtApartments(gameManager);
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void streetsShedTrigger(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;
                        BoundingBox box = new BoundingBox(33, 55, -112, 38, 61, -103);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.STREETS_SHED.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },

                // Alleys
                new Listener() {
                    @EventHandler
                    public void alleysOpening(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;

                        BoundingBox box = new BoundingBox(34, 41, -89, 43, 48, -84);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_OPENING.pickRandom(gameManager.getPlayers().size()));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysPurpleCar(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;

                        BoundingBox box = new BoundingBox(42, 41, -73, 32, 47, -66);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, List.of(e.getPlayer()), DepartureDialogue.PURPLE_CAR.pickRandom(1));
                        HandlerList.unregisterAll(this);
                    }
                },
                new Listener() {
                    @EventHandler
                    public void alleysSafeHouse(PlayerMoveEvent e) {
                        if (!gameManager.getPlayers().contains(e.getPlayer())) return;
                        if (!gameManager.getPlayerStat(e.getPlayer()).isAlive()) return;

                        BoundingBox box = new BoundingBox(54, 41, -19, 41, 53, -25);
                        if (!box.contains(e.getPlayer().getLocation().toVector())) return;

                        Dialogue.display(plugin, gameManager.getPlayers(), DepartureDialogue.ALLEYS_SAFE_HOUSE.pickRandom(gameManager.getPlayers().size()));
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

                        if (e.getClickedBlock().getLocation().toVector().equals(new Vector(22, 44, -11))) {
                            Achievements.A_BITE_TO_EAT.grant(e.getPlayer());
                        }
                    }
                }
        );
    }

    private void spawnHordeAtApartments(GameManager gameManager) {
        final Vector[] spawnLocations = new Vector[]{
                new Vector(21.5, 50, -108.5),
                new Vector(27.5, 50, -98.5),
                new Vector(21.5, 50, -97.5),
                new Vector(27.5, 50, -87.5),
                new Vector(27.5, 56, -87.5),
                new Vector(21.5, 56, -97.5),
                new Vector(27.5, 56, -98.5),
                new Vector(21.5, 56, -108.5)
        };

        new BukkitRunnable() {
            final World world = gameManager.getWorld();
            int t = 4;
            @Override
            public void run() {
                if (!gameManager.isRunning() || gameManager.getDirector().getPhase() == Director.Phase.DISABLED) {
                    this.cancel();
                    return;
                }
                if (t < 0) {
                    this.cancel();
                    return;
                }

                t--;
                // TODO: SPAWN HORDE
            }
        }.runTaskTimer(gameManager.getPlugin(), 1, 10);
    }
}
