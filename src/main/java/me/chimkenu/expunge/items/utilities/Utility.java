package me.chimkenu.expunge.items.utilities;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public interface Utility extends GameItem {
    @Override
    default ItemStack get() {
        ItemStack utility = new ItemStack(getMaterial());
        ItemMeta meta = utility.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));
        }
        utility.setItemMeta(meta);
        return utility;
    }

    void use(JavaPlugin plugin, GameManager gameManager, LivingEntity entity);
}
