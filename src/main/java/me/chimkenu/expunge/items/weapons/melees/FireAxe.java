package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

public class FireAxe implements Melee {
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
        return 3;
    }

    @Override
    public Component getName() {
        return "&fFire Axe";
    }

    @Override
    public Material getMaterial() {
        return Material.IRON_AXE;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
