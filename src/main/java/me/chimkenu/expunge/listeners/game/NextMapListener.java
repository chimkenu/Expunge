package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.campaigns.Barrier;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.game.campaign.CampaignGameState;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

public class NextMapListener extends GameListener {
    public final CampaignGameManager gameManager;

    private static final int MAX_CLICKS = 4;
    private static final int INVALID = -1;
    private int clicks = 0;
    private long lastClick = 0;

    public NextMapListener(Expunge plugin, CampaignGameManager gameManager) {
        super(plugin, gameManager);
        this.gameManager = gameManager;
    }

    private void check(Player player, Block block) {
        if (block == null) {
            return;
        }

        if (clicks == INVALID) {
            return;
        }

        if (!gameManager.getPlayers().contains(player) || player.getGameMode() != GameMode.ADVENTURE) {
            return;
        }

        var barriers = gameManager.getMap().barrierLocations();
        var clickedLoc = block.getLocation().toVector();
        if (!barriers.contains(new Barrier(clickedLoc, Material.BEEHIVE, true))) {
            return;
        }

        var debounce = System.currentTimeMillis() - lastClick;
        if (debounce < 100) {
            return;
        }
        lastClick = System.currentTimeMillis();

        BoundingBox endRegion = gameManager.getMap().endRegion();
        for (Player p : gameManager.getPlayers()) {
            if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                Location pLoc = p.getLocation();
                if (!endRegion.contains(pLoc.getX(), pLoc.getY(), pLoc.getZ())) {
                    ChatUtil.sendActionBar(player, "&cNot all alive players are in the safe-zone!");
                    return;
                }
            }
        }

        // this is reached when all alive players reach the end region
        var world = gameManager.getWorld();

        // first do a little animation for breaking the barrier (click check)
        clicks++;
        float progress = Math.min(1, (float) clicks / MAX_CLICKS);
        Bukkit.broadcastMessage(progress + "");
        world.playSound(block.getLocation(), Sound.BLOCK_WOOD_HIT, 1, 1);
        gameManager.getPlayers().forEach(p ->
                barriers.forEach(b -> {
                    if (b.isInit() && b.type() != Material.BARRIER) {
                        p.sendBlockDamage(b.position().toLocation(world), progress, player);
                    }
                })
        );
        if (clicks <= MAX_CLICKS) {
            return;
        }
        clicks = INVALID;

        // runs when players have clicked the barrier MAX_CLICKS times
        world.playSound(block.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        barriers.forEach(b -> {
            var loc = b.position().toLocation(world);
            if (b.isInit()) {
                world.setBlockData(loc, Material.BARRIER.createBlockData());
                world.spawnParticle(Particle.BLOCK, loc, 50, 0.2, 0.2, 0.2, b.type().createBlockData());
            } else {
                world.setBlockData(loc, b.type().createBlockData());
            }
        });

        gameManager.getWorld().getPlayers().forEach(p -> ChatUtil.sendFormatted(p, "&2Safe-zone reached!"));
        gameManager.endMap();

        // check if it is the last scene then end the game
        if (gameManager.getCampaign().maps().length - 1 <= ((CampaignGameState) gameManager.getState()).getCampaignMapIndex()) {
            gameManager.getWorld().getPlayers().forEach(p -> ChatUtil.sendFormatted(p, "&2END OF GAME"));

            // achievements
            boolean hasMrCookie = false;
            for (Player p : gameManager.getPlayers()) {
                Achievements.SURVIVOR.grant(p);

                // the departure achievements TODO: bring ts back
                // if (gameManager.getCampaign() instanceof TheDeparture) {
                //     Achievements.THE_DEPARTURE.grant(p);
                //     for (int i = 0; i < 5; i++) {
                //         ItemStack item = p.getInventory().getItem(i);
                //         if (item != null && item.getType() == Material.COOKIE) hasMrCookie = true;
                //     }
                // }
            }

            if (hasMrCookie) {
                for (Player p : gameManager.getPlayers()) {
                    Achievements.COOKIE_MONSTER.grant(p);
                }
            }

            gameManager.stop(false);
            return;
        }

        for (Player p : gameManager.getPlayers()) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 10 * 20, true, false));
            p.sendMessage(gameManager.getState().displayStats());
        }

        // increment scene index then start
        new BukkitRunnable() {
            int i = 5 * 20;
            @Override
            public void run() {
                if (!gameManager.isRunning() || gameManager.getPlayers().isEmpty()) {
                    this.cancel();
                }
                for (Player p : gameManager.getPlayers()) {
                    ChatUtil.sendActionBar(p, "Sending you to the next map...");
                }
                if (i <= 0) {
                    try {
                        gameManager.moveToNextMap();
                    } catch (RuntimeException e) {
                        for (Player p : gameManager.getPlayers()) {
                            ChatUtil.sendActionBar(p, "Something went wrong! Sending you back...");
                        }
                        e.printStackTrace();
                        gameManager.stop(true);
                    }
                    this.cancel();
                }
                i--;
            }
        }.runTaskTimer(plugin, 1, 1);
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent e) {
        if (!e.getAnimationType().equals(PlayerAnimationType.ARM_SWING)) {
            return;
        }

        check(e.getPlayer(), e.getPlayer().getTargetBlockExact(4));
    }

    @EventHandler
    public void onPress(PlayerInteractEvent e) {
        if (!gameManager.isRunning()) {
            return;
        }
        if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        check(e.getPlayer(), e.getClickedBlock());
    }
}
