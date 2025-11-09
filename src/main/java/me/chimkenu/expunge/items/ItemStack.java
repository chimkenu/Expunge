package me.chimkenu.expunge.items;

public record ItemStack(
        GameItem item,
        org.bukkit.inventory.ItemStack stack
) {}
