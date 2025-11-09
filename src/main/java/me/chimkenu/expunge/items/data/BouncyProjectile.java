package me.chimkenu.expunge.items.data;

import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Throwable;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class BouncyProjectile extends BukkitRunnable {
    final double RAY_TRACE_DISTANCE = 3;
    final double RAY_TRACE_ACCURACY = 0.01;
    final GameManager manager;
    final Throwable throwable;
    final GameEntity shooter;

    Snowball projectile;
    int t;
    Location loc;
    Vector velocity;

    /**
     * Makes projectiles bouncy (only snowball) for a given amount of time
     * Usage: (new BouncyProjectile(args)).runTaskTimer(plugin, delay, period);
     *
     * @param manager the game manager for this projectile
     * @param throwable the throwable associated with the projectile
     * @param shooter the shooter of the projectile
     * @param projectile the projectile in question
     */
    public BouncyProjectile(GameManager manager, Throwable throwable, GameEntity shooter, Snowball projectile) {
        this.manager = manager;
        this.shooter = shooter;
        this.projectile = projectile;
        this.throwable = throwable;
        t = throwable.flightDuration();
        loc = projectile.getLocation();
        velocity = projectile.getVelocity();
    }

    @Override
    public void run() {
        if (t <= 0) {
            throwable.landType().trigger(manager, projectile.getLocation(), shooter, throwable.effectDuration());
            cancel();
            return;
        }
        if (projectile.isDead()) {
            handleCollision(new RayTrace(loc.toVector(), velocity));
        }

        loc = projectile.getLocation();
        velocity = projectile.getVelocity();

        t--;
    }

    private void handleCollision(RayTrace rayTrace) {
        World world = projectile.getWorld();
        projectile = world.spawn(loc, Snowball.class);
        projectile.setItem(new ItemStack(throwable.thrownItem()));
        projectile.setShooter((ProjectileSource) shooter.getHandle());
        projectile.getPersistentDataContainer().set(throwable.namespacedKey(), PersistentDataType.BOOLEAN, false);

        for (Vector v : rayTrace.traverse(RAY_TRACE_DISTANCE, RAY_TRACE_ACCURACY)) {
            Location loc = v.toLocation(world);
            Block block = loc.getBlock();

            var bounceVelocity = throwable.bounceVelocity();
            if (!block.isEmpty() && !block.isPassable()) {

                BoundingBox b = block.getBoundingBox();
                if (b.contains(v)) {
                    if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                        velocity.multiply(new Vector(-bounceVelocity, bounceVelocity, bounceVelocity));
                    } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                        velocity.multiply(new Vector(bounceVelocity, bounceVelocity, -bounceVelocity));
                    } else {
                        velocity.multiply(new Vector(bounceVelocity, -bounceVelocity, bounceVelocity));
                    }

                    projectile.setVelocity(velocity);
                    return;
                }

            } else {

                for (Entity e : world.getNearbyEntities(loc, 1, 1, 1)) {
                    BoundingBox b = e.getBoundingBox();
                    if (b.contains(v)) {

                        if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                            velocity.multiply(new Vector(-bounceVelocity, bounceVelocity, bounceVelocity));
                        } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                            velocity.multiply(new Vector(bounceVelocity, bounceVelocity, -bounceVelocity));
                        } else {
                            velocity.multiply(new Vector(bounceVelocity, -bounceVelocity, bounceVelocity));
                        }

                        projectile.setVelocity(velocity);

                        return;
                    }
                }

            }
        }

        projectile.setGravity(false);
    }
}
