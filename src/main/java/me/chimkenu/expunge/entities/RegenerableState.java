package me.chimkenu.expunge.entities;

import org.bukkit.World;

public interface RegenerableState<T extends GameEntity> {
    T regenerate(World world);
}
