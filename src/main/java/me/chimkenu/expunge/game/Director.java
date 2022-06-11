package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.enums.Utilities;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.game.maps.Map;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.guns.ShootEvent;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import me.chimkenu.expunge.guns.utilities.Utility;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Director extends BukkitRunnable implements Listener {
    private long gameTime = 0;
    private long sceneTime = 0;
    private int sceneAttempts = 0;
    private final HashMap<Player, Integer> kills = new HashMap<>();
    private final HashMap<Player, Integer[]> shots = new HashMap<>();
    public final HashSet<LivingEntity> activeMobs = new HashSet<>();
    public boolean chillOut = false;
    public long timeSinceLastHorde = 0;

    public <T extends LivingEntity> LivingEntity spawnMob(World world, Location loc, Class<T> mob) {
        LivingEntity spawnedMob = world.spawn(loc, mob);
        spawnedMob.addScoreboardTag("MOB");
        activeMobs.add(spawnedMob);
        return spawnedMob;
    }

    private <T extends LivingEntity> void spawnAdditionalMob(Map map, int sceneIndex, Class<T> mob) {
        // get a random player
        Player player = Expunge.playing.getKeys().get(ThreadLocalRandom.current().nextInt(0, Expunge.playing.getKeys().size()));

        // sort locations based on distance to player
        ArrayList<Location> spawnLocations = map.getScenes().get(sceneIndex).spawnLocations();
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

                Location loc = spawnLocations.get(j);
                spawnLocations.set(j, spawnLocations.get(j - 1));
                spawnLocations.set(j - 1, loc);

                j--;
            }
        }

        // spawn at random nearest
        LivingEntity spawnedMob = spawnMob(map.getWorld(), spawnLocations.get(ThreadLocalRandom.current().nextInt(0, Math.min(3, spawnLocations.size()))), mob);
        spawnedMob.addScoreboardTag("HORDE");
        spawnedMob.setRemoveWhenFarAway(false);
    }

    private <T extends LivingEntity> void spawnBossMob(Map map, int sceneIndex, Class<T> mob) {
        ArrayList<Location> spawnLocations = map.getScenes().get(sceneIndex).bossLocations();
        Random random = new Random();
        Location loc = spawnLocations.get(random.nextInt(spawnLocations.size()));
        LivingEntity spawnedMob = spawnMob(map.getWorld(), loc, mob);
        spawnedMob.addScoreboardTag("BOSS");
    }

    public void spawnAtRandomLocations(World world, BoundingBox b, int numToSpawn, boolean hasWandererTag) {
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

            Zombie zombie = (Zombie) spawnMob(world, loc, Zombie.class);
            if (hasWandererTag) zombie.addScoreboardTag("WANDERER");
            else zombie.addScoreboardTag("WANDERER?");
            activeMobs.add(zombie);
        }
    }

    public static void spawnWeapon(World world, Location loc, Gun gun, boolean isInvulnerable) {
        Item item = world.dropItem(loc, gun.getWeapon());
        if (isInvulnerable) {
            ItemMeta meta = item.getItemStack().getItemMeta();
            if (meta != null && meta.getLore() != null) {
                meta.getLore().add("invulnerable");
            }
        }
        item.addScoreboardTag("ITEM");
        item.setInvulnerable(isInvulnerable);
    }

    private void spawnGunAtRandom(Map map, int sceneIndex, Gun gun) {
        Scene scene = map.getScenes().get(sceneIndex);
        ArrayList<Location> weaponLocations = scene.weaponLocations();
        if (weaponLocations.size() < 1) return;
        int index = weaponLocations.size() == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.size());
        spawnWeapon(map.getWorld(), weaponLocations.get(index), gun, false);
    }

    public static void spawnWeapon(World world, Location loc, Melee melee, boolean isInvulnerable) {
        Item item = world.dropItem(loc, melee.getWeapon());
        item.addScoreboardTag("ITEM");
        item.setInvulnerable(isInvulnerable);
    }

    private void spawnMeleeAtRandom(Map map, int sceneIndex, Melee melee) {
        Scene scene = map.getScenes().get(sceneIndex);
        ArrayList<Location> weaponLocations = scene.weaponLocations();
        if (weaponLocations.size() < 1) return;
        int index = weaponLocations.size() == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.size());
        spawnWeapon(map.getWorld(), weaponLocations.get(index), melee, false);
    }

    public static void spawnUtility(World world, Location loc, Utility utility) {
        Item item = world.spawn(loc, Item.class);
        item.setItemStack(utility.getUtility());
        item.addScoreboardTag("ITEM");
    }

    private void spawnUtilityAtRandom(Map map, int sceneIndex, Utility utility) {
        Scene scene = map.getScenes().get(sceneIndex);
        ArrayList<Location> itemLocations = scene.itemLocations();
        if (itemLocations.size() < 1) return;
        int index = itemLocations.size() == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, itemLocations.size());
        spawnUtility(map.getWorld(), itemLocations.get(index), utility);
    }

    public static Melee getRandomMelee() {
        ArrayList<Weapons.Melees> melees = new ArrayList<>(List.of(Weapons.Melees.values()));
        melees.remove(Weapons.Melees.CHAINSAW);
        return melees.get(ThreadLocalRandom.current().nextInt(0, melees.size())).getMelee();
    }

    public static Gun getRandomGun(Weapons.Tier tier) {
        switch (tier) {
            case TIER1 -> {
                return Utils.getTier1Guns().get(ThreadLocalRandom.current().nextInt(0, Utils.getTier1Guns().size()));
            }
            case TIER2 -> {
                return Utils.getTier2Guns().get(ThreadLocalRandom.current().nextInt(0, Utils.getTier2Guns().size()));
            }
            default -> {
                return Utils.getSpecialGuns().get(ThreadLocalRandom.current().nextInt(0, Utils.getSpecialGuns().size()));
            }
        }
    }

    private double playerNearestDistanceFrom(Vector loc) {
        double nearestDistance = 0;
        for (Player p : Expunge.playing.getKeys()) {
            double distance = p.getLocation().toVector().distanceSquared(loc);
            if (distance < nearestDistance) {
                nearestDistance = distance;
            }
        }
        return nearestDistance;
    }

    @Override
    public void run() {
        gameTime++;
        sceneTime++;

        if (Expunge.isSpawningEnabled && !chillOut) {
            // spawning based on difficulty
            if (activeMobs.size() < (Expunge.difficulty + 1) * 15) {
                if (Expunge.difficulty < 1 && (sceneTime % (20 * 10)) == 0)
                    spawnAdditionalMob(Expunge.currentMap, Expunge.currentSceneIndex, Zombie.class);
                else if (Expunge.difficulty == 1 && (sceneTime % (20 * 5)) == 0)
                    spawnAdditionalMob(Expunge.currentMap, Expunge.currentSceneIndex, Zombie.class);
                else if ((sceneTime % (20 * 15)) == 0)
                    spawnAdditionalMob(Expunge.currentMap, Expunge.currentSceneIndex, Zombie.class);

                // spawn additional mobs if number of mobs are too low
                if (activeMobs.size() < 10 && sceneTime > 30 * 10) {
                    chillOut = true;
                    timeSinceLastHorde = sceneTime;
                    for (int i = 0; i < (20 + (Expunge.playing.getKeys().size() * 5)); i++) {
                        spawnAdditionalMob(Expunge.currentMap, Expunge.currentSceneIndex, Zombie.class);
                    }
                }
            }
        } else if (chillOut && activeMobs.size() <= 5 && sceneTime - timeSinceLastHorde > 20 * 30) chillOut = false;

        // look through every mob if its alive && look at its distance from the nearest player
        if ((sceneTime % (20 * 5)) == 0) {
            HashSet<LivingEntity> mobsToRemove = new HashSet<>();
            for (LivingEntity mob : activeMobs) {
                if (mob.isDead()) {
                    mobsToRemove.add(mob);
                    continue;
                }
                if (mob instanceof Zombie zombie && zombie.getTarget() != null && zombie.getTarget() instanceof Player target && Expunge.playing.getKeys().contains(target)) {
                    if (zombie.getLocation().distanceSquared(target.getLocation()) < 20 * 20)
                    continue;
                }
                if (playerNearestDistanceFrom(mob.getLocation().toVector()) > 20 * 20) {
                    mob.remove();
                    mobsToRemove.add(mob);
                }
            }
            activeMobs.removeAll(mobsToRemove);
        }

        // players
        for (Player p : Expunge.playing.getKeys()) {
            // decrease absorption points & kill down players
            if (p.getAbsorptionAmount() > 0) {
                if ((sceneTime % (20 * 15)) == 0) p.setAbsorptionAmount(Math.max(0, p.getAbsorptionAmount() - 1));
            }
            if (!Expunge.playing.isAlive(p)) {
                if ((sceneTime % (20 * 10)) == 0) {
                    p.damage(1);
                }
            }

            // give slow to low players
            if (p.getHealth() + p.getAbsorptionAmount() <= 6) {
                p.setFoodLevel(6);
                if (p.getHealth() + p.getAbsorptionAmount() <= 1)
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2, 0, false, false, true));
            }
            else if (p.getFoodLevel() < 20)
                p.setFoodLevel(20);
        }

        if (this != Expunge.runningDirector || !Expunge.isGameRunning) {
            this.cancel();
        }
    }

    public void clearEntities() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=RESPAWN_ARMOR_STAND]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=ITEM]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=AMMO_PILE]");
        for (LivingEntity entity : activeMobs) {
            entity.remove();
        }
        activeMobs.clear();
    }

    private int getPlayerProgress(Player player) {
        Scene scene = Expunge.currentMap.getScenes().get(Expunge.currentSceneIndex);
        Vector v = player.getLocation().toVector();
        int nearest = 0;
        double nearestDistance = v.distanceSquared(scene.pathRegions().get(nearest).getCenter());
        for (int i = 0; i < scene.pathRegions().size(); i++) {
            BoundingBox b = scene.pathRegions().get(i);
            double distance = v.distanceSquared(b.getCenter());
            if (distance < nearestDistance) {
                nearest = i;
                nearestDistance = distance;
            }
        }
        return nearest;
    }

    public double calculateRating() {
        double totalHealth = 0;
        double skillAverage = 0;

        for (int i = 0; i < Expunge.playing.getKeys().size(); i++) {
            // health
            Player p = Expunge.playing.getKeys().get(i);
            double estimateHealth = p.getHealth();
            if (p.getInventory().contains(Material.BRICK)) {
                estimateHealth += (20 - estimateHealth) * 0.8;
            }
            estimateHealth += p.getAbsorptionAmount();
            if (!Expunge.playing.isAlive(p)) estimateHealth = 0;
            estimateHealth = Math.min(estimateHealth, 20);
            totalHealth += estimateHealth;

            // skill average
            skillAverage += calculateRating(p);
        }

        totalHealth = totalHealth / (20 * Expunge.playing.getKeys().size());
        skillAverage = skillAverage / Expunge.playing.getKeys().size();
        return (totalHealth * .5) + (skillAverage * .5);
    }

    public int getTotalKills() {
        int total = 0;
        for (Player player : kills.keySet()) {
            total += kills.get(player);
        }
        return total;
    }

    public int getKills(Player player) {
        if (kills.get(player) == null) return 0;
        return kills.get(player);
    }

    public double getAccuracy(Player player) {
        Integer[] shot = shots.get(player);
        if (shot == null) return 1;
        return (double) shot[1] / shot[0];
    }

    public double getHeadshotAccuracy(Player player) {
        Integer[] shot = shots.get(player);
        if (shot == null) return 1;
        return (double) shot[2] / shot[1];
    }

    public double getPace(Player player) {
        if (Expunge.playing.getKeys().size() > 1) {
            double progressAverage = 0;
            for (Player p : Expunge.playing.getKeys()) {
                progressAverage += getPlayerProgress(p);
            }
            progressAverage = progressAverage / (Expunge.playing.getKeys().size());
            return progressAverage - getPlayerProgress(player);
        }
        return 0;
    }

    public double calculateRating(Player player) {
        double rating = 0;

        // accuracy & headshot accuracy
        rating += getAccuracy(player) * 0.25;
        rating += getHeadshotAccuracy(player) * 0.25;

        // progress : are you rushing or are you dragging?
        double diff = Math.abs(getPace(player));
        diff = (1 - (1 / (1 + Math.exp(-diff)))) + 0.5;
        rating += diff * 0.5;

        return rating;
    }

    public void spawnStartingMobs() {
        World world = Expunge.currentMap.getWorld();

        // MOB CALCULATION: STARTING HORDE IS DEFINED BY THE FOLLOWING EQUATION:
        // 6 + sd + 2p - (a - (a / 5) % 5)
        // WHEREIN 's' IS THE SCENE INDEX, 'd' IS THE DIFFICULTY (0-2),
        // 'p' IS THE NUMBER OF PLAYERS, AND 'a' IS THE NUMBER OF SCENE ATTEMPTS
        int totalMobs = 6 + (Expunge.currentSceneIndex * Expunge.difficulty) + (2 * Expunge.playing.getKeys().size()) - ((sceneAttempts - (sceneAttempts % 5)) / 5);
        if (totalMobs <= 0) {
            totalMobs = 1;
        }

        // distribute throughout first 3 paths
        ArrayList<BoundingBox> path = Expunge.currentMap.getScenes().get(Expunge.currentSceneIndex).pathRegions();
        totalMobs = totalMobs / 3;

        for (int i = 0; i < Math.min(path.size(), 3); i++) {
            spawnAtRandomLocations(world, path.get(i), totalMobs, true);
        }
    }

    public void generateItems() {
        Scene scene = Expunge.currentMap.getScenes().get(Expunge.currentSceneIndex);

        int itemsToSpawn = scene.baseItemsToSpawn();
        itemsToSpawn += (int) (4 * (1 - calculateRating()));
        for (int i = 0; i < itemsToSpawn; i++) {
            double r = Math.random();

            // throwable - 40% chance
            if (r < 0.2) {
                Utilities.Throwables[] throwables = Utilities.Throwables.values();
                spawnUtilityAtRandom(Expunge.currentMap, Expunge.currentSceneIndex, throwables[ThreadLocalRandom.current().nextInt(0, throwables.length)].getUtility());
            }

            // healing item - 60% chance
            else {
                Utilities.Healings[] healings = new Utilities.Healings[2];
                healings[0] = Utilities.Healings.PILLS;
                healings[1] = Utilities.Healings.ADRENALINE;
                spawnUtilityAtRandom(Expunge.currentMap, Expunge.currentSceneIndex, healings[ThreadLocalRandom.current().nextInt(0, healings.length)].getUtility());
            }
        }

        itemsToSpawn = 1;
        itemsToSpawn += (int) (2 * (1 - calculateRating()));
        for (int i = 0; i < itemsToSpawn; i++) {
            double r = Math.random();

            // primary - 60% chance
            if (r < 0.6) {
                Weapons.Guns[] primaries = Weapons.Guns.values();
                Weapons.Guns primary = primaries[ThreadLocalRandom.current().nextInt(0, primaries.length)];
                spawnGunAtRandom(Expunge.currentMap, Expunge.currentSceneIndex, primary.getGun());
            }

            // melee - 40% chance
            else {
                Weapons.Melees[] melees = Weapons.Melees.values();
                Weapons.Melees melee = melees[ThreadLocalRandom.current().nextInt(0, melees.length)];
                spawnMeleeAtRandom(Expunge.currentMap, Expunge.currentSceneIndex, melee.getMelee());
            }
        }

        // spawn ammo
        for (Location loc : scene.ammoLocations()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in minecraft:overworld run summon falling_block " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " {BlockState:{Name:\"minecraft:gray_candle\",Properties:{candles:\"4\",lit:\"false\",waterlogged:\"false\"}},NoGravity:1b,Glowing:1b,Time:-2147483648,Tags:[\"AMMO_PILE\"],Invulnerable:1b,CustomName:'[{\"text\":\"Ammo Pile\",\"color\":\"blue\"},{\"text\":\" (Right Click)\",\"color\":\"gray\"}]',CustomNameVisible:1b}");
        }
    }

    public void incrementSceneAttempts() {
        sceneAttempts++;
        sceneTime = 0;
    }

    public void resetSceneAttempts() {
        sceneAttempts = 0;
        sceneTime = 0;
    }

    public long getGameTime() {
        return gameTime;
    }

    public long getSceneTime() {
        return sceneTime;
    }

    public int getSceneAttempts() {
        return sceneAttempts;
    }

    public HashSet<LivingEntity> getActiveMobs() {
        return activeMobs;
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        if (activeMobs.contains(dead)) {
            if (dead.getKiller() != null && Expunge.playing.getKeys().contains(dead.getKiller())) {
                kills.putIfAbsent(dead.getKiller(), 0);
                kills.put(dead.getKiller(), kills.get(dead.getKiller()) + 1);
            }
            activeMobs.remove(dead);
        }
    }

    @EventHandler
    public void onShoot(ShootEvent e) {
        shots.putIfAbsent(e.getShooter(), new Integer[]{0, 0, 0});
        Set<LivingEntity> hitEntities = e.getHitEntities().keySet();

        shots.get(e.getShooter())[0] += 1; // shot
        hitEntities.removeIf(entity -> entity instanceof Player);
        shots.get(e.getShooter())[1] += hitEntities.size() > 0 ? 1 : 0; // hit / miss
        hitEntities.removeIf(entity -> e.getHitEntities().get(entity));
        shots.get(e.getShooter())[2] += hitEntities.size() > 0 ? 1 : 0; // headshot / not
    }
}
