package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.Interactable;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class InteractableListener extends GameListener {
    public InteractableListener(Expunge plugin, GameManager gameManager) {
        super(plugin, gameManager);
    }

    @EventHandler
    public void onPickUp(PlayerInteractAtEntityEvent e) {
        if (!gameManager.getWorld().equals(e.getRightClicked().getWorld())) {
            return;
        }
        Interactable interactable = plugin.getItems().toInteractable(e.getRightClicked());
        if (interactable == null) {
            return;
        }

        e.setCancelled(true);
        interactable.onInteract(gameManager, e.getRightClicked(), e.getPlayer());
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        if (!gameManager.getWorld().equals(e.getEntity().getWorld())) {
            return;
        }
        Interactable interactable = plugin.getItems().toInteractable(e.getEntity());
        if (interactable == null) {
            return;
        }

        // TODO: avoid debounce? -- might not be an issue, need to test
        // e.getEntity().removeScoreboardTag(interactable.getTag());

        interactable.onDamage(gameManager, e.getEntity(), e.getDamager());
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent e) {
        if (!e.getAction().toString().contains("LEFT")) {
            return;
        }

        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        GameItem gameItem = plugin.getItems().toGameItem(playerInventory.getItemInMainHand());
        if (!(gameItem instanceof Interactable interactable) || player.hasCooldown(interactable.toItem())) {
            return;
        }

        playerInventory.getItemInMainHand().setAmount(playerInventory.getItemInMainHand().getAmount() - 1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);

        throwInteractable(player, interactable);
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

        GameItem gameItem = plugin.getItems().toGameItem(item);
        if (!(gameItem instanceof Interactable interactable)) {
            return;
        }

        playerInventory.setItem(e.getPreviousSlot(), null);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.25f, 0);

        throwInteractable(player, interactable);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = e.getItemDrop().getItemStack();
        GameItem gameItem = plugin.getItems().toGameItem(item);
        if (!(gameItem instanceof Interactable interactable)) {
            return;
        }

        e.getItemDrop().remove();

        throwInteractable(player, interactable);
    }

    private void throwInteractable(Player player, Interactable interactable) {
        ArmorStand physicsArmorStand = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), ArmorStand.class);
        Entity interactableEntity = interactable.spawn(player.getEyeLocation());
        gameManager.addEntity(physicsArmorStand);
        gameManager.addEntity(interactableEntity);
        physicsArmorStand.setSmall(true);
        physicsArmorStand.setInvisible(true);
        physicsArmorStand.setVelocity(player.getLocation().getDirection());

        new BukkitRunnable() {
            Location previousLocation = null;
            final double dy = -1.4f; // TODO: interactable.getYOffset();
            @Override
            public void run() {
                if (interactableEntity.isDead() || physicsArmorStand.isDead()) {
                    end();
                    return;
                }
                if (previousLocation != null && previousLocation.equals(physicsArmorStand.getLocation())) {
                    end();
                    return;
                }

                previousLocation = physicsArmorStand.getLocation();
                interactableEntity.teleport(previousLocation.clone().add(0, dy, 0));
            }

            private void end() {
                physicsArmorStand.remove();
                this.cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
