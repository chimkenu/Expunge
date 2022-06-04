package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class M60MachineGun extends Gun {
    public M60MachineGun() {
        super(25, 1, 50, 2, 60, 64, 128, 1, Particle.FIREWORKS_SPARK, Material.NETHERITE_PICKAXE, "&8M60 Machine Gun", Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0);
    }
}
