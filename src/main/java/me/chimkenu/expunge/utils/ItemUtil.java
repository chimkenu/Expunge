package me.chimkenu.expunge.utils;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ItemUtil {
    public static ItemStack setDestroyable(ItemStack item, List<Material> blocks) {
        var serialized = item.serialize();
        serialized.putIfAbsent("components", new LinkedHashMap<>());
        @SuppressWarnings("unchecked")
        var o = (Map<String, String>) serialized.get("components");
        o.put("minecraft:can_break", "[" + listToNbt(blocks) + "]");
        return ItemStack.deserialize(serialized);
    }

    private static String listToNbt(List<Material> blocks) {
        StringBuilder s = new StringBuilder();
        for (var b : blocks) {
            s.append("{blocks:\"minecraft:").append(b.toString().toLowerCase()).append("\"},");
        }
        return s.substring(0, s.length() - 1);
    }

    public static void putOnRandomClothes(EntityEquipment equipment) {
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE));
        equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS));
        equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS));
    }

    public static ItemStack getDyedArmor(Material material, int red, int green, int blue) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(red, green, blue));
            item.setItemMeta(meta);
        }
        return item;
    }

    public static ItemStack getDyedArmor(Material material) {
        return getDyedArmor(material, ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255));
    }

    // TAKEN FROM https://www.spigotmc.org/threads/how-to-create-heads-with-custom-base64-texture.352562
    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        var meta = (SkullMeta) skull.getItemMeta();
        if (meta == null) {
            return skull;
        }

        var profile = Bukkit.createPlayerProfile(UUID.fromString("487087e2-c51b-4442-adc9-98f7eae883e5"));
        var texture = profile.getTextures();
        try {
            texture.setSkin(URI.create("http://textures.minecraft.net/texture/" + url).toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        profile.setTextures(texture);
        meta.setOwnerProfile(profile);

        skull.setItemMeta(meta);
        return skull;
    }

    public static ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(player);
            skull.setItemMeta(skullMeta);
        }
        return skull;
    }
}
