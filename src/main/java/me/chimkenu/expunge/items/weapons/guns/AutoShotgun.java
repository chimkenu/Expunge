package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AutoShotgun implements Gun {
    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public int getPellets() {
        return 11;
    }

    @Override
    public int getRange() {
        return 10;
    }

    @Override
    public int getCooldown() {
        return 3;
    }

    @Override
    public int getReload() {
        return 60;
    }

    @Override
    public int getClipSize() {
        return 10;
    }

    @Override
    public int getMaxAmmo() {
        return 100;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public Particle getParticle() {
        return Particle.SMOKE_NORMAL;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_AXE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public Slot getSlot() {
        return Slot.PRIMARY;
    }

    @Override
    public String getName() {
        return "&9Auto Shotgun";
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_GENERIC_EXPLODE;
    }

    @Override
    public float getPitch() {
        return 2;
    }
}
