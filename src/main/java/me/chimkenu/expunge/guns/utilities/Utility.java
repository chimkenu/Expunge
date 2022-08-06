package me.chimkenu.expunge.guns.utilities;

import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.guns.GameItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Utility extends GameItem {

    public Utility(int cooldown, Material material, String name, Slot slot) {
        super(cooldown, material, name, slot);
    }

    public ItemStack getUtility() {
        ItemStack utility = new ItemStack(getMaterial());
        ItemMeta meta = utility.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
        }
        utility.setItemMeta(meta);
        return utility;
    }

    public abstract void use(LivingEntity entity);
}
