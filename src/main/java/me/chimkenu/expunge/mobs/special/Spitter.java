package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.guns.utilities.throwable.Spit;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Spitter extends GameMob {
    public <T extends Mob> Spitter(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, ZombieVillager.class, mob -> {
            double distance = 0;
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer(world));
            else distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
            if (!mob.hasPotionEffect(PotionEffectType.CONFUSION) && distance < 10 * 10) {
                // spit at player direction
                mob.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 20, 0, true, true, false));
                new Spit().use(mob);

                // TODO: run away
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false));
        });

        getMob().addScoreboardTag("SPITTER");
    }
}
