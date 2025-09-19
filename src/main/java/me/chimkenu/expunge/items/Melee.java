package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.MeleeType;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

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
    public static final double BLOCK_BREAK_SPEED = 3;

    public Melee {
        if (slot == null) slot = Slot.SECONDARY;
        if (damageType == MeleeType.Damage.RELATIVE) damage = 21;
    }

    @Override
    public ItemStack toItem() {
        ItemStack melee = new ItemStack(material());
        melee = ItemUtil.setDestroyable(melee, List.of(Material.GLASS, Material.GLASS_PANE));
        ItemMeta meta = melee.getItemMeta();

        final double DEFAULT_REACH = 3.0;
        double reach = range() - DEFAULT_REACH;

        final double DEFAULT_ATTACK_SPEED = 4.0;
        double attack_speed = -1 * DEFAULT_ATTACK_SPEED * (swapCooldown() / 30d);

        if (meta != null) {
            meta.setDisplayName(ChatUtil.format(name()));
            meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, true);

            meta.addAttributeModifier(Attribute.ENTITY_INTERACTION_RANGE, new AttributeModifier(
                    namespacedKey(),
                    reach,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            ));
            meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(
                    namespacedKey(),
                    damage(),
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            ));
            meta.addAttributeModifier(Attribute.BLOCK_BREAK_SPEED, new AttributeModifier(
                    namespacedKey(),
                    BLOCK_BREAK_SPEED,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            ));
            meta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(
                    namespacedKey(),
                    attack_speed,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlotGroup.MAINHAND
            ));

            melee.setItemMeta(meta);
        }

        return melee;
    }
}
