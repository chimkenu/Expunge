package me.chimkenu.expunge.listeners;

import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener implements Listener {
    @EventHandler
    public void switchItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItem(e.getNewSlot());
        player.setLevel(0);
        player.setExp(0);

        // switch cooldown
        if (item == null) return;
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            int cooldown = player.hasPotionEffect(PotionEffectType.SPEED) ? 5 : 10;
            player.setCooldown(item.getType(), cooldown);
        }

        // display ammo if gun
        Gun gun = Utils.getPlayerHeldGun(item);
        if (gun == null) {
            return;
        }
        player.setLevel(ShootListener.getAmmo(item));
    }
}
