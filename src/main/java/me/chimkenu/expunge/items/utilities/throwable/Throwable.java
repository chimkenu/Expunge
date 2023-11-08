package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.items.utilities.Utility;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;

public interface Throwable extends Utility {
    String getTag();

    void onLand(World world, Location loc, Entity shooter);

    @Override
    default int getCooldown() {
        return 20;
    }

    @Override
    default Slot getSlot() {
        return Slot.TERTIARY;
    }
}
