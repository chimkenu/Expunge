package me.chimkenu.expunge.guns.guns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public abstract class Gun {

    private final double damage;
    private final int pellets;
    private final int range;
    private final int cooldown;
    private final int reload;
    private final int clipSize;
    private final int maxAmmo;
    private final int entitiesToHit;
    private final Particle particle;
    private final Material material;
    private final String name;
    private final Sound sound;
    private final float pitch;

    public Gun(double damage, int pellets, int range, int cooldown, int reload, int clipSize, int maxAmmo, int entitiesToHit, Particle particle, Material material, String name, Sound sound, float pitch) {
        this.damage = damage;
        this.pellets = pellets;
        this.range = range;
        this.cooldown = cooldown;
        this.reload = reload;
        this.clipSize = clipSize;
        this.maxAmmo = maxAmmo;
        this.entitiesToHit = entitiesToHit;
        this.particle = particle;
        this.material = material;
        this.name = name;
        this.sound = sound;
        this.pitch = pitch;
    }

    public double getDamage() {
        return damage;
    }

    public int getPellets() {
        return pellets;
    }

    public int getRange() {
        return range;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getReload() {
        return reload;
    }

    public int getClipSize() {
        return clipSize;
    }

    public int getMaxAmmo() {
        return maxAmmo;
    }

    public int getEntitiesToHit() {
        return entitiesToHit;
    }

    public Particle getParticle() {
        return particle;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    public ItemStack getGun() {
        ItemStack gun = new ItemStack(material, clipSize);
        ItemMeta meta = gun.getItemMeta();
        if (meta != null) {
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("0a4af6ae-896d-458e-8712-ed8845740753"), "generic.attack_damage", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            modifier = new AttributeModifier(UUID.fromString("d0538ce4-708b-463b-ac91-cfd57d6adbd2"), "generic.attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            List<String> lore = new ArrayList<>();
            lore.add(String.valueOf(maxAmmo));
            meta.setLore(lore);
        }
        gun.setItemMeta(meta);
        return gun;
    }
}
