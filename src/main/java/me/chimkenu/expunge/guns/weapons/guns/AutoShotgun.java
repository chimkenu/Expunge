package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AutoShotgun extends Gun {
    public AutoShotgun() {
        super(10, 11, 10, 3, 60, 10, 100, 3, Particle.SMOKE_NORMAL, Material.DIAMOND_AXE, Tier.TIER2, Slot.PRIMARY, "&9Auto Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
