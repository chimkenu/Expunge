package me.chimkenu.expunge.mobs.uncommon;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.Common;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Robot extends Common {
    public Robot(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, difficulty, mob -> {
            if (mob.getTarget() == null) {
                mob.setTarget(getRandomPlayer(world));
            }
        });
        putOnClothes(getMob());
        getMob().getEquipment().setHelmet(Utils.getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTQ5YzQ3ZTg3YjRmZmUzN2U1OGZmYmJiYjQxYzRkMzYxNGE3NjU3YWFjNGE1YWFjNTRhNDBiNWZkMTc0ZGEzYiJ9fX0="));
        getMob().addScoreboardTag("ROBOT");
        ((Ageable) getMob()).setAdult();
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
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE, 75, 87, 75));
        equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS, 32, 152, 139));
        equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS, 39, 44, 33));
    }
}
