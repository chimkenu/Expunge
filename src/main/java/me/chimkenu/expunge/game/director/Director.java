package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.ShootEvent;
import me.chimkenu.expunge.mobs.GameMob;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class Director implements Listener {
    private final GameManager gameManager;
    private final ItemHandler itemHandler;
    private final MobHandler mobHandler;
    private final StatsHandler statsHandler;

    private int sceneTime = 0;
    private int sceneAttempts = 0;

    public Director(JavaPlugin plugin, GameManager gameManager) {
        this.gameManager = gameManager;
        itemHandler = new ItemHandler(this);
        mobHandler = new MobHandler(plugin, this);
        statsHandler = new StatsHandler(this);
    }

    public void run() {
        sceneTime++;
        mobHandler.run(sceneTime, sceneAttempts, gameManager.getDifficulty());

        // players
        for (Player p : gameManager.getPlayers()) {
            // decrease absorption points & kill down players
            if (p.getAbsorptionAmount() > 0) {
                if ((sceneTime % (20 * 15)) == 0) p.setAbsorptionAmount(Math.max(0, p.getAbsorptionAmount() - 1));
            }
            if (!gameManager.getPlayerStat(p).isAlive()) {
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
            else if (p.getFoodLevel() < 20) p.setFoodLevel(20);
        }
    }

    public void bile(JavaPlugin plugin, LivingEntity target, double radius) {
        if (mobHandler.isSpawningEnabled() && mobHandler.getActiveMobs().size() < 15) {
            new BukkitRunnable() {
                int i = 6;
                @Override
                public void run() {
                    for (int j = 0; j < 5; j++) {
                        mobHandler.spawnAdditionalInfected(getDifficulty(), 1);
                    }
                    i--;
                    if (i <= 0) this.cancel();
                    else if (!mobHandler.isSpawningEnabled()) this.cancel();
                }
            }.runTaskTimer(plugin, 0, 20 * 5);
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
        }.runTaskTimer(plugin, 0, 20);
    }

    public void clearEntities() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=RESPAWN_ARMOR_STAND]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=KNOCKED]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=ITEM]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=AMMO_PILE]");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[tag=THROWN_BILE]");
        mobHandler.clearMobs();
    }

    public Component displayStats() {
        return statsHandler.displayStats();
    }

    public void incrementSceneAttempts() {
        sceneAttempts++;
        sceneTime = 0;
    }

    public void resetSceneAttempts() {
        sceneAttempts = 0;
        sceneTime = 0;
    }

    public double calculateRating() {
        double totalHealth = 0;
        double skillAverage = 0;

        for (Player p : gameManager.getPlayers()) {
            // Health
            double estimateHealth = gameManager.getPlayerStat(p).isAlive() ? p.getHealth() + p.getAbsorptionAmount() : 0;
            if (p.getInventory().contains(Material.BRICK)) estimateHealth += (20 - estimateHealth) * 0.8;
            estimateHealth = Math.min(estimateHealth, 20);
            totalHealth += estimateHealth;
            skillAverage += statsHandler.calculateRating(p);
        }

        if (mobHandler.isSpawningEnabled()) {
            double mobsOnPlayer = 0;
            for (GameMob mob : mobHandler.getActiveMobs()) {
                if (mob.getMob().getTarget() instanceof Player player && mob.getMob().getLocation().distanceSquared(player.getLocation()) < 8)
                    mobsOnPlayer++;
            }

            totalHealth = totalHealth / (20 * gameManager.getPlayers().size());
            skillAverage = skillAverage / gameManager.getPlayers().size();
            mobsOnPlayer = 1 - (mobsOnPlayer / mobHandler.getActiveMobs().size());
            return (totalHealth * 0.2) + (skillAverage * 0.45) + (mobsOnPlayer * 0.35);
        }
        return totalHealth * 0.5 + skillAverage * 0.5;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    protected CampaignMap getMap() {
        return gameManager.getMap();
    }

    protected World getWorld() {
        return gameManager.getWorld();
    }

    protected Set<Player> getPlayers() {
        return gameManager.getPlayers();
    }

    protected Difficulty getDifficulty() {
        return gameManager.getDifficulty();
    }

    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    public MobHandler getMobHandler() {
        return mobHandler;
    }

    public StatsHandler getStatsHandler() {
        return statsHandler;
    }

    public int getSceneTime() {
        return sceneTime;
    }

    public int getSceneAttempts() {
        return sceneAttempts;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMobDeath(EntityDeathEvent e) {
        LivingEntity dead = e.getEntity();
        GameMob mobToRemove = null;
        if (dead.getKiller() == null || !gameManager.getPlayers().contains(dead.getKiller())) return;
        for (GameMob mob : mobHandler.getActiveMobs()) {
            if (mob.getMob().equals(dead)) {
                if (gameManager.getPlayers().contains(dead.getKiller())) {
                    statsHandler.kills.putIfAbsent(dead.getKiller(), 0);
                    statsHandler.kills.put(dead.getKiller(), statsHandler.kills.get(dead.getKiller()) + 1);

                    // broadcast if player killed special infected
                    if (!(dead instanceof Zombie))
                        gameManager.getWorld().getPlayers().forEach(player -> player.sendMessage(dead.getKiller().name().color(NamedTextColor.RED).append(Component.text(" killed ", NamedTextColor.RED)).append(mob.getMob().name()).color(NamedTextColor.RED)));
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
        statsHandler.shots.get(e.getShooter())[1] += !hitEntities.isEmpty() ? 1 : 0; // hit / miss
        hitEntities.removeIf(entity -> !e.getHitEntities().get(entity));
        statsHandler.shots.get(e.getShooter())[2] += !hitEntities.isEmpty() ? 1 : 0; // headshot / not
    }
}
