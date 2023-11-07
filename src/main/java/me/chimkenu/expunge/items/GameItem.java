package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.Slot;
import org.bukkit.Material;

public abstract class GameItem {
    private final int cooldown;
    private final Material material;
    private final String name;
    private final Slot slot;

    public GameItem(int cooldown, Material material, String name, Slot slot) {
        this.cooldown = cooldown;
        this.material = material;
        this.name = name;
        this.slot = slot;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public Slot getSlot() {
        return slot;
    }
}
