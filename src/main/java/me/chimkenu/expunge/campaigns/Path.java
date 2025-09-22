package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Stream;

public interface Path {
    default boolean isWithin(Stream<Player> players) {
        return players.anyMatch(this::isWithin);
    }
    boolean isWithin(Player player);
    void showPath(GameManager manager);
    Set<Block> spawnBlocks(GameManager manager);
}
