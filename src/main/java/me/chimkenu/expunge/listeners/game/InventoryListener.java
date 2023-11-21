package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class InventoryListener extends GameListener {
    public InventoryListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void switchItem(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();

        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

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

    @EventHandler
    public void onFlowerPotInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (!gameManager.getPlayers().contains(e.getPlayer())) {
            return;
        }
        if (block != null && (block.getType().name().startsWith("POTTED_") || block.getType() == Material.FLOWER_POT)) {
            if (gameManager.getPlayers().contains(e.getPlayer())) {
                e.setCancelled(true);
            }
        }
    }
}
