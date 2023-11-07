package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class SMG implements Gun {
    @Override
    public double getDamage() {
        return 8;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 30;
    }

    @Override
    public int getCooldown() {
        return 1;
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
        return 700;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public Particle getParticle() {
        return Particle.ASH;
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_PICKAXE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }

    @Override
    public String getName() {
        return "&7SMG";
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_FIREWORK_ROCKET_BLAST;
    }

    @Override
    public float getPitch() {
        return 2;
    }
}
