package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class MP5 extends Gun {
    public MP5() {
        super(10, 1, 30, 1, 60, 50, 700, 2, Particle.ASH, Material.STONE_PICKAXE, Tier.TIER1, Slot.PRIMARY, "&7MP5", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2);
    }
}
