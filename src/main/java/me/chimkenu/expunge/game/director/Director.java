package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.ShootEvent;
import me.chimkenu.expunge.mobs.GameMob;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
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
        mobHandler.run(sceneTime, gameManager.getDifficulty());

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
            mobHandler.spawnHorde(getDifficulty());
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
        mobHandler.clear();
        itemHandler.clear();
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
                    // broadcast if player killed special infected
                    if (!(dead instanceof Zombie) || (dead instanceof ZombieVillager || dead instanceof Husk)) {
                        statsHandler.addSpecialKill(dead.getKiller());
                        gameManager.getWorld().getPlayers().forEach(player -> player.sendMessage(dead.getKiller().name().color(NamedTextColor.RED).append(Component.text(" killed ", NamedTextColor.RED)).append(mob.getMob().name()).color(NamedTextColor.RED)));
                    } else {
                        statsHandler.addCommonKill(dead.getKiller());
                    }
                }
                mobToRemove = mob;
                break;
            }
        }
        mobHandler.getActiveMobs().remove(mobToRemove);
    }

    @EventHandler
    public void onShoot(ShootEvent e) {
        boolean shotHit = false;
        boolean headshot = false;
        for (LivingEntity hit : e.getHitEntities().keySet()) {
            if (!(hit instanceof Player)) {
                shotHit = true;
                if (e.getHitEntities().get(hit)) {
                    headshot = true;
                }
            }
        }

        statsHandler.addShot(e.getShooter(), shotHit, headshot);
    }
}
