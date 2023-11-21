package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class Nightstick implements Melee {
    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 13;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public Component getName() {
        return "&8Nightstick";
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
