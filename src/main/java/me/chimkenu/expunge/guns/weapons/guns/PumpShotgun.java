package me.chimkenu.expunge.guns.weapons.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class PumpShotgun extends Gun {
    public PumpShotgun() {
        super(7, 10, 10, 10, 64, 8, 80, 1, Particle.SMOKE_NORMAL, Material.STONE_AXE, "&7Pump Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
