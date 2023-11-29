package me.chimkenu.expunge.mobs.special;

import me.chimkenu.expunge.mobs.GameMob;
import me.chimkenu.expunge.mobs.MobBehavior;
import org.bukkit.World;
import org.bukkit.entity.Mob;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public abstract class Special extends GameMob {
    final protected JavaPlugin plugin;

    public <T extends Mob> Special(JavaPlugin plugin, World world, Vector locationToSpawn, Class<T> mobToSpawn, MobBehavior behavior) {
        super(plugin, world, locationToSpawn, mobToSpawn, behavior);
        this.plugin = plugin;
        playJingle();
    }

    protected abstract void playJingle();
}
