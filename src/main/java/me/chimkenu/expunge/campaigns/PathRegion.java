package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record PathRegion(
        BoundingBox box,
        List<Vector> showPathVectors
) implements Path {
    @Override
    public boolean isWithin(Survivor survivor) {
        return box.contains(survivor.getLocation().toVector());
    }

    @Override
    public void showPath(World world) {
        for (var v : showPathVectors) {
            world.spawnParticle(Particle.END_ROD, v.toLocation(world).add(0, 0.2, 0), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public Set<Block> spawnBlocks(World world, Set<Survivor> survivors) {
        Set<Block> blocks = new HashSet<>();
        for (int x = (int) Math.floor(box.getMinX()); x < box.getMaxX(); x++) {
            for (int z = (int) Math.floor(box.getMinZ()); z < box.getMaxZ(); z++) {
                blocks.addAll(SpawnUtil.getSurroundingBlocks(world.getBlockAt(x, (int) box.getMinY(), z), 0, 4));
            }
        }
        SpawnUtil.filterBlocks(survivors, blocks);
        return blocks;
    }
}
