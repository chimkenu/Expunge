package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.utils.RayTrace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

        if (args.length == 1) {
            Component component = Component.text(args[0]);
            Bukkit.broadcastMessage(PlainTextComponentSerializer.plainText().serialize(component));

            Component test = Component.text("cum lord", NamedTextColor.BLUE);
            Bukkit.broadcast(test);
            Bukkit.broadcastMessage(PlainTextComponentSerializer.plainText().serialize(test));
            return true;
        }

        GameMode gameMode = player.getGameMode();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || player.getGameMode() != gameMode) this.cancel();
                ArrayList<Block> blocks = getValidSurroundingBlocks(player.getWorld().getBlockAt(player.getLocation().add(0, -0.1, 0)), 20);
                blocks.removeIf(block -> canBeSeenByPlayer(block, player));

                final int tooClose = 50;
                for (Block b : blocks) {
                    b.getWorld().spawnParticle(Particle.REDSTONE, b.getLocation().add(0.5, 1.1, 0.5), 1, new Particle.DustOptions(b.getLocation().distanceSquared(player.getLocation()) < tooClose ? Color.RED : Color.GREEN, 1));
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        Bukkit.broadcastMessage(player.getWorld().getBlockAt(player.getLocation()).getType().toString());
        return true;
    }

    private boolean canBeSeenByPlayer(Block block, Player player) {
        final Vector playerToBlock = block.getLocation().toVector().subtract(player.getEyeLocation().toVector());
        final double maxAngle = 60 * Math.PI / 180;
        if (playerToBlock.angle(player.getEyeLocation().getDirection()) > maxAngle)
            return false;

        // Ray cast
        Vector target = block.getLocation().toVector().add(new Vector(0.5, 2.5, 0.5));
        RayTrace ray = new RayTrace(player.getEyeLocation().toVector(), target.subtract(player.getEyeLocation().toVector()).normalize());
        ArrayList<Vector> positions = ray.traverse(playerToBlock.length(), 1);
        for (Vector v : positions) {
            if (v.distanceSquared(player.getEyeLocation().toVector()) > target.distanceSquared(player.getEyeLocation().toVector()))
                continue;
            if (player.getWorld().getBlockAt(v.toLocation(player.getWorld())).getType() != Material.AIR) {
                return false;
            }
        }
        return true;
    }

    private ArrayList<Block> getValidSurroundingBlocks(Block block, int radius) {
        ArrayList<Block> blocks = new ArrayList<>();
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                Block testBlock = block.getWorld().getBlockAt(block.getLocation().add(x, 0, z));
                testBlock = searchDepth(testBlock, 3);

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
}
