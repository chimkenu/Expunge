package me.chimkenu.expunge.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.items.utilities.Utility;
import me.chimkenu.expunge.items.utilities.healing.*;
import me.chimkenu.expunge.items.utilities.throwable.*;
import me.chimkenu.expunge.items.utilities.throwable.Throwable;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {
    public static ArrayList<Gun> getGuns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Gun> getTier1Guns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getGun().getTier() == Tier.TIER1) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Gun> getTier2Guns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getGun().getTier() == Tier.TIER2) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Gun> getSpecialGuns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getGun().getTier() == Tier.SPECIAL) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Healing> getHealings() {
        ArrayList<Healing> healings = new ArrayList<>();
        healings.add(new Adrenaline());
        healings.add(new Pills());
        healings.add(new Medkit());
        healings.add(new Defibrillator());
        return healings;
    }

    public static ArrayList<Melee> getMelees() {
        ArrayList<Melee> melees = new ArrayList<>();
        for (Weapons.Melees weapon : Weapons.Melees.values()) {
            melees.add(weapon.getMelee());
        }
        return melees;
    }

    public static ArrayList<Throwable> getThrowables() {
        ArrayList<Throwable> throwables = new ArrayList<>();
        throwables.add(new Grenade());
        throwables.add(new Molotov());
        throwables.add(new Spit());
        throwables.add(new FreshAir());
        throwables.add(new Bile());
        return throwables;
    }

    public static Gun getPlayerHeldGun(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        Damageable damageable = (Damageable) meta;
        if (damageable.getDamage() > 0) return null;
        for (Gun gun : getGuns()) {
            ItemMeta gunMeta = gun.getWeapon().getItemMeta();
            if (gunMeta == null) continue;
            if (item.getType() == gun.getWeapon().getType() && meta.hasDisplayName() && gunMeta.hasDisplayName() && meta.getDisplayName().equals(gunMeta.getDisplayName())) {
                return gun;
            }
        }
        return null;
    }

    public static Utility getPlayerHeldUtility(ItemStack item) {
        ArrayList<Utility> utilities = new ArrayList<>();
        utilities.addAll(getHealings());
        utilities.addAll(getThrowables());
        for (Utility utility : utilities) {
            if (item.isSimilar(utility.getUtility())) {
                return utility;
            }
        }
        return null;
    }

    public static Melee getPlayerHeldMelee(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        for (Melee melee : getMelees()) {
            ItemMeta meleeMeta = melee.getWeapon().getItemMeta();
            if (meleeMeta == null) continue;
            if (item.getType() == melee.getWeapon().getType() && meta.hasDisplayName() && meleeMeta.hasDisplayName() && meta.getDisplayName().equals(meleeMeta.getDisplayName())) {
                return melee;
            }
        }
        return null;
    }

    public static Throwable getThrowableFromProjectile(Projectile projectile) {
        for (Throwable throwable : getThrowables()) {
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
