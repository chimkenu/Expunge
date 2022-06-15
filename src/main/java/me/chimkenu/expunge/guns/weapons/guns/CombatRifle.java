package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class CombatRifle extends Gun {
    public CombatRifle() {
        super(15, 1, 40, 3, 66, 60, 420, 1, Particle.WHITE_ASH, Material.IRON_HOE, Tier.TIER2, Slot.PRIMARY, "&fCombat Rifle", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0);
    }
}
