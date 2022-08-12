package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class HuntingRifle extends Gun {
    public HuntingRifle() {
        super(22, 1, 50, 4, 64, 15, 165, 3, Particle.WHITE_ASH, Material.DIAMOND_HOE, Tier.TIER2, Slot.PRIMARY, "&9Hunting Rifle", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0);
    }
}
