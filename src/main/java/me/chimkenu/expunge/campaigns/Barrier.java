package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

public class Barrier implements NextMapCondition {
    private static final int MAX_CLICKS = 5;
    private static final String BLOCK = "BARRIER_BLOCK";
    private static final String CLICK_COUNT = "BARRIER_CLICK_COUNT";

    private final List<Block> blocks;

    public Barrier(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public boolean init(CampaignGameManager manager, Event event, Map<String, Object> data) {
        if (event instanceof PlayerAnimationEvent e) {
            if (e.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
                var block = e.getPlayer().getTargetBlockExact(4);
                data.put(BLOCK, block);
                return true;
            }
        } else if (event instanceof PlayerInteractEvent e) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                var block = e.getClickedBlock();
                data.put(BLOCK, block);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean check(CampaignGameManager manager, Event event, Map<String, Object> data) {
        if (!(event instanceof PlayerEvent e)) {
            return false;
        }
        var player = e.getPlayer();

        Object blockObject = data.get(BLOCK);
        if (!(blockObject instanceof org.bukkit.block.Block block)) {
            return false;
        }
        var clickedLoc = block.getLocation().toVector();
        if (!blocks.contains(new Block(clickedLoc, Material.BEEHIVE, true))) {
            return false;
        }

        BoundingBox endRegion = manager.getMap().endRegion();
        for (Player p : manager.getDirector().getAlivePlayers().toList()) {
            Location pLoc = p.getLocation();
            if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                ChatUtil.sendActionBar(player, "&cNot all players are in the safe-zone!");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean sideEffect(CampaignGameManager manager, Event event, Map<String, Object> data) {
        var world = manager.getWorld();

        // first do a little animation for breaking the barrier (click check)
        Object blockObject = data.get(BLOCK);
        if (!(blockObject instanceof org.bukkit.block.Block block)) {
            return false;
        }
        Object clickObject = data.getOrDefault(CLICK_COUNT, 0);
        if (!(clickObject instanceof Integer clicks)) {
            return false;
        }
        clicks++;
        data.put(CLICK_COUNT, clicks);

        float progress = Math.min(1, (float) clicks / MAX_CLICKS);
        world.playSound(block.getLocation(), Sound.BLOCK_WOOD_HIT, 1, 1);
        manager.getPlayers().forEach(p ->
                blocks.forEach(b -> {
                    if (b.isInit() && b.type() != Material.BARRIER) {
                        p.sendBlockDamage(b.position().toLocation(world), progress);
                    }
                })
        );
        if (clicks < MAX_CLICKS) {
            return false;
        }

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
}
