package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

import java.util.logging.Level;

public class NextMapListener extends GameListener {
    public NextMapListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onPress(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
            if (!gameManager.isRunning()) {
                return;
            }
            Location buttonLoc = gameManager.getMap().buttonLocation().toLocation(gameManager.getWorld());
            Location clickedLoc = e.getClickedBlock().getLocation();
            if (!buttonLoc.equals(clickedLoc)) {
                return;
            }
            if (!gameManager.getPlayers().contains(e.getPlayer())) {
                return;
            }
            if (e.getPlayer().getGameMode() != GameMode.ADVENTURE) {
                return;
            }

            BoundingBox endRegion = gameManager.getMap().endRegion();
            for (Player p : gameManager.getPlayers()) {
                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                    Location pLoc = p.getLocation();
                    if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                        // a player is still not in the end zone
                        e.getPlayer().sendActionBar(Component.text("Not all alive players are in the safe-zone!", NamedTextColor.RED));
                        return;
                    }
                }
            }

            // this is reached when all alive players reach the end region
            gameManager.getWorld().getPlayers().forEach(player -> player.sendMessage(Component.text("Safe-zone reached!", NamedTextColor.GREEN)));
            gameManager.endMap();

            // check if it is the last scene then end the game
            if (gameManager.getCampaign().getMaps().length - 1 <= gameManager.getCampaignMapIndex()) {
                gameManager.getWorld().getPlayers().forEach(player -> player.sendMessage(Component.text("END OF GAME", NamedTextColor.GREEN)));

                // achievements
                boolean hasMrCookie = false;
                for (Player p : gameManager.getPlayers()) {
                    Achievements.SURVIVOR.grant(p);

                    // the departure achievements
                    if (gameManager.getCampaign() instanceof TheDeparture) {
                        Achievements.THE_DEPARTURE.grant(p);
                        for (int i = 0; i < 5; i++) {
                            ItemStack item = p.getInventory().getItem(i);
                            if (item != null && item.getType() == Material.COOKIE) hasMrCookie = true;
                        }
                    }
                }

                if (hasMrCookie) {
                    for (Player p : gameManager.getPlayers()) {
                        Achievements.COOKIE_MONSTER.grant(p);
                    }
                }

                gameManager.stop(false);
                return;
            }

            for (Player p : gameManager.getPlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10 * 20, true, false));
                p.sendMessage(gameManager.getDirector().getStatsHandler().displayStats());
            }

            // increment scene index then start
            gameManager.loadNextMap();
            new BukkitRunnable() {
                int i = 5 * 20;
                @Override
                public void run() {
                    if (!gameManager.isRunning() || gameManager.getPlayers().isEmpty()) {
                        this.cancel();
                    }
                    for (Player p : gameManager.getPlayers()) {
                        p.sendActionBar(Component.text("Sending you to the next map..."));
                    }
                    if (i <= 0) {
                        try {
                            gameManager.moveToNextMap();
                        } catch (RuntimeException e) {
                            for (Player p : gameManager.getPlayers()) {
                                p.sendMessage(Component.text("Something went wrong! Sending you back..."));
                            }
                            Bukkit.getLogger().log(Level.SEVERE, e.toString());
                            gameManager.stop(true);
                        }
                        this.cancel();
                    }
                    i--;
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }
}
