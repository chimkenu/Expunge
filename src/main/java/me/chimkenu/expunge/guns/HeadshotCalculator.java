package me.chimkenu.expunge.guns;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class HeadshotCalculator {

    // TODO: make one for smaller entities?
    public static boolean isHeadshot(RayTrace ray, LivingEntity target, int range) {
        double x = target.getLocation().getX();
        double y = target.getBoundingBox().getMaxY();
        double z = target.getLocation().getZ();
        BoundingBox head = new BoundingBox(x - 0.25, y - 0.5, z - 0.25, x + 0.25, y, z + 0.25);
        return ray.intersects(head, range, 0.05);
    }
}
