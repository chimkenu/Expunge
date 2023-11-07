package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Machete implements Melee {
    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 14;
    }

    @Override
    public int getEntitiesToHit() {
        return 2;
    }

    @Override
    public String getName() {
        return "&fMachete";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_SWORD;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
