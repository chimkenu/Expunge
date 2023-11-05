package me.chimkenu.expunge.mobs.uncommon;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Robot extends GameMob {
    public <T extends Mob> Robot(World world, Location locationToSpawn) {
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
        getMob().addScoreboardTag("ROBOT");
        ((Ageable) getMob()).setAdult();
        try {
            getMob().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(0.25 + (Expunge.currentDifficulty.ordinal() * 0.25));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private ItemStack getDyedArmor(Material material, int red, int green, int blue) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(red, green, blue));
            item.setItemMeta(meta);
        }
        return item;
    }

    private void putOnClothes(Mob mob) {
        EntityEquipment equipment = mob.getEquipment();
        if (equipment != null) {
            equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE, 75, 87, 75));
            equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS, 32, 152, 139));
            equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS, 39, 44, 33));
        }
    }
}