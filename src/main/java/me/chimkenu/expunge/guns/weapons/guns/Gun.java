package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.guns.weapons.Weapon;
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

public abstract class Gun extends Weapon {

    private final int pellets;
    private final int reload;
    private final int clipSize;
    private final int maxAmmo;
    private final Particle particle;
    private final Sound sound;
    private final float pitch;

    public Gun(double damage, int pellets, int range, int cooldown, int reload, int clipSize, int maxAmmo, int entitiesToHit, Particle particle, Material material, Tier tier, Slot slot, String name, Sound sound, float pitch) {
        super(damage, range, cooldown, entitiesToHit, name, material, tier, slot);
        this.pellets = pellets;
        this.reload = reload;
        this.clipSize = clipSize;
        this.maxAmmo = maxAmmo;
        this.particle = particle;
        this.sound = sound;
        this.pitch = pitch;
    }

    public int getPellets() {
        return pellets;
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

    public Particle getParticle() {
        return particle;
    }

    public Sound getSound() {
        return sound;
    }

    public float getPitch() {
        return pitch;
    }

    @Override
    public ItemStack getWeapon() {
        ItemStack gun = new ItemStack(getMaterial(), clipSize);
        ItemMeta meta = gun.getItemMeta();
        if (meta != null) {
            AttributeModifier modifier = new AttributeModifier(UUID.fromString("0a4af6ae-896d-458e-8712-ed8845740753"), "generic.attack_damage", 0.5, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
            modifier = new AttributeModifier(UUID.fromString("d0538ce4-708b-463b-ac91-cfd57d6adbd2"), "generic.attack_speed", 1, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
            meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier);
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
            List<String> lore = new ArrayList<>();
            lore.add(String.valueOf(maxAmmo));
            meta.setLore(lore);
        }
        gun.setItemMeta(meta);
        return gun;
    }
}
