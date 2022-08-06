package me.chimkenu.expunge.guns.utilities.throwable;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Slot;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Spit extends Throwable {
    public Spit() {
        super(20, Material.SLIME_BALL, "&2Spit", Slot.TERTIARY, "THROWABLE_SPIT");
    }

    @Override
    public void use(LivingEntity entity) {
        entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, SoundCategory.HOSTILE, 0.5f, 0);
        Projectile ball = entity.launchProjectile(Snowball.class);
        ball.addScoreboardTag("THROWABLE_SPIT");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute as @e[tag=THROWABLE_SPIT] run data merge entity @s {Item:{id:\"minecraft:slime_block\",Count:3b}}");
    }

    @Override
    public void onLand(World world, Location loc, Entity shooter) {
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
            int t = 20 * 5;
            @Override
            public void run() {
                Set<Player> players = new HashSet<>();
                for (String coordinates : coordinateSet) {
                    // string to double[]
                    String[] result = coordinates.split(" ");
                    if (result.length == 6) {
                        double[] doubles = new double[result.length];
                        int i = 0;
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
                    player.damage(2, shooter);
                    player.setVelocity(velocity);
                    player.setNoDamageTicks(1);
                }
                t--;
                if (t <= 0) this.cancel();
            }
        }.runTaskTimer(Expunge.instance, 1, 1);
    }
}
