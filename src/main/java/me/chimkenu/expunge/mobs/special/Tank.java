package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Tank extends GameMob {
    public <T extends Mob> Tank(World world, Location locationToSpawn) {
        super(world, locationToSpawn, IronGolem.class, mob -> {
            if (mob.getTarget() == null || (!(mob.getTarget() instanceof Player player) || player.getGameMode() != GameMode.ADVENTURE)) mob.setTarget(getRandomPlayer());
            int speed = 1;
            if (mob.getFireTicks() > 0) speed += 2;
            if (mob.getTarget() != null) {
                if (mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 10 * 10 && Math.random() < 0.1) {
                    mob.setVelocity(mob.getVelocity().add(mob.getLocation().getDirection().normalize().multiply(3)).add(new Vector(0, 0.1, 0)));
                }
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
            mob.removePotionEffect(PotionEffectType.SLOW);
            mob.removePotionEffect(PotionEffectType.WEAKNESS);
        });
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(10 + (Expunge.currentDifficulty.ordinal() * 3));
            getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2000);
            getMob().setHealth(2000);
            getMob().setAbsorptionAmount(500 + (Expunge.currentDifficulty.ordinal() * 1000));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getMob().addScoreboardTag("TANK");
    }
}
