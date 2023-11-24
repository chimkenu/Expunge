package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enderman;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Witch extends GameMob {
    public Witch(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Enderman.class, mob -> {
            if (mob.getTarget() != null) {
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, 200, false, false, false));
            }
        });
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).setBaseValue(39);
        Objects.requireNonNull(getMob().getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(1000);
        getMob().setHealth(1000);
    }
}
