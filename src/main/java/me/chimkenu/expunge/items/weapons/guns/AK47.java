package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AK47 implements Gun {
    @Override
    public int getCooldown() {
        return 3;
    }

    @Override
    public Material getMaterial() {
        return Material.GOLDEN_HOE;
    }

    @Override
    public String getName() {
        return "&eAK-47";
    }

    @Override
    public Slot getSlot() {
        return Slot.PRIMARY;
    }

    @Override
    public double getDamage() {
        return 19;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public int getRange() {
        return 50;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getReload() {
        return 48;
    }

    @Override
    public int getClipSize() {
        return 40;
    }

    @Override
    public int getMaxAmmo() {
        return 400;
    }

    @Override
    public Particle getParticle() {
        return Particle.WHITE_ASH;
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
