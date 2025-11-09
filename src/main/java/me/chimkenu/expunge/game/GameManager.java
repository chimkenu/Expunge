package me.chimkenu.expunge.game;

import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.Infected;
import me.chimkenu.expunge.entities.MobType;
import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.Set;

public interface GameManager {
    JavaPlugin getPlugin();
    World getWorld();

    Set<Player> getPlayers();
    Optional<Survivor> getSurvivor(Player player);

    void start();
    void stop(boolean isAbrupt);
    void restart();
    boolean isRunning();

    void addListener(Listener listener);
    void addTask(BukkitTask task);

    GameEntity spawnItem(GameItem item, Vector location, boolean isInvulnerable);
    default GameEntity spawnItem(GameItem item, Vector location) {
        return spawnItem(item, location, false);
    }

    Infected spawnInfected(MobType type, Class<? extends Mob> mob, Vector location);
    default Infected spawnInfected(MobType type, Vector location) {
        return spawnInfected(type, type.defaultMob(), location);
    }

    void spawnMobNaturally(MobType type, Class<? extends Mob> mob);
    default void spawnMobNaturally(MobType type) {
        spawnMobNaturally(type, type.defaultMob());
    }

    void addEntity(GameEntity entity);
    default Optional<GameEntity> getEntity(Entity entity) {
        return getEntities().stream().filter(e -> e.getHandle().equals(entity)).findAny();
    }

    Set<GameEntity> getEntities();
    Set<Survivor> getSurvivors();
    Set<Infected> getInfected();
}
