package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Knife implements Melee {
    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public String getName() {
        return "&fFire Axe";
    }

    @Override
    public Material getMaterial() {
        return Material.LEVER;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
