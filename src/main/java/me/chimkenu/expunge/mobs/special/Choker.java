package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.World;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class Choker extends GameMob {
    public <T extends Mob> Choker(JavaPlugin plugin, World world, Vector locationToSpawn) {
        super(plugin, world, locationToSpawn, Husk.class, mob -> {

        });
    }
}
