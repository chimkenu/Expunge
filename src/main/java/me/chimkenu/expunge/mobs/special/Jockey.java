package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Jockey extends GameMob {
    public <T extends Mob> Jockey(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Spider.class, mob -> {
            if (mob.getVehicle() instanceof Player target) {
                mob.setTarget(target);
                target.getInventory().setHeldItemSlot(8);
                target.setVelocity(target.getVelocity().add(mob.getEyeLocation().getDirection()));
                target.damage(0.5, mob);
            } else {
                if (mob.getTarget() instanceof Player player) {
                    double distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
                    int speed = 1;
                    if (distance > 10 * 10) speed += 2;
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
                    if (distance < 3 * 3 && Math.random() < 0.5) {
                        mob.setVelocity(mob.getVelocity().add(mob.getEyeLocation().getDirection().normalize().multiply(2)).add(new Vector(0, 0.25, 0)));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.getInventory().setHeldItemSlot(8);
                                player.addPassenger(mob);
                            }
                        }.runTaskLater(Expunge.instance, 5);
                    }
                } else mob.setTarget(getRandomPlayer());
            }
        });
        getMob().addScoreboardTag("SPECIAL");
        getMob().addScoreboardTag("JOCKEY");
    }
}
