package me.chimkenu.expunge.entities.item;

import me.chimkenu.expunge.entities.Regenerable;
import me.chimkenu.expunge.entities.RegenerableState;
import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Interactable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class InteractableEntity implements GameEntity, Regenerable {
    private final Interactable interactable;
    private final Entity entity;

    public InteractableEntity(Interactable interactable, Entity entity) {
        this.interactable = interactable;
        this.entity = entity;
    }

    public InteractableEntity(Interactable interactable, World world, Vector position) {
        this(interactable, interactable.spawn(world, position, false).getHandle());
    }

    public void interact(GameManager manager, GameEntity actor) {
        interactable.onInteract(manager, this, actor);
    }

    public void damage(GameManager manager, GameEntity actor) {
        interactable.onDamage(manager, this, actor);
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

    @Override
    public RegenerableState<InteractableEntity> getState() {
        return new State(
                interactable,
                getLocation()
        );
    }

    private record State(
            Interactable interactable,
            Location location
    ) implements RegenerableState<InteractableEntity> {
        @Override
        public InteractableEntity regenerate(World world) {
            return new InteractableEntity(interactable, world, location.toVector());
        }
    }
}
