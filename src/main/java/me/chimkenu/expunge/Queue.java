package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.CampaignDifficulty;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
    private Campaign campaign;
    private CampaignDifficulty difficulty;
    private final Player creator;
    private final Set<Player> queue;
    private boolean isDone;

    // NOTE: NO LONGER USED AS OF v3, opting to use only a single game instance per server
    public Queue(JavaPlugin plugin, Lobby lobby, int minPlayers, int maxPlayers, Campaign campaign, CampaignDifficulty difficulty, Player creator) {
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
        if ((queue.contains(player) && queue.size() == 1) || player == creator) {
            stop(true);
        }
        return queue.remove(player);
    }

    public void stop(boolean isAbrupt) {
        isDone = true;
        HandlerList.unregisterAll(this);

        if (isAbrupt) {
            for (Player player : queue) {
                player.spigot().sendMessage(new ComponentBuilder("Queue cancelled.").color(ChatColor.RED).create());
            }
            return;
        }

        if (queue.size() < minPlayers) {
            for (Player player : queue) {
                player.spigot().sendMessage(new ComponentBuilder("Unable to meet player requirements, please start a new queue.").color(ChatColor.RED).create());
            }
            return;
        }

        // lobby.createGame(plugin, campaign.get(), difficulty, queue);
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public CampaignDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(CampaignDifficulty difficulty) {
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
