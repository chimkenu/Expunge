package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.LocalGameManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;

public class Lobby implements Listener {
    private final JavaPlugin plugin;
    private final World world;
    private final Location lobbySpawn;
    private final Set<GameManager> games = new HashSet<>();
    private final Set<Queue> queues = new HashSet<>();

    public Lobby(JavaPlugin plugin, World world, Location lobbySpawn) {
        this.plugin = plugin;
        this.world = world;
        this.lobbySpawn = lobbySpawn;
    }

    public void teleportToLobby(Player player) {
        player.teleport(lobbySpawn);
    }

    public void setSpectator(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() != GameMode.SURVIVAL)
                p.hidePlayer(plugin, player);
            else
                p.showPlayer(plugin, player);
            player.showPlayer(plugin, p);
        }
        player.setPlayerListName(ChatColor.GRAY + player.getDisplayName());
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20d);
        player.setAbsorptionAmount(0d);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0, false, false, false));
    }

    public World getWorld() {
        return world;
    }

    public Queue createQueue(int countdown, Campaign campaign, Difficulty difficulty) {
        final int MIN_PLAYERS = 1;
        final int MAX_PLAYERS = 4;

        Queue queue = new Queue(plugin, this, MIN_PLAYERS, MAX_PLAYERS, countdown, campaign, difficulty);
        queues.add(queue);
        return queue;
    }

    public Set<Queue> getQueues() {
        queues.removeIf(queue -> !queue.isCountdownRunning());
        return queues;
    }

    public void createGame(JavaPlugin plugin, Campaign campaign, Difficulty difficulty, Set<Player> queue) {
        games.add(new LocalGameManager((Expunge) plugin, campaign, difficulty, 0, queue));
    }

    public Set<GameManager> getGames() {
        games.removeIf(game -> !game.isRunning());
        return games;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        setSpectator(player);
        teleportToLobby(player);
    }
}