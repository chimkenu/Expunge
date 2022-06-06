package me.chimkenu.expunge.guns.weapons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Weapon {
    private final double damage;
    private final int range;
    private final int cooldown;
    private final int entitiesToHit;

    private final String name;
    private final Material material;

    public Weapon(double damage, int range, int cooldown, int entitiesToHit, String name, Material material) {
        this.damage = damage;
        this.range = range;
        this.cooldown = cooldown;
        this.entitiesToHit = entitiesToHit;
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

    public int getRange() {
        return range;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public abstract ItemStack getWeapon();
}
