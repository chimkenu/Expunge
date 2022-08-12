package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Pistol extends Gun {
    public Pistol() {
        super(9, 1, 20, 4, 34, 15, 5000, 1, Particle.CRIT, Material.WOODEN_HOE, Tier.TIER1, Slot.SECONDARY, "&6Pistol", Sound.ENTITY_IRON_GOLEM_HURT, 1.8f);
    }
}
