package me.chimkenu.expunge.listeners.game;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.listeners.GameListener;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class NextMapListener extends GameListener {
    public final CampaignGameManager gameManager;

    private final Map<String, Object> data = new HashMap<>();
    private boolean isFinishedChecking = false;
    private long lastCheck = 0;

    public NextMapListener(Expunge plugin, CampaignGameManager gameManager) {
        super(plugin, gameManager);
        this.gameManager = gameManager;
    }

    private void nextMap() {
        isFinishedChecking = true;
        gameManager.getWorld().getPlayers().forEach(p -> ChatUtil.sendFormatted(p, "&2Safe-zone reached!"));
        gameManager.endMap();

        // check if it is the last scene then end the game
        if (gameManager.getCampaign().maps().length - 1 <= gameManager.getState().getCampaignMapIndex()) {
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

    private boolean debounceCheck() {
        final int DEBOUNCE = 300;
        var now = System.currentTimeMillis();
        if (now - lastCheck > DEBOUNCE) {
            lastCheck = now;
            return true;
        }
        return false;
    }

    private void check(Event e) {
        if (isFinishedChecking) {
            return;
        }
        if (!gameManager.getMap().nextMapCondition().init(gameManager, e, data)) {
            return;
        }
        if (!debounceCheck()) {
            return;
        }
        if (!gameManager.getMap().nextMapCondition().check(gameManager, e, data)) {
            return;
        }
        if (!gameManager.getMap().nextMapCondition().sideEffect(gameManager, e, data)) {
            return;
        }
        nextMap();
    }

    @EventHandler
    public void onSwing(PlayerAnimationEvent e) {
        check(e);
    }

    @EventHandler
    public void onPress(PlayerInteractEvent e) {
        check(e);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        check(e);
    }
}
