package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.Slot;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface GameItem {
    int getCooldown();

    Material getMaterial();

    String getName();

    Slot getSlot();

    ItemStack get();
}
