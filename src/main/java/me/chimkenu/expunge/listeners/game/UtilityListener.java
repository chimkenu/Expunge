package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.*;
import me.chimkenu.expunge.items.Throwable;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class UtilityListener extends GameListener {
    public UtilityListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onUtilityUse(PlayerAnimationEvent e) {
        if (e.getAnimationType() != PlayerAnimationType.ARM_SWING) {
            return;
        }

        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        var item = player.getInventory().getItemInMainHand();
        if (player.hasCooldown(item)) {
            return;
        }

        var gameItem = plugin.getItems().toGameItem(item);
        if (gameItem instanceof Utility util) {
            if (util.use(gameManager, player)) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (!gameManager.getWorld().equals(projectile.getWorld())) {
            return;
        }

        Throwable throwable = plugin.getItems().toThrowable(projectile);
        if (throwable == null || !(projectile.getShooter() instanceof LivingEntity shooter)) {
            return;
        }

        var shouldTrigger = projectile.getPersistentDataContainer().get(throwable.namespacedKey(), PersistentDataType.BOOLEAN);
        if (Boolean.TRUE.equals(shouldTrigger)) {
            throwable.land(gameManager, projectile.getLocation(), shooter);
        }
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

        var gameItem = plugin.getItems().toGameItem(oldItem);
        if (gameItem instanceof Explosive explosive && !explosive.canSwitchSlot()) {
            e.setCancelled(true);
        }
    }
}
