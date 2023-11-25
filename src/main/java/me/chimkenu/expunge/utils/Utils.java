package me.chimkenu.expunge.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.utilities.throwable.Throwable;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static GameItem getGameItemFromItemStack(ItemStack itemStack) {
        for (GameItems gameItems : GameItems.values()) {
            GameItem gameItem = gameItems.getGameItem();
            ItemStack itemForComparison = gameItem.get();
            if (itemForComparison.getType() == itemStack.getType() && itemForComparison.displayName().examinableName().equalsIgnoreCase(itemStack.displayName().examinableName())) return gameItem;
        }
        return null;
    }

    public static Throwable getThrowableFromProjectile(Projectile projectile) {
        for (GameItems gameItems : GameItems.getThrowables()) {
            Throwable throwable = (Throwable) gameItems.getGameItem();
            if (projectile.getScoreboardTags().contains(throwable.getTag())) {
                return throwable;
            }
        }
        return null;
    }

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

    public static ItemStack getSkull(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setPlayerProfile(Bukkit.createProfile(UUID.randomUUID(), null));
        PlayerProfile playerProfile = skullMeta.getPlayerProfile();
        if (playerProfile != null) playerProfile.setProperty(new ProfileProperty("textures", url));
        skullMeta.setPlayerProfile(playerProfile);
        skull.setItemMeta(skullMeta);
        return skull;
    }

    public static ItemStack getSkull(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
