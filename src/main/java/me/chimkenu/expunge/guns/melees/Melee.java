package me.chimkenu.expunge.guns.melees;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public abstract class Melee {

    private final double damage;
    private final int cooldown;
    private final int entitiesToHit;
    private final int reach;
    private final String name;
    private final Material material;

    public Melee(double damage, int cooldown, int entitiesToHit, int reach, String name, Material material) {
        this.damage = damage;
        this.cooldown = cooldown;
        this.entitiesToHit = entitiesToHit;
        this.reach = reach;
        this.name = name;
        this.material = material;
    }

    public double getDamage() {
        return damage;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getEntitiesToHit() {
        return entitiesToHit;
    }

    public int getReach() {
        return reach;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public ItemStack getMelee() {
        ItemStack gun = new ItemStack(material);
        ItemMeta meta = gun.getItemMeta();
        if (meta != null) {
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("0a4af6ae-896d-458e-8712-ed8845740753"), "generic.attack_damage", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            modifier = new AttributeModifier(UUID.fromString("d0538ce4-708b-463b-ac91-cfd57d6adbd2"), "generic.attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        gun.setItemMeta(meta);
        return gun;
    }
}
