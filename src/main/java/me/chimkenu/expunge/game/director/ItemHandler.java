package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.items.utilities.Utility;
import me.chimkenu.expunge.items.utilities.healing.Adrenaline;
import me.chimkenu.expunge.items.utilities.healing.Pills;
import me.chimkenu.expunge.items.weapons.Weapon;
import me.chimkenu.expunge.items.weapons.guns.Gun;
import me.chimkenu.expunge.items.weapons.melees.Melee;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class ItemHandler {
    private final Director director;
    private final Set<Entity> entities;

    public ItemHandler(Director director) {
        this.director = director;
        this.entities = new HashSet<>();
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
                List<GameItems> throwables = GameItems.getThrowables();
                spawnUtilityAtRandom((Utility) throwables.get(ThreadLocalRandom.current().nextInt(throwables.size())).getGameItem());
            }

            // healing item - 50% chance
            else {
                r = Math.random();
                if (r < 0.5)
                    spawnUtilityAtRandom(new Pills());
                else
                    spawnUtilityAtRandom(new Adrenaline());
            }
        }

        itemsToSpawn = 1;
        itemsToSpawn += (int) (2 * (1 - directorRating));
        for (int i = 0; i < itemsToSpawn; i++) {
            List<GameItems> weapons = GameItems.getWeapons();
            spawnWeaponAtRandom((Weapon) weapons.get(ThreadLocalRandom.current().nextInt(weapons.size())).getGameItem());
        }

        // Spawn ammo
        for (Vector v : map.ammoLocations()) {
            spawnAmmo(v);
        }
    }

    public void spawnWeaponAtRandom(Weapon weapon) {
        CampaignMap map = director.getMap();
        Vector[] weaponLocations = map.weaponLocations();
        if (weaponLocations.length < 1) return;
        int index = weaponLocations.length == 1 ? 0 : ThreadLocalRandom.current().nextInt(0, weaponLocations.length);
        spawnWeapon(weaponLocations[index], weapon, false);
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
        item.setInvulnerable(isInvulnerable);
        setItemProperties(item);
        addEntity(item);
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
        setItemProperties(item);
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
        addEntity(ammoPile);
    }

    private void setItemProperties(Item item) {
        item.setGlowing(true);
        item.setCanMobPickup(false);
        item.setUnlimitedLifetime(true);
        item.addScoreboardTag("ITEM");
    }

    public void clear() {
        entities.forEach(Entity::remove);
        entities.clear();
    }

    public void addEntity(Entity e) {
        if (e instanceof Item item) {
            setItemProperties(item);
        }
        entities.add(e);
    }

    public static Melee getRandomMelee(Tier tier) {
        List<GameItems> melees = GameItems.getMelees();
        melees.removeIf(gameItems -> ((Melee) gameItems.getGameItem()).getTier() != tier);
        return (Melee) melees.get(ThreadLocalRandom.current().nextInt(0, melees.size())).getGameItem();
    }

    public static Gun getRandomGun(Tier tier) {
        List<GameItems> guns = GameItems.getGuns();
        guns.removeIf(gameItem -> ((Gun) gameItem.getGameItem()).getTier() != tier);
        return (Gun) guns.get(ThreadLocalRandom.current().nextInt(guns.size())).getGameItem();
    }
}
