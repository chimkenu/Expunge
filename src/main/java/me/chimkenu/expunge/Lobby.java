package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Campaigns;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.FileUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Lobby {
    private final JavaPlugin plugin;
    private final World lobbyWorld;
    private final String gameWorldPrefix;

    private String currentGameWorld;
    private GameManager currentGame;

    public Lobby(JavaPlugin plugin, String lobbyWorldName, String gameWorldPrefix, Campaign initialCampaign) {
        this.plugin = Objects.requireNonNull(plugin);
        this.lobbyWorld = Objects.requireNonNull(Bukkit.getWorld(lobbyWorldName), "Lobby must be a valid (loaded) world");
        this.gameWorldPrefix = Objects.requireNonNull(gameWorldPrefix);
        this.currentGameWorld = gameWorldPrefix;

        // load initial map
        changeMap(initialCampaign);
    }

    public Lobby(JavaPlugin plugin, String lobbyWorldName, String gameWorldPrefix) {
        this(plugin, lobbyWorldName, gameWorldPrefix, Campaigns.THE_DEPARTURE);
    }

    public void changeMap(Campaign campaign) {
        plugin.getLogger().info("changing map...");
        String mapSourcePath = new File(plugin.getDataFolder(), campaign.directoryName()).getPath();
        String newGameWorldName = gameWorldPrefix + System.currentTimeMillis();
        File newGameWorldDir = new File(Bukkit.getWorldContainer(), newGameWorldName);
        File oldGameWorldDir = new File(Bukkit.getWorldContainer(), currentGameWorld);

        plugin.getLogger().info("copying from source...");
        // copy from source
        try {
            if (newGameWorldDir.exists()) {
                FileUtil.deleteDirectory(newGameWorldDir.toPath());
            }
            FileUtil.copyDirectory(mapSourcePath, newGameWorldDir.getPath());
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to copy source (" + mapSourcePath + ") to world folder " + newGameWorldName);
            e.printStackTrace();
            return;
        }

        plugin.getLogger().info("loading new map...");
        // load new world
        World newWorld = Bukkit.createWorld(new WorldCreator(newGameWorldName));
        if (newWorld == null) {
            plugin.getLogger().severe("failed to load new world");
            return;
        }

        // unload and delete old world
        World oldWorld = getGameWorld();
        if (oldWorld != null) {
            plugin.getLogger().info("unloading old world...");
            oldWorld.getPlayers().forEach(this::teleportToLobby);
            Bukkit.unloadWorld(oldWorld, false);
        }
        if (oldGameWorldDir.exists()) {
            plugin.getLogger().info("deleting old world...");
            try {
                FileUtil.deleteDirectory(oldGameWorldDir.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("failed to delete old world...");
                e.printStackTrace();
                return;
            }
        }

        Bukkit.broadcastMessage(ChatUtil.format("&aMap changed to " + mapSourcePath + "!"));
        currentGameWorld = newGameWorldName;
    }

    public void startNewGame(GameManager manager) {
        if (currentGame != null && currentGame.isRunning()) {
            currentGame.stop(true);
        }
        currentGame = manager;
    }

    public void stopGame() {
        if (currentGame != null) {
            currentGame.stop(true);
        }
    }

    public void unload() {
        if (currentGame != null && currentGame.isRunning()) {
            currentGame.stop(true);
            currentGame = null;
        }

        var world = getGameWorld();
        if (world != null) {
            world.getPlayers().forEach(this::teleportToLobby);
            Bukkit.unloadWorld(world, false);
        }

        var worldFolder = new File(Bukkit.getWorldContainer().getParentFile(), currentGameWorld);
        if (worldFolder.exists()) {
            try {
                FileUtil.deleteDirectory(worldFolder.toPath());
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to delete " + worldFolder);
                e.printStackTrace();
            }
        }
    }

    public void teleportToLobby(Player player) {
        player.teleport(lobbyWorld.getSpawnLocation());
    }

    public void setSpectator(Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() != GameMode.SURVIVAL)
                p.hidePlayer(plugin, player);
            else
                p.showPlayer(plugin, player);
            player.showPlayer(plugin, p);
        }
        player.setDisplayName(ChatColor.GRAY + player.getName());
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setHealth(20d);
        player.setAbsorptionAmount(0d);
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 0, false, false, false));
    }

    public World getLobbyWorld() {
        return lobbyWorld;
    }

    public World getGameWorld() {
        return Bukkit.getWorld(currentGameWorld);
    }

    public GameManager getCurrentGame() {
        return currentGame;
    }
}
