package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.items.utilities.Utility;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public interface Throwable extends Utility {
    String getTag();

    void onLand(JavaPlugin plugin, World world, Location loc, Entity shooter);

    @Override
    default int getCooldown() {
        return 20;
    }

    @Override
    default Slot getSlot() {
        return Slot.TERTIARY;
    }
}
