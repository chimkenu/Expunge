package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class Shovel implements Melee {
    @Override
    public int getRange() {
        return 4;
    }

    @Override
    public int getCooldown() {
        return 18;
    }

    @Override
    public int getEntitiesToHit() {
        return 4;
    }

    @Override
    public String getName() {
        return "&8Shovel";
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_SHOVEL;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}