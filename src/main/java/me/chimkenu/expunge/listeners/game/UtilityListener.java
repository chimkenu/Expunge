package me.chimkenu.expunge.listeners.game;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.items.utilities.Utility;
import me.chimkenu.expunge.items.utilities.healing.Healing;
import me.chimkenu.expunge.items.utilities.throwable.Throwable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilityListener extends GameListener {
    public UtilityListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    private void useUtil(Player player, Utility utility) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isSimilar(utility.get()) && player.getCooldown(utility.getMaterial()) == 0) {
            utility.use(plugin, gameManager, player);
            player.setCooldown(utility.getMaterial(), utility.getCooldown());
            if (utility instanceof Healing) {
                return;
            }
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        }
    }

    @EventHandler
    public void onUtilityUse(PlayerArmSwingEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        if (Utils.getGameItemFromItemStack(player.getInventory().getItemInMainHand()) instanceof Utility utility) {
            e.setCancelled(true);
            useUtil(player, utility);
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (!gameManager.getWorld().equals(projectile.getWorld())) {
            return;
        }

        Throwable throwable = Utils.getThrowableFromProjectile(projectile);
        if (throwable != null && projectile.getShooter() instanceof Entity shooter) {
            throwable.onLand(plugin, projectile.getWorld(), projectile.getLocation(), shooter);
        }
    }
}
