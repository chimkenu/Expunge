package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Wanderer extends GameMob {
    public Wanderer(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Zombie.class, mob -> {
            if (!mob.getScoreboardTags().contains("WANDERER")) {
                if (mob.getTarget() != null) {
                    Location mobLoc = mob.getLocation();
                    Location targetLoc = mob.getTarget().getLocation();
                    double distance = mobLoc.distanceSquared(targetLoc);
                    int speed = Expunge.difficulty;
                    if (distance > 5 * 5) speed += Math.max(1, Expunge.difficulty);
                    if (mob.getHealth() < 20) speed += Math.max(1, Expunge.difficulty);
                    if (mob.getHealth() < 7) speed *= 2;
                    if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 2;
                    mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, Math.max(speed - 1, 0), false, false));
                } else
                    mob.setTarget(getRandomPlayer());
            }
        });
        putOnRandomClothes(getMob());
        getMob().addScoreboardTag("WANDERER");
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.5);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (getMob().getEquipment() != null) getMob().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }
}
