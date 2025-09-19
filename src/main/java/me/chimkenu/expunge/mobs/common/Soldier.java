package me.chimkenu.expunge.mobs.common;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.mobs.MobSettings;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;

import static me.chimkenu.expunge.utils.ItemUtil.getDyedArmor;

public class Soldier extends Common {
    public Soldier(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);

        var equipment = mob.getEquipment();
        assert equipment != null;
        equipment.setHelmet(ItemUtil.getSkull("c67a001af73dde6a49c8e687fe7a66e2245628e8ee7078f2ae7e90303d770ce9"));
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE, 78, 101, 24));
        equipment.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        equipment.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        equipment.setItemInMainHand(new ItemStack(Material.STONE_SWORD));

        ((Ageable) mob).setAdult();
    }
}
