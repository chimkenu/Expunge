package me.chimkenu.expunge.items.weapons.melees;

import me.chimkenu.expunge.enums.Tier;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;

public class MrCookie implements Melee {
    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public int getEntitiesToHit() {
        return 1;
    }

    @Override
    public Component getName() {
        return Component.text("Mr. Cookie", NamedTextColor.GOLD);
    }

    @Override
    public Material getMaterial() {
        return Material.COOKIE;
    }

    @Override
    public Tier getTier() {
        return Tier.SPECIAL;
    }
}
