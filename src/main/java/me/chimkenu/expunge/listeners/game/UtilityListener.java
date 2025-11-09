package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.*;
import me.chimkenu.expunge.items.Throwable;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilityListener extends GameListener {
    public UtilityListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onUtilityUse(PlayerAnimationEvent e) {
        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }

        Player player = e.getPlayer();
        gameManager.getSurvivor(player).ifPresent(survivor -> {
            var opt = survivor.getActiveItem();
            if (opt.isEmpty()) return;
            var item = opt.get();
            if (survivor.hasCooldown(item.item())) {
                return;
            }

            if (item.item() instanceof Utility util) {
                if (util.use(gameManager, survivor)) {
                    item.stack().setAmount(item.stack().getAmount() - 1);
                }
            }
        });
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();
        GameEntity entity = gameManager.getEntity(projectile).orElse(null);
        if (entity == null) {
            return;
        }

        Expunge.getItems().toThrowable(projectile).ifPresent(proj -> {
            var shouldTrigger = projectile.getPersistentDataContainer().get(proj.namespacedKey(), PersistentDataType.BOOLEAN);
            if (Boolean.TRUE.equals(shouldTrigger)) {
                proj.land(gameManager, projectile.getLocation(), entity);
            }
        });
    }

    @EventHandler
    public void onSwitchSlot(PlayerItemHeldEvent e) {
        if (!gameManager.isRunning()) {
            return;
        }
        var player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        var oldItem = player.getInventory().getItem(e.getPreviousSlot());
        if (oldItem == null) {
            return;
        }

        var gameItem = Expunge.getItems().toGameItem(oldItem);
        if (gameItem.isPresent() && gameItem.get() instanceof Explosive explosive && !explosive.canSwitchSlot()) {
            e.setCancelled(true);
        }
    }
}
