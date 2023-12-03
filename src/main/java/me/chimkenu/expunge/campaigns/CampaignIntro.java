package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public interface CampaignIntro extends Cutscene {
    default int play(GameManager gameManager, Location[] points, String main, Component sub, Color mainColorStart, Color mainColorEnd, int delay, double blocksPerSecond, int stayTime) {
        displayTitle(gameManager, main, sub, mainColorStart, mainColorEnd, delay);
        return Cutscene.displayLocation(gameManager, points, blocksPerSecond, stayTime);
    }

    private void displayTitle(GameManager gameManager, String main, Component sub, Color mainColorStart, Color mainColorEnd, int delay) {
        Title.Times charTime = Title.Times.times(Duration.ZERO, Duration.ofMillis(1000), Duration.ofMillis(100));
        Title.Times finalTime = Title.Times.times(Duration.ZERO, Duration.ofMillis(4000), Duration.ofMillis(1000));

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
                            p.playSound(p, Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5f, (float) (1.6 + ThreadLocalRandom.current().nextDouble() * 0.4));
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
}
