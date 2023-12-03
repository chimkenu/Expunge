package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class AWP implements Gun {
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
        return 66;
    }

    @Override
    public int getClipSize() {
        return 20;
    }

    @Override
    public int getMaxAmmo() {
        return 200;
    }

    @Override
    public int getEntitiesToHit() {
        return 5;
    }

    @Override
    public Particle getParticle() {
        return Particle.WAX_ON;
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SHOVEL;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER2;
    }

    @Override
    public Component getName() {
        return Component.text("AWP", NamedTextColor.WHITE);
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
