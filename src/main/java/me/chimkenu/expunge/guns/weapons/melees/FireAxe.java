package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class FireAxe extends Melee {
    public FireAxe() {
        super(30, 2, 18, 3, "&fFire Axe", Material.IRON_AXE, Tier.TIER1, Slot.SECONDARY);
    }
}
