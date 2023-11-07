package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class BaseballBat implements Melee {
    @Override
    public double getDamage() {
        return 20;
    }

    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 20;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public String getName() {
        return "&6Baseball Bat";
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_SWORD;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
