package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.MeleeType;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public record Melee(
        String id,
        String name,
        Material material,
        Tier tier,
        Slot slot,
        int swapCooldown,

        double damage,
        int pierceNumber,
        int range,

        MeleeType.Attack attackType,
        MeleeType.Damage damageType
) implements Weapon {
    public Melee {
        if (slot == null) slot = Slot.SECONDARY;
    }

    @Override
    public ItemStack toItem() {
        ItemStack melee = new ItemStack(material());
        ItemMeta meta = melee.getItemMeta();

        final double DEFAULT_REACH = 3.0; // vanilla default
        double adjustment = range() - DEFAULT_REACH;

        if (meta != null) {
            meta.setDisplayName(ChatUtil.format(name()));
            meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, true);

            AttributeModifier modifier = new AttributeModifier(
                    namespacedKey(),
                    adjustment,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.HAND
            );

            meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, modifier);
            melee.setItemMeta(meta);
        }
        return melee;
    }
}
