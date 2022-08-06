package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Mob;

public class Witch extends GameMob {
    public <T extends Mob> Witch(World world, Location locationToSpawn) {
        super(world, locationToSpawn, Enderman.class, mob -> {
            if (mob.getTarget() != null) {

            }
        });
        getMob().setAbsorptionAmount(1);
    }
}
