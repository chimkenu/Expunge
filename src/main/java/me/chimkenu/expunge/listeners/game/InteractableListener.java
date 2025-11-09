package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.entities.item.InteractableEntity;
import me.chimkenu.expunge.entities.item.MiscEntity;
import me.chimkenu.expunge.entities.survivor.Survivor;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Interactable;
import me.chimkenu.expunge.listeners.GameListener;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.*;
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
        var interactableOpt = gameManager.getEntity(e.getRightClicked());
        if (interactableOpt.isEmpty()) return;
        if (!(interactableOpt.get() instanceof InteractableEntity interactable)) return;

        var playerOpt = gameManager.getEntity(e.getPlayer());
        if (playerOpt.isEmpty()) return;
        var player = playerOpt.get();

        e.setCancelled(true);
        interactable.interact(gameManager, player);
    }

    @EventHandler
    public void onInteract(EntityDamageByEntityEvent e) {
        var interactableOpt = gameManager.getEntity(e.getEntity());
        if (interactableOpt.isEmpty()) return;
        if (!(interactableOpt.get() instanceof InteractableEntity interactable)) return;

        var damagerOpt = gameManager.getEntity(e.getDamager());
        if (damagerOpt.isEmpty()) return;
        var damager = damagerOpt.get();

        // TODO: avoid debounce? -- might not be an issue, need to test
        // e.getEntity().removeScoreboardTag(interactable.getTag());

        interactable.damage(gameManager, damager);
    }

    @EventHandler
    public void onThrow(PlayerInteractEvent e) {
        if (!e.getAction().toString().contains("LEFT")) {
            return;
        }

        var opt = gameManager.getSurvivor(e.getPlayer());
        if (opt.isEmpty()) return;
        var survivor = opt.get();

        var itemOpt = survivor.getActiveItem();
        if (itemOpt.isEmpty()) return;
        var item = itemOpt.get();

        if (survivor.hasCooldown(item.item())) return;
        if (!(item.item() instanceof Interactable interactable)) return;

        item.stack().setAmount(item.stack().getAmount() - 1); // TODO: TEST THIS SHIT
        gameManager.getWorld().playSound(survivor.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.PLAYERS, 0.5f, 0);
        throwInteractable(survivor, interactable);
    }

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent e) {
        var player = e.getPlayer();
        var opt = gameManager.getSurvivor(player);
        if (opt.isEmpty()) return;
        var survivor = opt.get();

        PlayerInventory playerInventory = player.getInventory();
        ItemStack item = playerInventory.getItem(e.getPreviousSlot());
        if (item == null) {
            return;
        }

        var itemOpt = Expunge.getItems().toGameItem(item);
        if (itemOpt.isEmpty() || !(itemOpt.get() instanceof Interactable interactable)) return;

        playerInventory.setItem(e.getPreviousSlot(), null);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.25f, 0);

        throwInteractable(survivor, interactable);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        var opt = gameManager.getSurvivor(e.getPlayer());
        if (opt.isEmpty()) return;
        var survivor = opt.get();

        ItemStack item = e.getItemDrop().getItemStack();
        var itemOpt = Expunge.getItems().toGameItem(item);
        if (itemOpt.isEmpty() || !(itemOpt.get() instanceof Interactable interactable)) return;

        e.getItemDrop().remove();
        throwInteractable(survivor, interactable);
    }

    private void throwInteractable(Survivor survivor, Interactable interactable) {
        ArmorStand physicsArmorStand = gameManager.getWorld().spawn(survivor.getEyeLocation().add(survivor.getLocation().getDirection()), ArmorStand.class);
        physicsArmorStand.setSmall(true);
        physicsArmorStand.setInvisible(true);
        physicsArmorStand.setVelocity(survivor.getLocation().getDirection());

        gameManager.addEntity(new MiscEntity(physicsArmorStand));
        var interactableEntity = interactable.spawn(gameManager.getWorld(), survivor.getEyeLocation().toVector(), false);

        gameManager.addTask(
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
                        interactableEntity.setLocation(previousLocation.clone().add(0, dy, 0));
                    }

                    private void end() {
                        physicsArmorStand.remove();
                        this.cancel();
                    }
                }.runTaskTimer(plugin, 0, 1)
        );
    }
}
