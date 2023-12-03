package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class CombatRifle implements Gun {
    @Override
    public double getDamage() {
        return 17;
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
        return 3;
    }

    @Override
    public int getReload() {
        return 66;
    }

    @Override
    public int getClipSize() {
        return 60;
    }

    @Override
    public int getMaxAmmo() {
        return 420;
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
        return Material.IRON_HOE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public Component getName() {
        return Component.text("Combat Rifle");
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
