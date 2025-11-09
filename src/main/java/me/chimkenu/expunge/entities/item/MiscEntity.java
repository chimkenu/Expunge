package me.chimkenu.expunge.entities.item;

import me.chimkenu.expunge.entities.GameEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class MiscEntity implements GameEntity {
    private final Entity entity;

    public MiscEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Location getLocation() {
        return entity.getLocation();
    }

    @Override
    public void setLocation(Location l) {
        entity.teleport(l);
    }

    @Override
    public Vector getVelocity() {
        return entity.getVelocity();
    }

    @Override
    public void setVelocity(Vector v) {
        entity.setVelocity(v);
    }

    @Override
    public void remove() {
        entity.remove();
    }

    @Override
    public boolean isDead() {
        return entity.isDead();
    }

    @Override
    public String getIdentifier() {
        return entity.getType() + " - " + entity.getUniqueId();
    }

    @Override
    public Entity getHandle() {
        return entity;
    }
}
