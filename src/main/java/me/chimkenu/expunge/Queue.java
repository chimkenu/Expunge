package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Queue implements Listener {
    private final JavaPlugin plugin;
    private final Lobby lobby;
    private final int minPlayers;
    private final int maxPlayers;
    private final Campaign campaign;
    private final Difficulty difficulty;
    private final Player creator;
    private final Set<Player> queue;
    private final BukkitTask runnable;

    public Queue(JavaPlugin plugin, Lobby lobby, int minPlayers, int maxPlayers, int countdown, Campaign campaign, Difficulty difficulty, Player creator) {
        this.plugin = plugin;
        this.lobby = lobby;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.campaign = campaign;
        this.difficulty = difficulty;
        this.creator = creator;
        queue = new HashSet<>();
        queue.add(creator);

        runnable = new BukkitRunnable() {
            int timeLeft = countdown * 20; // seconds to ticks
            @Override
            public void run() {
                if (timeLeft <= 0) {
                    stop(false);
                }

                Component message = Component.text("In queue: ", NamedTextColor.GRAY);
                Iterator<Player> iterator = queue.iterator();
                if (iterator.hasNext()) {
                    message = message.append(iterator.next().displayName().color(NamedTextColor.RED));
                }
                while (iterator.hasNext()) {
                    message = message.append(iterator.next().displayName().color(NamedTextColor.RED)).append(Component.text(", ", NamedTextColor.GRAY));
                }
                for (Player player : queue) {
                    player.sendActionBar(message.append(Component.text("(" + timeLeft / 20 + ")", NamedTextColor.GRAY)));
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 1, 1);

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public boolean join(Player player) {
        if (queue.size() >= maxPlayers) return false;
        return queue.add(player);
    }

    public boolean quit(Player player) {
        if (queue.contains(player) && queue.size() - 1 <= 0) stop(true);
        if (player == creator) stop(true);
        return queue.remove(player);
    }

    public void stop(boolean isAbrupt) {
        HandlerList.unregisterAll(this);
        runnable.cancel();

        if (isAbrupt) {
            for (Player player : queue) {
                player.sendMessage(Component.text("Queue cancelled.", NamedTextColor.RED));
            }
            return;
        }

        if (queue.size() < minPlayers) {
            for (Player player : queue) {
                player.sendMessage(Component.text("Unable to meet player requirements, please start a new queue.", NamedTextColor.RED));
            }
            return;
        }

        lobby.createGame(plugin, campaign, difficulty, queue);
    }

    public boolean isCancelled() {
        return runnable.isCancelled();
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Player getCreator() {
        return creator;
    }

    public Set<Player> getQueue() {
        return queue;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }
}
