package me.chimkenu.expunge.guns.weapons.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class M16AssaultRifle extends Gun {
    public M16AssaultRifle() {
        super(14, 1, 40, 2, 44, 50, 410, 1, Particle.WHITE_ASH, Material.STONE_HOE, "&7M-16 Assault Rifle", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 0);
    }
}
