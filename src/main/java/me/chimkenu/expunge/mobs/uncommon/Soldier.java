package me.chimkenu.expunge.mobs.uncommon;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.Common;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Soldier extends Common {
    public Soldier(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, difficulty, mob -> {
            if (mob.getTarget() == null) {
                mob.setTarget(getRandomPlayer(world));
            }
        });
        putOnClothes(getMob());
        getMob().getEquipment().setHelmet(Utils.getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY3YTAwMWFmNzNkZGU2YTQ5YzhlNjg3ZmU3YTY2ZTIyNDU2MjhlOGVlNzA3OGYyYWU3ZTkwMzAzZDc3MGNlOSJ9fX0="));
        getMob().addScoreboardTag("SOLDIER");
        ((Ageable) getMob()).setAdult();
        getMob().getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
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
