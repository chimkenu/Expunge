package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class Pitchfork implements Melee {
    @Override
    public int getRange() {
        return 6;
    }

    @Override
    public int getCooldown() {
        return 17;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public Component getName() {
        return Component.text("Pitchfork", NamedTextColor.AQUA);
    }

    @Override
    public Material getMaterial() {
        return Material.TRIDENT;
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }
}
