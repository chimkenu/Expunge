package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Chainsaw extends Melee {
    public Chainsaw() {
        super(250, 4, 60, 3, "&fChainsaw", Material.IRON_HORSE_ARMOR, Tier.SPECIAL, Slot.SECONDARY);
    }
}
