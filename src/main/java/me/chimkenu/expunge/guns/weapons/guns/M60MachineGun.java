package me.chimkenu.expunge.guns.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class M60MachineGun extends Gun {
    public M60MachineGun() {
        super(30, 1, 50, 2, 60, 64, 128, 1, Particle.FIREWORKS_SPARK, Material.NETHERITE_PICKAXE, Tier.SPECIAL, Slot.PRIMARY, "&8M60 Machine Gun", Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0);
    }
}
