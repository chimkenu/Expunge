package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.utils.SpawnUtil;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    public boolean isWithin(Survivor survivor) {
        return survivor.getLocation().toVector().distanceSquared(vector) <= radius * radius;
    }

    @Override
    public void showPath(World world) {
        world.spawnParticle(Particle.END_ROD, vector.toLocation(world).add(0, 0.2, 0), 1, 0, 0, 0, 0);
    }

    @Override
    public Set<Block> spawnBlocks(World world, Set<Survivor> survivors) {
        return SpawnUtil.getValidSurroundingBlocks(world, survivors, List.of(vector));
    }
}
