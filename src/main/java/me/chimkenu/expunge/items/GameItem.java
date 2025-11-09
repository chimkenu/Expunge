package me.chimkenu.expunge.items;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.item.ItemEntity;
import me.chimkenu.expunge.enums.Slot;
import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Explosive.class, name = "Explosive"),
        @JsonSubTypes.Type(value = Gun.class, name = "Gun"),
        @JsonSubTypes.Type(value = Healing.class, name = "Healing"),
        @JsonSubTypes.Type(value = Melee.class, name = "Melee"),
        @JsonSubTypes.Type(value = Throwable.class, name = "Throwable")
})
public sealed interface GameItem
        permits Healing, Interactable, Throwable, Weapon {
    String id();
    String name();
    Material material();
    Tier tier();
    Slot slot();
    int swapCooldown();
    default NamespacedKey namespacedKey() {
        return new NamespacedKey(Expunge.getPlugin(Expunge.class), id());
    }
    default ItemStack toItem() {
        var item = new ItemStack(material());
        var meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtil.format(name()));
            meta.getPersistentDataContainer().set(namespacedKey(), PersistentDataType.BOOLEAN, true);
            item.setItemMeta(meta);
        }
        return item;
    }
    default GameEntity spawn(World world, Vector position, boolean isInvulnerable) {
        Item item = world.spawn(position.toLocation(world), Item.class, false, null);
        item.setItemStack(toItem());
        item.setInvulnerable(isInvulnerable);
        return new ItemEntity(this, item);
    }
}
