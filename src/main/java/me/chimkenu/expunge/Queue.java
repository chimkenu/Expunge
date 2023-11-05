package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class Queue implements Listener {
    private final JavaPlugin plugin;
    private final Lobby lobby;
    private final int minPlayers;
    private final int maxPlayers;
    private final Campaign campaign;
    private final Difficulty difficulty;
    private final Set<Player> queue;
    private final BukkitTask runnable;

    public Queue(JavaPlugin plugin, Lobby lobby, int minPlayers, int maxPlayers, int countdown, Campaign campaign, Difficulty difficulty) {
        this.plugin = plugin;
        this.lobby = lobby;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.campaign = campaign;
        this.difficulty = difficulty;
        queue = new HashSet<>();

        runnable = new BukkitRunnable() {
            int timeLeft = countdown * 20; // seconds to ticks
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    stop(false);
                }

                StringBuilder message = new StringBuilder();
                message.append("§7In queue: ");
                for (Player player : queue) {
                    message.append("§c").append(player.getDisplayName()).append("§7, ");
                }
                for (Player player : queue) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message.substring(0, message.length() - 2) + " (" + timeLeft / 20 + ")"));
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 1, 1);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void join(Player player) {
        if (queue.size() >= maxPlayers) return;
        queue.add(player);
    }

    public void quit(Player player) {
        if (queue.contains(player) && queue.size() - 1 <= 0) stop(true);
        queue.remove(player);
    }

    public void stop(boolean isAbrupt) {
        HandlerList.unregisterAll(this);
        runnable.cancel();

        if (isAbrupt) {
            for (Player player : queue) {
                player.sendMessage(ChatColor.RED + "Queue cancelled.");
            }
            return;
        }

        if (queue.size() < minPlayers) {
            for (Player player : queue) {
                player.sendMessage(ChatColor.RED + "Unable to meet player requirements, please start a new queue.");
            }
            return;
        }

        lobby.createGame(plugin, campaign, difficulty, queue);
    }

    public boolean isCountdownRunning() {
        return !runnable.isCancelled();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }
}
