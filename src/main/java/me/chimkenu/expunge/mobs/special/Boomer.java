package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.*;
import org.bukkit.entity.*;

public class Boomer extends GameMob{
    public <T extends Mob> Boomer(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Creeper.class, mob -> {
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer());
            else if (mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 4 * 4) {
                mob.damage(2);
            }
        });
        Creeper creeper = (Creeper) getMob();
        creeper.setPowered(true);
        creeper.setMaxFuseTicks(20 * 60);
        creeper.setExplosionRadius(1);
        creeper.setHealth(1);
        getMob().addScoreboardTag("SPECIAL");
        getMob().addScoreboardTag("BOOMER");
    }
}
