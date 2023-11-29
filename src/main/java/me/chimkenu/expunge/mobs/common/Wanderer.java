package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.mobs.Common;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Wanderer extends Common {
    public Wanderer(JavaPlugin plugin, World world, Vector locationToSpawn, Difficulty difficulty) {
        super(plugin, world, locationToSpawn, difficulty, mob -> {
            if (!mob.getScoreboardTags().contains("WANDERER") && mob.getTarget() == null) {
                mob.setTarget(getRandomPlayer(world));
            }
        });
        Utils.putOnRandomClothes(getMob().getEquipment());
        getMob().addScoreboardTag("WANDERER");
        getMob().getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
    }
}
