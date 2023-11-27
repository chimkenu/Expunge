package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.GameItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public interface Interactable extends GameItem {
    default boolean cannotBePickedUp() {
        return false;
    }

    String getTag();

    default double getYOffset() {
        return -1.4;
    }

    Entity spawn(Location locationToSpawn);

    void onInteract(JavaPlugin plugin, GameManager gameManager, Entity entity, Entity actor);
}
