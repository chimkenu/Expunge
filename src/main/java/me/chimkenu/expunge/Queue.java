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

import java.util.HashSet;
import java.util.Set;

public class Queue implements Listener {
    private final JavaPlugin plugin;
    private final Lobby lobby;
    private final int minPlayers;
    private final int maxPlayers;
    private Campaign.List campaign;
    private Difficulty difficulty;
    private final Player creator;
    private final Set<Player> queue;
    private boolean isDone;

    public Queue(JavaPlugin plugin, Lobby lobby, int minPlayers, int maxPlayers, Campaign.List campaign, Difficulty difficulty, Player creator) {
        this.plugin = plugin;
        this.lobby = lobby;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.campaign = campaign;
        this.difficulty = difficulty;
        this.creator = creator;
        isDone = false;
        queue = new HashSet<>();
        queue.add(creator);

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
        isDone = true;
        HandlerList.unregisterAll(this);

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

        lobby.createGame(plugin, campaign.get(), difficulty, queue);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Campaign.List getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign.List campaign) {
        this.campaign = campaign;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Player getCreator() {
        return creator;
    }

    public Set<Player> getQueue() {
        return queue;
    }

    public boolean isDone() {
        return isDone;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        quit(event.getPlayer());
    }
}
