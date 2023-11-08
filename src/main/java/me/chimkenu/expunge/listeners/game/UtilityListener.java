package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.items.utilities.Utility;
import me.chimkenu.expunge.items.utilities.healing.Healing;
import me.chimkenu.expunge.items.utilities.throwable.Throwable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class UtilityListener extends GameListener {
    public UtilityListener(JavaPlugin plugin, LocalGameManager localGameManager) {
        super(plugin, localGameManager);
    }

    private void useUtil(Player player, Utility utility) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isSimilar(utility.getUtility()) && player.getCooldown(utility.getMaterial()) == 0) {
            utility.use(plugin, localGameManager, player);
            player.setCooldown(utility.getMaterial(), utility.getCooldown());
            if (utility instanceof Healing) {
                return;
            }
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        }
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (!localGameManager.getPlayers().contains(player)) {
            return;
        }

        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            Utility util = Utils.getPlayerHeldUtility(player.getInventory().getItemInMainHand());

            if (util != null) {
                e.setCancelled(true);
                useUtil(player, util);
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        Projectile projectile = e.getEntity();

        if (!localGameManager.getWorld().equals(projectile.getWorld())) {
            return;
        }

        Throwable throwable = Utils.getThrowableFromProjectile(projectile);
        if (throwable != null && projectile.getShooter() instanceof Entity shooter) {
            throwable.onLand(plugin, projectile.getWorld(), projectile.getLocation(), shooter);
        }
    }
}
