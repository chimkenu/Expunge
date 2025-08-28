package me.chimkenu.expunge.items;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

public sealed interface Interactable extends GameItem permits Explosive {
    Entity spawn(Location locationToSpawn);
    void onDamage(GameManager manager, Entity interactable, Entity actor);
    void onInteract(GameManager manager, Entity interactable, Entity actor);
}
