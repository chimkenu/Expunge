package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class SniperRifle extends Gun {
    public SniperRifle() {
        super(21, 1, 60, 7, 66, 30, 210, 5, Particle.WAX_ON, Material.DIAMOND_SHOVEL, Tier.TIER2, Slot.PRIMARY, "&9Sniper Rifle", Sound.ENTITY_IRON_GOLEM_REPAIR, 0);
    }
}
