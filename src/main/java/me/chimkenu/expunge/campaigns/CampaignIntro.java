package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;
import java.time.Duration;
import java.util.logging.Level;

public interface CampaignIntro {
    int play(GameManager gameManager);

    default int play(GameManager gameManager, Location[] points, String main, Component sub, Color mainColorStart, Color mainColorEnd, int delay, double blocksPerSecond, int stayTime) {
        displayTitle(gameManager, main, sub, mainColorStart, mainColorEnd, delay);
        return displayLocation(gameManager, points, blocksPerSecond, stayTime);
    }

    private void displayTitle(GameManager gameManager, String main, Component sub, Color mainColorStart, Color mainColorEnd, int delay) {
        Title.Times charTime = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(100));
        Title.Times finalTime = Title.Times.times(Duration.ZERO, Duration.ofMillis(3000), Duration.ofMillis(500));

        int time = delay;
        Component titlePart = Component.text("");
        for (int i = 0; i < main.length(); i++) {
            double a = i / (main.length() - 1d);
            int r = (int) ((1 - a) * mainColorStart.getRed() + a * mainColorEnd.getRed());
            int g = (int) ((1 - a) * mainColorStart.getGreen() + a * mainColorEnd.getGreen());
            int b = (int) ((1 - a) * mainColorStart.getBlue() + a * mainColorEnd.getBlue());
            int finalI = i;

            if (main.charAt(i) == ' ') {
                delay += (int) (delay * 0.5);
            }

            titlePart = titlePart.append(Component.text(main.charAt(finalI)).color(TextColor.color(r, g, b)));
            Component finalTitlePart = titlePart;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : gameManager.getWorld().getPlayers()) {
                        p.showTitle(Title.title(finalTitlePart, Component.empty(), charTime));
                        if (main.charAt(finalI) != ' ')
                            p.playSound(p, Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5f, (float) (1.6 + Math.random() * 0.4));
                        else
                            p.playSound(p, Sound.BLOCK_GROWING_PLANT_CROP, SoundCategory.PLAYERS, 0.5f, 0);
                    }
                }
            }.runTaskLater(gameManager.getPlugin(), time);
            time += delay;
        }
        Component finalTitlePart = titlePart;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : gameManager.getWorld().getPlayers()) {
                    p.showTitle(Title.title(finalTitlePart, sub, finalTime));
                    p.playSound(p, Sound.AMBIENT_CAVE, 2, 0);
                    p.playSound(p, Sound.BLOCK_GROWING_PLANT_CROP, SoundCategory.PLAYERS, 1, 0);
                }
            }
        }.runTaskLater(gameManager.getPlugin(), time + delay);
    }

    private int displayLocation(GameManager gameManager, Location[] points, double blocksPerSecond, int stayTime) {
        int time = 0;
        for (int i = 0; i < points.length - 1; i++) {
            Location l0 = points[i];
            Location l1 = points[i + 1];

            if (l0.toVector().equals(l1.toVector())) {
                for (Player p : gameManager.getWorld().getPlayers()) {
                    p.teleport(l0);
                }
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
                        for (Player p : gameManager.getWorld().getPlayers()) {
                            p.teleport(lerpLocation(l0, l1, finalT / duration));
                        }
                    }
                }.runTaskLater(gameManager.getPlugin(), time + t);
                t++;
            }
            time += t;
        }
        return time;
    }

    private Location lerpLocation(Location v0, Location v1, double t) {
        Location direction = new Location(v0.getWorld(), 0, 0, 0);
        Vector d0 = v0.getDirection();
        Vector d1 = v1.getDirection();
        direction.setDirection(new Vector(lerp(d0.getX(), d1.getX(), t), lerp(d0.getY(), d1.getY(), t), lerp(d0.getZ(), d1.getZ(), t)));
        return new Location(v0.getWorld(), lerp(v0.getX(), v1.getX(), t), lerp(v0.getY(), v1.getY(), t), lerp(v0.getZ(), v1.getZ(), t), direction.getYaw(), direction.getPitch());
    }

    private double lerp(double v0, double v1, double t) {
        return v0 + t * (v1 - v0);
    }
}
