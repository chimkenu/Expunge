package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.enums.Utilities;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.items.utilities.Utility;
import me.chimkenu.expunge.items.weapons.Weapon;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
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

    public void generateStartingItems() {
        CampaignMap map = director.getMap();
        double directorRating = director.calculateRating();
        int itemsToSpawn = map.baseItemsToSpawn();
        itemsToSpawn += (int) (4 * (1 - directorRating));
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
        itemsToSpawn += (int) (2 * (1 - directorRating));
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
            spawnAmmo(v);
        }
    }

    public void spawnGunAtRandom(Gun gun) {
        CampaignMap map = director.getMap();
        Vector[] weaponLocations = map.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index], gun, false);
    }

    public void spawnMeleeAtRandom(Melee melee) {
        CampaignMap map = director.getMap();
        Vector[] weaponLocations = map.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index], melee, false);
    }

    public void spawnWeapon(Vector loc, Weapon weapon, boolean isInvulnerable) {
        Item item = director.getWorld().dropItem(loc.toLocation(director.getWorld()), weapon.get());
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
        spawnUtility(itemLocations[index], utility, false);
    }

    public void spawnUtility(Vector loc, Utility utility, boolean isInvulnerable) {
        Item item = director.getWorld().spawn(loc.toLocation(director.getWorld()), Item.class);
        item.setItemStack(utility.get());
        item.setInvulnerable(isInvulnerable);
        item.setGlowing(true);
        item.addScoreboardTag("ITEM");
    }

    private void spawnAmmo(Vector loc) {
        FallingBlock ammoPile = director.getWorld().spawn(loc.toLocation(director.getWorld()), FallingBlock.class);
        ammoPile.setGravity(false);
        ammoPile.setGlowing(true);
        ammoPile.setDropItem(false);
        ammoPile.setCancelDrop(true);
        ammoPile.setInvulnerable(true);
        ammoPile.customName(Component.text("Ammo Pile (Right Click)"));
        ammoPile.setCustomNameVisible(true);
        ammoPile.setBlockData(Material.GRAY_CANDLE.createBlockData("[candles=4,lit=false,waterlogged=false]"));
        ammoPile.addScoreboardTag("AMMO_PILE");
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
