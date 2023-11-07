package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class HuntingRifle implements Gun {
    @Override
    public double getDamage() {
        return 22;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 50;
    }

    @Override
    public int getCooldown() {
        return 4;
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
        return 165;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public Particle getParticle() {
        return Particle.WHITE_ASH;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_HOE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public String getName() {
        return "&9Hunting Rifle";
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_FIREWORK_ROCKET_BLAST;
    }

    @Override
    public float getPitch() {
        return 0;
    }
}
