package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Mob;

public class Choker extends GameMob {
    public <T extends Mob> Choker(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Husk.class, mob -> {

        });
    }
}
