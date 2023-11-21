package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class M60MachineGun implements Gun {
    @Override
    public double getDamage() {
        return 40;
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
        return 2;
    }

    @Override
    public int getReload() {
        return 60;
    }

    @Override
    public int getClipSize() {
        return 64;
    }

    @Override
    public int getMaxAmmo() {
        return 120;
    }

    @Override
    public int getEntitiesToHit() {
        return 5;
    }

    @Override
    public Particle getParticle() {
        return Particle.FIREWORKS_SPARK;
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_PICKAXE;
    }

    @Override
    public Tier getTier() {
        return Tier.SPECIAL;
    }

    @Override
    public Component getName() {
        return Component.text("M60 Machine Gun", NamedTextColor.DARK_GRAY);
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST;
    }

    @Override
    public float getPitch() {
        return 0;
    }
}
