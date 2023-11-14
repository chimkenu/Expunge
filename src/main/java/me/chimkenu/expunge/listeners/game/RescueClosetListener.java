package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class RescueClosetListener extends GameListener {
    private final BreakDoorListener breakDoorListener;

    public RescueClosetListener(JavaPlugin plugin, GameManager gameManager, BreakDoorListener breakDoorListener) {
        super(plugin, gameManager);
        this.breakDoorListener = breakDoorListener;
    }

    @EventHandler
    public void onOpen(PlayerInteractEvent e) {
        Player savior = e.getPlayer();
        if (!gameManager.getPlayers().contains(savior)) {
            return;
        }
        if (!gameManager.getPlayerStat(savior).isAlive()) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }
        if (gameManager.getWorld() != e.getClickedBlock().getWorld()) {
            return;
        }
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK || e.useInteractedBlock() != Event.Result.ALLOW) {
            return;
        }

        Block block = e.getClickedBlock();
        if (!block.getType().toString().contains("_DOOR") || block.getType() == Material.IRON_DOOR) {
            return;
        }
        if (block.getRelative(0, 1, 0).getType() == block.getType()) {
            block = block.getRelative(0, 1, 0);
        }
        if (!doesMapContainRescueCloset(gameManager.getMap().rescueClosetLocations(), block.getLocation().toVector())) {
            return;
        }
        if (breakDoorListener.hasBeenInteractedWith(block)) {
            return; // Someone already touched the rescue closet and thus cannot respawn any players here
        }

        // Respawn dead players
        for (Player p : gameManager.getPlayers()) {
            PlayerStats s = gameManager.getPlayerStat(p);
            if (s.isAlive()) {
                continue;
            }

            s.revive();
            p.teleport(block.getLocation().add(0.5, -1, 0.5));
            p.removePotionEffect(PotionEffectType.GLOWING);
            p.setGameMode(GameMode.ADVENTURE);
            // TODO: broadcast message
        }
    }

    private boolean doesMapContainRescueCloset(Vector[] rescueClosetLocations, Vector rescueClosetInQuestion) {
        for (Vector v : rescueClosetLocations) {
            if (v == rescueClosetInQuestion) return true;
        }
        return false;
    }
}
