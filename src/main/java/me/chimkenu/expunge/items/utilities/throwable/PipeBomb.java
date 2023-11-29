package me.chimkenu.expunge.items.utilities.throwable;

import me.chimkenu.expunge.enums.Tier;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.RayTrace;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class PipeBomb implements Throwable {

    @Override
    public Material getMaterial() {
        return Material.GRAY_CANDLE;
    }

    @Override
    public Component getName() {
        return Component.text("Pipe Bomb", NamedTextColor.GRAY);
    }

    @Override
    public Tier getTier() {
        return Tier.TIER1;
    }

    @Override
    public void use(JavaPlugin plugin, GameManager gameManager, LivingEntity entity) {
        final ArmorStand armorStand = entity.getWorld().spawn(entity.getLocation(), ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setMarker(true);
        armorStand.setSmall(true);
        gameManager.getDirector().getItemHandler().addEntity(armorStand);

        Snowball p = entity.launchProjectile(Snowball.class);
        p.setItem(get());

        new BukkitRunnable() {
            final double RAY_TRACE_DISTANCE = 3;
            final double RAY_TRACE_ACCURACY = 0.01;
            final double MULTIPLIER = 0.5;

            int t = 6 * 20;
            Snowball projectile = p;
            Location loc = projectile.getLocation();
            Vector velocity = projectile.getVelocity();

            @Override
            public void run() {
                if (!gameManager.isRunning() || !gameManager.getDirector().getMobHandler().isSpawningEnabled()) {
                    end();
                    return;
                }
                if (t <= 0) {
                    new Grenade().onLand(plugin, gameManager.getWorld(), loc, entity);
                    end();
                    return;
                }
                if (projectile.isDead()) {
                    handleCollision(new RayTrace(loc.toVector(), velocity));
                    gameManager.getDirector().getItemHandler().addEntity(projectile);
                }


                loc = projectile.getLocation();
                velocity = projectile.getVelocity();
                armorStand.teleport(loc);
                for (LivingEntity e : loc.getNearbyLivingEntities(30)) {
                    if (e instanceof Zombie z) {
                        z.setTarget(armorStand);
                    }
                }

                if (t < 20) {
                    if (t % 2 == 0)
                        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                } else if (t < 60) {
                    if (t % 5 == 0)
                        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                } else {
                    if (t % 10 == 0)
                        loc.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                }

                t--;
            }

            private void handleCollision(RayTrace rayTrace) {
                World world = projectile.getWorld();
                projectile = world.spawn(loc, Snowball.class);
                projectile.setItem(get());

                for (Vector v : rayTrace.traverse(RAY_TRACE_DISTANCE, RAY_TRACE_ACCURACY)) {
                    Location loc = v.toLocation(world);
                    Block block = loc.getBlock();

                    if (!block.isEmpty() && block.isSolid()) {

                        BoundingBox b = block.getBoundingBox();
                        if (b.contains(v)) {
                            if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                                velocity.multiply(new Vector(-MULTIPLIER, MULTIPLIER, MULTIPLIER));
                            } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                                velocity.multiply(new Vector(MULTIPLIER, MULTIPLIER, -MULTIPLIER));
                            } else {
                                velocity.multiply(new Vector(MULTIPLIER, -MULTIPLIER, MULTIPLIER));
                            }

                            projectile.setVelocity(velocity);
                            return;
                        }

                    } else {

                        for (Entity e : loc.getNearbyLivingEntities(1)) {
                            BoundingBox b = e.getBoundingBox();
                            if (b.contains(v)) {

                                if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                                    velocity.multiply(new Vector(-MULTIPLIER, MULTIPLIER, MULTIPLIER));
                                } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                                    velocity.multiply(new Vector(MULTIPLIER, MULTIPLIER, -MULTIPLIER));
                                } else {
                                    velocity.multiply(new Vector(MULTIPLIER, -MULTIPLIER, MULTIPLIER));
                                }

                                projectile.setVelocity(velocity);

                                return;
                            }
                        }

                    }
                }
                projectile.setGravity(false);
            }

            private void end() {
                projectile.remove();
                this.cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @Override
    public String getTag() {
        return "THROWABLE_PIPE_BOMB";
    }

    @Override
    public void onLand(JavaPlugin plugin, World world, Location loc, Entity shooter) {}
}
