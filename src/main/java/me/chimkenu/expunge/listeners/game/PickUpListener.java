package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtils;
import me.chimkenu.expunge.items.Gun;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class PickUpListener extends GameListener {
    public PickUpListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    private final HashMap<Player, Long> pickUp = new HashMap<>();

    private boolean canPickUp(Player player) {
        pickUp.putIfAbsent(player, System.currentTimeMillis() - 501);
        return ((System.currentTimeMillis() - pickUp.get(player)) > 500);
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }
        if (!gameManager.getPlayerStat(player).isAlive()) {
            e.setCancelled(true);
            return;
        }
        if (player.getCooldown(player.getInventory().getItemInMainHand().getType()) > 0) {
            e.setCancelled(true);
            return;
        }
        if (!player.isSneaking()) {
            ChatUtil.sendActionBar(player, "&eSneak to pick up.");
            e.setCancelled(true);
            return;
        }

        Item item = e.getItem();
        ItemStack itemStack = item.getItemStack();
        GameItem gameItem = plugin.getItems().toGameItem(itemStack);
        if (gameItem == null) {
            ChatUtil.sendActionBar(player, "&cYou can't pick this up.");
            e.setCancelled(true);
            return;
        }

        if (!canPickUp(player)) {
            e.setCancelled(true);
            return;
        }
        pickUp.put(player, System.currentTimeMillis());

        int hotbarSlot = gameItem.slot().ordinal();

        if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
            e.setCancelled(true);

            // add to ammo if gun
            if (gameItem instanceof Gun gun) {
                ItemStack gunInHotbar = player.getInventory().getItem(hotbarSlot);
                if (gunInHotbar != null) {
                    gun.setAmmo(gunInHotbar, Math.min(gun.getAmmo(gunInHotbar) + gun.getAmmo(item.getItemStack()), gun.maxAmmo()));
                }

                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1, 1);
                ChatUtil.sendActionBar(player, "&9+Ammo");
                item.setPickupDelay(20);
                if (!item.isInvulnerable()) item.remove();
                return;
            }
            ChatUtil.sendActionBar(player, "&cYou cannot carry any more of this item.");
            return;
        }

        e.setCancelled(true);
        player.getWorld().playSound(item.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f);

        ItemStack hotbarItem = player.getInventory().getItem(hotbarSlot);

        if (hotbarItem != null && !(hotbarItem.getType().equals(Material.AIR))) {
            ItemMeta meta = hotbarItem.getItemMeta();
            if (!(meta != null && meta.getLore() != null && meta.getLore().contains("invulnerable"))) {
                Item itemSwapped = player.getWorld().dropItem(player.getLocation(), hotbarItem);
                itemSwapped.setPickupDelay(20);
                gameManager.getDirector().getItemHandler().addEntity(itemSwapped);
            }
        }

        player.getInventory().setItem(hotbarSlot, item.getItemStack());
        // invulnerable items can be picked up more than once
        if (item.isInvulnerable())
            item.setPickupDelay(20);
        else
            item.remove();
    }

    private void cancelDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand.getAmount() > 1) {
            e.getItemDrop().remove();
            mainHand.setAmount(mainHand.getAmount() + 1);
        } else e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }
        if (!gameManager.getPlayerStat(player).isAlive()) {
            cancelDrop(e);
            return;
        }

        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();
        if (plugin.getItems().toGameItem(item) == null) {
            // fix item if broken
            if (item.getItemMeta() instanceof Damageable damageable) {
                // this is added to fix reload as it breaks when opening the inventory, however it can be abused
                // to avoid such, add a cooldown longer than all reload times
                if (damageable.getDamage() > 0) {
                    damageable.setDamage(0);
                    item.setItemMeta(damageable);
                    player.setCooldown(item.getType(), 20 * 5);
                    player.updateInventory();
                }
            }
            ChatUtil.sendActionBar(player, "&cYou can't drop this.");
            cancelDrop(e);
            return;
        }

        item.setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
        player.getInventory().remove(item.getType());
        entity.setItemStack(item);
        gameManager.getDirector().getItemHandler().addEntity(entity);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player player) {
            if (gameManager.getPlayers().contains(player))
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent e) {
        if (gameManager.isRunning() && gameManager.getWorld() == e.getEntity().getWorld())
            e.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        if (gameManager.isRunning() && gameManager.getWorld() == e.getEntity().getWorld())
            e.setCancelled(true);
    }
}
