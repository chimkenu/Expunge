package me.chimkenu.expunge.mobs;

import me.chimkenu.expunge.enums.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Common extends GameMob {
    public Common(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty, MobBehavior behavior) {
        super(plugin, world, locationToSpawn, Zombie.class, mob -> {
            if (mob.getTarget() != null) {
                Location mobLoc = mob.getLocation();
                Location targetLoc = mob.getTarget().getLocation();
                double distance = mobLoc.distanceSquared(targetLoc);
                int speed = 2 + Math.min(1, difficulty.ordinal());
                if (distance > 30 * 30) speed += 1;
                if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 1;
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, Math.max(speed - 1, 0), false, false));
            }
            behavior.run(mob);
        });
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0.25 + (difficulty.ordinal() * difficulty.ordinal() * 0.25));
    }
}
