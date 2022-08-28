package me.chimkenu.expunge.guns.weapons.melees;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import org.bukkit.Material;

public class MrCookie extends Melee {
    public MrCookie() {
        super(5, 3, 2, 1, "&6Mr. Cookie", Material.COOKIE, Tier.SPECIAL, Slot.SECONDARY);
    }
}
