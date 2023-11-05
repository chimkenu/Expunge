package me.chimkenu.expunge.utils;

import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.guns.utilities.Utility;
import me.chimkenu.expunge.guns.utilities.healing.*;
import me.chimkenu.expunge.guns.utilities.throwable.*;
import me.chimkenu.expunge.guns.utilities.throwable.Throwable;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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
}