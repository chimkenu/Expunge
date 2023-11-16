package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class OxygenTank extends Explosive {
    public OxygenTank(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTVkYjM2YmFlOWExNTFlZGI4YmY4ZjhiMDY3NDEyMjFlYTE2M2ZmOTExYWFhZTNhNDJlMTgxMTgzZDdjYmRmNSJ9fX0=");
    }

    @Override
    public void onInteract(GameManager gameManager, LivingEntity actor) {

    }
}
