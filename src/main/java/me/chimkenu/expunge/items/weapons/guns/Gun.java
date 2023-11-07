package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.items.weapons.Weapon;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface Gun extends Weapon {
    int getPellets();

    int getReload();

    int getClipSize();

    int getMaxAmmo();

    Particle getParticle();

    Sound getSound();

    float getPitch();

    @Override
    default ItemStack getWeapon() {
        ItemStack gun = new ItemStack(getMaterial(), getClipSize());
        ItemMeta meta = gun.getItemMeta();
        if (meta != null) {
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("0a4af6ae-896d-458e-8712-ed8845740753"), "generic.attack_damage", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            modifier = new AttributeModifier(UUID.fromString("d0538ce4-708b-463b-ac91-cfd57d6adbd2"), "generic.attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
            List<String> lore = new ArrayList<>();
            lore.add(String.valueOf(getMaxAmmo()));
            meta.setLore(lore);
        }
        gun.setItemMeta(meta);
        return gun;
    }
}
