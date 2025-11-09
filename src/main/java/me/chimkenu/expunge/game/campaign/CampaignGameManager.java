package me.chimkenu.expunge.game.campaign;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignIntro;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.entities.*;
import me.chimkenu.expunge.entities.item.AmmoPile;
import me.chimkenu.expunge.entities.survivor.PlayerSurvivor;
import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.listeners.CleanUp;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CampaignGameManager implements GameManager {
    private final JavaPlugin plugin;
    private final World world;
    private final Set<Player> players;

    private final Campaign campaign;
    private final Difficulty difficulty;
    private int mapIndex;
    private int attempts = 0;

    private final InfectedHandler infectedHandler;
    private final Set<Listener> listeners;
    private final Set<BukkitTask> tasks;
    private final Set<GameEntity> entities;
    private final Map<Survivor, RestorableState<Survivor>> survivors;
    private final Set<Infected> infected;
    private final Set<RegenerableState<? extends Regenerable>> regenerables;

    private BukkitTask main;
    private long sysTimeStart = 0;
    private long currentTick = 0;
    private boolean isSpawningActive = false;

    public CampaignGameManager(JavaPlugin plugin, World world, Set<Player> players, Campaign campaign, Difficulty difficulty, int startingMapIndex) {
        this.plugin = plugin;
        this.world = world;
        this.players = new HashSet<>(players);

        this.campaign = campaign;
        this.difficulty = difficulty;
        this.mapIndex = startingMapIndex;

        infectedHandler = new InfectedHandler();
        listeners = new HashSet<>();
        tasks = new HashSet<>();
        entities = new HashSet<>();
        survivors = new HashMap<>();
        infected = new HashSet<>();
        regenerables = new HashSet<>();

        players.forEach(p -> {
            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);
            var s = new PlayerSurvivor(this, p);
            s.setLocation(getMap().startLocation().toLocation(world));
            survivors.put(s, s.getState());
        });

        if (getMap() instanceof CampaignIntro intro) {
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
            }.runTaskLater(plugin, 60); // Wait for players to load map
        } else start();
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public Set<Player> getPlayers() {
        return players;
    }

    @Override
    public Optional<Survivor> getSurvivor(Player player) {
        return survivors.keySet().stream().filter(s -> s.getHandle().equals(player)).findAny();
    }

    @Override
    public void start() {
        sysTimeStart = System.currentTimeMillis();
        main = new BukkitRunnable() {
            @Override
            public void run() {
                currentTick++;
                getSurvivors().forEach(s -> s.update(currentTick));
                if (isSpawningActive) {
                    infectedHandler.tick(currentTick);
                }
            }
        }.runTaskTimer(plugin, 1, 1);

        spawnStartingItems();
        saveState(true);
        clearEntities();
        startMap();
    }

    @Override
    public void stop(boolean isAbrupt) {
        if (main != null) main.cancel();
        main = null;

        entities.forEach(GameEntity::remove); entities.clear();
        tasks.forEach(BukkitTask::cancel); tasks.clear();
        listeners.forEach(HandlerList::unregisterAll); listeners.clear();

        for (Player player : world.getPlayers()) {
            player.sendMessage(ChatUtil.format("&1Game ended at &5" + getGameTime()));
            if (isAbrupt) {
                Expunge.getLobby().teleportToLobby(player);
            } else {
                Expunge.getLobby().setSpectator(player);
            }
        }

        if (isAbrupt) {
            return;
        }

        int time = 10 * 20;
        new BukkitRunnable() {
            int i = time;
            @Override
            public void run() {
                for (Player player : world.getPlayers()) {
                    ChatUtil.sendActionBar(player, "&7Returning to lobby in &5" + i / 20 + "&7...");
                }

                if (world.getPlayers().isEmpty()) {
                    this.cancel();
                }

                if (i <= 0) {
                    for (Player player : world.getPlayers()) {
                        Expunge.getLobby().teleportToLobby(player);
                    }
                    this.cancel();
                }

                i--;
            }

            @Override
            public void cancel() {
                super.cancel();
                Expunge.getLobby().unload();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public void restart() {
        isSpawningActive = false;
        clearTasks();
        clearEntities();
        startMap();
    }

    @Override
    public boolean isRunning() {
        return main != null && !main.isCancelled();
    }

    private void startMap() {
        // RESET CAMPAIGN BARRIERS (if any)
        /* TODO: barrier reset logic inside campaign map
        var prev = state.getCampaignMapIndex() - 1;
        if (prev >= 0) {
            var prevMap = campaign.maps()[prev];
            if (prevMap.nextMapCondition() instanceof Barrier b) {
                b.place(getWorld(), false);
            }
        }
        if (getMap().nextMapCondition() instanceof Barrier b) {
            b.place(getWorld(), true);
        }
         */

        loadState();

        // REGISTER EVENTS AND HAPPENINGS
        clearListeners();
        for (Listener listener : getMap().gameListeners(plugin, this)) {
            addListener(listener);
        }
        for (Listener listener : getMap().happenings(plugin, this)) {
            addListener(listener);
        }

        attempts++;
        spawnMapItems();
        getMap().runAtStart().accept(this);

        currentTick = 0;
        infectedHandler.resetState();
        isSpawningActive = true;
    }

    private void endMap() {
        isSpawningActive = false;
        getMap().runAtEnd().accept(this);
        clearListeners();
        clearTasks();
        saveState(false);
        clearEntities();
        attempts = 0;
    }

    private void saveState(boolean isStartRegion) {
        regenerables.clear();

        var region = isStartRegion ? getMap().startRegion() : getMap().endRegion();
        world.getNearbyEntities(region).stream()
                .map(this::getEntity)
                .forEach(p -> p.ifPresent(entity -> {
                    if (entity instanceof Regenerable r) {
                        regenerables.add(r.getState());
                    }
                }));
        for (var survivor : getSurvivors()) {
            survivors.put(survivor, survivor.getState());
        }
    }

    private void loadState() {
        regenerables.forEach(state -> {
            var entity = state.regenerate(world);
            if (entity instanceof Infected i) {
                infected.add(i);
            } else {
                entities.add(entity);
            }
        });
        for (Survivor survivor : getSurvivors()) {
            survivor.clearEffects();
            survivor.addEffect(PotionEffectType.BLINDNESS, 20 * 3, 4);

            survivors.get(survivor).restore(survivor);
            if (!survivor.isAlive()) {
                survivor.revive();
                survivor.setLocation(getMap().startLocation().toLocation(getWorld()));
            }
        }
    }

    public void moveToNextMap() {
        endMap();
        mapIndex++;

        // check if it is the last scene then end the game
        if (mapIndex >= campaign.maps().length) {
            players.forEach(p -> ChatUtil.sendFormatted(p, "&2END OF GAME"));
            stop(false);
            return;
        }

        startMap();
    }

    @Override
    public void addListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        listeners.add(listener);
    }

    private void clearListeners() {
        for (var listener : listeners) {
            if (listener instanceof CleanUp cleanUp) {
                cleanUp.clean();
            }
            HandlerList.unregisterAll(listener);
        }
        listeners.clear();
    }

    @Override
    public void addTask(BukkitTask task) {
        tasks.add(task);
    }

    private void clearTasks() {
        for (var task : tasks) {
            task.cancel();
        }
        tasks.clear();
    }

    @Override
    public GameEntity spawnItem(GameItem item, Vector position, boolean isInvulnerable) {
        var entity = item.spawn(world, position, isInvulnerable);
        addEntity(entity);
        return entity;
    }

    @Override
    public Infected spawnInfected(MobType type, Class<? extends Mob> mob, Vector position) {
        var infected = new Infected(this, position, type, mob, difficulty.settings().mobs().get(type));
        addEntity(infected);
        return infected;
    }

    @Override
    public void spawnMobNaturally(MobType type, Class<? extends Mob> mob) {
        throw new NotImplementedException("sorry!");
    }

    @Override
    public void addEntity(GameEntity entity) {
        entities.add(entity);
    }

    @Override
    public Set<GameEntity> getEntities() {
        var set = new HashSet<>(entities);
        set.addAll(getSurvivors());
        set.addAll(getInfected());
        return set;
    }

    @Override
    public Set<Survivor> getSurvivors() {
        return survivors.keySet();
    }

    @Override
    public Set<Infected> getInfected() {
        return infected;
    }

    private void clearEntities() {
        entities.forEach(GameEntity::remove);
        entities.clear();
        infected.forEach(GameEntity::remove);
        infected.clear();
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public CampaignMap getMap() {
        return campaign.maps()[mapIndex];
    }

    private void spawnStartingItems() {
        var map = getMap();
        for (ItemRandomizer randomizer : map.startItems()) {
            randomizer.randomize(this, attempts, survivors.size());
        }
    }

    private void spawnMapItems() {
        var map = getMap();
        for (ItemRandomizer randomizer : map.mapItems()) {
            randomizer.randomize(this, attempts, survivors.size());
        }
        for (Vector v : map.ammoLocations()) {
            addEntity(new AmmoPile(world, v.toLocation(world)));
        }
    }

    private String getGameTime() {
        long totalGameTime = System.currentTimeMillis() - sysTimeStart;
        int milliseconds = (int) (totalGameTime % 1000);
        int seconds = (int) (totalGameTime / 1000) % 60;
        int minutes = (int) (totalGameTime / (1000 * 60)) % 60;
        int hours = (int) (totalGameTime / (1000 * 60 * 60)) % 60;
        return String.format("%01d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds);
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isSpawningActive() {
        return isSpawningActive;
    }

    public void setSpawningActive(boolean isSpawningActive) {
        this.isSpawningActive = isSpawningActive;
    }

    private class InfectedHandler {
        private static final int RECOMPUTE_COOLDOWN = 2000;
        private static final int MAX_QUEUE = 30;
        private static final int WANDERER_LOOKAHEAD = 8;
        private static final int SPECIAL_DEQUEUE_FREQUENCY = 20;
        private static final int MAX_IDLE_DISTANCE = 30 * 30;
        private static final int MAX_ACTIVE_DISTANCE = 20 * 20;
        private static final int DESPAWN_INTERVAL = 20 * 20;

        private Phase currentPhase;
        private int furthestProgress;
        private int commonQueue;
        private int specialQueue;

        private Set<Block> blocks;
        Map<Double, Vector> spawnLocations;
        Double[] spawnLocationKeys;
        private long lastCompute = 0;
        private int nodeBuffer = 0;

        public InfectedHandler() {
            resetState();
        }

        //
        // MOB HANDLER
        //
        public void tick(long tick) {
            generateSpawnBlocks();
            if (furthestProgress == 0) {
                spawnCommon(difficulty.settings().startingMobCount(), true, true);
                furthestProgress++;
            }

            switch (currentPhase) {
                case BUILD -> updateQueue();
                case PEAK -> {
                    if (countHorde() <= 0) setPhase(Phase.RELAX);
                }
            }

            // progress = how far the players have moved since the last check
            var oldProgress = furthestProgress;
            checkProgress();
            var progress = furthestProgress - oldProgress;
            reactToProgress(progress);

            // spawn special infected
            if (tick % SPECIAL_DEQUEUE_FREQUENCY == 0) {
                dequeueSpecial();
            }

            // TODO: debugging
            players.forEach(p -> ChatUtil.sendActionBar(p, "p: " + furthestProgress + "/" + getMap().escapePath().size() + " - c: " + commonQueue + " - s: " + specialQueue + " - phase: " + currentPhase.name() + " - time: " + currentTick + " - activeMobs: " + infected.size() + " - hordeCount: " + countHorde()));
            for (Block b : blocks) {
                b.getWorld().spawnParticle(Particle.DUST, b.getLocation().add(0.5, 1.1, 0.5), 1, new Particle.DustOptions(Color.GREEN, 0.5f));
            }
            // -------------------

            // despawn stuck common infected
            if (tick % DESPAWN_INTERVAL == 0) {
                getInfected().forEach(entity -> {
                    if (!(entity instanceof Mob mob)) {
                        return;
                    }
                    var loc = entity.getLocation();
                    var distance = SpawnUtil.nearestDistanceFrom(getSurvivors().stream().map(GameEntity::getLocation).toList(), loc);
                    if (
                            (mob.getTarget() == null && distance > MAX_IDLE_DISTANCE) ||
                            (mob.getTarget() != null && distance > MAX_ACTIVE_DISTANCE)
                    ) {
                        mob.remove();
                    }
                });
            }
        }

        public void resetState() {
            currentPhase = Phase.BUILD;
            blocks = null;
            furthestProgress = 0;
            commonQueue = 0;
            specialQueue = 0;
        }

        private void generateSpawnBlocks() {
            blocks = new HashSet<>();
            var escapePath = getMap().escapePath();
            var path = escapePath.subList(furthestProgress, Math.min(furthestProgress + WANDERER_LOOKAHEAD, escapePath.size()));
            for (var p : path) {
                blocks.addAll(p.spawnBlocks(world, getSurvivors()));
            }
        }

        private void checkProgress() {
            var points = getMap().escapePath();
            for (int i = points.size() - 1; i > furthestProgress; i--) {
                var path = points.get(i);
                path.showPath(world);
                if (path.isWithin(getSurvivors().stream().filter(Survivor::isAlive))) {
                    furthestProgress = i;
                    break;
                }
            }
        }

        private void reactToProgress(int progress) {
            if (progress <= 0) {
                return;
            }
            switch (currentPhase) {
                case BUILD -> dequeueCommon();
                case PEAK -> {
                    nodeBuffer -= progress;
                    if (nodeBuffer <= 0) {
                        // they're still going?? not on my watch.
                        commonQueue = MAX_QUEUE;
                        dequeueCommon();
                        setPhase(Phase.RELAX);
                    }
                }
                case RELAX -> {
                    nodeBuffer -= progress;
                    if (nodeBuffer <= 0) {
                        setPhase(Phase.BUILD);
                    }
                }
            }
        }

        private void updateQueue() {
            var settings = difficulty.settings();
            if (currentTick % settings.commonMobQueueFrequency() == 0) {
                commonQueue = Math.min(30, commonQueue + 1);
            }
            if (currentTick % settings.specialMobQueueFrequency() == 0) {
                specialQueue++;
            }
        }

        private void dequeueCommon() {
            if (commonQueue <= 0) {
                return;
            }
            var dequeued = 0;
            if (commonQueue > thresholdHordeSize()) {
                // HORDE
                dequeued = Math.min(commonQueue, maxHordeSize());
                spawnCommon(dequeued, false, true);
                setPhase(Phase.PEAK);
            } else {
                // WANDERERS
                dequeued = Math.min(MAX_QUEUE - commonQueue, maxHordeSize());
                spawnCommon(dequeued, true, true);
            }
            commonQueue = Math.max(commonQueue - dequeued, 0);
        }

        private void dequeueSpecial() {
            if (specialQueue <= 0) {
                return;
            }
            specialQueue--;
            var specials = Arrays.stream(MobType.values()).filter(mobType -> mobType.classification() == MobType.Classification.SPECIAL).toList();
            var choice = specials.get(ThreadLocalRandom.current().nextInt(specials.size()));
            spawnInfected(choice, findSuitableSpawn(false));
        }

        private int maxHordeSize() {
            return difficulty.settings().maxHordeSpawnSize();
        }

        private int thresholdHordeSize() {
            return maxHordeSize() / 2;
        }

        private void spawnCommon(int numberToSpawn, boolean isWanderer, boolean force) {
            if (!force) {
                commonQueue += numberToSpawn;
            }

            for (int i = 0; i < numberToSpawn; i++) {
                Vector loc = findSuitableSpawn(isWanderer);
                // TODO: law of demeter
                var entity = spawnInfected(MobType.COMMON, loc);
                var mob = entity.getHandle();
                if (isWanderer) {
                    mob.getScoreboardTags().add("WANDERER");
                } else {
                    mob.getScoreboardTags().add("HORDE");
                    var survivor = getSurvivors().stream().filter(Survivor::isAlive).findAny();
                    survivor.ifPresent(entity::setTarget);
                }
                addEntity(entity);
            }
        }

        private Vector findSuitableSpawn(boolean useGeneratedBlocks) {
            if (useGeneratedBlocks && blocks != null && !blocks.isEmpty()) {
                var b = blocks.stream().toList();
                return b.get(ThreadLocalRandom.current().nextInt(b.size())).getLocation().toVector().add(new Vector(0.5, 1, 0.5));
            }

            var now = System.currentTimeMillis();
            if (now - lastCompute < RECOMPUTE_COOLDOWN) {
                return spawnLocations.get(spawnLocationKeys[ThreadLocalRandom.current().nextInt(0, Math.min(3, spawnLocationKeys.length))]);
            }
            lastCompute = now;

            // Sort locations based on distance to the nearest player (has to be alive)
            final double MIN_DISTANCE = Math.pow(getPlugin().getConfig().getDouble("director.too-close-radius", 8), 2);

            spawnLocations = new HashMap<>();
            for (var loc : getMap().spawnLocations()) {
                var dist = SpawnUtil.nearestDistanceFrom(
                        getSurvivors().stream().filter(Survivor::isAlive).map(Survivor::getLocation).toList(),
                        loc.toLocation(world)
                );
                if (dist < MIN_DISTANCE) {
                    continue;
                }
                spawnLocations.put(dist, loc);
            }

            spawnLocationKeys = spawnLocations.keySet().toArray(new Double[0]);
            Arrays.sort(spawnLocationKeys);

            // Return a random near spawn location
            return spawnLocations.get(spawnLocationKeys[ThreadLocalRandom.current().nextInt(0, Math.min(3, spawnLocationKeys.length))]);
        }

        private void setPhase(Phase phase) {
            switch (phase) {
                case PEAK -> nodeBuffer = difficulty.settings().peakNodeBuffer();
                case RELAX -> nodeBuffer = difficulty.settings().relaxNodeBuffer();
                default -> nodeBuffer = 0;
            }
            currentPhase = phase;
        }

        private long countHorde() {
            return infected.stream().filter(e -> e.getHandle().getScoreboardTags().contains("HORDE")).count();
        }

        public void bile(LivingEntity target, double radius) {
            spawnCommon(30, false, true);
            new BukkitRunnable() {
                int i = 15;
                @Override
                public void run() {
                    for (Entity e : target.getNearbyEntities(radius, radius, radius)) {
                        if (e instanceof Zombie zombie) {
                            zombie.setTarget(target);
                        }
                    }
                    if (i <= 0) this.cancel();
                    i--;
                }
            }.runTaskTimer(getPlugin(), 0, 20);
        }

        private enum Phase {
            BUILD,
            PEAK,
            RELAX
        }
    }
}
