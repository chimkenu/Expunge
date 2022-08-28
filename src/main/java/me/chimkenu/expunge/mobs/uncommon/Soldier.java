package me.chimkenu.expunge.mobs.uncommon;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Soldier extends GameMob {
    public <T extends Mob> Soldier(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Zombie.class, mob -> {
            if (mob.getTarget() != null) {
                Location mobLoc = mob.getLocation();
                Location targetLoc = mob.getTarget().getLocation();
                double distance = mobLoc.distanceSquared(targetLoc);
                int speed = Expunge.currentDifficulty.ordinal();
                if (distance > 5 * 5) speed += 1;
                if (mob.getHealth() < 20) speed += 1;
                if (mob.getHealth() < 7) speed += 1;
                if (Math.abs(mobLoc.getYaw() - targetLoc.getYaw()) < 25) speed += 2;
                mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, speed, false, false));
            } else
                mob.setTarget(getRandomPlayer());
        });
        putOnClothes(getMob());
        getMob().addScoreboardTag("SOLDIER");
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.25 + (Expunge.currentDifficulty.ordinal() * 0.25));
            getMob().getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void putOnClothes(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        if (equipment != null) {

            // chestplate is dyed leather
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
}
