package me.chimkenu.expunge.entities.goals.common;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.MobSettings;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.*;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Mob;

import static me.chimkenu.expunge.utils.ItemUtil.getDyedArmor;

public class Robot extends Common {
    public Robot(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);

        var equipment = mob.getEquipment();
        assert equipment != null;
        equipment.setHelmet(ItemUtil.getSkull("e49c47e87b4ffe37e58ffbbbb41c4d3614a7657aac4a5aac54a40b5fd174da3b"));
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE, 75, 87, 75));
        equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS, 32, 152, 139));
        equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS, 39, 44, 33));

        ((Ageable) mob).setAdult();
    }
}
