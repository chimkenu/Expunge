package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public TestCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        GameMode gameMode = player.getGameMode();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getGameMode() == gameMode) {
                    for (Block b : getValidSurroundingBlocks(Bukkit.getWorld("world").getPlayers())) {
                        b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 1.1, 0.5), 1, new Particle.DustOptions(Color.GREEN, 0.5f));
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    private Set<Block> getValidSurroundingBlocks(Collection<Player> player) {
        final int SPAWN_RADIUS = 30;
        final int TOO_CLOSE_RADIUS = 10;
        final int DEPTH = 2;

        Set<Block> blocks = new HashSet<>();
        Set<Block> tooClose = new HashSet<>();

        ArrayList<Player> players = new ArrayList<>();

        // Gather all the possible spawn locations
        for (Player p : player) {

            // Disregard player if they are close to another player
            boolean isTooClose = false;
            for (Player q : players) {
                if (isLocationTooClose(p, q.getLocation(), TOO_CLOSE_RADIUS)) {
                    isTooClose = true;
                }
            }
            if (isTooClose) continue;
            players.add(p);

            // Gather nearby valid blocks for entities to spawn
            for (Block b : getSurroundingBlocks(p.getWorld().getBlockAt(p.getLocation().add(0, -0.1, 0)), SPAWN_RADIUS, DEPTH)) {
                blocks.add(b);
                if (isLocationTooClose(p, b.getLocation(), TOO_CLOSE_RADIUS)) tooClose.add(b);
            }
        }

        blocks.removeAll(tooClose);
        blocks.removeIf(block -> {
            for (Player p : players) {
                if (canBeSeenByPlayer(block, p)) return true;
            }
            return false;
        });

        return blocks;
    }

    private boolean canBeSeenByPlayer(Block block, Player player) {
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
            if (player.getWorld().getBlockAt(v.toLocation(player.getWorld())).isSolid()) {
                return false;
            }
        }
        return true;
    }

    private Set<Block> getSurroundingBlocks(Block block, int radius, int depth) {
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

    private Block searchDepth(Block block, int depth) {
        for (int h = -depth; h <= depth; h++) {
            Block search = block.getRelative(0, h, 0);
            if (search.getType() != Material.AIR && search.getRelative(0, 1, 0).getType() == Material.AIR)
                return search;
        }
        return block;
    }

    private boolean isLocationTooClose(Player p, Location l, int distance) {
        return p.getLocation().distanceSquared(l) < distance * distance;
    }
}