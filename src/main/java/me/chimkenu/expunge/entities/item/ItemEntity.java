package me.chimkenu.expunge.entities.item;

import me.chimkenu.expunge.entities.Regenerable;
import me.chimkenu.expunge.entities.RegenerableState;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ItemEntity implements GameEntity, Regenerable {
    private final GameItem gameItem;
    private final org.bukkit.entity.Item itemEntity;

    public ItemEntity(GameItem gameItem, org.bukkit.entity.Item itemEntity) {
        this.gameItem = gameItem;
        this.itemEntity = itemEntity;
    }

    public me.chimkenu.expunge.items.ItemStack getItem() {
        return new me.chimkenu.expunge.items.ItemStack(gameItem, itemEntity.getItemStack());
    }

    @Override
    public Location getLocation() {
        return itemEntity.getLocation();
    }

    @Override
    public void setLocation(Location l) {
        itemEntity.teleport(l);
    }

    @Override
    public Vector getVelocity() {
        return itemEntity.getVelocity();
    }

    @Override
    public void setVelocity(Vector v) {
        itemEntity.setVelocity(v);
    }

    @Override
    public void remove() {
        itemEntity.remove();
    }

    @Override
    public boolean isDead() {
        return itemEntity.isDead();
    }

    @Override
    public String getIdentifier() {
        return itemEntity.getType() + " - " + itemEntity.getItemStack().getType() + " - " + itemEntity.getUniqueId();
    }

    @Override
    public org.bukkit.entity.Item getHandle() {
        return itemEntity;
    }

    @Override
    public RegenerableState<ItemEntity> getState() {
        return new State(
                itemEntity.getLocation(),
                gameItem,
                itemEntity.getItemStack().clone()
        );
    }

    private record State(
            Location location,
            GameItem gameItem,
            ItemStack itemStack
    ) implements RegenerableState<ItemEntity> {
        @Override
        public ItemEntity regenerate(World world) {
            var itemEntity = world.spawn(location, Item.class, false, null);
            itemEntity.setItemStack(itemStack);
            return new ItemEntity(gameItem, itemEntity);
        }
    }
}
