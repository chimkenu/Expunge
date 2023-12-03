package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.CleanUp;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class BreakDoorListener extends GameListener implements CleanUp {
    private final HashMap<Block, DoorData> doorLocations;

    public BreakDoorListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
        this.doorLocations = new HashMap<>();
    }

    @Override
    public void clean() {
        for (Block block : doorLocations.keySet()) {
            block.setType(doorLocations.get(block).material(), false);
            block.setBlockData(doorLocations.get(block).upper(), false);
            block.getRelative(0, -1, 0).setType(doorLocations.get(block).material(), false);
            block.getRelative(0, -1, 0).setBlockData(doorLocations.get(block).lower(), false);
        }
        doorLocations.clear();
    }

    public void updateDoor(Block block) {
        if (!block.getType().toString().contains("_DOOR") || block.getType() == Material.IRON_DOOR) {
            return;
        }

        if (block.getRelative(0, 1, 0).getType() == block.getType()) {
            block = block.getRelative(0, 1, 0);
        }

        doorLocations.putIfAbsent(block, new DoorData(block.getType(), block.getBlockData(), block.getRelative(0, -1, 0).getBlockData()));
    }

    public boolean hasBeenInteractedWith(Block block) {
        return doorLocations.containsKey(block);
    }

    private record DoorData(Material material, BlockData upper, BlockData lower) {}

    @EventHandler
    public void onBreakDoor(EntityBreakDoorEvent e) {
        if (gameManager.getWorld() != e.getBlock().getWorld()) {
            return;
        }
        updateDoor(e.getBlock());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDoorInteract(PlayerInteractEvent e) {
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (e.getClickedBlock() == null || !e.getClickedBlock().getType().toString().contains("_DOOR")) {
            return;
        }
        if (gameManager.getWorld() != e.getClickedBlock().getWorld()) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.useInteractedBlock() != Event.Result.ALLOW) {
            return;
        }
        updateDoor(e.getClickedBlock());
    }
}
