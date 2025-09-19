package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.Expunge;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;

public enum Modifiers {
    NULLIFY {
        @Override
        public AttributeModifier modifier() {
            return new AttributeModifier(
                    new NamespacedKey(Expunge.getPlugin(Expunge.class), name()),
                    -1,
                    AttributeModifier.Operation.ADD_SCALAR,
                    EquipmentSlotGroup.ANY
            );
        }

        @Override
        public void apply(Attributable target, Attribute attribute) {
            try {
                target.getAttribute(attribute).addModifier(modifier());
            } catch (Exception ignored) {}
        }

        @Override
        public void remove(Attributable target, Attribute attribute) {
            try {
                target.getAttribute(attribute).removeModifier(modifier());
            } catch (Exception ignored) {}
        }
    };

    public abstract AttributeModifier modifier();
    public abstract void apply(Attributable target, Attribute attribute);
    public abstract void remove(Attributable target, Attribute attribute);
}
