package me.chimkenu.expunge.items;

import me.chimkenu.expunge.enums.Slot;
import org.bukkit.Material;

public interface GameItem {
    int getCooldown();

    Material getMaterial();

    String getName();

    Slot getSlot();
}
