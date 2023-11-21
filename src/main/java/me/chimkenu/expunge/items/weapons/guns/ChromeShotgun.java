package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class ChromeShotgun implements Gun {
    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public int getPellets() {
        return 8;
    }

    @Override
    public int getRange() {
        return 10;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getReload() {
        return 64;
    }

    @Override
    public int getClipSize() {
        return 8;
    }

    @Override
    public int getMaxAmmo() {
        return 80;
    }

    @Override
    public int getEntitiesToHit() {
        return 2;
    }

    @Override
    public Particle getParticle() {
        return Particle.SMOKE_NORMAL;
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_AXE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }

    @Override
    public Component getName() {
        return Component.text("Chrome Shotgun", NamedTextColor.GOLD);
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
