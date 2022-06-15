package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AWP extends Gun {
    public AWP() {
        super(25, 1, 60, 20, 66, 20, 200, 3, Particle.WAX_ON, Material.IRON_SHOVEL, Tier.TIER2, Slot.PRIMARY, "&fAWP", Sound.ENTITY_IRON_GOLEM_REPAIR, 0);
    }
}
