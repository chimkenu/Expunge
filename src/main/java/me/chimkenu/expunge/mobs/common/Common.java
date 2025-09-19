package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.MobGoal;
import me.chimkenu.expunge.mobs.MobSettings;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Common extends MobGoal {
    public Common(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
        ItemUtil.putOnRandomClothes(Objects.requireNonNull(mob.getEquipment()));
        mob.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }

    @Override
    public boolean canUse() {
        var target = mob.getTarget();
        if (target != null) {
            Location mobLoc = mob.getLocation();
            Location targetLoc = target.getLocation();
            double distance = mobLoc.distanceSquared(targetLoc);
            int speed = 1;
            if (distance > 30 * 30) speed += 1;
            if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 1;
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
        }
        return false;
    }
}
