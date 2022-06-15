package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Shovel extends Melee {
    public Shovel() {
        super(30, 4, 18, 4, "&8Shovel", Material.STONE_SHOVEL, Tier.TIER1, Slot.SECONDARY);
    }
}
