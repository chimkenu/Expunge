package me.chimkenu.expunge.game.campaign;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.GameState;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.Interactable;
import me.chimkenu.expunge.mobs.MobType;
import me.chimkenu.expunge.mobs.common.Common;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class CampaignDirector implements Director {
    private static final int RECOMPUTE_COOLDOWN = 2000;
    private static final int MAX_QUEUE = 30;
    private static final int WANDERER_LOOKAHEAD = 8;
    private static final int SPECIAL_DEQUEUE_FREQUENCY = 20;
    private static final int MAX_IDLE_DISTANCE = 30 * 30;
    private static final int MAX_ACTIVE_DISTANCE = 20 * 20;

    private final CampaignGameManager manager;

    private Phase currentPhase;
    private CampaignGameState currentState;
    private int furthestProgress;
    private int commonQueue;
    private int specialQueue;

    private Set<Block> blocks;
    Map<Double, Vector> spawnLocations;
    Double[] spawnLocationKeys;
    private long lastCompute = 0;
    private int nodeBuffer = 0;

    public CampaignDirector(CampaignGameManager gameManager) {
        this.manager = gameManager;
    }

    //
    // MOB HANDLER
    //
    public void update(GameState state) {
        if (currentPhase == Phase.DISABLED) {
            return;
        }
        currentState = (CampaignGameState) state;

        updatePlayers();
        generateSpawnBlocks();
        if (furthestProgress == 0) {
            spawnCommon(currentState.getDifficulty().settings().startingMobCount(), true, true);
            furthestProgress++;
        }

        switch (currentPhase) {
            case BUILD -> updateQueue();
            case PEAK -> {
                if (countHorde() <= 0) setPhase(Phase.RELAX);
            }
        }

        var oldProgress = furthestProgress;
        checkProgress();
        reactToProgress(furthestProgress - oldProgress);

        if (state.getGameTicks() % SPECIAL_DEQUEUE_FREQUENCY == 0) {
            dequeueSpecial();
        }

        // TODO: debugging
        state.getPlayers().forEach(p -> ChatUtil.sendActionBar(p, "p: " + furthestProgress + "/" + manager.getMap().escapePath().size() + " - c: " + commonQueue + " - s: " + specialQueue + " - phase: " + currentPhase.name() + " - time: " + state.getGameTicks() + " - activeMobs: " + getActiveMobs().count() + " - hordeCount: " + countHorde()));
        for (Block b : blocks) {
            b.getWorld().spawnParticle(Particle.DUST, b.getLocation().add(0.5, 1.1, 0.5), 1, new Particle.DustOptions(Color.GREEN, 0.5f));
        }
        // -------------------

        // despawn stuck common infected
        if (state.getGameTicks() % (20 * 20) == 0) {
            getActiveMobs().forEach(entity -> {
                if (!(entity instanceof Mob mob)) {
                    return;
                }
                var loc = entity.getLocation().toVector();
                var distance = SpawnUtil.playerNearestDistanceFrom(getAlivePlayers().toList(), loc);
                if (
                        (mob.getTarget() == null && distance > MAX_IDLE_DISTANCE) ||
                        (mob.getTarget() != null && distance > MAX_ACTIVE_DISTANCE)
                ) {
                        mob.remove();
                }
            });
        }
    }

    private void updatePlayers() {
        var time = currentState.getGameTicks();
        for (Player p : currentState.getPlayers()) {
            // decrease absorption points & kill down players
            if (p.getAbsorptionAmount() > 0) {
                if (time % (20 * 15) == 0) p.setAbsorptionAmount(Math.max(0, p.getAbsorptionAmount() - 1));
            }
            if (!(currentState.getPlayerStat(p).isAlive())) {
                if (time % (20 * 10) == 0) {
                    p.damage(1);
                }
            }

            // give slow to low players
            if (p.getHealth() + p.getAbsorptionAmount() <= 6) {
                p.setFoodLevel(6);
                if (p.getHealth() + p.getAbsorptionAmount() <= 1)
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 2, 0, false, false, true));
            }
            else if (p.getFoodLevel() < 20) p.setFoodLevel(20);
        }
    }

    private void checkProgress() {
        var points = manager.getMap().escapePath();
        for (int i = points.size() - 1; i > furthestProgress; i--) {
            var path = points.get(i);
            path.showPath(manager);
            if (path.isWithin(getAlivePlayers())) {
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

    private void generateSpawnBlocks() {
        var path = manager.getMap().escapePath();
        for (var p : path) {
            blocks.addAll(p.spawnBlocks(manager));
        }
    }

    private void updateQueue() {
        var settings = currentState.getDifficulty().settings();
        var time = currentState.getTimeTicks();
        if (time % settings.commonMobQueueFrequency() == 0) {
            commonQueue = Math.min(30, commonQueue + 1);
        }
        if (time % settings.specialMobQueueFrequency() == 0) {
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
        spawnMob(specials.get(ThreadLocalRandom.current().nextInt(specials.size())), 1, findSuitableSpawn(false), true);
    }

    private int maxHordeSize() {
        return currentState.getDifficulty().settings().maxHordeSpawnSize();
    }

    private int thresholdHordeSize() {
        return maxHordeSize() / 2;
    }

    @Override
    public void spawnMob(Class<? extends Mob> mob, MobType type, int numberToSpawn, Vector position, boolean isAggressive) {
        for (int i = 0; i < numberToSpawn; i++) {
            var entity = type.spawn(manager, position, mob, manager.getState().getDifficulty().settings().mobs().get(type));
            manager.addEntity(entity);
            if (isAggressive) {
                entity.setTarget(getAlivePlayers().findAny().orElse(null));
            }
        }
    }

    @Override
    public void spawnCommon(int numberToSpawn, boolean isWanderer, boolean force) {
        if (!force) {
            commonQueue += numberToSpawn;
        }

        for (int i = 0; i < numberToSpawn; i++) {
            Vector loc = findSuitableSpawn(isWanderer);
            var mob = MobType.COMMON.spawn(manager, loc, currentState.getDifficulty().settings().mobs().get(MobType.COMMON));
            if (isWanderer) {
                mob.getScoreboardTags().add("WANDERER");
            } else {
                mob.getScoreboardTags().add("HORDE");
                mob.setTarget(getAlivePlayers().findAny().orElse(null));
            }
            manager.addEntity(mob);
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
        Set<Player> alivePlayers = new HashSet<>(manager.getState().getPlayers());
        alivePlayers.removeIf(p -> !manager.getState().getPlayerStat(p).isAlive());
        final double MIN_DISTANCE = Math.pow(manager.getPlugin().getConfig().getDouble("director.too-close-radius", 8), 2);

        spawnLocations = new HashMap<>();
        for (var loc : manager.getMap().spawnLocations()) {
            var dist = SpawnUtil.playerNearestDistanceFrom(alivePlayers, loc);
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

    @Override
    public void setPhase(Phase phase) {
        switch (phase) {
            case PEAK -> nodeBuffer = currentState.getDifficulty().settings().peakNodeBuffer();
            case RELAX -> nodeBuffer = currentState.getDifficulty().settings().relaxNodeBuffer();
            default -> nodeBuffer = 0;
        }
        currentPhase = phase;
    }

    @Override
    public Phase getPhase() {
        return currentPhase;
    }

    @Override
    public void resetState() {
        currentPhase = null;
        blocks = null;
        furthestProgress = 0;
        commonQueue = 0;
        specialQueue = 0;
    }

    @Override
    public Stream<Entity> getActiveMobs() {
        return manager.getEntities().stream().filter(e -> e instanceof Mob);
    }

    private long countHorde() {
        return getActiveMobs().filter(e -> e.getScoreboardTags().contains("HORDE")).count();
    }

    @Override
    public Stream<Player> getAlivePlayers() {
        return manager.getPlayers().stream().filter(p -> manager.getPlayerStat(p).isAlive());
    }

    @Override
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
        }.runTaskTimer(manager.getPlugin(), 0, 20);
    }

    //
    // ITEM HANDLER
    //
    @Override
    public void spawnItem(GameItem item, Vector pos, boolean isInvulnerable) {
        Entity entity;
        if (item instanceof Interactable interactable) {
            entity = interactable.spawn(pos.toLocation(manager.getWorld()));
        } else {
            var ent = manager.getWorld().spawn(pos.toLocation(manager.getWorld()), Item.class);
            ent.setItemStack(item.toItem());
            ent.setInvulnerable(isInvulnerable);
            entity = ent;
        }
        manager.addEntity(entity);
    }

    @Override
    public void spawnStartingItems() {
        CampaignMap map = manager.getMap();
        for (ItemRandomizer randomizer : map.startItems()) {
            randomizer.randomize(manager, manager.getPlugin().getItems(), manager.getState().getAttempts());
        }
    }

    @Override
    public void spawnMapItems() {
        CampaignMap map = manager.getMap();
        for (ItemRandomizer randomizer : map.mapItems()) {
            randomizer.randomize(manager, manager.getPlugin().getItems(), manager.getState().getAttempts());
        }
    }

    @Override
    public void spawnAmmoPiles() {
        CampaignMap map = manager.getMap();
        for (Vector v : map.ammoLocations()) {
            spawnAmmo(v);
        }
    }

    private void spawnAmmo(Vector loc) {
        FallingBlock ammoPile = manager.getWorld().spawnFallingBlock(
                loc.toLocation(manager.getWorld()),
                Material.GRAY_CANDLE.createBlockData("[candles=4,lit=false,waterlogged=false]")
        );
        ammoPile.setGravity(false);
        ammoPile.setDropItem(false);
        ammoPile.setCancelDrop(true);
        ammoPile.setInvulnerable(true);
        ammoPile.setCustomName(ChatUtil.format("&7Ammo Pile (Right Click)"));
        ammoPile.setCustomNameVisible(true);
        ammoPile.addScoreboardTag("AMMO_PILE");
        manager.addEntity(ammoPile);
    }

    //
    // GENERIC
    //
    @Override
    public void clearEntities() {
        manager.getEntities().forEach(Entity::remove);
        manager.getEntities().clear();
    }

    public static void playCrescendoEventEffect(Set<Player> players) {
        for (Player p : players) {
            ChatUtil.sendInfo(p, "Here they come...");
            p.playSound(p, Sound.AMBIENT_CAVE, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
}
