package me.chimkenu.expunge.entities;

import me.chimkenu.expunge.game.GameManager;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.util.Vector;

import java.util.Optional;
import java.util.Set;

public class Infected implements GameEntity, Damageable, Targeting, Restorable {
    private final GameManager manager;
    private Mob mob;
    private MobType mobType;
    private Class<? extends Mob> mobClass;
    private MobSettings settings;

    private GameEntity target;

    public Infected(GameManager manager, Vector spawn, MobType mobType, Class<? extends Mob> mobClass, MobSettings settings) {
        this.manager = manager;
        this.mob = mobType.spawn(manager, spawn, mobClass, settings);
        this.mobType = mobType;
        this.mobClass = mobClass;
        this.settings = settings;
        this.target = null;
    }

    public Infected(GameManager manager, RestorableState<Infected> state) {
        this.manager = manager;
        state.restore(this);
    }

    @Override
    public double getHealth() {
        return mob.getHealth();
    }

    @Override
    public void setHealth(double health) {
        mob.setHealth(health);
    }

    @Override
    public double getAbsorption() {
        throw new NotImplementedException("Infected do not have absorption");
    }

    @Override
    public void setAbsorption(double absorption) {
        throw new NotImplementedException("Infected do not have absorption");
    }

    @Override
    public Location getLocation() {
        return mob.getLocation();
    }

    @Override
    public void setLocation(Location l) {
        mob.teleport(l);
    }

    @Override
    public Vector getVelocity() {
        return mob.getVelocity();
    }

    @Override
    public void setVelocity(Vector v) {
        mob.setVelocity(v);
    }

    @Override
    public void remove() {
        mob.remove();
    }

    @Override
    public boolean isDead() {
        return mob.isDead();
    }

    @Override
    public String getIdentifier() {
        return mob.getType() + " - " + mob.getUniqueId();
    }

    @Override
    public Mob getHandle() {
        return mob;
    }

    @Override
    public Optional<GameEntity> getTarget(Set<GameEntity> entities) {
        var trueTarget = mob.getTarget();
        if (trueTarget == null) {
            target = null;
            return Optional.empty();
        }

        if (target != null && trueTarget.equals(target.getHandle())) {
            return Optional.of(target);
        }

        target = entities.stream().filter(e -> e.getHandle().equals(trueTarget)).findAny().orElse(null);
        return Optional.ofNullable(target);
    }

    @Override
    public void setTarget(GameEntity target) {
        if (target.getHandle() instanceof LivingEntity e) {
            this.target = target;
            mob.setTarget(e);
        }
    }

    @Override
    public RestorableState<Infected> getState() {
        return new State(
                mobType,
                mobClass,
                settings,
                getLocation(),
                getHealth()
        );
    }

    private record State(
            MobType mobType,
            Class<? extends Mob> mobClass,
            MobSettings settings,
            Location location,
            double health
    ) implements RestorableState<Infected> {
        @Override
        public void restore(Infected entity) {
            entity.mob = mobType.spawn(entity.manager, location.toVector(), mobClass, settings);
            entity.mobType = mobType;
            entity.mobClass = mobClass;
            entity.settings = settings;
            entity.target = null;
            entity.setHealth(health);
        }
    }
}
