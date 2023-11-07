package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class PumpShotgun extends Gun {
    public PumpShotgun() {
        super(5, 10, 10, 10, 64, 8, 80, 1, Particle.SMOKE_NORMAL, Material.STONE_AXE, Tier.TIER1, Slot.PRIMARY, "&7Pump Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
