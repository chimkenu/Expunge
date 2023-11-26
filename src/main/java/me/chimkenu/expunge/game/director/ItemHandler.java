package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.ItemRandomizer;
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
    private final List<Entity> entities;

    public ItemHandler(Director director) {
        this.director = director;
        this.entities = new ArrayList<>();
    }

    public void generateStartingItems() {
        CampaignMap map = director.getMap();
        for (ItemRandomizer randomizer : map.randomizedGameItems()) {
            randomizer.randomize(director.getGameManager());
        }

        // Spawn ammo
        for (Vector v : map.ammoLocations()) {
            spawnAmmo(v);
        }
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
        addEntity(item);
    }

    public void spawnUtility(Vector loc, Utility utility, boolean isInvulnerable) {
        Item item = director.getWorld().spawn(loc.toLocation(director.getWorld()), Item.class);
        item.setItemStack(utility.get());
        item.setInvulnerable(isInvulnerable);
        addEntity(item);
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
}
