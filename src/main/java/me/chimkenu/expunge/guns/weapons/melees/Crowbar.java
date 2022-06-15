package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Crowbar extends Melee {
    public Crowbar() {
        super(30, 2, 18, 1, "&8Crowbar", Material.NETHERITE_HOE, Tier.TIER1, Slot.SECONDARY);
    }
}
