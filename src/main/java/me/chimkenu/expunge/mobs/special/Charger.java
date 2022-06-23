package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zoglin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Charger extends GameMob {
    public <T extends Mob> Charger(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Zoglin.class, mob -> {
            double distance = 0;
            if (mob.getTarget() == null) mob.setTarget(getRandomPlayer());
            else distance = mob.getLocation().distanceSquared(mob.getTarget().getLocation());
            if (!mob.hasPotionEffect(PotionEffectType.CONFUSION) && distance > 5 * 5 && distance < 10 * 10) {
                // charge at player
                mob.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 0, true, true, false));
                new BukkitRunnable() {
                    int time = 0;
                    final Vector direction = mob.getLocation().getDirection().setY(0).normalize().multiply(1.5);
                    @Override
                    public void run() {
                        if (mob.isDead())
                            this.cancel();
                        mob.setVelocity(mob.getVelocity().add(direction));

                        if (time >= 5)
                            this.cancel();
                        else if (!mob.hasPotionEffect(PotionEffectType.CONFUSION))
                            this.cancel();
                        time++;
                    }
                }.runTaskTimer(Expunge.instance, 0, 10);

                // stun
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!mob.isDead()) {
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 200, false, false, false));
                            mob.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 5, 200, false, false, false));
                        }
                    }
                }.runTaskLater(Expunge.instance, 10);
                mob.setVelocity(mob.getVelocity().add(mob.getLocation().getDirection().normalize().multiply(3)).add(new Vector(0, 0.1, 0)));
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 1, false, false));
        });
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(4);
            getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(400 + (Expunge.difficulty * 200));
            getMob().setHealth(400 + (Expunge.difficulty * 200));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getMob().addScoreboardTag("CHARGER");
    }
}
