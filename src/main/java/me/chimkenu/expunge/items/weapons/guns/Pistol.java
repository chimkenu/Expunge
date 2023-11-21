package me.chimkenu.expunge.items.weapons.guns;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Pistol implements Gun {
    @Override
    public double getDamage() {
        return 9;
    }

    @Override
    public int getPellets() {
        return 1;
    }

    @Override
    public int getRange() {
        return 20;
    }

    @Override
    public int getCooldown() {
        return 4;
    }

    @Override
    public int getReload() {
        return 34;
    }

    @Override
    public int getClipSize() {
        return 15;
    }

    @Override
    public int getMaxAmmo() {
        return 5000;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public Particle getParticle() {
        return Particle.CRIT;
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_HOE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }

    @Override
    public Slot getSlot() {
        return Slot.SECONDARY;
    }

    @Override
    public Component getName() {
        return Component.text("Pistol", NamedTextColor.GOLD);
    }

    @Override
    public Sound getSound() {
        return Sound.ENTITY_IRON_GOLEM_HURT;
    }

    @Override
    public float getPitch() {
        return 1.8f;
    }
}
