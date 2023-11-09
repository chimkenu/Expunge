package me.chimkenu.expunge;

import me.chimkenu.expunge.game.director.Director;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface GameAction {
    void run(JavaPlugin plugin, Director director, Player player);
}
