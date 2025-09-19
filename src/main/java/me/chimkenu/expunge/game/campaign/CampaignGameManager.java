package me.chimkenu.expunge.game.campaign;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignIntro;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.CampaignDifficulty;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.listeners.CleanUp;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class CampaignGameManager implements GameManager {
    private final Expunge plugin;
    private final Campaign campaign;

    private final CampaignGameState state;
    private final CampaignDirector director;
    private final Set<Listener> listeners;
    private final Set<BukkitTask> tasks;
    private final Set<Entity> entities;

    private BukkitTask main;

    public CampaignGameManager(Expunge plugin, Campaign campaign, CampaignDifficulty difficulty, int campaignMapIndex, Set<Player> players) throws RuntimeException {
        this.plugin = plugin;
        this.campaign = campaign;

        state = new CampaignGameState(players, difficulty, campaignMapIndex);
        director = new CampaignDirector(this);

        listeners = new HashSet<>();
        tasks = new HashSet<>();
        entities = new HashSet<>();

        players.forEach(p -> {
            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
        });

        if (campaign.maps()[campaignMapIndex] instanceof CampaignIntro intro) {
            getPlayers().forEach(p -> {
                p.setGameMode(GameMode.SPECTATOR);
                p.teleport(getMap().startLocation().toLocation(getWorld()));
            });
            var manager = this;
            main = new BukkitRunnable() {
                @Override
                public void run() {
                    main = new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (getPlayers().isEmpty()) {
                                stop(true);
                                this.cancel();
                            }
                            start();
                        }
                    }.runTaskLater(plugin, intro.play(manager));
                }
            }.runTaskLater(plugin, 40); // Wait for players to load map
        } else start();
    }

    @Override
    public Expunge getPlugin() {
        return plugin;
    }

    @Override
    public World getWorld() {
        return plugin.getLobby().getGameWorld();
    }

    @Override
    public Director getDirector() {
        return director;
    }

    @Override
    public CampaignGameState getState() {
        return state;
    }

    @Override
    public Set<Player> getPlayers() {
        return state.getPlayers();
    }

    @Override
    public PlayerStats getPlayerStat(Player player) {
        return state.getPlayerStat(player);
    }

    @Override
    public void start() {
        state.setSysTimeStart();
        main = new BukkitRunnable() {
            @Override
            public void run() {
                if (state.getPlayers().isEmpty()) cancel();
                state.incrementTimeTicks();
                director.update(state);
            }
        }.runTaskTimer(plugin, 0, 1);
        startMap();
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) main.cancel();
        main = null;

        entities.forEach(Entity::remove); entities.clear();
        tasks.forEach(BukkitTask::cancel); tasks.clear();
        listeners.forEach(HandlerList::unregisterAll); listeners.clear();

        var gameWorld = getWorld();
        if (gameWorld == null) return;

        long totalGameTime = System.currentTimeMillis() - state.getSysTimeStart();
        int milliseconds = (int) (totalGameTime % 1000);
        int seconds = (int) (totalGameTime / 1000) % 60;
        int minutes = (int) (totalGameTime / (1000 * 60)) % 60;
        int hours = (int) (totalGameTime / (1000 * 60 * 60)) % 60;
        String gameTime = String.format("%01d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds);

        for (Player player : gameWorld.getPlayers()) {
            player.sendMessage(ChatUtil.format("&1Game ended at &5" + gameTime));
            if (isAbrupt) {
                plugin.getLobby().teleportToLobby(player);
                continue;
            }
            plugin.getLobby().setSpectator(player);
        }

        if (isAbrupt) {
            return;
        }

        int time = 10 * 20;
        new BukkitRunnable() {
            int i = time;
            @Override
            public void run() {
                for (Player player : gameWorld.getPlayers()) {
                    ChatUtil.sendActionBar(player, "&7Returning to lobby in &5" + i / 20 + "&7...");
                }

                if (gameWorld.getPlayers().isEmpty()) {
                    this.cancel();
                }

                if (i <= 0) {
                    for (Player player : gameWorld.getPlayers()) {
                        plugin.getLobby().teleportToLobby(player);
                    }
                    this.cancel();
                }

                i--;
            }

            @Override
            public void cancel() {
                super.cancel();
                plugin.getLobby().unload();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public boolean isRunning() {
        return main != null && !main.isCancelled();
    }

    public void restartMap() {
        state.resetGameTicks();
        director.setPhase(Director.Phase.DISABLED);
        director.clearEntities();
        state.incrementAttempts();
        startMap();
    }

    public void startMap() {
        director.resetState();

        for (Player p : getPlayers()) {
            p.leaveVehicle();
            p.teleport(getMap().startLocation().toLocation(getWorld()));

            p.removePotionEffect(PotionEffectType.GLOWING);
            p.removePotionEffect(PotionEffectType.NIGHT_VISION);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
            p.setGameMode(GameMode.ADVENTURE);

            getPlayerStat(p).loadPlayerStats(p);
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

        for (Listener listener : getMap().gameListeners(plugin, this)) {
            addListener(listener);
        }
        for (Listener listener : getMap().happenings(plugin, this)) {
            addListener(listener);
        }

        director.spawnStartingItems();
        director.spawnMapItems();
        director.spawnAmmoPiles();
        if (getMap().runAtStart() != null) getMap().runAtStart().run(this, null);
        director.setPhase(Director.Phase.BUILD);
    }

    public void endMap() {
        director.setPhase(Director.Phase.DISABLED);

        if (getMap().runAtEnd() != null) getMap().runAtEnd().run(this, null);

        state.resetAttempts();
        director.clearEntities();

        for (Player p : getPlayers()) {
            if (!getPlayerStat(p).isAlive()) getPlayerStat(p).revive();
            getPlayerStat(p).savePlayerStats(p);
        }
    }

    public void moveToNextMap() throws RuntimeException {
        director.setPhase(Director.Phase.DISABLED);
        if (state.getCampaignMapIndex() >= campaign.maps().length - 1) {
            throw new RuntimeException("There's no next map to go to!");
        }
        state.incrementCampaignMapIndex();
        startMap();
    }

    public void addListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        listeners.add(listener);
    }

    @Override
    public void addTask(BukkitTask task) {
        tasks.add(task);
        tasks.removeIf(BukkitTask::isCancelled);
    }

    @Override
    public void addEntity(Entity entity) {
        if (entity instanceof Item item) {
            item.setUnlimitedLifetime(true);
        }
        entities.add(entity);
    }

    @Override
    public Set<Entity> getEntities() {
        entities.removeIf(Entity::isDead);
        return entities;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public CampaignMap getMap() {
        return campaign.maps()[state.getCampaignMapIndex()];
    }
}
