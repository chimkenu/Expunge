package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class SMG extends Gun {
    public SMG() {
        super(8, 1, 30, 1, 44, 50, 700, 1, Particle.ASH, Material.WOODEN_PICKAXE, Tier.TIER1, Slot.PRIMARY, "&7SMG", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2);
    }
}
