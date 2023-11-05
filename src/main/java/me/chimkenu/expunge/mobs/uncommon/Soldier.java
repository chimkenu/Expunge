package me.chimkenu.expunge.mobs.uncommon;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Soldier extends GameMob {
    public <T extends Mob> Soldier(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
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
        putOnClothes(getMob());
        getMob().addScoreboardTag("SOLDIER");
        ((Ageable) getMob()).setAdult();
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.25 + (difficulty.ordinal() * 0.25));
            getMob().getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void putOnClothes(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(78, 101, 24));
            chestplate.setItemMeta(meta);
        }
        equipment.setChestplate(chestplate);

        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
    }
}
