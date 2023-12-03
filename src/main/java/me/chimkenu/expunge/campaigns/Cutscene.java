package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public interface Cutscene {
    default int play(GameManager gameManager) {
        gameManager.getPlayers().forEach(player -> gameManager.getPlayers().forEach(toHide -> {
            if (player != toHide) player.hidePlayer(gameManager.getPlugin(), toHide);
        }));
        return -1;
    }

    default void end(GameManager gameManager) {
        gameManager.getPlayers().forEach(player -> gameManager.getPlayers().forEach(toHide -> {
            if (player != toHide) player.showPlayer(gameManager.getPlugin(), toHide);
        }));
    }

    static int displayLocation(GameManager gameManager, Location[] points, double blocksPerSecond, int stayTime) {
        int time = 0;
        for (int i = 0; i < points.length - 1; i++) {
            Location l0 = points[i];
            Location l1 = points[i + 1];

            if (l0.toVector().equals(l1.toVector())) {
                new BukkitRunnable() {
                    int t = stayTime;
                    @Override
                    public void run() {
                        gameManager.getWorld().getPlayers().forEach(player -> player.teleport(l0));
                        t--;
                        if (t < 0) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(gameManager.getPlugin(), 0, 1);
                time += stayTime;
                continue;
            }

            double distance = l0.toVector().distance(l1.toVector());
            double duration = distance / blocksPerSecond;

            int t = 0;
            while (t < duration) {
                int finalT = t;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        gameManager.getWorld().getPlayers().forEach(player -> player.teleport(lerpLocation(l0, l1, finalT / duration)));
                    }
                }.runTaskLater(gameManager.getPlugin(), time + t);
                t++;
            }
            time += t;
        }
        return time;
    }

    private static Location lerpLocation(Location v0, Location v1, double t) {
        Location direction = new Location(v0.getWorld(), 0, 0, 0);
        Vector d0 = v0.getDirection();
        Vector d1 = v1.getDirection();
        direction.setDirection(new Vector(lerp(d0.getX(), d1.getX(), t), lerp(d0.getY(), d1.getY(), t), lerp(d0.getZ(), d1.getZ(), t)));
        return new Location(v0.getWorld(), lerp(v0.getX(), v1.getX(), t), lerp(v0.getY(), v1.getY(), t), lerp(v0.getZ(), v1.getZ(), t), direction.getYaw(), direction.getPitch());
    }

    private static double lerp(double v0, double v1, double t) {
        return v0 + t * (v1 - v0);
    }
}
