package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.MobBehavior;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Horde extends GameMob {
    public <T extends Mob> Horde(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Zombie.class, mob -> {
            if (mob.getTarget() != null) {
                Location mobLoc = mob.getLocation();
                Location targetLoc = mob.getTarget().getLocation();
                double distance = mobLoc.distanceSquared(targetLoc);
                int speed = 1;
                if (distance > 5 * 5) speed += 1;
                if (distance > 10 * 10) speed += 1;
                if (distance > 15 * 15) speed += 2;
                if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 2;
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
            } else
                mob.setTarget(getRandomPlayer());
        });
        putOnRandomClothes(getMob());
        getMob().addScoreboardTag("HORDE");
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.5);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}