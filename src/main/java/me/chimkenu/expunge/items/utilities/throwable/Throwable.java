package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.items.utilities.Utility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public abstract class Throwable extends Utility {
    private final String tag;

    public Throwable(int cooldown, Material material, String name, Slot slot, String tag) {
        super(cooldown, material, name, slot);
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public abstract void onLand(World world, Location loc, Entity shooter);
}
