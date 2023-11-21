package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Horde extends GameMob {
    public Horde(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, Zombie.class, mob -> {
            if (mob.getTarget() != null) {
                Location mobLoc = mob.getLocation();
                Location targetLoc = mob.getTarget().getLocation();
                double distance = mobLoc.distanceSquared(targetLoc);
                int speed = difficulty.ordinal();
                if (distance > 5 * 5) speed += 1;
                if (mob.getHealth() < 20) speed += 1;
                if (mob.getHealth() < 7) speed += 1;
                if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 2;
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
            } else
                mob.setTarget(getRandomPlayer(world));
        });
        Utils.putOnRandomClothes(getMob().getEquipment());
        getMob().addScoreboardTag("HORDE");
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(0.25 + (difficulty.ordinal() * 0.25));
        getMob().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }
}
