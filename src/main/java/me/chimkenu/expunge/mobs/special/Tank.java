package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.enums.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Tank extends Special {
    public Tank(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, IronGolem.class, mob -> {
            if (mob.getTarget() == null || (!(mob.getTarget() instanceof Player player) || player.getGameMode() != GameMode.ADVENTURE)) mob.setTarget(getRandomPlayer(world));
            int speed = 1;
            if (mob.getFireTicks() > 0) speed += 2;
            if (mob.getTarget() != null) {
                if (mob.getLocation().distanceSquared(mob.getTarget().getLocation()) < 10 * 10 && ThreadLocalRandom.current().nextDouble() < 0.1) {
                    mob.setVelocity(mob.getVelocity().add(mob.getLocation().getDirection().normalize().multiply(3)).add(new Vector(0, 0.1, 0)));
                }
            }
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
            mob.removePotionEffect(PotionEffectType.SLOW);
            mob.removePotionEffect(PotionEffectType.WEAKNESS);
        });
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(10 + (difficulty.ordinal() * 3));
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(2000);
        getMob().setHealth(2000);
        getMob().setAbsorptionAmount(500 + (difficulty.ordinal() * 1000));
        getMob().addScoreboardTag("TANK");
    }

    @Override
    protected void playJingle() {

    }
}
