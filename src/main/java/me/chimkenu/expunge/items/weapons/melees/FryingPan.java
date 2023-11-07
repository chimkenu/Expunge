package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class FryingPan implements Melee {
    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public int getCooldown() {
        return 22;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public String getName() {
        return "&8Frying Pan";
    }

    @Override
    public Material getMaterial() {
        return Material.NETHERITE_SHOVEL;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
