package me.chimkenu.expunge.game;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.util.HashSet;

public class BreakGlass {
    private static final HashSet<Block> glassLocations = new HashSet<>();

    public static void returnGlass() {
        for (Block block : glassLocations) {
            block.getLocation().getBlock().setType(block.getType());
            block.getLocation().getBlock().setBlockData(block.getBlockData());
        }
        glassLocations.clear();
    }

    public static void breakGlass(Block block) {
        if (block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE) {
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0, .5, 0), 15, 0.2, 0.2, 0.2, block.getType().createBlockData());
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
            glassLocations.add(block);
            block.setType(Material.AIR);
        }
    }
}