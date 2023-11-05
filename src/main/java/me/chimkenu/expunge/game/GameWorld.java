package me.chimkenu.expunge.game;

import org.bukkit.World;

public interface GameWorld {
    boolean load();
    void unload();
    boolean isLoaded();
    World getWorld();
}
