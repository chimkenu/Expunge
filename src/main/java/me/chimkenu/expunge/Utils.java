package me.chimkenu.expunge;

import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.guns.guns.*;
import me.chimkenu.expunge.guns.melees.Melee;
import me.chimkenu.expunge.guns.utilities.*;
import me.chimkenu.expunge.guns.utilities.Throwable;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

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
            if (gun.getTier() == Weapons.Tier.TIER1) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Gun> getTier2Guns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getTier() == Weapons.Tier.TIER2) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Gun> getSpecialGuns() {
        ArrayList<Gun> guns = new ArrayList<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getTier() == Weapons.Tier.SPECIAL) guns.add(gun.getGun());
        }
        return guns;
    }

    public static ArrayList<Healing> getHealings() {
        ArrayList<Healing> healings = new ArrayList<>();
        healings.add(new Adrenaline());
        healings.add(new Pills());
        healings.add(new Medkit());
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
        return throwables;
    }

    public static Gun getPlayerHeldGun(ItemStack item) {
        for (Gun gun : getGuns()) {
            if (item.isSimilar(gun.getGun())) {
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
        for (Melee melee : getMelees()) {
            if (item.isSimilar(melee.getMelee())) {
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

    public static <T extends Gun> Weapons.Guns getEnumFromGun(Class<T> gunToCheck) {
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            if (gun.getGun().getClass().equals(gunToCheck)) {
                return gun;
            }
        }
        return null;
    }
}
