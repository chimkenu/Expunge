package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

public class NextScene implements Listener {
    @EventHandler
    public void onPress(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null && e.getClickedBlock().getType().toString().contains("_BUTTON")) {
            if (!Expunge.isGameRunning) {
                return;
            }
            Location buttonLoc = Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex).buttonLocation();
            Location clickedLoc = e.getClickedBlock().getLocation();
            if (!buttonLoc.toVector().equals(clickedLoc.toVector())) {
                return;
            }
            if (!Expunge.playing.getKeys().contains(e.getPlayer())) {
                return;
            }
            if (!Expunge.isSpawningEnabled) {
                return;
            }

            Location loc = e.getPlayer().getLocation();
            BoundingBox endRegion = Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex).endRegion();
            if (endRegion.contains(loc.getX(), loc.getY(), loc.getZ())) {
                for (Player p : Expunge.playing.getKeys()) {
                    if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                        Location pLoc = p.getLocation();
                        if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                            // a player is still not in the end zone
                            e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cNot all alive players are in the safe-zone!"));
                            return;
                        }
                    }
                }
            }

            // this is reached when all alive players reach the end region
            Bukkit.broadcastMessage(ChatColor.GREEN + "Safe-zone reached!");
            Expunge.endScene(Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex));

            // check if it is the last scene then end the game
            if (Expunge.currentMap.getMaps().size() - 1 <= Expunge.currentSceneIndex) {
                Bukkit.broadcastMessage(ChatColor.GREEN + "END OF GAME");

                // achievements
                boolean hasMrCookie = false;
                for (Player p : Expunge.playing.getKeys()) {
                    Achievements.SURVIVOR.grant(p);

                    // the departure achievements
                    if (Expunge.currentMap.getName().equals("The Departure")) {
                        Achievements.THE_DEPARTURE.grant(p);
                        for (int i = 0; i < 5; i++) {
                            ItemStack item = p.getInventory().getItem(i);
                            if (item != null && item.getType() == Material.COOKIE) hasMrCookie = true;
                        }
                    }
                }

                if (hasMrCookie) {
                    for (Player p : Expunge.playing.getKeys()) {
                        Achievements.COOKIE_MONSTER.grant(p);
                    }
                }

                Expunge.stopGame();
                return;
            }

            // increment scene index then start
            Expunge.updateSceneIndex();
            Expunge.startScene(Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex));
        }
    }
}