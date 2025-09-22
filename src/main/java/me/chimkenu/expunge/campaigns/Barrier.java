package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;

public class Barrier implements NextMapCondition {
    private final List<Block> blocks;

    public Barrier(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean check(CampaignGameManager manager, Event event) {
        Block block;
        if (event instanceof PlayerAnimationEvent e) {
            if (!e.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
                return false;
            }

            return checkBlock(manager, e.getPlayer(), e.getPlayer().getTargetBlockExact(4));
        } else if (event instanceof PlayerInteractEvent e) {
            if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                return false;
            }

            return checkBlock(manager, e.getPlayer(), e.getClickedBlock());
        }
        return false;
    }

    private boolean checkBlock(CampaignGameManager manager, Player player, org.bukkit.block.Block block) {
        if (block == null) {
            return false;
        }

        var blockState = block.getWorld().getBlockAt(block.getX(), -64, block.getZ());
        var state = BreakState.fromBlock(blockState);
        if (state == BreakState.INVALID) {
            return false;
        }

        if (!manager.getPlayers().contains(player) || player.getGameMode() != GameMode.ADVENTURE) {
            return false;
        }

        var clickedLoc = block.getLocation().toVector();
        if (!blocks.contains(clickedLoc)) {
            return false;
        }

        BoundingBox endRegion = manager.getMap().endRegion();
        for (Player p : manager.getPlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                Location pLoc = p.getLocation();
                if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                    ChatUtil.sendActionBar(player, "&cNot all alive players are in the safe-zone!");
                    return false;
                }
            }
        }

        // this is reached when all alive players reach the end region
        var world = manager.getWorld();

        // first do a little animation for breaking the barrier (click check)
        var clicks = state.ordinal();
        clicks++;
        float progress = Math.min(1, (float) clicks / BreakState.MAX_CLICKS);
        Bukkit.broadcastMessage(progress + "");
        world.playSound(block.getLocation(), Sound.BLOCK_WOOD_HIT, 1, 1);
        manager.getPlayers().forEach(p ->
                blocks.forEach(b -> {
                    if (b.isInit() && b.type() != Material.BARRIER) {
                        p.sendBlockDamage(b.position().toLocation(world), progress, player);
                    }
                })
        );
        if (clicks <= BreakState.MAX_CLICKS) {
            return false;
        }
        blockState.setType(Material.AIR);

        // runs when players have clicked the barrier MAX_CLICKS times
        world.playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        blocks.forEach(b -> {
            var loc = b.position().toLocation(world);
            if (b.isInit()) {
                world.setBlockData(loc, Material.BARRIER.createBlockData());
                world.spawnParticle(Particle.BLOCK, loc, 50, 0.2, 0.2, 0.2, b.type().createBlockData());
            } else {
                world.setBlockData(loc, b.type().createBlockData());
            }
        });

        return true;
    }


    public record Block(Vector position, Material type, boolean isInit) {
        @Override
        public boolean equals(Object o) {
            if (o instanceof Barrier.Block b) {
                return this.position.equals(b.position);
            } else if (o instanceof Vector v) {
                return this.position.equals(v);
            }
            return false;
        }
    }

    private enum BreakState {
        FIRST,
        SECOND,
        THIRD,
        FOURTH,
        FIFTH,
        INVALID;

        public static final int MAX_CLICKS = INVALID.ordinal();
        private static final Material[] blocks = { Material.RED_CONCRETE, Material.ORANGE_CONCRETE, Material.YELLOW_CONCRETE, Material.GREEN_CONCRETE, Material.BLUE_CONCRETE };
        public static Material getMaterial(BreakState state) {
            if (state == INVALID) {
                return null;
            }
            return blocks[state.ordinal()];
        }
        public static BreakState fromBlock(org.bukkit.block.Block block) {
            for (var s : BreakState.values()) {
                if (block.getType().equals(getMaterial(s))) {
                    return s;
                }
            }
            return INVALID;
        }
    }
}
