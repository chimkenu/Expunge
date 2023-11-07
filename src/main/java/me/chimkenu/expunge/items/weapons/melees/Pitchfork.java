package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Pitchfork extends Melee {
    public Pitchfork() {
        super(0, 6, 17, 1, "&bPitchfork", Material.TRIDENT, Tier.TIER1, Slot.SECONDARY);
    }
}
