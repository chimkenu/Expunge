package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Witch extends GameMob {
    public <T extends Mob> Witch(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Enderman.class, mob -> {
            if (mob.getTarget() != null) {
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 200, false, false, false));
            }
        });
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(39);
            getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
            getMob().setHealth(1000);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
