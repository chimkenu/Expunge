package me.chimkenu.expunge.items.weapons;

import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.inventory.ItemStack;

public interface Weapon extends GameItem {
    double getDamage();

    int getEntitiesToHit();

    int getRange();

    Tier getTier();

    ItemStack getWeapon();
}
