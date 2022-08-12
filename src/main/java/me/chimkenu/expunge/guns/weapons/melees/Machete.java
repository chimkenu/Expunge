package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Machete extends Melee {
    public Machete() {
        super(60, 3, 14, 2, "&fMachete", Material.IRON_SWORD, Tier.TIER1, Slot.SECONDARY);
    }
}
