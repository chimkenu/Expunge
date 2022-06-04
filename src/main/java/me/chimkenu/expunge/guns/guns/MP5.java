package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class MP5 extends Gun {
    public MP5() {
        super(5, 1, 30, 1, 60, 50, 700, 1, Particle.ASH, Material.STONE_PICKAXE, "&7MP5", Sound.ENTITY_FIREWORK_ROCKET_BLAST, 2);
    }
}
