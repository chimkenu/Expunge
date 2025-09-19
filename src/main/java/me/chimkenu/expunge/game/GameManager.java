package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Expunge;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import java.util.Set;

public interface GameManager {
    Expunge getPlugin();
    World getWorld();
    Director getDirector();

    GameState getState();
    Set<Player> getPlayers();
    PlayerStats getPlayerStat(Player player);

    void start();
    void stop(boolean isAbrupt);
    boolean isRunning();

    void addListener(Listener listener);
    void addTask(BukkitTask task);

    void addEntity(Entity entity);
    Set<Entity> getEntities();
}
