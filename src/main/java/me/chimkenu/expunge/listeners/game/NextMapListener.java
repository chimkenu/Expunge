package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

public class NextMapListener extends GameListener {
    public NextMapListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    @EventHandler
    public void onPress(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
            if (!localGameManager.isRunning()) {
                return;
            }
            Location buttonLoc = localGameManager.getMap().buttonLocation().toLocation(localGameManager.getWorld());
            Location clickedLoc = e.getClickedBlock().getLocation();
            if (!buttonLoc.equals(clickedLoc)) {
                return;
            }
            if (!localGameManager.getPlayers().contains(e.getPlayer())) {
                return;
            }
            if (!localGameManager.getDirector().getMobHandler().isSpawningEnabled()) {
                return;
            }

            BoundingBox endRegion = localGameManager.getMap().endRegion();
            for (Player p : localGameManager.getPlayers()) {
                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                    Location pLoc = p.getLocation();
                    if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                        // a player is still not in the end zone
                        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cNot all alive players are in the safe-zone!"));
                        return;
                    }
                }
            }

            // this is reached when all alive players reach the end region
            Bukkit.broadcastMessage(ChatColor.GREEN + "Safe-zone reached!");
            localGameManager.endMap();

            // check if it is the last scene then end the game
            if (localGameManager.getCampaign().getMaps().length - 1 <= localGameManager.getCampaignMapIndex()) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "END OF GAME");

                // achievements
                boolean hasMrCookie = false;
                for (Player p : localGameManager.getPlayers()) {
                    Achievements.SURVIVOR.grant(p);

                    // the departure achievements
                    if (localGameManager.getCampaign().getName().equals("The Departure")) {
                        Achievements.THE_DEPARTURE.grant(p);
                        for (int i = 0; i < 5; i++) {
                            ItemStack item = p.getInventory().getItem(i);
                            if (item != null && item.getType() == Material.COOKIE) hasMrCookie = true;
                        }
                    }
                }

                if (hasMrCookie) {
                    for (Player p : localGameManager.getPlayers()) {
                        Achievements.COOKIE_MONSTER.grant(p);
                    }
                }

                localGameManager.stop(false);
                return;
            }

            for (Player p : localGameManager.getPlayers()) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10 * 20, true, false));
                p.sendMessage(localGameManager.getDirector().displayStats());
            }

            // increment scene index then start
            localGameManager.loadNextMap();
            new BukkitRunnable() {
                int i = 5 * 20;
                @Override
                public void run() {
                    if (!localGameManager.isRunning() || localGameManager.getPlayers().size() == 0) {
                        this.cancel();
                    }
                    for (Player p : localGameManager.getPlayers()) {
                        p.sendActionBar(Component.text("Sending you to the next map..."));
                    }
                    if (i <= 0) {
                        localGameManager.moveToNextMap();
                        this.cancel();
                    }
                    i--;
                }
            }.runTaskTimer(plugin, 1, 1);
        }
    }
}
