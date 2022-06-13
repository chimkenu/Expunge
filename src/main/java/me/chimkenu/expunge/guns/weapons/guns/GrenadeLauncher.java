package me.chimkenu.expunge.guns.weapons.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class GrenadeLauncher extends Gun {
    public GrenadeLauncher() {
        super(80, 1, 10, 40, 100, 1, 31, 20, Particle.SMOKE_NORMAL, Material.CROSSBOW, "&6Grenade Launcher", Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST, 0.2f);
    }
}
