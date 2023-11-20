package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignIntro;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.listeners.CleanUp;
import me.chimkenu.expunge.listeners.game.BreakGlassListener;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
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
    private final UUID uuid;
    private final List<Listener> listeners;

    private BukkitTask main;
    private long gameTimeStart;
    private int campaignMapIndex;

    public LocalGameManager(Expunge plugin, Campaign campaign, Difficulty difficulty, int campaignMapIndex, Set<Player> players) throws RuntimeException {
        this.plugin = plugin;
        this.campaign = campaign;
        this.difficulty = difficulty;
        this.uuid = UUID.randomUUID();

        // Load the first map in the campaign
        gameWorlds = new LinkedList<>();
        this.campaignMapIndex = Math.min(Math.max(0, campaignMapIndex), campaign.getMaps().length);
        if (!loadMap(getMap())) {
            throw new RuntimeException("Campaign map index " + this.campaignMapIndex + " could not be loaded.");
        }

        // Set player defaults
        this.players = new HashMap<>();
        for (Player player : players) {
            player.getInventory().clear();
            this.players.put(player, new PlayerStats());
        }

        director = new Director(plugin, this);

        listeners = new ArrayList<>();

        gameTimeStart = -1;

        for (Player player : players) {
            player.setGameMode(GameMode.ADVENTURE);
        }

        if (campaign.getMaps()[this.campaignMapIndex] instanceof CampaignIntro intro) {
            startWithIntro();
            new BukkitRunnable() {
                @Override
                public void run() {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (players.isEmpty()) this.cancel();
                            start();
                        }
                    }.runTaskLater(plugin, intro.play(director.getGameManager()));
                }
            }.runTaskLater(plugin, 40); // Wait for players to load map

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
        startMap();
    }

    private void startWithIntro() {
        for (Player p : getPlayers()) {
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(getMap().startLocation().toLocation(getWorld()));
        }
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) main.cancel();
        main = null;

        director.clearEntities();

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
            if (isAbrupt) {
                plugin.getLobby().teleportToLobby(player);
                continue;
            }
            plugin.getLobby().setSpectator(player);
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

                if (gameWorld.getWorld().getPlayers().isEmpty()) {
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
        return main != null && !main.isCancelled();
    }

    @Override
    public void restartMap() {
        director.getMobHandler().setSpawningEnabled(false);
        director.clearEntities();
        director.incrementSceneAttempts();
        startMap();
    }

    @Override
    public void startMap() {
        for (Player p : getPlayers()) {
            p.leaveVehicle();
            p.teleport(getMap().startLocation().toLocation(getWorld()));
            /*
            TODO: Apparently, the old code had a bug where players that were riding a
             vehicle didn't get teleported back to the start location. Please test this!
             * new BukkitRunnable() {
             *     @Override
             *     public void run() {
             *         p.teleport(scene.startLocation());
             *     }
             * }.runTaskLater(instance, 1);
             */

            p.removePotionEffect(PotionEffectType.GLOWING);
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
            p.setGameMode(GameMode.ADVENTURE);

            loadPlayerStats(p);
            getPlayerStat(p).setAlive(true);
        }

        // REGISTER EVENTS AND HAPPENINGS
        if (!listeners.isEmpty()) {
            for (Listener listener : listeners) {
                if (listener instanceof CleanUp cleanUp) {
                    cleanUp.clean();
                }

                HandlerList.unregisterAll(listener);
            }
            listeners.clear();
        }

        listeners.addAll(List.of(getMap().gameListeners(plugin, this)));
        listeners.addAll(List.of(getMap().happenings(plugin, this)));
        listeners.add(director);

        for (Listener listener : listeners) {
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }

        director.getItemHandler().generateStartingItems();
        director.getMobHandler().spawnStartingMobs();

        if (getMap().runAtStart() != null) getMap().runAtStart().run(plugin, director.getGameManager(), null);
        director.getMobHandler().setSpawningEnabled(true);
    }

    @Override
    public void endMap() {
        director.getMobHandler().setSpawningEnabled(false);

        if (getMap().runAtEnd() != null) getMap().runAtEnd().run(plugin, director.getGameManager(), null);

        director.resetSceneAttempts();
        director.clearEntities();

        for (Player p : getPlayers()) {
            if (!getPlayerStat(p).isAlive()) players.remove(p);
            savePlayerStats(p);
        }
    }

    @Override
    public void loadNextMap() throws RuntimeException {
        campaignMapIndex++;
        CampaignMap map = getMap();
        if (!loadMap(map)) {
            stop(true);
            throw new RuntimeException("Failed to load next map " + map);
        }
    }

    @Override
    public void moveToNextMap() throws RuntimeException {
        director.getMobHandler().setSpawningEnabled(false);
        if (gameWorlds.size() < 2) {
            throw new RuntimeException("There's no next map to go to!");
        }
        GameWorld currentWorld = gameWorlds.remove();
        GameWorld nextWorld = gameWorlds.peek();
        if (nextWorld == null || !nextWorld.isLoaded()) {
            throw new RuntimeException("Next world is not loaded!");
        }
        for (Player p : currentWorld.getWorld().getPlayers()) {
            p.teleport(getMap().startLocation().toLocation(nextWorld.getWorld())); // This is here to teleport any non-playing players
        }
        currentWorld.unload();
        startMap();
    }

    @Override
    public Campaign getCampaign() {
        return campaign;
    }

    @Override
    public int getCampaignMapIndex() {
        return campaignMapIndex;
    }

    @Override
    public CampaignMap getMap() {
        return campaign.getMaps()[campaignMapIndex];
    }

    @Override
    public World getWorld() {
        if (gameWorlds.isEmpty()) return null;
        return gameWorlds.peek().getWorld();
    }

    @Override
    public Difficulty getDifficulty() {
        return difficulty;
    }

    @Override
    public Set<Player> getPlayers() {
        return players.keySet();
    }

    @Override
    public PlayerStats getPlayerStat(Player player) {
        return players.get(player);
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public Director getDirector() {
        return director;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    private boolean loadMap(CampaignMap map) {
        LocalGameWorld localGameWorld = new LocalGameWorld(new File(plugin.getDataFolder(), campaign.getMainDirectory() + "/" + map.directory()));
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
        s.setLivesFromLastSave(s.getLives());
    }

    private void loadPlayerStats(Player p) {
        PlayerStats s = players.get(p);
        p.setHealth(s.getHealth());
        p.setAbsorptionAmount(s.getAbsorption());
        p.getInventory().clear();
        for (int i = 0; i < s.getHotbar().length; i++) {
            p.getInventory().setItem(i, s.getHotbar()[i]);
        }
        s.setLives(s.getLivesFromLastSave());
    }
}
