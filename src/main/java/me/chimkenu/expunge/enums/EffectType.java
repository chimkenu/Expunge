package me.chimkenu.expunge.enums;

import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.entities.item.MiscEntity;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.items.Throwable;
import me.chimkenu.expunge.utils.RayTrace;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class EffectType {
    public enum Flight {
        NOTHING {
            @Override
            public void trigger(GameManager manager, Throwable throwable, GameEntity shooter, Entity projectile) {}
        },

        ATTRACT {
            @Override
            public void trigger(GameManager manager, Throwable throwable, GameEntity shooter, Entity projectile) {
                final ArmorStand armorStand = manager.getWorld().spawn(projectile.getLocation(), ArmorStand.class);
                manager.addEntity(new MiscEntity(armorStand));
                armorStand.setInvisible(true);
                armorStand.setMarker(true);
                armorStand.setSmall(true);

                manager.addTask(
                        new BukkitRunnable() {
                            final double RAY_TRACE_DISTANCE = 3;
                            final double RAY_TRACE_ACCURACY = 0.01;
                            final double MULTIPLIER = 0.5;

                            int t = 6 * 20;
                            Snowball p = (Snowball) projectile;
                            Location loc = p.getLocation();
                            Vector velocity = p.getVelocity();

                            @Override
                            public void run() {
                                if (t <= 0) {
                                    throwable.landType().trigger(manager, p.getLocation(), shooter, throwable.effectDuration());
                                    cancel();
                                    return;
                                }
                                if (p.isDead()) {
                                    handleCollision(new RayTrace(loc.toVector(), velocity));
                                }


                                loc = p.getLocation();
                                velocity = p.getVelocity();
                                armorStand.teleport(loc);
                                for (Entity e : p.getWorld().getNearbyEntities(loc, 30, 30, 30)) {
                                    if (e instanceof Zombie z) {
                                        z.setTarget(armorStand);
                                    }
                                }

                                if (t < 20) {
                                    if (t % 2 == 0)
                                        p.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                                } else if (t < 60) {
                                    if (t % 5 == 0)
                                        p.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                                } else {
                                    if (t % 10 == 0)
                                        p.getWorld().playSound(loc, Sound.BLOCK_NOTE_BLOCK_HARP, SoundCategory.PLAYERS, 1.5f, 2);
                                }

                                t--;
                            }

                            private void handleCollision(RayTrace rayTrace) {
                                World world = p.getWorld();
                                p = world.spawn(loc, Snowball.class);
                                p.setItem(new ItemStack(throwable.thrownItem()));
                                p.getPersistentDataContainer().set(throwable.namespacedKey(), PersistentDataType.BOOLEAN, false);

                                for (Vector v : rayTrace.traverse(RAY_TRACE_DISTANCE, RAY_TRACE_ACCURACY)) {
                                    Location loc = v.toLocation(world);
                                    Block block = loc.getBlock();

                                    if (!block.isEmpty() && !block.isPassable()) {

                                        BoundingBox b = block.getBoundingBox();
                                        if (b.contains(v)) {
                                            if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                                                velocity.multiply(new Vector(-MULTIPLIER, MULTIPLIER, MULTIPLIER));
                                            } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                                                velocity.multiply(new Vector(MULTIPLIER, MULTIPLIER, -MULTIPLIER));
                                            } else {
                                                velocity.multiply(new Vector(MULTIPLIER, -MULTIPLIER, MULTIPLIER));
                                            }

                                            p.setVelocity(velocity);
                                            return;
                                        }

                                    } else {

                                        for (Entity e : world.getNearbyEntities(loc, 1, 1, 1)) {
                                            BoundingBox b = e.getBoundingBox();
                                            if (b.contains(v)) {

                                                if (!b.contains(v.clone().add(new Vector(RAY_TRACE_ACCURACY * 2, 0, 0))) || !b.contains(v.clone().add(new Vector(-RAY_TRACE_ACCURACY * 2, 0, 0)))) {
                                                    velocity.multiply(new Vector(-MULTIPLIER, MULTIPLIER, MULTIPLIER));
                                                } else if (!b.contains(v.clone().add(new Vector(0, 0, RAY_TRACE_ACCURACY * 2))) || !b.contains(v.clone().add(new Vector(0, 0, -RAY_TRACE_ACCURACY * 2)))) {
                                                    velocity.multiply(new Vector(MULTIPLIER, MULTIPLIER, -MULTIPLIER));
                                                } else {
                                                    velocity.multiply(new Vector(MULTIPLIER, -MULTIPLIER, MULTIPLIER));
                                                }

                                                p.setVelocity(velocity);

                                                return;
                                            }
                                        }

                                    }
                                }
                                p.setGravity(false);
                            }

                            @Override
                            public void cancel() {
                                p.remove();
                                armorStand.remove();
                                super.cancel();
                            }
                        }.runTaskTimer(manager.getPlugin(), 0, 1)
                );
            }
        };

        public abstract void trigger(GameManager manager, Throwable throwable, GameEntity shooter, Entity projectile);
    }

    public enum Land {
        EXPLODE {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                manager.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER,loc,1);
                manager.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS,1,1);
                int numOfMobs = 0;
                for(Entity entity : manager.getWorld().getNearbyEntities(loc,4,4,4)){
                    if (!(entity instanceof LivingEntity livingEntity)) {
                        continue;
                    }
                    if (livingEntity instanceof ArmorStand) {
                        continue;
                    }
                    if (livingEntity instanceof Player player) {
                        if (player.getGameMode() == GameMode.ADVENTURE)
                            livingEntity.damage(1, shooter.getHandle());
                        continue;
                    }
                    numOfMobs++;
                    livingEntity.getWorld().spawnParticle(Particle.BLOCK, livingEntity.getLocation().add(0, .5, 0), 50, 0.2, 0.2, 0.2, Material.NETHER_WART_BLOCK.createBlockData());
                    livingEntity.damage(80, shooter.getHandle());
                }

                // achievement
                if(numOfMobs >= 20 && shooter instanceof Player player) {
                    Achievements.WHEN_GUTS_FLY.grant(player);
                }
            }
        },
        FLAME {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                manager.getWorld().playSound(loc, Sound.ITEM_FIRECHARGE_USE, 1, 0);
                manager.addTask(
                        new BukkitRunnable() {
                            int t = 0;

                            @Override
                            public void run() {
                                manager.getWorld().spawnParticle(Particle.FLAME, loc, 20, 0.8, 0.8, 0.8, 0, null, true);
                                t++;
                                if (t > duration) this.cancel();

                                // damage nearby
                                if (t % 4 == 0) {
                                    for (Entity entity : manager.getWorld().getNearbyEntities(loc, 2, 2, 2)) {
                                        if (entity instanceof LivingEntity livingEntity) {
                                            Vector vec = livingEntity.getVelocity();
                                            livingEntity.setVelocity(vec);
                                            livingEntity.setNoDamageTicks(0);
                                            if (livingEntity instanceof Player) {
                                                livingEntity.damage(0.25d);
                                                livingEntity.setFireTicks(40);
                                            } else {
                                                livingEntity.damage(10, shooter.getHandle());
                                                livingEntity.setFireTicks(20 * 60);
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(manager.getPlugin(), 0, 1)
                );
            }
        },
        FLAME_SPREAD {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                Location location = loc.add(0, 1.4, 0);
                FLAME.trigger(manager, location, shooter, ThreadLocalRandom.current().nextInt(duration));
                for (int i = 0; i < 6; i++) {
                    manager.addTask(
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    FLAME.trigger(manager, location.clone().add(ThreadLocalRandom.current().nextDouble(-3, 3), 0, ThreadLocalRandom.current().nextDouble(-3, 3)), shooter, ThreadLocalRandom.current().nextInt(duration));
                                }
                            }.runTaskLater(manager.getPlugin(), i * 6 + 5 * ThreadLocalRandom.current().nextInt(4))
                    );
                }
            }
        },
        ATTRACT {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                World world = manager.getWorld();
                world.playSound(loc, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0);
                world.playSound(loc, Sound.ENTITY_PLAYER_SPLASH_HIGH_SPEED, SoundCategory.PLAYERS, 1, 2);
                ArmorStand target = world.spawn(loc, ArmorStand.class);
                target.setInvisible(true);
                target.setGravity(false);
                target.addScoreboardTag("THROWN_BILE");
                Mob targetEntity = null;
                for (Entity e : world.getNearbyEntities(loc, 3, 3, 3)) {
                    if (e instanceof Mob mob) {
                        targetEntity = mob;
                    }
                }
                Mob finalTargetEntity = targetEntity;
                new BukkitRunnable() {
                    int t = 40;

                    @Override
                    public void run() {
                        world.spawnParticle(Particle.SNEEZE, loc, 25, 0.5, 0.5, 0.5, 0.1);
                        t--;
                        if (t <= 0) {
                            target.remove();
                            this.cancel();
                        } else if (target.isDead()) {
                            this.cancel();
                        }

                        // attract nearby mobs
                        for (Entity e : world.getNearbyEntities(loc, 30, 30, 30)) {
                            if (e instanceof Mob mob && mob != finalTargetEntity) {
                                mob.setTarget(Objects.requireNonNullElse(finalTargetEntity, target));
                            }
                        }
                    }
                }.runTaskTimer(manager.getPlugin(), 0, 10);
            }
        },
        LEVITATE {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                World world = manager.getWorld();
                world.spawnParticle(Particle.EFFECT, loc, 200, 2, 0.5, 2, 0);
                world.playSound(loc, Sound.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1, 0);
                world.playSound(loc, Sound.BLOCK_REDSTONE_TORCH_BURNOUT, SoundCategory.PLAYERS, 1, 0);
                int numOfMobs = 0;
                for (Entity entity : world.getNearbyEntities(loc, 5, 5, 5)) {
                    if (!(entity instanceof LivingEntity livingEntity)) {
                        continue;
                    }
                    if (livingEntity instanceof ArmorStand) {
                        continue;
                    }
                    if (livingEntity instanceof Player) {
                        continue;
                    }
                    numOfMobs++;
                    livingEntity.getWorld().spawnParticle(Particle.EFFECT, livingEntity.getLocation().add(0, .5, 0), 25, 0.2, 0.2, 0.2, 0);
                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 20 * 10, 0, false, true, false));
                }

                // TODO: achievement
                if (numOfMobs >= 20 && shooter instanceof Player player) {
                    Achievements.ZERO_GRAVITY.grant(player);
                }
            }
        },
        ACID {
            @Override
            public void trigger(GameManager manager, Location loc, GameEntity shooter, int duration) {
                World world = manager.getWorld();
                Block origin = loc.getBlock();
                BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN};

                // blocks = all adjacent non-air blocks
                Set<Block> blocks = new HashSet<>();
                blocks.add(origin);
                for (int i = 0; i < 5; i++) {
                    Set<Block> blocksToAdd = new HashSet<>();
                    for (Block block : blocks) {
                        for (BlockFace face : faces) {
                            Block newBlock = block.getRelative(face);
                            blocksToAdd.add(newBlock);
                        }
                    }
                    blocks.addAll(blocksToAdd);
                }
                blocks.removeIf(Block::isEmpty);

                // remove 50% of blocks randomly
                int numToRemove = (int) Math.floor(blocks.size() * 0.5);
                List<Block> blockList = new LinkedList<>(blocks.stream().toList());
                Collections.shuffle(blockList);
                for (int i = 0; i < numToRemove; i++) {
                    blocks.remove(blockList.get(i));
                }

                // get and save the coordinates for the particles
                Set<String> coordinateSet = new HashSet<>();
                for (Block block : blocks) {
                    Location blockLoc = block.getLocation();
                    blockLoc.add(0.5, 0.5, 0.5);
                    for (BlockFace face : faces) {
                        if (block.getRelative(face).isEmpty()) {
                            double x = blockLoc.getX();
                            double y = blockLoc.getY();
                            double z = blockLoc.getZ();
                            switch(face) {
                                case UP -> coordinateSet.add(x + " " + (y + 0.7) + " " + z + " 0.2 0 0.2");
                                case DOWN -> coordinateSet.add(x + " " + (y - 0.7) + " " + z + " 0.2 0 0.2");
                                case NORTH -> coordinateSet.add(x + " " + y + " " + (z - 0.7) + " 0.2 0.2 0");
                                case SOUTH -> coordinateSet.add(x + " " + y + " " + (z + 0.7) + " 0.2 0.2 0");
                                case EAST -> coordinateSet.add((x + 0.7) + " " + y + " " + z + " 0 0.2 0.2");
                                case WEST -> coordinateSet.add((x - 0.7) + " " + y + " " + z + " 0 0.2 0.2");
                            }
                        }
                    }
                }

                // play effect
                new BukkitRunnable() {
                    int t = 5 * 10;
                    @Override
                    public void run() {
                        Set<Player> players = new HashSet<>();
                        for (String coordinates : coordinateSet) {
                            // string to double[]
                            String[] result = coordinates.split(" ");
                            if (result.length == 6) {
                                double[] doubles = new double[result.length];
                                int i;
                                for (i = 0; i < result.length; i++) {
                                    try { doubles[i] = Double.parseDouble(result[i]); }
                                    catch (NumberFormatException ignored) { break; }
                                }
                                // continue if not all doubles were parsed
                                if (i < result.length) continue;
                                // particle effect
                                Location loc = new Location(world, doubles[0], doubles[1], doubles[2]);
                                world.spawnParticle(Particle.SNEEZE, loc, 1, doubles[3], doubles[4], doubles[5], 0);
                                for (Entity entity : world.getNearbyEntities(loc, 0.5, 0.25, 0.5)) {
                                    if (entity instanceof Player player) {
                                        players.add(player);
                                    }
                                }
                            }
                        }
                        for (Player player : players) {
                            Vector velocity = player.getVelocity();
                            player.damage(0.4, shooter.getHandle());
                            player.setVelocity(velocity);
                            player.setNoDamageTicks(1);
                        }
                        t--;
                        if (t <= 0) this.cancel();
                    }
                }.runTaskTimer(manager.getPlugin(), 1, 4);
            }
        };

        public abstract void trigger(GameManager manager, Location loc, GameEntity shooter, int duration);
    }
}
