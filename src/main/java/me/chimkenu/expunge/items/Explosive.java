package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.EffectType;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public record Explosive(
        String id,
        String name,
        Material material,
        Tier tier,
        Slot slot,
        int swapCooldown,

        String texture,
        boolean canBePickedUp,
        boolean canSwitchSlot,
        EffectType.Land interactionType,
        int effectDuration
) implements Interactable {
    public Explosive {
        if (material == null) material = Material.PLAYER_HEAD;
        if (slot == null) slot = Slot.SENARY;
    }

    @Override
    public Entity spawn(Location locationToSpawn) {
        ArmorStand armorStand = locationToSpawn.getWorld().spawn(locationToSpawn.subtract(0, 1.4, 0), ArmorStand.class);
        armorStand.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, true);
        armorStand.getEquipment().setHelmet(toItem());
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        return armorStand;
    }

    @Override
    public void onDamage(GameManager manager, Entity interactable, Entity actor) {
        interactionType().trigger(manager, interactable.getLocation(), actor, effectDuration());
        interactable.remove();
    }

    @Override
    public void onInteract(GameManager manager, Entity interactable, Entity actor) {
        if (!canBePickedUp) {
            manager.getWorld().playSound(interactable, Sound.BLOCK_GRINDSTONE_USE, 0.2f, 0);
            return;
        }

        if (!(actor instanceof Player player)) {
            return;
        }

        ItemStack itemStack = player.getInventory().getItem(slot().ordinal());
        if (itemStack != null && itemStack.getType() != Material.AIR) {
            return;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.5f, 1f);
        interactable.remove();
        player.getInventory().setHeldItemSlot(slot().ordinal());
        player.getInventory().setItem(slot().ordinal(), toItem());
        player.setCooldown(toItem(), swapCooldown());
    }

    @Override
    public ItemStack toItem() {
        ItemStack item = ItemUtil.getSkull(texture());
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtil.format(name()));
            meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, true);
            item.setItemMeta(meta);
        }
        return item;
    }
}
