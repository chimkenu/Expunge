package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.common.Horde;
import me.chimkenu.expunge.mobs.common.Wanderer;
import me.chimkenu.expunge.mobs.special.Tank;
import me.chimkenu.expunge.mobs.special.Witch;
import me.chimkenu.expunge.mobs.uncommon.Robot;
import me.chimkenu.expunge.mobs.uncommon.Soldier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public class MobHandler {
    private final Campaign map;
    private int sceneIndex;
    private final World world;
    public final HashSet<GameMob> activeMobs = new HashSet<>();

    public MobHandler(Campaign map) {
        this.map = map;
        sceneIndex = 0;
        world = map.getWorld();
    }

    public void updateSceneIndex() {
        sceneIndex++;
    }

    public <T extends GameMob> boolean activeMobsContains(Class<T> mobType) {
        for (GameMob gameMob : activeMobs) {
            if (gameMob.getClass() == mobType) {
                return true;
            }
        }
        return false;
    }

    public HashSet<GameMob> getActiveMobs() {
        return activeMobs;
    }

    public LivingEntity spawnMob(GameMob mob) {
        activeMobs.add(mob);
        return mob.getMob();
    }

    public Location getRandomSpawnLocation() {
        // get a random player
        Player player = Expunge.playing.getKeys().get(ThreadLocalRandom.current().nextInt(0, Expunge.playing.getKeys().size()));

        // sort locations based on distance to player
        Location[] spawnLocations = map.getScenes().get(sceneIndex).spawnLocations();
        ArrayList<Double> distancesSquared = new ArrayList<>();
        for (Location loc : spawnLocations) {
            distancesSquared.add(loc.distanceSquared(player.getLocation()));
        }
        for(int i = 0; i < distancesSquared.size(); ++i){
            int j = i;
            while(j > 0 && distancesSquared.get(j - 1) > distancesSquared.get(j)){
                double key = distancesSquared.get(j);
                distancesSquared.set(j, distancesSquared.get(j - 1));
                distancesSquared.set(j - 1, key);

                Location loc = spawnLocations[j];
                spawnLocations[j] =  spawnLocations[j - 1];
                spawnLocations[j - 1] = loc;

                j--;
            }
        }

        // return a random near spawn location
        return spawnLocations[ThreadLocalRandom.current().nextInt(0, Math.min(3, spawnLocations.length))];
    }

    public void spawnAdditionalMob() {
        // chance to spawn uncommon zombie (5%)
        int random = ThreadLocalRandom.current().nextInt(0, 20);
        LivingEntity spawnedMob;
        if (random == 0)
            spawnedMob = spawnMob(new Robot(map.getWorld(), getRandomSpawnLocation()));
        else if (random == 1)
            spawnedMob = spawnMob(new Soldier(map.getWorld(), getRandomSpawnLocation()));
        else
            spawnedMob = spawnMob(new Horde(map.getWorld(), getRandomSpawnLocation()));
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
                loc = new Location(world, x, y, z);
                if (world.getBlockAt(loc.clone().add(0, 1, 0)).getType().equals(Material.AIR) && world.getBlockAt(loc.clone().add(0, 2, 0)).getType().equals(Material.AIR)) isValid = true;
                else loc = null;
            } while (!isValid || attempts < 15);

            if (loc == null) {
                // spawn in a valid known location
                if (savedLocations.size() > 0) loc = savedLocations.get(ThreadLocalRandom.current().nextInt(0, savedLocations.size()));
                else continue;
            } else {
                loc.add(0.5, 1, 0.5);
                savedLocations.add(loc);
            }

            spawnMob(new Wanderer(world, loc));
        }

        // random chance to spawn boss
        int random = ThreadLocalRandom.current().nextInt(0, 10);
        if (random > 7 - Expunge.currentDifficulty.ordinal()) {
            if (random == 8)
                spawnTank();
            else
                spawnWitch();
        }
    }

    public void spawnTank() {
        Location[] bossLoc = map.getScenes().get(sceneIndex).bossLocations();
        if (bossLoc.length > 0) {
            LivingEntity spawnedMob = spawnMob(new Tank(map.getWorld(), bossLoc[ThreadLocalRandom.current().nextInt(0, bossLoc.length)]));
            spawnedMob.setRemoveWhenFarAway(false);
        }
    }

    public void spawnWitch() {
        Location[] bossLoc = map.getScenes().get(sceneIndex).bossLocations();
        if (bossLoc.length > 0) {
            LivingEntity spawnedMob = spawnMob(new Witch(map.getWorld(), bossLoc[ThreadLocalRandom.current().nextInt(0, bossLoc.length)]));
            spawnedMob.setRemoveWhenFarAway(false);
        }
    }
}
