package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            player.sendActionBar(Component.text("Sneak to pick up.", NamedTextColor.YELLOW));
            e.setCancelled(true);
            return;
        }

        Item item = e.getItem();
        ItemStack itemStack = item.getItemStack();
        GameItem gameItem = Utils.getGameItemFromItemStack(itemStack);
        if (gameItem == null) {
            player.sendActionBar(Component.text("You can't pick this up.", NamedTextColor.RED));
            e.setCancelled(true);
            return;
        }

        if (!canPickUp(player)) {
            e.setCancelled(true);
            return;
        }
        pickUp.put(player, System.currentTimeMillis());

        int hotbarSlot = gameItem.getSlot().ordinal();

        if (player.getInventory().containsAtLeast(item.getItemStack(), 1)) {
            e.setCancelled(true);

            // add to ammo if gun
            if (gameItem instanceof Gun gun) {
                ItemStack gunInHotbar = player.getInventory().getItem(hotbarSlot);
                if (gunInHotbar != null) ShootListener.setAmmo(gunInHotbar, Math.min(ShootListener.getAmmo(gunInHotbar) + ShootListener.getAmmo(item.getItemStack()), gun.getMaxAmmo()));
                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1, 1);
                player.sendActionBar(Component.text("+Ammo", NamedTextColor.BLUE));
                item.setPickupDelay(20);
                if (!item.isInvulnerable()) item.remove();
                return;
            }
            player.sendActionBar(Component.text("You cannot carry any more of this item.", NamedTextColor.RED));
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
                itemSwapped.addScoreboardTag("ITEM");
                itemSwapped.setGlowing(true);
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
        if (Utils.getGameItemFromItemStack(item) == null) {
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
            player.sendActionBar(Component.text("You can't drop this.", NamedTextColor.RED));
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
