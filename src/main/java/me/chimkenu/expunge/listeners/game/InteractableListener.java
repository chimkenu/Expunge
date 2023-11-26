package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.interactables.Interactable;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InteractableListener extends GameListener {
    public InteractableListener(JavaPlugin plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onPickUp(PlayerInteractAtEntityEvent e) {
        if (!gameManager.getWorld().equals(e.getRightClicked().getWorld())) {
            return;
        }
        Interactable interactable = Utils.getInteractableFromEntity(e.getRightClicked());
        if (interactable == null) {
            return;
        }

        e.setCancelled(true);
        if (interactable.cannotBePickedUp()) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(interactable.getSlot().ordinal());
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return;
        }

        e.getRightClicked().remove();
        player.getInventory().setItem(interactable.getSlot().ordinal(), interactable.get());
        player.getInventory().setHeldItemSlot(interactable.getSlot().ordinal());
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        if (!gameManager.getWorld().equals(e.getEntity().getWorld())) {
            return;
        }
        Interactable interactable = Utils.getInteractableFromEntity(e.getEntity());
        if (interactable == null) {
            return;
        }

        e.getEntity().removeScoreboardTag(interactable.getTag());

        interactable.onInteract(plugin, gameManager, e.getEntity(), e.getEntity());
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItem(e.getPreviousSlot());
        if (item == null) {
            return;
        }

        GameItem gameItem = Utils.getGameItemFromItemStack(item);
        if (!(gameItem instanceof Interactable interactable)) {
            return;
        }

        playerInventory.setItem(e.getPreviousSlot(), null);
        Location loc = player.getEyeLocation();
        while (loc.getBlock().getType().equals(Material.AIR)) {
            loc.subtract(0, 0.05, 0);
        }
        interactable.spawn(loc);
    }
}
