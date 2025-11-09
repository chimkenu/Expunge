package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.game.GameManager;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.stream.Stream;

public interface Path {
    default boolean isWithin(Stream<Survivor> survivors) {
        return survivors.anyMatch(this::isWithin);
    }
    boolean isWithin(Survivor survivor);
    void showPath(World world);
    Set<Block> spawnBlocks(World world, Set<Survivor> survivors);
}
