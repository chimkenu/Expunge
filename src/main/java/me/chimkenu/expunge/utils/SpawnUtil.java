package me.chimkenu.expunge.utils;

import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.game.GameManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpawnUtil {
    public static final int SPAWN_RADIUS = 8;
    public static final int TOO_CLOSE_RADIUS = 8;
    public static final int DEPTH = 2;

    public static Set<Block> getValidSurroundingBlocks(World world, Set<Survivor> survivors, List<Vector> path) {
        Set<Block> blocks = new HashSet<>();

        // Gather all the possible spawn locations
        for (var v : path) {
            blocks.addAll(getSurroundingBlocks(world.getBlockAt(v.toLocation(world)), SPAWN_RADIUS, DEPTH));
        }

        filterBlocks(survivors, blocks);
        return blocks;
    }

    public static void filterBlocks(Set<Survivor> survivors, Set<Block> blocks) {
        // Filter blocks that cannot be used (assumes all blocks are already valid, just checking visibility)
        Set<Block> blacklist = new HashSet<>();
        for (Block b : blocks) {
            for (var s : survivors) {
                if (!s.isAlive()) {
                    continue;
                }
                if (isLocationTooClose(s.getLocation(), b.getLocation(), TOO_CLOSE_RADIUS) || canBeSeenByPlayer(b, s)) {
                    blacklist.add(b);
                    break;
                }
            }
        }

        blocks.removeAll(blacklist);
    }

    private static boolean canBeSeenByPlayer(Block block, Survivor survivor) {
        var eyeLoc = survivor.getEyeLocation();
        final Vector playerToBlock = block.getLocation().toVector().subtract(eyeLoc.toVector());
        final double maxAngle = 60 * Math.PI / 180;
        final double accuracy = 0.5;

        if (playerToBlock.angle(eyeLoc.getDirection()) > maxAngle)
            return false;

        // Ray cast
        Vector target = block.getLocation().toVector().add(new Vector(0.5, 2.5, 0.5));
        RayTrace ray = new RayTrace(eyeLoc.toVector(), target.clone().subtract(eyeLoc.toVector()).normalize());
        for (Vector v : ray.traverse(playerToBlock.length(), accuracy)) {
            if (v.distanceSquared(eyeLoc.toVector()) > target.distanceSquared(eyeLoc.toVector()))
                continue;
            if (!survivor.getHandle().getWorld().getBlockAt(v.toLocation(survivor.getHandle().getWorld())).isPassable()) {
                return false;
            }
        }
        return true;
    }

    public static Set<Block> getSurroundingBlocks(Block block, int radius, int depth) {
        Set<Block> blocks = new HashSet<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block testBlock = block.getWorld().getBlockAt(block.getLocation().add(x, 0, z));
                testBlock = searchDepth(testBlock, depth);

                if (testBlock.getType() == Material.AIR) {
                    continue;
                }

                if (testBlock.getRelative(0, 1, 0).getType() != Material.AIR) {
                    continue;
                }

                if (testBlock.getRelative(0, 2, 0).getType() != Material.AIR) {
                    continue;
                }

                blocks.add(testBlock);
            }
        }
        return blocks;
    }

    private static Block searchDepth(Block block, int depth) {
        for (int h = -depth; h <= depth; h++) {
            Block search = block.getRelative(0, h, 0);
            if (search.getType() != Material.AIR && search.getRelative(0, 1, 0).getType() == Material.AIR)
                return search;
        }
        return block;
    }

    private static boolean isLocationTooClose(Location from, Location to, int distance) {
        return from.distanceSquared(to) < distance * distance;
    }

    public static double nearestDistanceFrom(Collection<Location> locations, Location target) {
        double nearestDistance = Double.MAX_VALUE;
        for (var loc : locations) {
            nearestDistance = Math.min(nearestDistance, loc.distanceSquared(target));
        }
        return nearestDistance;
    }
}
