package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.CleanUp;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class BreakGlassListener extends GameListener implements CleanUp {
    private final HashMap<Block, GlassData> glassLocations;

    public BreakGlassListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
        glassLocations = new HashMap<>();
    }

    @Override
    public void clean() {
        for (Block block : glassLocations.keySet()) {
            block.getLocation().getBlock().setType(glassLocations.get(block).material(), false);
            block.getLocation().getBlock().setBlockData(glassLocations.get(block).blockData(), false);
        }
        glassLocations.clear();
    }

    public void breakGlass(Block block) {
        if (block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE) {
            block.getWorld().spawnParticle(Particle.BLOCK_CRACK, block.getLocation().add(0, .5, 0), 15, 0.2, 0.2, 0.2, block.getType().createBlockData());
            block.getWorld().playSound(block.getLocation(), Sound.BLOCK_GLASS_BREAK, 1, 1);
            glassLocations.put(block, new GlassData(block.getType(), block.getBlockData()));
            block.setType(Material.AIR, false);
        }
    }

    private record GlassData(Material material, BlockData blockData) {}
}