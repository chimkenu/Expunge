package me.chimkenu.expunge.entities.goals.special;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.entities.goals.MobGoal;
import me.chimkenu.expunge.entities.MobSettings;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Tank extends MobGoal {
    public Tank(GameManager manager, Mob mob, MobSettings settings) {
        super(manager, mob, settings);
    }

    @Override
    public boolean canUse() {
        mob.removePotionEffect(PotionEffectType.SLOWNESS);
        mob.removePotionEffect(PotionEffectType.WEAKNESS);
        if (mob.getFireTicks() > 0) {
            mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 2, false, false));
        }

        return super.canUse();
    }

    @Override
    public void start() {
        LivingEntity target = mob.getTarget();
        if (target == null) return;

        if (mob.getLocation().distance(target.getLocation()) < 4) {
            attackFrameTicks = 32;
            executionTick = 20;
            pendingAttack = () -> groundSmash(mob);
            playTellAnimation("ground_smash");
        } else {
            attackFrameTicks = 10;
            executionTick = 10;
            pendingAttack = () -> rockThrow(mob, target);
            playTellAnimation("rock_throw");
        }
    }

    protected void playTellAnimation(String type) {
        mob.playEffect(EntityEffect.IRON_GOLEN_ATTACK);
        if (type.equals("ground_smash")) {
            mob.setVelocity(mob.getVelocity().add(new Vector(0, 1, 0)));
            manager.addTask(
                    new BukkitRunnable() {
                        int t = 20;
                        @Override
                        public void run() {
                            if (t <= 0) {
                                cancel();
                                return;
                            }
                            mob.getWorld().spawnParticle(Particle.CRIT, mob.getLocation(), 20, 1, 1, 1, 0.5);
                            t--;
                        }
                    }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
            );
        }
    }

    public void groundSmash(LivingEntity source) {
        final Block src = source.getLocation().getBlock().getRelative(0, -1, 0);
        final Set<FallingBlock> blocks = new HashSet<>();
        manager.addTask(
                new BukkitRunnable() {
                    int i = 2;
                    @Override
                    public void run() {
                        if (i > 10) {
                            cancel();
                            return;
                        }
                        getBlocksInRadius(i++);
                    }

                    @Override
                    public void cancel() {
                        super.cancel();
                        manager.addTask(
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        blocks.removeIf(e -> {
                                            if (e.isDead()) {
                                                e.getWorld().spawnParticle(Particle.BLOCK, e.getLocation(), 10, 0.5, 0.5, 0.5, e.getBlockData());
                                                e.getWorld().playSound(e.getLocation(), e.getBlockData().getSoundGroup().getBreakSound(), 0.2f, 1);
                                                return true;
                                            }
                                            return false;
                                        });
                                        if (blocks.isEmpty()) {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
                        );
                    }

                    void getBlocksInRadius(int r) {
                        for (int x = -r; x <= r; x++) {
                            for (int z = -r; z <= r; z++) {
                                var block = src.getRelative(x, 0, z);

                                int attempts = 0;
                                while (block.isPassable() && attempts++ < 3) {
                                    block = block.getRelative(0, -1, 0);
                                }
                                attempts = 0;
                                while (!block.getRelative(0, 1, 0).isPassable() && attempts++ < 5) {
                                    block = block.getRelative(0, 1, 0);
                                }

                                if (!block.isPassable() && Math.round(block.getLocation().distance(src.getLocation())) == r) {
                                    var loc = block.getLocation().add(0.5, 0, 0.5);

                                    for (var e : block.getWorld().getNearbyEntities(loc, 1, 2, 1)) {
                                        if (e instanceof Player player && player.getGameMode() == GameMode.ADVENTURE) {
                                            if (player.getNoDamageTicks() <= 0) {
                                                player.damage(2, source);
                                                player.setVelocity(player.getVelocity().add(new Vector(0, 0.5, 0)));
                                            }
                                        }
                                    }

                                    var ent = block.getWorld().spawnFallingBlock(loc, block.getBlockData());
                                    ent.setCancelDrop(true);
                                    ent.setDropItem(false);
                                    ent.setDamagePerBlock(10);
                                    ent.setVelocity(new Vector(0, (Math.random() / 3) + 0.25, 0));
                                    block.getWorld().spawnParticle(Particle.BLOCK, block.getLocation().add(0, 1, 0), 50, 0.2, 0.2, 0.2, block.getBlockData());
                                    block.getWorld().playSound(block.getLocation(), block.getBlockData().getSoundGroup().getBreakSound(), 0.2f, 1);
                                    blocks.add(ent);
                                }
                            }
                        }
                    }
                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 2)
        );
    }

    public void rockThrow(LivingEntity source, LivingEntity target) {
        final Block src = source.getLocation().add(source.getEyeLocation().getDirection().setY(0).multiply(3)).getBlock().getRelative(0, -1, 0);
        manager.addTask(
                new BukkitRunnable() {
                    final BlockFace[] faces = { BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };
                    final Map<Block, BlockData> blocks = getAdjacentBlocks(src, new HashMap<>(), 2);
                    final Set<FallingBlock> rock = spawnRock(blocks.keySet());

                    Vector pastTargetLoc;
                    int t = 20;


                    @Override
                    public void run() {
                        if (t == 1) {
                            pastTargetLoc = target.getLocation().toVector();
                        }
                        if (t <= 0) {
                            cancel();
                            return;
                        }

                        rock.forEach(b -> {
                            b.getWorld().spawnParticle(Particle.BLOCK, b.getLocation(), 50, 0.2, 0.2, 0.2, b.getBlockData());
                            b.setVelocity(new Vector(0, 0.2, 0));
                            b.getNearbyEntities(0.5, 2, 0.5).forEach(e -> {
                                if (e instanceof Player player && player.getGameMode() == GameMode.ADVENTURE) {
                                    if (player.getNoDamageTicks() <= 0) {
                                        player.setVelocity(player.getVelocity().add(new Vector(0, 0.5, 0)));
                                        player.damage(2, source);
                                    }
                                }
                            });
                        });

                        t--;
                    }

                    @Override
                    public void cancel() {
                        super.cancel();

                        // TODO: rock throw intercept problem
                        var targetVelocity = target.getLocation().subtract(pastTargetLoc).toVector();
                        var toTarget = target.getLocation().subtract(source.getEyeLocation());

                        var velocity = target.getLocation().add(target.getVelocity()).subtract(source.getEyeLocation()).toVector().normalize().multiply(1.5);
                        rock.forEach(b -> {
                            b.setGravity(true);
                            b.setVelocity(velocity);
                        });
                        blocks.forEach(Block::setBlockData);

                        manager.addTask(
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        rock.removeIf(e -> {
                                            if (e.isDead()) {
                                                e.getWorld().spawnParticle(Particle.BLOCK, e.getLocation(), 10, 0.5, 0.5, 0.5, e.getBlockData());
                                                e.getWorld().playSound(e.getLocation(), e.getBlockData().getSoundGroup().getBreakSound(), 0.2f, 1);
                                                return true;
                                            }
                                            return false;
                                        });
                                        if (rock.isEmpty()) {
                                            cancel();
                                            return;
                                        }
                                        rock.forEach(b -> {
                                            b.getNearbyEntities(0.5, 0.5, 0.5).forEach(e -> {
                                                if (e instanceof Player player && player.getGameMode() == GameMode.ADVENTURE) {
                                                    player.damage(10, source);
                                                    b.getWorld().spawnParticle(Particle.BLOCK, b.getLocation(), 10, 0.5, 0.5, 0.5, b.getBlockData());
                                                    b.getWorld().playSound(b, b.getBlockData().getSoundGroup().getBreakSound(), 0.2f, 1);
                                                    b.remove();
                                                }
                                            });
                                        });
                                    }
                                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
                        );
                    }

                    Map<Block, BlockData> getAdjacentBlocks(Block src, Map<Block, BlockData> blocks, int step) {
                        if (step <= 0) {
                            return blocks;
                        }

                        for (BlockFace face : faces) {
                            var next = src.getRelative(face);
                            if (!blocks.containsKey(next)) {
                                blocks.put(next, next.getBlockData().clone());
                                getAdjacentBlocks(next, blocks, step - (int) Math.round(Math.random() + 1));
                            }
                        }
                        return blocks;
                    }

                    Set<FallingBlock> spawnRock(Set<Block> blocks) {
                        Set<FallingBlock> result = new HashSet<>();
                        for (Block b : blocks) {
                            var fb = b.getWorld().spawnFallingBlock(b.getLocation().add(0.5, 0, 0.5), b.getBlockData());
                            b.setType(Material.AIR);
                            fb.setCancelDrop(true);
                            fb.setDropItem(false);
                            fb.setDamagePerBlock(15);
                            fb.setGravity(false);
                            result.add(fb);
                        }
                        return result;
                    }
                }.runTaskTimer(Expunge.getPlugin(Expunge.class), 0, 1)
        );
    }
}
