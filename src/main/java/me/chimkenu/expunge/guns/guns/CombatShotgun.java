package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class CombatShotgun extends Gun {
    public CombatShotgun() {
        super(6, 9, 10, 3, 60, 10, 100, 1, Particle.SMOKE_NORMAL, Material.GOLDEN_AXE, "&eCombat Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
