package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class BaseballBat extends Melee {
    public BaseballBat() {
        super(0, 3, 20, 3, "&6Baseball Bat", Material.WOODEN_SWORD, Tier.TIER1, Slot.SECONDARY);
    }
}
