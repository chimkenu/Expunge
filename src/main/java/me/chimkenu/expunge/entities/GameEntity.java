package me.chimkenu.expunge.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public interface GameEntity {
    Location getLocation();
    void setLocation(Location l);

    Vector getVelocity();
    void setVelocity(Vector v);

    void remove();
    boolean isDead();

    String getIdentifier();
    Entity getHandle();
}
