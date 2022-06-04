package me.chimkenu.expunge.guns;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;

public class HeadshotCalculator {

    // TODO: make one for smaller entities?
    public static boolean isHeadshot(Player shooter, LivingEntity target, int range) {
        double x = target.getLocation().getX();
        double y = target.getLocation().getY();
        double z = target.getLocation().getZ();
        BoundingBox head = new BoundingBox(x + 0.25, y + 1.5, z + 0.25, x - 0.25, y + 2.0, z - 0.25);
        if (target.getType() == EntityType.PLAYER && ((Player) target).isSneaking()) {
            head = new BoundingBox(x + 0.25, y + 1, z + 0.25, x - 0.25, y + 1.5, z - 0.25);
        }
        RayTrace ray = new RayTrace(shooter.getEyeLocation().toVector(), shooter.getEyeLocation().getDirection());
        return ray.intersects(head, range, 0.05);
    }
}
