package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.bukkit.Particle;
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
    public boolean isWithin(Player player) {
        return box.contains(player.getLocation().toVector());
    }

    @Override
    public void showPath(GameManager manager) {
        for (var v : showPathVectors) {
            manager.getWorld().spawnParticle(Particle.END_ROD, v.toLocation(manager.getWorld()).add(0, 0.2, 0), 1, 0, 0, 0, 0);
        }
    }

    @Override
    public Set<Block> spawnBlocks(GameManager manager) {
        Set<Block> blocks = new HashSet<>();
        for (int x = (int) Math.floor(box.getMinX()); x < box.getMaxX(); x++) {
            for (int z = (int) Math.floor(box.getMinZ()); z < box.getMaxZ(); z++) {
                blocks.addAll(SpawnUtil.getSurroundingBlocks(manager.getWorld().getBlockAt(x, (int) box.getMinY(), z), 0, 4));
            }
        }
        SpawnUtil.filterBlocks(manager, blocks);
        return blocks;
    }
}
