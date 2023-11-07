package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Nightstick extends Melee {
    public Nightstick() {
        super(0, 3, 13, 1, "&8Nightstick", Material.STICK, Tier.TIER1, Slot.SECONDARY);
    }
}
