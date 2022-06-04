package me.chimkenu.expunge.guns.guns;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AWP extends Gun {
    public AWP() {
        super(23, 1, 60, 20, 66, 20, 200, 3, Particle.WAX_ON, Material.IRON_SHOVEL, "&fAWP", Sound.ENTITY_IRON_GOLEM_REPAIR, 0);
    }
}
