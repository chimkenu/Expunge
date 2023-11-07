package me.chimkenu.expunge.listeners;

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

public class UtilityListener implements Listener {

    private void useUtil(Player player, Utility utility) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item.isSimilar(utility.getUtility()) && player.getCooldown(utility.getMaterial()) < 1) {
            utility.use(player);
            player.setCooldown(utility.getMaterial(), utility.getCooldown());
            if (utility instanceof Healing healing && !healing.isInstantUse()) {
                return;
            }
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        }
    }

    @EventHandler
    public void onClickBlock(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {

            Player player = e.getPlayer();
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
        Throwable throwable = Utils.getThrowableFromProjectile(projectile);
        if (throwable != null && projectile.getShooter() instanceof Entity shooter) {
            throwable.onLand(projectile.getWorld(), projectile.getLocation(), shooter);
        }
    }
}
