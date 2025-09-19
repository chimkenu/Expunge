package me.chimkenu.expunge.utils;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpawnUtil {
    public static Set<Block> getValidSurroundingBlocks(GameManager manager, List<Vector> path, Set<Player> players) {
        final int SPAWN_RADIUS = manager.getPlugin().getConfig().getInt("director.spawn-radius");
        final int TOO_CLOSE_RADIUS = manager.getPlugin().getConfig().getInt("director.too-close-radius");
        final int DEPTH = manager.getPlugin().getConfig().getInt("director.depth");

        Set<Block> blocks = new HashSet<>();

        // Gather all the possible spawn locations
        var world = manager.getWorld();
        for (var v : path) {
            for (Block b : getSurroundingBlocks(world.getBlockAt(v.toLocation(world)), SPAWN_RADIUS, DEPTH)) {

                var isValid = true;
                for (var p : players) {
                    if (p.getGameMode() != GameMode.ADVENTURE) {
                        continue;
                    }

                    if (blocks.contains(b) || isLocationTooClose(p, b.getLocation(), TOO_CLOSE_RADIUS) || canBeSeenByPlayer(b, p)) {
                        isValid = false;
                        break;
                    }
                }

                if (isValid) {
                    blocks.add(b);
                }
            }
        }

        return blocks;
    }

    private static boolean canBeSeenByPlayer(Block block, Player player) {
        final Vector playerToBlock = block.getLocation().toVector().subtract(player.getEyeLocation().toVector());
        final double maxAngle = 60 * Math.PI / 180;
        final double accuracy = 0.5;

        if (playerToBlock.angle(player.getEyeLocation().getDirection()) > maxAngle)
            return false;

        // Ray cast
        Vector target = block.getLocation().toVector().add(new Vector(0.5, 2.5, 0.5));
        RayTrace ray = new RayTrace(player.getEyeLocation().toVector(), target.clone().subtract(player.getEyeLocation().toVector()).normalize());
        for (Vector v : ray.traverse(playerToBlock.length(), accuracy)) {
            if (v.distanceSquared(player.getEyeLocation().toVector()) > target.distanceSquared(player.getEyeLocation().toVector()))
                continue;
            if (!player.getWorld().getBlockAt(v.toLocation(player.getWorld())).isPassable()) {
                return false;
            }
        }
        return true;
    }

    private static Set<Block> getSurroundingBlocks(Block block, int radius, int depth) {
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

    private static boolean isLocationTooClose(Player p, Location l, int distance) {
        return p.getLocation().distanceSquared(l) < distance * distance;
    }

    public static double playerNearestDistanceFrom(Collection<Player> players, Vector vector) {
        double nearestDistance = Double.MAX_VALUE;
        for (var p : players) {
            nearestDistance = Math.min(nearestDistance, p.getLocation().toVector().distanceSquared(vector));
        }
        return nearestDistance;
    }
}
