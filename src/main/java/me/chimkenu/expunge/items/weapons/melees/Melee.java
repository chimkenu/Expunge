package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.items.weapons.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public interface Melee extends Weapon {
    @Override
    default double getDamage() {
        return 0; // Melee weapons usually instantly kill, don't worry about it.
    }

    @Override
    default Slot getSlot() {
        return Slot.SECONDARY;
    }

    @Override
    default ItemStack get() {
        ItemStack melee = new ItemStack(getMaterial());
        ItemMeta meta = melee.getItemMeta();
        if (meta != null) {
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("0a4af6ae-896d-458e-8712-ed8845740753"), "generic.attack_damage", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            modifier = new AttributeModifier(UUID.fromString("d0538ce4-708b-463b-ac91-cfd57d6adbd2"), "generic.attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
        }
        melee.setItemMeta(meta);
        return melee;
    }
}
