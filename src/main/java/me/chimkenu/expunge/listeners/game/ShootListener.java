package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.events.ShootEvent;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.PlayerStatsable;
import me.chimkenu.expunge.items.Gun;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadLocalRandom;

public class ShootListener extends GameListener {
    private final BreakGlassListener breakGlassListener;

    public ShootListener(Expunge plugin, GameManager gameManager, BreakGlassListener breakGlassListener) {
        super(plugin, gameManager);
        this.breakGlassListener = breakGlassListener;
    }

    @EventHandler
    public void onClick(PlayerAnimationEvent e) {
        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }

        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        if (!(plugin.getItems().toGameItem(player.getInventory().getItemInMainHand()) instanceof Gun gun)) {
            return;
        }

        e.setCancelled(true);
        if (player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }

        if (player.isSneaking()) {
            gun.reload(gameManager, player);
            return;
        }

        // OFFSET MODIFICATION : Offset is increased based on certain actions/conditions
        double offset = 0;
        Location loc = player.getLocation();

        // is on ladder
        if (loc.subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER)
            offset += plugin.getConfig().getDouble("gun.ladder-offset");

        // is not on solid ground
        if (!(loc.subtract(.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(.31, 0.1, -.31).getBlock().getType().isSolid() ||
                loc.subtract(-.31, 0.1, .31).getBlock().getType().isSolid() ||
                loc.subtract(0, 0.1, 0).getBlock().getType().isSolid()
        ))
            offset += plugin.getConfig().getDouble("gun.jumping-offset");

        // is moving a lot
        double movingOffset = plugin.getConfig().getDouble("gun.moving-offset");
        if (player.getVelocity().getX() != 0) offset += movingOffset;
        if (player.getVelocity().getZ() != 0) offset += movingOffset;
        if (player.isSprinting()) offset += movingOffset;

        // is down
        if (player.getVehicle() != null) offset += plugin.getConfig().getDouble("gun.downed-offset");

        var blocksToBreak = gun.fire(gameManager, player, offset);
        if (blocksToBreak == null) {
            return;
        }
        blocksToBreak.forEach(breakGlassListener::breakGlass);

        // Play flash effect
        Location light = player.getEyeLocation();
        if (light.getBlock().isEmpty()) {
            player.sendBlockChange(light, Material.LIGHT.createBlockData("[level=" + (int) (10 + ThreadLocalRandom.current().nextDouble() * 5) + "]"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendBlockChange(light, Material.AIR.createBlockData());
                }
            }.runTaskLater(plugin, 2);
        }
    }

    @EventHandler
    public void onShoot(ShootEvent e) {
        if (!(gameManager.getState() instanceof PlayerStatsable statsable)) {
            return;
        }

        var player = e.getShooter();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        boolean shotHit = false;
        boolean headshot = false;
        for (LivingEntity hit : e.getHitEntities().keySet()) {
            if (!(hit instanceof Player)) {
                shotHit = true;
                if (e.getHitEntities().get(hit)) {
                    headshot = true;
                }
            }
        }

        statsable.getPlayerStat(player).addShot(shotHit, headshot);
    }
}