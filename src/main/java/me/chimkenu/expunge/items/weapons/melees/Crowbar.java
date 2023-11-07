package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Crowbar implements Melee {
    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 18;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public String getName() {
        return "&8Crowbar";
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_HOE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
