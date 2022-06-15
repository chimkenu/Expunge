package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Pitchfork extends Melee {
    public Pitchfork() {
        super(30, 4, 17, 1, "&bPitchfork", Material.TRIDENT, Tier.TIER1, Slot.SECONDARY);
    }
}
