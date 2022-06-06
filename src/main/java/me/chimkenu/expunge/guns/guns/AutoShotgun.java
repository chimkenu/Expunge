package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AutoShotgun extends Gun {
    public AutoShotgun() {
        super(5, 11, 10, 3, 60, 10, 100, 3, Particle.SMOKE_NORMAL, Material.DIAMOND_AXE, "&9Auto Shotgun", Sound.ENTITY_GENERIC_EXPLODE, 2);
    }
}
