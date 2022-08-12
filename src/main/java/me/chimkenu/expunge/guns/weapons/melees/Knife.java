package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Knife extends Melee {
    public Knife() {
        super(44, 2, 10, 1, "&7Knife", Material.LEVER, Tier.TIER1, Slot.SECONDARY);
    }
}
