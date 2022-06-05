package me.chimkenu.expunge.guns.utilities;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Utility {

    private final int cooldown;
    private final Material material;
    private final String name;

    public Utility(int cooldown, Material material, String name) {
        this.cooldown = cooldown;
        this.material = material;
        this.name = name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemStack getUtility() {
        ItemStack utility = new ItemStack(material);
        ItemMeta meta = utility.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        utility.setItemMeta(meta);
        return utility;
    }

    public abstract void use(Player player);
}
