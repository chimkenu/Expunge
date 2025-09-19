package me.chimkenu.expunge.campaigns.thedeparture.extras;

import me.chimkenu.expunge.campaigns.Dialogue;
import me.chimkenu.expunge.campaigns.thedeparture.DepartureDialogue;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.campaign.CampaignDirector;
import me.chimkenu.expunge.game.campaign.CampaignGameState;
import me.chimkenu.expunge.mobs.MobType;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class StadiumFinale implements Listener {
    private final GameManager gameManager;
    private final CampaignDirector director;

    public StadiumFinale(GameManager gameManager) {
        this.gameManager = gameManager;
        director = (CampaignDirector) gameManager.getDirector();
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

        CampaignDirector.playCrescendoEventEffect(gameManager.getPlayers());
        director.setPhase(Director.Phase.DISABLED);

        // spawn in tank after specific number of kills
        gameManager.addListener(afterHordeRequirement());

        // after tank dies
        gameManager.addListener(new Listener() {
            @EventHandler
            public void afterTank(EntityDeathEvent e) {
                if (e.getEntity().getWorld().equals(gameManager.getWorld()) && !e.getEntityType().equals(EntityType.IRON_GOLEM)) {
                    return;
                }

                director.setPhase(Director.Phase.DISABLED);
                final int attempts = ((CampaignGameState) gameManager.getState()).getAttempts();

                // 30-second timer
                new BukkitRunnable() {
                    int i = 30;
                    @Override
                    public void run() {
                        if (i <= 0) {
                            // do the same thing
                            director.setPhase(Director.Phase.BUILD);
                            CampaignDirector.playCrescendoEventEffect(gameManager.getPlayers());

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
                        if (!gameManager.isRunning() || ((CampaignGameState) gameManager.getState()).getAttempts() != attempts) this.cancel();
                        gameManager.getPlayers().forEach(player -> ChatUtil.sendActionBar(player, "&7" + i));
                        i--;
                    }
                }.runTaskTimer(gameManager.getPlugin(), 0, 20);
                HandlerList.unregisterAll(this);
            }
        });

        HandlerList.unregisterAll(this);
    }

    private Listener afterHordeRequirement() {
        int requirement = 30 + 25 * ((CampaignGameState) gameManager.getState()).getDifficulty().ordinal();
        return new Listener() {
            int kills = 0;
            @EventHandler(priority = EventPriority.LOWEST)
            public void afterHorde(EntityDeathEvent e) {
                if (!(e.getEntity() instanceof Mob mob) || !gameManager.getEntities().contains(mob)) {
                    return;
                }

                String text = "&7" + kills + " / " + requirement;
                gameManager.getPlayers().forEach(player -> ChatUtil.sendActionBar(player, text));
                if (kills >= requirement) {
                    director.spawnMob(MobType.TANK, 1, mob.getLocation().toVector(), true);
                    HandlerList.unregisterAll(this);
                }
            }
        };
    }
}
