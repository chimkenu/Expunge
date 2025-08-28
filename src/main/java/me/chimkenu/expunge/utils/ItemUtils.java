package me.chimkenu.expunge.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
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
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class ItemUtils {
    public static void putOnRandomClothes(EntityEquipment equipment) {
        equipment.setChestplate(getDyedArmor(Material.LEATHER_CHESTPLATE));
        equipment.setLeggings(getDyedArmor(Material.LEATHER_LEGGINGS));
        equipment.setBoots(getDyedArmor(Material.LEATHER_BOOTS));
    }

    public static ItemStack getDyedArmor(Material material) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        if (meta != null) {
            meta.setColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255), ThreadLocalRandom.current().nextInt(255)));
            item.setItemMeta(meta);
        }
        return item;
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
