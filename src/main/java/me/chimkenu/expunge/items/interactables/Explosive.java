package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public abstract class Explosive implements Interactable {
    private final ArmorStand armorStand;

    public Explosive(JavaPlugin plugin, World world, Vector locationToSpawn, String textureURL) {
        armorStand = world.spawn(new Location(world, locationToSpawn.getX(), locationToSpawn.getY(), locationToSpawn.getZ()), ArmorStand.class);
        armorStand.addScoreboardTag("INTERACTABLE");
        armorStand.setItem(EquipmentSlot.HEAD, Utils.getSkull(textureURL));
    }


    public ArmorStand getEntity() {
        return armorStand;
    }

    public void remove() {
        armorStand.remove();
    }
}
