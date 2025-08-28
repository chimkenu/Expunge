package me.chimkenu.expunge.game.director;

import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.game.ItemRandomizer;
import me.chimkenu.expunge.items.Items;
import me.chimkenu.expunge.items.Weapon;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class ItemHandler {
    private final Director director;
    private final Items items;
    private final List<Entity> entities;

    public ItemHandler(Director director, Items items) {
        this.director = director;
        this.items = items;
        this.entities = new ArrayList<>();
    }

    public void generateStartingItems() {
        CampaignMap map = director.getMap();
        for (ItemRandomizer randomizer : map.randomizedGameItems()) {
            randomizer.randomize(director.getGameManager(), items);
        }

        // Spawn ammo
        for (Vector v : map.ammoLocations()) {
            spawnAmmo(v);
        }
    }

    public void spawnWeapon(Vector loc, String id, boolean isInvulnerable) {
        Item item = director.getWorld().dropItem(loc.toLocation(director.getWorld()), items.toGameItem(id).toItem());
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

    public void spawnUtility(Vector loc, String id, boolean isInvulnerable) {
        Item item = director.getWorld().spawn(loc.toLocation(director.getWorld()), Item.class);
        item.setItemStack(items.toGameItem(id).toItem());
        item.setInvulnerable(isInvulnerable);
        addEntity(item);
    }

    private void spawnAmmo(Vector loc) {
        FallingBlock ammoPile = director.getWorld().spawnFallingBlock(
                loc.toLocation(director.getWorld()),
                Material.GRAY_CANDLE.createBlockData("[candles=4,lit=false,waterlogged=false]")
        );
        ammoPile.setGravity(false);
        ammoPile.setGlowing(true);
        ammoPile.setDropItem(false);
        ammoPile.setCancelDrop(true);
        ammoPile.setInvulnerable(true);
        ammoPile.setCustomName(ChatUtil.format("Ammo Pile (Right Click)"));
        ammoPile.setCustomNameVisible(true);
        ammoPile.addScoreboardTag("AMMO_PILE");
        addEntity(ammoPile);
    }

    private void setItemProperties(Item item) {
        item.setGlowing(true);
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
