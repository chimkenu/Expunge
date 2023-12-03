package me.chimkenu.expunge.campaigns.thedeparture.extras;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.director.MobHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StadiumFinale implements Listener {
    private final GameManager gameManager;
    private final MobHandler mobHandler;

    public StadiumFinale(GameManager gameManager) {
        this.gameManager = gameManager;
        mobHandler = gameManager.getDirector().getMobHandler();
    }

    @EventHandler
    public void finaleBegin(PlayerInteractEvent e) {
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (e.getClickedBlock() == null || !(e.getClickedBlock().getType() == Material.LEVER)) {
            return;
        }

        Vector clickedLoc = e.getClickedBlock().getLocation().toVector();
        if (!(clickedLoc.equals(new Vector(49, 46, 180)) || clickedLoc.equals(new Vector(51, 46, 180)))) {
            return;
        }

        Campaign.playCrescendoEventEffect(gameManager.getPlayers());
        mobHandler.setGoHam(true);

        // spawn in tank after specific number of kills
        gameManager.addListener(afterHordeRequirement());

        // after tank dies
        gameManager.addListener(new Listener() {
            @EventHandler
            public void afterTank(EntityDeathEvent e) {
                if (e.getEntity().getWorld().equals(gameManager.getWorld()) && !e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                    return;
                }

                mobHandler.setSpawningEnabled(false);
                final int attempts = gameManager.getDirector().getSceneAttempts();

                // 30-second timer
                new BukkitRunnable() {
                    int i = 30;
                    @Override
                    public void run() {
                        if (i <= 0) {
                            // do the same thing
                            mobHandler.setSpawningEnabled(true);
                            mobHandler.spawnHorde(gameManager.getDifficulty());
                            Campaign.playCrescendoEventEffect(gameManager.getPlayers());

                            gameManager.addListener(afterHordeRequirement());

                            gameManager.getPlugin().getServer().getPluginManager().registerEvents(new Listener() {
                                @EventHandler
                                public void afterTank(EntityDeathEvent e) {
                                    if (e.getEntity().getWorld().equals(gameManager.getWorld()) && e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                                        Dialogue.display(gameManager.getPlugin(), gameManager.getPlayers(), DepartureDialogue.STADIUM_ENDING.pickRandom(gameManager.getPlayers().size()));
                                        gameManager.getWorld().getBlockAt(32, 17, 122).setType(Material.REDSTONE_BLOCK);
                                        HandlerList.unregisterAll(this);
                                    }
                                }
                            }, gameManager.getPlugin());

                            this.cancel();
                            return;
                        }

                        // timer
                        if (!gameManager.isRunning() || gameManager.getDirector().getSceneAttempts() != attempts) this.cancel();
                        gameManager.getPlayers().forEach(player -> player.sendActionBar(Component.text(i, NamedTextColor.GRAY)));
                        i--;
                    }
                }.runTaskTimer(gameManager.getPlugin(), 0, 20);
                HandlerList.unregisterAll(this);
            }
        });

        HandlerList.unregisterAll(this);
    }

    private Listener afterHordeRequirement() {
        int requirement = gameManager.getDirector().getStatsHandler().getTotalKills() + 30 + 25 * gameManager.getDifficulty().ordinal();
        return new Listener() {
            @EventHandler
            public void afterHorde(EntityDeathEvent e) {
                if (gameManager.getDirector().getStatsHandler().getTotalKills() >= requirement) {
                    mobHandler.spawnTank();
                    HandlerList.unregisterAll(this);
                }
            }
        };
    }
}
