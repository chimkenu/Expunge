package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Rider extends GameMob {
    public <T extends Mob> Rider(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Spider.class, mob -> {
            if (mob.getVehicle() instanceof Player target) {
                mob.setTarget(target);
                target.getInventory().setHeldItemSlot(5);
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
                                if (mob.getTarget() != null && mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 4 * 4) {
                                    player.getInventory().setHeldItemSlot(5);
                                    player.addPassenger(mob);
                                }
                            }
                        }.runTaskLater(plugin, 3);
                    }
                } else mob.setTarget(getRandomPlayer(world));
            }
        });
        getMob().addScoreboardTag("SPECIAL");
        getMob().addScoreboardTag("JOCKEY");
    }
}
