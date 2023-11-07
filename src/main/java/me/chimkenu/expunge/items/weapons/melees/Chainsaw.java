package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Chainsaw implements Melee {
    @Override
    public int getRange() {
        return 4;
    }

    @Override
    public int getCooldown() {
        return 60;
    }

    @Override
    public int getEntitiesToHit() {
        return 3;
    }

    @Override
    public String getName() {
        return "&fChainsaw";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_HORSE_ARMOR;
    }

    @Override
    public Tier getTier() {
        return Tier.SPECIAL;
    }
}