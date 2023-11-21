package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class GrenadeLauncher implements Gun {
    @Override
    public double getDamage() {
        return 400;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 10;
    }

    @Override
    public int getCooldown() {
        return 40;
    }

    @Override
    public int getReload() {
        return 100;
    }

    @Override
    public int getClipSize() {
        return 1;
    }

    @Override
    public int getMaxAmmo() {
        return 31;
    }

    @Override
    public int getEntitiesToHit() {
        return 20;
    }

    @Override
    public Particle getParticle() {
        return Particle.SMOKE_NORMAL;
    }

    @Override
    public Material getMaterial() {
        return Material.CROSSBOW;
    }

    @Override
    public Tier getTier() {
        return Tier.SPECIAL;
    }

    @Override
    public Component getName() {
        return Component.text("Grenade Launcher", NamedTextColor.GOLD);
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
    }

    @Override
    public float getPitch() {
        return 0.2f;
    }
}
