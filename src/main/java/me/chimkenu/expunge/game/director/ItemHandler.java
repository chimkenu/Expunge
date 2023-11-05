package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.enums.Utilities;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.guns.utilities.Utility;
import me.chimkenu.expunge.guns.weapons.Weapon;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemHandler {
    private final Director director;

    public ItemHandler(Director director) {
        this.director = director;
    }

    public void generateItems() {
        CampaignMap map = director.getMap();

        int itemsToSpawn = map.baseItemsToSpawn();
        itemsToSpawn += (int) (4 * (1 - director.calculateRating()));
        for (int i = 0; i < itemsToSpawn; i++) {
            double r = Math.random();

            // throwable - 50% chance
            if (r < 0.5) {
                Utilities.Throwables[] throwables = Utilities.Throwables.values();
                spawnUtilityAtRandom(throwables[ThreadLocalRandom.current().nextInt(0, throwables.length)].getUtility());
            }

            // healing item - 50% chance
            else {
                Utilities.Healings[] healings = new Utilities.Healings[2];
                healings[0] = Utilities.Healings.PILLS;
                healings[1] = Utilities.Healings.ADRENALINE;
                spawnUtilityAtRandom(healings[ThreadLocalRandom.current().nextInt(0, healings.length)].getUtility());
            }
        }

        itemsToSpawn = 1;
        itemsToSpawn += (int) (2 * (1 - director.calculateRating()));
        for (int i = 0; i < itemsToSpawn; i++) {
            double r = Math.random();

            // primary - 60% chance
            if (r < 0.6) {
                Weapons.Guns[] primaries = Weapons.Guns.values();
                Weapons.Guns primary = primaries[ThreadLocalRandom.current().nextInt(0, primaries.length)];
                spawnGunAtRandom(primary.getGun());
            }

            // melee - 40% chance
            else {
                Weapons.Melees[] melees = Weapons.Melees.values();
                Weapons.Melees melee = melees[ThreadLocalRandom.current().nextInt(0, melees.length)];
                spawnMeleeAtRandom(melee.getMelee());
            }
        }

        // Spawn ammo
        for (Vector v : map.ammoLocations()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute in minecraft:overworld run summon falling_block " + v.getX() + " " + v.getY() + " " + v.getZ() + " {BlockState:{Name:\"minecraft:gray_candle\",Properties:{candles:\"4\",lit:\"false\",waterlogged:\"false\"}},NoGravity:1b,Glowing:1b,Time:-2147483648,Tags:[\"AMMO_PILE\"],Invulnerable:1b,CustomName:'[{\"text\":\"Ammo Pile\",\"color\":\"blue\"},{\"text\":\" (Right Click)\",\"color\":\"gray\"}]',CustomNameVisible:1b}");
        }
    }

    public void spawnGunAtRandom(Gun gun) {
        CampaignMap map = director.getMap();
        Vector[] weaponLocations = map.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index].toLocation(director.getWorld()), gun, false);
    }

    public void spawnMeleeAtRandom(Melee melee) {
        CampaignMap map = director.getMap();
        Vector[] weaponLocations = map.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index].toLocation(director.getWorld()), melee, false);
    }

    public void spawnWeapon(Location loc, Weapon weapon, boolean isInvulnerable) {
        Item item = director.getWorld().dropItem(loc, weapon.getWeapon());
        if (isInvulnerable) {
            ItemMeta meta = item.getItemStack().getItemMeta();
            if (meta != null && meta.getLore() != null) {
                meta.getLore().add("invulnerable");
            }
            item.getItemStack().setItemMeta(meta);
        }
        item.addScoreboardTag("ITEM");
        item.setInvulnerable(isInvulnerable);
    }

    public void spawnUtilityAtRandom(Utility utility) {
        CampaignMap map = director.getMap();
        Vector[] itemLocations = map.itemLocations();
        if (itemLocations.length < 1) return;
        int index = itemLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, itemLocations.length);
        spawnUtility(itemLocations[index].toLocation(director.getWorld()), utility);
    }

    public void spawnUtility(Location loc, Utility utility) {
        Item item = director.getWorld().spawn(loc, Item.class);
        item.setItemStack(utility.getUtility());
        item.addScoreboardTag("ITEM");
    }

    public static Melee getRandomMelee(Tier tier) {
        ArrayList<Weapons.Melees> melees = new ArrayList<>(List.of(Weapons.Melees.values()));
        melees.removeIf(melee -> melee.getMelee().getTier() != tier);
        return melees.get(ThreadLocalRandom.current().nextInt(0, melees.size())).getMelee();
    }

    public static Gun getRandomGun(Tier tier) {
        switch (tier) {
            case TIER1 -> {
                return Utils.getTier1Guns().get(ThreadLocalRandom.current().nextInt(0, Utils.getTier1Guns().size()));
            }
            case TIER2 -> {
                return Utils.getTier2Guns().get(ThreadLocalRandom.current().nextInt(0, Utils.getTier2Guns().size()));
            }
            default -> {
                return Utils.getSpecialGuns().get(ThreadLocalRandom.current().nextInt(0, Utils.getSpecialGuns().size()));
            }
        }
    }
}
