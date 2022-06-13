package me.chimkenu.expunge.guns.weapons.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class CombatShotgun extends Gun {
    public CombatShotgun() {
        super(12, 9, 10, 3, 60, 10, 100, 4, Particle.SMOKE_NORMAL, Material.GOLDEN_AXE, "&eCombat Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
