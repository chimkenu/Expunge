package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.Utils;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.game.maps.Map;
import me.chimkenu.expunge.game.maps.Scene;
import me.chimkenu.expunge.guns.utilities.Utility;
import me.chimkenu.expunge.guns.weapons.Weapon;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import me.chimkenu.expunge.guns.weapons.melees.Melee;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ItemHandler {
    private final Map map;
    private int sceneIndex;
    private final World world;

    public ItemHandler(Map map) {
        this.map = map;
        sceneIndex = 0;
        world = map.getWorld();
    }

    public void updateSceneIndex() {
        sceneIndex++;
    }

    public void spawnWeapon(Location loc, Weapon weapon, boolean isInvulnerable) {
        Item item = world.dropItem(loc, weapon.getWeapon());
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

    public void spawnGunAtRandom(Gun gun) {
        Scene scene = map.getScenes().get(sceneIndex);
        Location[] weaponLocations = scene.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index], gun, false);
    }

    public void spawnMeleeAtRandom(Melee melee) {
        Scene scene = map.getScenes().get(sceneIndex);
        Location[] weaponLocations = scene.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index], melee, false);
    }

    public void spawnUtility(Location loc, Utility utility) {
        Item item = world.spawn(loc, Item.class);
        item.setItemStack(utility.getUtility());
        item.addScoreboardTag("ITEM");
    }

    public void spawnUtilityAtRandom(Utility utility) {
        Scene scene = map.getScenes().get(sceneIndex);
        Location[] itemLocations = scene.itemLocations();
        if (itemLocations.length < 1) return;
        int index = itemLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, itemLocations.length);
        spawnUtility(itemLocations[index], utility);
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
