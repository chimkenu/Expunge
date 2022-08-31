package me.chimkenu.expunge.game;

import me.chimkenu.expunge.Expunge;

import java.util.ArrayList;

public class BreakGlass {
    private Set<Block> glassLocations = new Set<>();

    public static void returnGlass() {
        for (Block block : glassLocations) {
            block.setBlock(block);
        }
        glassLocations.clear();
    }

    public static void breakGlass() {
        if (block.getMaterial() == Material.GLASS_BLOCK or block.getMaterial() == Material.GLASS_PANE) {
            block.getWorld().spawnParticle(Particle.BLOCK);
            block.getWorld().playSound(Sound.BLOCK_GLASS_BREAK);
            glassLocations.add(block);
            block.setBlock(Material.AIR);
        }
        else
            throw new IllegalArgumentException();
    }
}