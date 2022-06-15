package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class FryingPan extends Melee {
    public FryingPan() {
        super(25, 1, 18, 1, "&8Frying Pan", Material.NETHERITE_SHOVEL, Tier.TIER1, Slot.SECONDARY);
    }
}
