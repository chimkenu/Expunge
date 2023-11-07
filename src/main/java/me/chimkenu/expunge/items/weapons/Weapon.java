package me.chimkenu.expunge.items.weapons;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Weapon extends GameItem {
    private final double damage;
    private final int range;
    private final int entitiesToHit;
    private final Tier tier;

    public Weapon(double damage, int range, int cooldown, int entitiesToHit, String name, Material material, Tier tier, Slot slot) {
        super(cooldown, material, name, slot);
        this.damage = damage;
        this.range = range;
        this.entitiesToHit = entitiesToHit;
        this.tier = tier;
    }

    public double getDamage() {
        return damage;
    }

    public int getEntitiesToHit() {
        return entitiesToHit;
    }

    public int getRange() {
        return range;
    }

    public Tier getTier() {
        return tier;
    }

    public abstract ItemStack getWeapon();
}
