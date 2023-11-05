package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignIntro;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.director.Director;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.*;

public class LocalGameManager implements GameManager {
    private final Expunge plugin;
    private final Campaign campaign;
    private final Difficulty difficulty;
    private final Queue<GameWorld> gameWorlds;
    private final HashMap<Player, PlayerStats> players;
    private final Director director;
    private final List<Listener> listeners;

    private BukkitTask main;
    private long gameTimeStart;
    private int campaignMapIndex;

    public LocalGameManager(Expunge plugin, Campaign campaign, Difficulty difficulty, int campaignMapIndex, Set<Player> players) throws RuntimeException {
        this.plugin = plugin;
        this.campaign = campaign;
        this.difficulty = difficulty;

        // Load the first map in the campaign
        gameWorlds = new LinkedList<>();
        this.campaignMapIndex = Math.min(campaignMapIndex, campaign.getMaps().length);
        if (!loadMap(this.campaignMapIndex)) {
            throw new RuntimeException("Campaign map index " + this.campaignMapIndex + " could not be loaded.");
        }

        // Set player defaults
        this.players = new HashMap<>();
        for (Player player : players) {
            player.getInventory().clear();
            this.players.put(player, new PlayerStats());
        }

        director = new Director(plugin, this);

        // Register listeners
        listeners = List.of(campaign.getMaps()[this.campaignMapIndex].happenings(this));
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }

        gameTimeStart = -1;

        for (Player player : players) {
            player.setGameMode(GameMode.ADVENTURE);
        }

        if (campaign.getMaps()[this.campaignMapIndex] instanceof CampaignIntro intro) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (players.size() < 1) this.cancel();
                    for (Player player : players) {
                        player.setGameMode(GameMode.ADVENTURE);
                    }
                    start();
                }
            }.runTaskLater(plugin, intro.play());
        } else start();
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }

        gameTimeStart = System.currentTimeMillis();
        main = new BukkitRunnable() {
            @Override
            public void run() {
                director.run();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) main.cancel();
        main = null;

        for (Listener listener : listeners) {
            HandlerList.unregisterAll(listener);
        }

        GameWorld gameWorld = gameWorlds.peek();
        if (gameWorld == null) return;

        long totalGameTime = System.currentTimeMillis() - gameTimeStart;
        int milliseconds = (int) (totalGameTime % 1000);
        int seconds = (int) (totalGameTime / 1000) % 60;
        int minutes = (int) (totalGameTime / (1000 * 60)) % 60;
        int hours = (int) (totalGameTime / (1000 * 60 * 60)) % 60;
        String gameTime = String.format("%01d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds);

        for (Player player : gameWorld.getWorld().getPlayers()) {
            player.sendMessage(ChatColor.BLUE + "Game ended at " + ChatColor.DARK_AQUA + gameTime);
            plugin.getLobby().setSpectator(player);
            if (isAbrupt) plugin.getLobby().teleportToLobby(player);
        }

        if (isAbrupt) {
            if (!gameWorlds.isEmpty()) gameWorlds.remove().unload();
            gameWorlds.clear();
            return;
        }

        int time = 10 * 20;
        new BukkitRunnable() {
            int i = time;
            @Override
            public void run() {
                for (Player player : gameWorld.getWorld().getPlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7Returning to lobby in §c" + (i / 20) + "§3..."));
                }

                if (gameWorld.getWorld().getPlayers().size() < 1) {
                    this.cancel();
                }

                if (i <= 0) {
                    for (Player player : gameWorld.getWorld().getPlayers()) {
                        plugin.getLobby().teleportToLobby(player);
                    }
                    this.cancel();
                }

                i--;
            }
        }.runTaskTimer(plugin, 0, 1);

        new BukkitRunnable() {
            @Override
            public void run() {
                gameWorld.unload();
            }
        }.runTaskLater(plugin, time + 1);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    public void restartMap() {
        director.setSpawningEnabled(false);
        director.clearEntities();
        director.incrementSceneAttempts();
        startMap();
    }

    public void startMap() {
        director.setSpawningEnabled(true);

        for (Player p : getPlayers()) {
            p.leaveVehicle();
            p.teleport(getMap().startLocation().toLocation(getWorld()));
            /*
            TODO: Apparently, the old code had a bug where players that were riding a vehicle didn't get teleported back to the start location. Please test this!
             * new BukkitRunnable() {
             *     @Override
             *     public void run() {
             *         p.teleport(scene.startLocation());
             *     }
             * }.runTaskLater(instance, 1);
             */

            p.removePotionEffect(PotionEffectType.GLOWING);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
            p.setGameMode(GameMode.ADVENTURE);

            loadPlayerStats(p);
            getPlayerStat(p).setAlive(true);
        }

        // REGISTER EVENTS AND HAPPENINGS

        // DIRECTOR.GENERATE_ITEMS
        // DIRECTOR.SPAWN_STARTING_MOBS

        // RUN GAME_ACTION_AT_START
    }

    public void endMap() {
        director.setSpawningEnabled(false);

        // RUN GAME_ACTION_AT_END

        // UNREGISTER LISTENERS AND HAPPENINGS

        director.resetSceneAttempts();
        director.clearEntities();

        for (Player p : getPlayers()) {
            if (!getPlayerStat(p).isAlive()) players.remove(p);
            savePlayerStats(p);
        }
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public CampaignMap getMap() {
        return campaign.getMaps()[campaignMapIndex];
    }

    public World getWorld() {
        if (gameWorlds.isEmpty()) return null;
        return gameWorlds.peek().getWorld();
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public Set<Player> getPlayers() {
        return players.keySet();
    }

    public PlayerStats getPlayerStat(Player player) {
        return players.get(player);
    }

    public Director getDirector() {
        return director;
    }

    private boolean loadMap(int index) {
        LocalGameWorld localGameWorld = new LocalGameWorld(new File(plugin.getDataFolder(), campaign.getMainDirectory() + campaign.getMaps()[index]));
        gameWorlds.add(localGameWorld);
        return localGameWorld.load();
    }

    private void savePlayerStats(Player p) {
        players.putIfAbsent(p, new PlayerStats());
        PlayerStats s = players.get(p);
        s.setHealth(p.getHealth());
        s.setAbsorption(p.getAbsorptionAmount());

        ItemStack[] itemStacks = s.getHotbar();
        for (int i = 0; i < 5; i++) {
            itemStacks[i] = p.getInventory().getItem(i);
        }
        s.setHotbar(itemStacks);

        // s.setLives();
    }

    private void loadPlayerStats(Player p) {
        PlayerStats s = players.get(p);
        p.setHealth(s.getHealth());
        p.setAbsorptionAmount(s.getAbsorption());
        p.getInventory().clear();
        for (int i = 0; i < s.getHotbar().length; i++) {
            p.getInventory().setItem(i, s.getHotbar()[i]);
        }
    }
}
