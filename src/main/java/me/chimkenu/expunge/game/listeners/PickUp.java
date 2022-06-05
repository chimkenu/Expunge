package me.chimkenu.expunge.game.listeners;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Utilities;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.guns.utilities.Utility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.HashMap;

public class PickUp implements Listener {
    @EventHandler
    public void onDespawn(ItemDespawnEvent e) {
        if (Expunge.isGameRunning) {
            e.setCancelled(true);
        }
    }

    private HashMap<ItemStack, Integer> getItems() {
        HashMap<ItemStack, Integer> items = new HashMap<>();
        for (Weapons.Guns weapon : Weapons.Guns.values()) {
            items.put(weapon.getGun().getGun(), 0);
        }
        for (Weapons.Melees weapon : Weapons.Melees.values()) {
            items.put(weapon.getMelee().getMelee(), 1);
        }
        for (Utilities.Throwable util : Utilities.Throwable.values()) {
            items.put(util.getUtility().getUtility(), 2);
        }
        for (Utilities.Healing util : Utilities.Healing.values()) {
            items.put(util.getUtility().getUtility(), util.isMain() ? 3 : 4);
        }
        return items;
    }

    private ItemStack getValidItemStack(ItemStack item) {
        for (ItemStack itemStack : getItems().keySet()) {
            if (item.isSimilar(itemStack)) {
                return itemStack;
            }
        }
        return null;
    }

    private boolean isItemInvalid(ItemStack item) {
        return getValidItemStack(item) == null;
    }

    private int getHotbarSlot(ItemStack item) {
        return getItems().get(getValidItemStack(item));
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (!(e.getEntity() instanceof Player player)) {
            return;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (!Expunge.isGameRunning) {
            e.setCancelled(true);
            return;
        }
        if (!Expunge.playing.getKeys().contains(player)) {
            e.setCancelled(true);
            return;
        }
        if (Utility.usingUtility.contains(player)) {
            e.setCancelled(true);
            return;
        }
        if (!player.isSneaking()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eSneak to pick up."));
            e.setCancelled(true);
            return;
        }

        Item item = e.getItem();
        if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou cannot carry any more of this item."));
            e.setCancelled(true);
            return;
        }

        if (isItemInvalid(item.getItemStack())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't pick this up."));
            e.setCancelled(true);
            return;
        }

        e.setCancelled(true);
        player.getWorld().playSound(item.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f);

        int hotbarSlot = getHotbarSlot(item.getItemStack());
        ItemStack hotbarItem = player.getInventory().getItem(hotbarSlot);

        if (hotbarItem != null && !(hotbarItem.getType().equals(Material.AIR))) {
            Item itemSwapped =  player.getWorld().dropItem(player.getLocation(), hotbarItem);
            itemSwapped.setPickupDelay(20);
            itemSwapped.addScoreboardTag("ITEM");
        }

        player.getInventory().setItem(hotbarSlot, item.getItemStack());

        // invulnerable items can be picked up more than once
        if (item.isInvulnerable())
            item.setPickupDelay(20);
        else
            item.remove();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        if (!Expunge.isGameRunning) {
            e.setCancelled(true);
            return;
        }
        if (!Expunge.playing.getKeys().contains(player)) {
            e.setCancelled(true);
            return;
        }
        if (!Expunge.playing.isAlive(player)) {
            e.setCancelled(true);
            return;
        }

        ItemStack item = e.getItemDrop().getItemStack();
        if (isItemInvalid(item)) {
            // fix item if broken
            if (item.getItemMeta() != null && item.getItemMeta() instanceof Damageable damageable) {
                // this is added to fix reload as it may break from time to time, however it can be abused
                // to avoid such, add a cooldown longer than all reload times
                if (damageable.getDamage() > 0) {
                    damageable.setDamage(0);
                    item.setItemMeta(damageable);
                    player.setCooldown(item.getType(), 20 * 5);
                }
            }
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§cYou can't drop this."));
            e.setCancelled(true);
            return;
        }

        item.setAmount(player.getInventory().getItemInMainHand().getAmount() + 1);
        player.getInventory().remove(item.getType());
        e.getItemDrop().setItemStack(item);
        e.getItemDrop().addScoreboardTag("ITEM");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemMerge(ItemMergeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e) {
        e.setCancelled(true);
    }
}
