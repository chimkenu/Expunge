package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class SMG extends Gun {
    public SMG() {
        super(4, 1, 30, 1, 44, 50, 700, 1, Particle.ASH, Material.WOODEN_PICKAXE, "&7SMG", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2);
    }
}
