package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Nightstick extends Melee {
    public Nightstick() {
        super(25, 2, 13, 2, "&8Nightstick", Material.STICK, Tier.TIER1, Slot.SECONDARY);
    }
}
