package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Scout implements Gun {
    @Override
    public double getDamage() {
        return 25;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 60;
    }

    @Override
    public int getCooldown() {
        return 7;
    }

    @Override
    public int getReload() {
        return 64;
    }

    @Override
    public int getClipSize() {
        return 15;
    }

    @Override
    public int getMaxAmmo() {
        return 195;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public Particle getParticle() {
        return Particle.WAX_ON;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_SHOVEL;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public String getName() {
        return "&eScout";
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_IRON_GOLEM_REPAIR;
    }

    @Override
    public float getPitch() {
        return 0;
    }
}
