package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Set;

public record PathVector(
        Vector vector,
        double radius
) implements Path {
    public static double DEFAULT_RADIUS = 6;

    public PathVector(double x, double y, double z, double radius) {
        this(new Vector(x, y, z), radius);
    }

    public PathVector(double x, double y, double z) {
        this(x, y, z, DEFAULT_RADIUS);
    }

    @Override
    public boolean isWithin(Player player) {
        return player.getLocation().toVector().distanceSquared(vector) <= radius * radius;
    }

    @Override
    public void showPath(GameManager manager) {
        manager.getWorld().spawnParticle(Particle.END_ROD, vector.toLocation(manager.getWorld()).add(0, 0.2, 0), 1, 0, 0, 0, 0);
    }

    @Override
    public Set<Block> spawnBlocks(GameManager manager) {
        return SpawnUtil.getValidSurroundingBlocks(manager, List.of(vector));
    }
}
