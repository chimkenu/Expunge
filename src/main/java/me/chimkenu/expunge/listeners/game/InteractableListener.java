package me.chimkenu.expunge.listeners.game;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.interactables.Interactable;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
            e.getRightClicked().getWorld().playSound(e.getRightClicked().getLocation(), Sound.BLOCK_GRINDSTONE_USE, 0.2f, 0);
            return;
        }

        Player player = e.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(interactable.getSlot().ordinal());
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f);
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
    public void onThrow(PlayerArmSwingEvent e) {
        Player player = e.getPlayer();
        if (!gameManager.getPlayers().contains(player)) {
            return;
        }

        PlayerInventory playerInventory = player.getInventory();
        GameItem gameItem = Utils.getGameItemFromItemStack(playerInventory.getItemInMainHand());
        if (!(gameItem instanceof  Interactable interactable)) {
            return;
        }

        playerInventory.getItemInMainHand().setAmount(playerInventory.getItemInMainHand().getAmount() - 1);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);

        ArmorStand physicsArmorStand = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), ArmorStand.class);
        Entity interactableEntity = interactable.spawn(player.getEyeLocation());
        gameManager.getDirector().getItemHandler().addEntity(physicsArmorStand);
        gameManager.getDirector().getItemHandler().addEntity(interactableEntity);
        physicsArmorStand.setSmall(true);
        physicsArmorStand.setInvisible(true);
        physicsArmorStand.setVelocity(player.getLocation().getDirection());

        new BukkitRunnable() {
            Location previousLocation = null;
            final double dy = interactable.getYOffset();
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
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.25f, 0);

        ArmorStand physicsArmorStand = player.getWorld().spawn(player.getEyeLocation().add(player.getLocation().getDirection()), ArmorStand.class);
        Entity interactableEntity = interactable.spawn(player.getEyeLocation());
        gameManager.getDirector().getItemHandler().addEntity(physicsArmorStand);
        gameManager.getDirector().getItemHandler().addEntity(interactableEntity);
        physicsArmorStand.setSmall(true);
        physicsArmorStand.setInvisible(true);

        new BukkitRunnable() {
            int t = 20;
            final double dy = interactable.getYOffset();
            @Override
            public void run() {
                if (interactableEntity.isDead() || physicsArmorStand.isDead()) {
                    end();
                    return;
                }
                if (t <= 0) {
                    end();
                    return;
                }

                t--;
                interactableEntity.teleport(physicsArmorStand.getLocation().clone().add(0, dy, 0));
            }

            private void end() {
                physicsArmorStand.remove();
                this.cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}
