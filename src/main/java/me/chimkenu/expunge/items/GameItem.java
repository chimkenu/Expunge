package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.Slot;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface GameItem {
    int getCooldown();

    Material getMaterial();

    Component getName();

    Slot getSlot();

    ItemStack get();
}
