package me.chimkenu.expunge.guns.weapons.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class HuntingRifle extends Gun {
    public HuntingRifle() {
        super(18, 1, 50, 5, 64, 15, 165, 1, Particle.WHITE_ASH, Material.DIAMOND_HOE, "&9Hunting Rifle", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0);
    }
}
