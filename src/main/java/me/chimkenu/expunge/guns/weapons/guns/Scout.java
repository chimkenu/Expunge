package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Scout extends Gun {
    public Scout() {
        super(23, 1, 60, 21, 64, 15, 195, 3, Particle.WAX_ON, Material.GOLDEN_SHOVEL, Tier.TIER2, Slot.PRIMARY, "&eScout", Sound.ENTITY_IRON_GOLEM_REPAIR, 0);
    }
}
