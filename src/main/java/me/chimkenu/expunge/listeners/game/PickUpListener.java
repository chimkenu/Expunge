package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.entities.item.ItemEntity;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class PickUpListener extends GameListener {
    public PickUpListener(JavaPlugin plugin, GameManager gameManager) {
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
        var opt = gameManager.getSurvivor(player);
        if (opt.isEmpty()) return;
        var survivor = opt.get();

        if (player.getCooldown(player.getInventory().getItemInMainHand().getType()) > 0) {
            e.setCancelled(true);
            return;
        }
        if (!player.isSneaking()) {
            ChatUtil.sendActionBar(player, "&eSneak to pick up.");
            e.setCancelled(true);
            return;
        }

        var itemEntityOpt = gameManager.getEntity(e.getItem());
        if (itemEntityOpt.isEmpty() || !(itemEntityOpt.get() instanceof ItemEntity itemEntity)) {
            ChatUtil.sendActionBar(player, "&cYou can't pick this up.");
            e.setCancelled(true);
            return;
        }

        if (!canPickUp(player)) {
            e.setCancelled(true);
            return;
        }
        pickUp.put(player, System.currentTimeMillis());

        var itemEntityStack = itemEntity.getItem();
        var hotbarSlot = itemEntityStack.item().slot();

        if (player.getInventory().containsAtLeast(itemEntityStack.stack(), 1)) {
            e.setCancelled(true);

            // add to ammo if gun
            if (itemEntityStack.item() instanceof Gun gun) {
                survivor.getItemSlot(hotbarSlot).ifPresent(gunInHotbar ->
                        gun.setAmmo(gunInHotbar.stack(), Math.min(gun.getAmmo(gunInHotbar.stack()) + gun.getAmmo(e.getItem().getItemStack()), gun.maxAmmo()))
                );

                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1, 1);
                ChatUtil.sendActionBar(player, "&9+Ammo");
                e.getItem().setPickupDelay(20);
                if (!e.getItem().isInvulnerable()) e.getItem().remove();
                return;
            }
            ChatUtil.sendActionBar(player, "&cYou cannot carry any more of this item.");
            return;
        }

        e.setCancelled(true);
        player.getWorld().playSound(e.getItem(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1f, 1f);

        survivor.getItemSlot(hotbarSlot).ifPresent(hotbarItem -> {
            ItemMeta meta = hotbarItem.stack().getItemMeta();
            if (!(meta != null && meta.getLore() != null && meta.getLore().contains("invulnerable"))) {
                Item itemSwapped = player.getWorld().dropItem(player.getLocation(), hotbarItem.stack());
                itemSwapped.setPickupDelay(20);
                gameManager.addEntity(new ItemEntity(hotbarItem.item(), itemSwapped));
            }
        });

        survivor.setItemSlot(hotbarSlot, itemEntityStack);
        // invulnerable items can be picked up more than once
        if (e.getItem().isInvulnerable()) {
            e.getItem().setPickupDelay(20);
        } else {
            e.getItem().remove();
        }
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
        var player = e.getPlayer();
        if (e.isCancelled()) {
            return;
        }
        var opt = gameManager.getSurvivor(player);
        if (opt.isEmpty()) return;
        var survivor = opt.get();
        if (!survivor.isAlive()) {
            cancelDrop(e);
            return;
        }

        Item entity = e.getItemDrop();
        ItemStack item = entity.getItemStack();
        var itemOpt = Expunge.getItems().toGameItem(item);
        if (itemOpt.isEmpty()) {
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
        gameManager.addEntity(new ItemEntity(itemOpt.get(), entity));
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
