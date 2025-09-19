package me.chimkenu.expunge;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.entity.Player;

public interface GameAction {
    void run(GameManager gameManager, Player player);
}
