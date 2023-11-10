package me.chimkenu.expunge;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public interface GameAction {
    void run(JavaPlugin plugin, GameManager gameManager, Player player);
}
