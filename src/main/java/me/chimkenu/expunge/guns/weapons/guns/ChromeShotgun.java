package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class ChromeShotgun extends Gun {
    public ChromeShotgun() {
        super(8, 8, 10, 10, 64, 8, 80, 2, Particle.SMOKE_NORMAL, Material.WOODEN_AXE, Tier.TIER1, Slot.PRIMARY, "&6Chrome Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
