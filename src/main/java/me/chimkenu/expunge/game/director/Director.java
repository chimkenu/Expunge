package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Utilities;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.guns.ShootEvent;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.special.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Director extends BukkitRunnable implements Listener {
    private final LocalGameManager localGameManager;
    private final ItemHandler itemHandler;
    private final MobHandler mobHandler;
    private final StatsHandler statsHandler;

    private long sceneTime = 0;
    private int sceneAttempts = 0;
    public boolean chillOut = false;
    public boolean forceChillOut = false;
    public long timeSinceLastHorde = 0;

    public Director(LocalGameManager localGameManager) {
        this.localGameManager = localGameManager;
        itemHandler = new ItemHandler(this);
        mobHandler = new MobHandler(this);
        statsHandler = new StatsHandler(this);
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

    private boolean isDoingGood() {
        return calculateRating() > 10 + ((difficulty + 1) * 0.25);
    }

    public void bile(LivingEntity target, double radius) {
        if (Expunge.isSpawningEnabled && mobHandler.getActiveMobs().size() < 15) {
            new BukkitRunnable() {
                int i = 6;
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        mobHandler.spawnAdditionalMob();
                    }
                    i--;
                    if (i <= 0) this.cancel();
                    else if (!Expunge.isSpawningEnabled) this.cancel();
                }
            }.runTaskTimer(Expunge.instance, 0, 20 * 5);
        }
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
        }.runTaskTimer(Expunge.instance, 0, 20);
    }

    @Override
    public void run() {
        gameTime++;
        sceneTime++;

        if (Expunge.isSpawningEnabled && !forceChillOut) {
            // spawning based on difficulty
            if (mobHandler.getActiveMobs().size() < (difficulty + 1) * 25 || isDoingGood()) {
                if (difficulty < 1 && (sceneTime % (20 * 10)) == 0)
                    mobHandler.spawnAdditionalMob();
                else if (difficulty == 1 && (sceneTime % (20 * 7)) == 0)
                    mobHandler.spawnAdditionalMob();
                else if ((sceneTime % (20 * 5)) == 0)
                    mobHandler.spawnAdditionalMob();
            }

            if ((sceneTime % (20 * 15)) == 0 || (isDoingGood() && (sceneTime % (20 * 5)) == 0)) {
                double r = Math.random();
                if (r < 0.16 && !mobHandler.activeMobsContains(Rider.class))
                    mobHandler.getActiveMobs().add(new Rider(world, mobHandler.getRandomSpawnLocation()));
                else if (r < 0.32 && !mobHandler.activeMobsContains(Spewer.class))
                    mobHandler.getActiveMobs().add(new Spewer(world, mobHandler.getRandomSpawnLocation()));
                else if (r < 0.48 && !mobHandler.activeMobsContains(Charger.class))
                    mobHandler.getActiveMobs().add(new Charger(world, mobHandler.getRandomSpawnLocation()));
                else if (r < 0.64 && !mobHandler.activeMobsContains(Pouncer.class))
                    mobHandler.getActiveMobs().add(new Pouncer(world, mobHandler.getRandomSpawnLocation()));
                else if (r < 0.86 && !mobHandler.activeMobsContains(Choker.class))
                    mobHandler.getActiveMobs().add(new Choker(world, mobHandler.getRandomSpawnLocation()));
                else if (!mobHandler.activeMobsContains(Spitter.class))
                    mobHandler.getActiveMobs().add(new Spitter(world, mobHandler.getRandomSpawnLocation()));
            }

            // spawn additional mobs if number of mobs are too low
            if (!chillOut && mobHandler.getActiveMobs().size() < 5 && sceneTime > 20 * 20) {
                chillOut = true;
                timeSinceLastHorde = sceneTime;
                for (int i = 0; i < (20 + (Expunge.playing.getKeys().size() * 5)); i++) {
                    mobHandler.spawnAdditionalMob();
                }
            }
        } else if (chillOut && sceneTime - timeSinceLastHorde > 20 * 30) chillOut = false;

        // look through every mob if its alive && look at its distance from the nearest player
        if ((sceneTime % (20 * 5)) == 0) {
            HashSet<GameMob> mobsToRemove = new HashSet<>();
            for (GameMob mob : mobHandler.getActiveMobs()) {
                if (mob.getMob().isDead()) {
                    mobsToRemove.add(mob);
                    continue;
                }
                if (mob instanceof Zombie zombie && zombie.getTarget() != null && zombie.getTarget() instanceof Player target && Expunge.playing.getKeys().contains(target)) {
                    if (zombie.getLocation().distanceSquared(target.getLocation()) < 20 * 20) continue;
                }
                if (playerNearestDistanceFrom(mob.getMob().getLocation().toVector()) > 20 * 20) {
                    mob.remove();
                    mobsToRemove.add(mob);
                }
            }
            mobHandler.getActiveMobs().removeAll(mobsToRemove);
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
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=KNOCKED]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=ITEM]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=AMMO_PILE]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=THROWN_BILE]");
        for (GameMob mob : mobHandler.getActiveMobs()) {
            mob.remove();
        }
        mobHandler.getActiveMobs().clear();
        // usually a restart - update forceChillOut
        forceChillOut = false;
    }

    public void spawnStartingMobs() {
        // MOB CALCULATION: STARTING HORDE IS DEFINED BY THE FOLLOWING EQUATION:
        // 6 + sd + 2p - (a - (a / 5) % 5)
        // WHEREIN 's' IS THE SCENE INDEX, 'd' IS THE DIFFICULTY (0-2),
        // 'p' IS THE NUMBER OF PLAYERS, AND 'a' IS THE NUMBER OF SCENE ATTEMPTS
        int totalMobs = 6 + (sceneIndex * difficulty) + (2 * Expunge.playing.getKeys().size()) - ((sceneAttempts - (sceneAttempts % 5)) / 5);
        if (totalMobs <= 0) {
            totalMobs = 1;
        }

        // distribute throughout first 3 paths
        BoundingBox[] path = Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex).pathRegions();
        totalMobs = totalMobs / 3;

        for (int i = 0; i < Math.min(path.length, 3); i++) {
            mobHandler.spawnAtRandomLocations(path[i], totalMobs);
        }
    }

    public void generateItems() {
        CampaignMap scene = map.getMaps().get(sceneIndex);

        int itemsToSpawn = scene.baseItemsToSpawn();
        itemsToSpawn += (int) (4 * (1 - calculateRating()));
        for (int i = 0; i < itemsToSpawn; i++) {
            double r = Math.random();

            // throwable - 50% chance
            if (r < 0.5) {
                Utilities.Throwables[] throwables = Utilities.Throwables.values();
                itemHandler.spawnUtilityAtRandom(throwables[ThreadLocalRandom.current().nextInt(0, throwables.length)].getUtility());
            }

            // healing item - 50% chance
            else {
                Utilities.Healings[] healings = new Utilities.Healings[2];
                healings[0] = Utilities.Healings.PILLS;
                healings[1] = Utilities.Healings.ADRENALINE;
                itemHandler.spawnUtilityAtRandom(healings[ThreadLocalRandom.current().nextInt(0, healings.length)].getUtility());
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
                itemHandler.spawnGunAtRandom(primary.getGun());
            }

            // melee - 40% chance
            else {
                Weapons.Melees[] melees = Weapons.Melees.values();
                Weapons.Melees melee = melees[ThreadLocalRandom.current().nextInt(0, melees.length)];
                itemHandler.spawnMeleeAtRandom(melee.getMelee());
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

    public int getActiveMobs() {
        return mobHandler.getActiveMobs().size();
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
            skillAverage += statsHandler.calculateRating(p);
        }

        double mobsOnPlayer = 0;
        for (GameMob mob : mobHandler.getActiveMobs()) {
            if (mob.getMob().getTarget() instanceof Player player && mob.getMob().getLocation().distanceSquared(player.getLocation()) < 4) mobsOnPlayer++;
        }

        totalHealth = totalHealth / (20 * Expunge.playing.getKeys().size());
        skillAverage = skillAverage / Expunge.playing.getKeys().size();
        mobsOnPlayer = 1 - (mobsOnPlayer / mobHandler.getActiveMobs().size());

        return (totalHealth * 0.4) + (skillAverage * 0.4) + (mobsOnPlayer * 0.2);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        GameMob mobToRemove = null;
        for (GameMob mob : mobHandler.getActiveMobs()) {
            if (mob.getMob().equals(dead)) {
                if (dead.getKiller() != null && Expunge.playing.getKeys().contains(dead.getKiller())) {
                    statsHandler.kills.putIfAbsent(dead.getKiller(), 0);
                    statsHandler.kills.put(dead.getKiller(), statsHandler.kills.get(dead.getKiller()) + 1);

                    // broadcast if player killed special infected
                    if (!(dead instanceof Zombie))
                        Bukkit.broadcastMessage(ChatColor.RED + dead.getKiller().getDisplayName() + " killed " + mob.getMob().getName());
                }
                mobToRemove = mob;
                break;
            }
        }
        mobHandler.getActiveMobs().remove(mobToRemove);
    }

    @EventHandler
    public void onShoot(ShootEvent e) {
        statsHandler.shots.putIfAbsent(e.getShooter(), new Integer[]{0, 0, 0});
        Set<LivingEntity> hitEntities = e.getHitEntities().keySet();

        statsHandler.shots.get(e.getShooter())[0] += 1; // shot
        hitEntities.removeIf(entity -> entity instanceof Player);
        statsHandler.shots.get(e.getShooter())[1] += hitEntities.size() > 0 ? 1 : 0; // hit / miss
        hitEntities.removeIf(entity -> !e.getHitEntities().get(entity));
        statsHandler.shots.get(e.getShooter())[2] += hitEntities.size() > 0 ? 1 : 0; // headshot / not
    }
}
