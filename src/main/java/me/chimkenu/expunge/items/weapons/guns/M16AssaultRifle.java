package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class M16AssaultRifle implements Gun {
    @Override
    public double getDamage() {
        return 16;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 40;
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public int getReload() {
        return 44;
    }

    @Override
    public int getClipSize() {
        return 50;
    }

    @Override
    public int getMaxAmmo() {
        return 410;
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
        return Material.STONE_HOE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public String getName() {
        return "&7M-16 Assault Rifle";
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
