package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AK47 extends Gun {
    public AK47() {
        super(19, 1, 50, 3, 48, 40, 400, 3, Particle.WHITE_ASH, Material.GOLDEN_HOE, Tier.TIER2, Slot.PRIMARY, "&eAK-47", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0);
    }
}
