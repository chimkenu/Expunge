package me.chimkenu.expunge.campaigns.thedeparture.extras;

import me.chimkenu.expunge.campaigns.Cutscene;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

import static me.chimkenu.expunge.campaigns.Campaign.playCrescendoEventEffect;

public class HighwayCarBoom implements Cutscene {
    @Override
    public int play(GameManager manager) {
        var gameManager = (CampaignGameManager) manager;
        HashMap<Player, Location> viewers = new HashMap<>();

        // filter viewers then stop if viewers < 0
        for (Player p : gameManager.getPlayers()) {
            if (p.getPassengers().isEmpty() && p.getVehicle() == null) {
                p.addScoreboardTag("HIGHWAY_SCENE");
                viewers.put(p, p.getLocation());
            }
        }
        if (viewers.isEmpty()) return 0;

        final int sceneAttempt = gameManager.getAttempts();
        gameManager.setSpawningActive(false);
        // TODO: gameManager.getDirector().clearEntities();

        new BukkitRunnable() {
            int i = 20 * 2;
            final Location loc = new Location(gameManager.getWorld(), -93.5, 36, 223.5);
            @Override
            public void run() {
                if (!gameManager.isRunning() || gameManager.getAttempts() != sceneAttempt) {
                    this.cancel();
                    return;
                }
                for (Player p : viewers.keySet()) {
                    p.teleport(loc);
                }
                i--;
                if (i <= 0) {
                    gameManager.getWorld().getBlockAt(-93, 31, 218).setType(Material.REDSTONE_BLOCK);
                    this.cancel();
                }
            }
        }.runTaskTimer(gameManager.getPlugin(), 0, 1);

        // wait 4 (+2) seconds after animation then bring em back
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!gameManager.isRunning() && gameManager.getAttempts() != sceneAttempt) {
                    this.cancel();
                    return;
                }

                playCrescendoEventEffect(gameManager.getPlayers());
                gameManager.setSpawningActive(true);
                // TODO: spawn horde
                for (Player p : viewers.keySet()) {
                    p.teleport(viewers.get(p));
                    p.removeScoreboardTag("HIGHWAY_SCENE");
                }
            }
        }.runTaskLater(gameManager.getPlugin(), (20 * 6) + 1);

        return 20 * 6 + 1;
    }
}
