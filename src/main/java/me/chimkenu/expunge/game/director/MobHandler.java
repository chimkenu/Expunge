package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.common.Horde;
import me.chimkenu.expunge.mobs.common.Wanderer;
import me.chimkenu.expunge.mobs.special.*;
import me.chimkenu.expunge.mobs.uncommon.Robot;
import me.chimkenu.expunge.mobs.uncommon.Soldier;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class MobHandler {
    private final JavaPlugin plugin;
    private final Director director;

    private final HashSet<GameMob> activeMobs = new HashSet<>();
    private boolean isSpawningEnabled;
    private boolean chillOut;
    private int timeSinceLastHorde;

    public MobHandler(JavaPlugin plugin, Director director) {
        this.plugin = plugin;
        this.director = director;
        isSpawningEnabled = false;
        chillOut = false;
        timeSinceLastHorde = 0;
    }

    public void run(int sceneTime, int sceneAttempts, Difficulty difficulty) {
        if (sceneTime % (20 * 3) == 0 && isSpawningEnabled) {
            // spawning based on difficulty
            if (activeMobs.size() < (difficulty.ordinal() + 1) * 25 || director.calculateRating() < 1) {
                switch (difficulty) {
                    case EASY -> { if (sceneTime % (20 * 10) == 0) spawnAdditionalMob(); }
                    case NORMAL -> { if (sceneTime % (20 * 7) == 0) spawnAdditionalMob(); }
                    case HARD -> { if (sceneTime % (20 * 5) == 0) spawnAdditionalMob(); }
                    case SUFFERING -> spawnAdditionalMob();
                }
            }

            if ((sceneTime % (20 * 15)) == 0) {
                double r = Math.random();
                if (r < 0.16)
                    getActiveMobs().add(new Rider(plugin, director.getWorld(), getRandomSpawnLocation()));
                else if (r < 0.32)
                    getActiveMobs().add(new Spewer(plugin, director.getWorld(),getRandomSpawnLocation()));
                else if (r < 0.48)
                    getActiveMobs().add(new Charger(plugin, director.getWorld(), getRandomSpawnLocation(), difficulty));
                else if (r < 0.64)
                    getActiveMobs().add(new Pouncer(plugin, director.getWorld(), getRandomSpawnLocation()));
                else if (r < 0.86)
                    getActiveMobs().add(new Choker(plugin, director.getWorld(), getRandomSpawnLocation()));
                else
                    getActiveMobs().add(new Spitter(plugin, director.getGameManager(), director.getWorld(), getRandomSpawnLocation()));
            }

            // spawn additional mobs if number of mobs are too low
            if (!chillOut && getActiveMobs().size() < 5 && sceneTime > 20 * 20) {
                chillOut = true;
                timeSinceLastHorde = sceneTime;
                for (int i = 0; i < (20 + (director.getGameManager().getPlayers().size() * 5)); i++) {
                    spawnAdditionalMob();
                }
            } else if (sceneTime - timeSinceLastHorde > 30 * 20) {
                chillOut = false;
            }
        }

        // look through every mob if its alive && look at its distance from the nearest player
        if ((sceneTime % (20 * 15)) == 0) {
            HashSet<GameMob> mobsToRemove = new HashSet<>();
            for (GameMob mob : activeMobs) {
                if (mob.getMob().isDead()) {
                    mobsToRemove.add(mob);
                    continue;
                }
                if (mob instanceof Zombie zombie && zombie.getTarget() != null && zombie.getTarget() instanceof Player target && director.getGameManager().getPlayers().contains(target)) {
                    if (zombie.getLocation().distanceSquared(target.getLocation()) < 20 * 20) continue;
                }
                if (playerNearestDistanceFrom(mob.getMob().getLocation().toVector()) > 20 * 20) {
                    mob.remove();
                    mobsToRemove.add(mob);
                }
            }
            activeMobs.removeAll(mobsToRemove);
        }
    }

    public void spawnMobNearby() {
        final int SPAWN_RADIUS = 30;
        final int TOO_CLOSE_RADIUS = 10;
        final int DEPTH = 2;

        Set<Block> blocks = new HashSet<>();
        Set<Block> tooClose = new HashSet<>();

        ArrayList<Player> players = new ArrayList<>();

        // Gather all the possible spawn locations
        for (Player p : director.getPlayers()) {

            // Disregard player if they are close to another player
            boolean isTooClose = false;
            for (Player q : players) {
                if (isLocationTooClose(p, q.getLocation(), TOO_CLOSE_RADIUS)) {
                    isTooClose = true;
                }
            }
            if (isTooClose) continue;
            players.add(p);

            // Gather nearby valid blocks for entities to spawn
            for (Block b : getValidSurroundingBlocks(p.getWorld().getBlockAt(p.getLocation().add(0, -0.1, 0)), SPAWN_RADIUS, DEPTH)) {
                blocks.add(b);
                if (isLocationTooClose(p, b.getLocation(), TOO_CLOSE_RADIUS)) tooClose.add(b);
            }
        }

        blocks.removeAll(tooClose);
        blocks.removeIf(block -> {
            for (Player p : director.getPlayers()) {
                return canBeSeenByPlayer(block, p);
            }
            return false;
        });

        for (Block b : blocks) {
            b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 1.1, 0.5), 1, new Particle.DustOptions(Color.GREEN, 0.5f));
        }
    }

    private boolean canBeSeenByPlayer(Block block, Player player) {
        final Vector playerToBlock = block.getLocation().toVector().subtract(player.getEyeLocation().toVector());
        final double maxAngle = 60 * Math.PI / 180;
        final double accuracy = 0.5;

        if (playerToBlock.angle(player.getEyeLocation().getDirection()) > maxAngle)
            return false;

        // Ray cast
        Vector target = block.getLocation().toVector().add(new Vector(0.5, 2.5, 0.5));
        RayTrace ray = new RayTrace(player.getEyeLocation().toVector(), target.subtract(player.getEyeLocation().toVector()).normalize());
        ArrayList<Vector> positions = ray.traverse(playerToBlock.length(), accuracy);
        for (Vector v : positions) {
            if (v.distanceSquared(player.getEyeLocation().toVector()) > target.distanceSquared(player.getEyeLocation().toVector()))
                continue;
            if (!player.getWorld().getBlockAt(v.toLocation(player.getWorld())).isSolid()) {
                return false;
            }
        }
        return true;
    }

    private Set<Block> getValidSurroundingBlocks(Block block, int radius, int depth) {
        Set<Block> blocks = new HashSet<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block testBlock = block.getWorld().getBlockAt(block.getLocation().add(x, 0, z));
                testBlock = searchDepth(testBlock, depth);

                if (testBlock.getType() == Material.AIR) {
                    continue;
                }

                if (testBlock.getRelative(0, 1, 0).getType() != Material.AIR) {
                    continue;
                }

                if (testBlock.getRelative(0, 2, 0).getType() != Material.AIR) {
                    continue;
                }

                blocks.add(testBlock);
            }
        }
        return blocks;
    }

    private Block searchDepth(Block block, int depth) {
        for (int h = -depth; h <= depth; h++) {
            Block search = block.getRelative(0, h, 0);
            if (search.getType() != Material.AIR && search.getRelative(0, 1, 0).getType() == Material.AIR)
                return search;
        }
        return block;
    }

    private boolean isLocationTooClose(Player p, Location l, int distance) {
        return p.getLocation().distanceSquared(l) > distance * distance;
    }

    public void spawnStartingMobs() {
        int sceneIndex = director.getGameManager().getCampaignMapIndex();
        int sceneAttempts = director.getSceneAttempts();
        Difficulty difficulty = director.getDifficulty();
        int numberOfPlayers = director.getPlayers().size();
        
        // MOB CALCULATION: STARTING HORDE IS DEFINED BY THE FOLLOWING EQUATION:
        // 6 + sd + 2p - (a - (a / 5) % 5)
        // WHEREIN 's' IS THE SCENE INDEX, 'd' IS THE DIFFICULTY (0-2),
        // 'p' IS THE NUMBER OF PLAYERS, AND 'a' IS THE NUMBER OF SCENE ATTEMPTS
        int totalMobs = 6 + (sceneIndex * difficulty.ordinal()) + (2 * numberOfPlayers) - ((sceneAttempts - (sceneAttempts % 5)) / 5);
        if (totalMobs <= 0) {
            totalMobs = 1;
        }

        // distribute throughout first 3 paths
        BoundingBox[] path = director.getMap().pathRegions();
        totalMobs = totalMobs / 3;

        for (int i = 0; i < Math.min(path.length, 3); i++) {
            spawnAtRandomLocations(path[i], totalMobs);
        }
    }

    public void clearMobs() {
        chillOut = false;
        timeSinceLastHorde = 0;
        for (GameMob mob : activeMobs) {
            mob.remove();
        }
        activeMobs.clear();
    }

    public Set<GameMob> getActiveMobs() {
        return activeMobs;
    }

    public LivingEntity spawnMob(GameMob mob) {
        activeMobs.add(mob);
        return mob.getMob();
    }

    public Vector getRandomSpawnLocation() {
        // Get a random player
        Player player = null;
        int item = ThreadLocalRandom.current().nextInt(director.getPlayers().size());
        int k = 0;
        for (Player p : director.getPlayers()) {
            if (player == null || k == item)
                player = p;
            k++;
        }

        if (player == null) throw new RuntimeException("Where the players at??");

        // Sort locations based on distance to player
        Vector[] spawnLocations = director.getMap().spawnLocations();
        ArrayList<Double> distancesSquared = new ArrayList<>();
        for (Vector loc : spawnLocations) {
            distancesSquared.add(loc.distanceSquared(player.getLocation().toVector()));
        }
        for(int i = 0; i < distancesSquared.size(); ++i){
            int j = i;
            while(j > 0 && distancesSquared.get(j - 1) > distancesSquared.get(j)){
                double key = distancesSquared.get(j);
                distancesSquared.set(j, distancesSquared.get(j - 1));
                distancesSquared.set(j - 1, key);

                Vector loc = spawnLocations[j];
                spawnLocations[j] =  spawnLocations[j - 1];
                spawnLocations[j - 1] = loc;

                j--;
            }
        }

        // Return a random near spawn location
        return spawnLocations[ThreadLocalRandom.current().nextInt(0, Math.min(3, spawnLocations.length))];
    }

    public void spawnAdditionalMob() {
        // chance to spawn uncommon zombie (5%)
        int random = ThreadLocalRandom.current().nextInt(0, 20);
        LivingEntity spawnedMob;
        if (random == 0)
            spawnedMob = spawnMob(new Robot(plugin, director.getWorld(), getRandomSpawnLocation(), director.getDifficulty()));
        else if (random == 1)
            spawnedMob = spawnMob(new Soldier(plugin, director.getWorld(), getRandomSpawnLocation(), director.getDifficulty()));
        else
            spawnedMob = spawnMob(new Horde(plugin, director.getWorld(), getRandomSpawnLocation(), director.getDifficulty()));
        spawnedMob.setRemoveWhenFarAway(false);
    }

    public void spawnAtRandomLocations(BoundingBox b, int numToSpawn) {
        ArrayList<Location> savedLocations = new ArrayList<>();
        int minX = (int) b.getMinX();
        int minZ = (int) b.getMinZ();
        int y = (int) b.getMinY();
        int maxX = (int) b.getMaxX();
        int maxZ = (int) b.getMaxZ();

        for (int i = 0; i < numToSpawn; i++) {
            Location loc;
            boolean isValid = false;
            int attempts = 0;
            do {
                attempts++;
                int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
                int z = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
                loc = new Location(director.getWorld(), x, y, z);
                if (director.getWorld().getBlockAt(loc.clone().add(0, 1, 0)).getType().equals(Material.AIR) && director.getWorld().getBlockAt(loc.clone().add(0, 2, 0)).getType().equals(Material.AIR)) isValid = true;
                else loc = null;
            } while (!isValid || attempts < 15);

            if (loc == null) {
                // spawn in a valid known location
                if (!savedLocations.isEmpty()) loc = savedLocations.get(ThreadLocalRandom.current().nextInt(0, savedLocations.size()));
                else continue;
            } else {
                loc.add(0.5, 1, 0.5);
                savedLocations.add(loc);
            }

            spawnMob(new Wanderer(plugin, director.getWorld(), loc.toVector(), director.getDifficulty()));
        }

        // random chance to spawn boss
        int random = ThreadLocalRandom.current().nextInt(0, 10);
        if (random > 7 - director.getGameManager().getDifficulty().ordinal()) {
            if (random == 8)
                spawnTank();
            else
                spawnWitch();
        }
    }

    public void spawnTank() {
        Vector[] bossLoc = director.getMap().bossLocations();
        if (bossLoc.length > 0) {
            LivingEntity spawnedMob = spawnMob(new Tank(plugin, director.getWorld(), bossLoc[ThreadLocalRandom.current().nextInt(0, bossLoc.length)], director.getDifficulty()));
            spawnedMob.setRemoveWhenFarAway(false);
        }
    }

    public void spawnWitch() {
        Vector[] bossLoc = director.getMap().bossLocations();
        if (bossLoc.length > 0) {
            LivingEntity spawnedMob = spawnMob(new Witch(plugin, director.getWorld(), bossLoc[ThreadLocalRandom.current().nextInt(0, bossLoc.length)]));
            spawnedMob.setRemoveWhenFarAway(false);
        }
    }

    private double playerNearestDistanceFrom(Vector vector) {
        double smallest = 2147483647;
        for (Player player : director.getGameManager().getPlayers()) {
            smallest = Math.min(smallest, player.getLocation().toVector().distanceSquared(vector));
        }
        return smallest;
    }

    public boolean isSpawningEnabled() {
        return isSpawningEnabled;
    }

    public void setSpawningEnabled(boolean spawningEnabled) {
        isSpawningEnabled = spawningEnabled;
    }
}
