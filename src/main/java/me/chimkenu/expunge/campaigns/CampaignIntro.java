package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;

public interface CampaignIntro extends Cutscene {
    default int play(GameManager gameManager, Location[] points, String main, String sub, Color mainColorStart, Color mainColorEnd, int delay, double blocksPerSecond, int stayTime) {
        displayTitle(gameManager, main, sub, mainColorStart, mainColorEnd, delay);
        return Cutscene.displayLocation(gameManager, points, blocksPerSecond, stayTime);
    }

    private void displayTitle(GameManager gameManager, String main, String sub, Color mainColorStart, Color mainColorEnd, int delay) {
        int time = delay;
        StringBuilder titlePart = new StringBuilder();
        for (int i = 0; i < main.length(); i++) {
            double a = i / (main.length() - 1d);
            int r = (int) ((1 - a) * mainColorStart.getRed() + a * mainColorEnd.getRed());
            int g = (int) ((1 - a) * mainColorStart.getGreen() + a * mainColorEnd.getGreen());
            int b = (int) ((1 - a) * mainColorStart.getBlue() + a * mainColorEnd.getBlue());
            int finalI = i;

            if (main.charAt(i) == ' ') {
                delay += (int) (delay * 0.5);
            }

            titlePart.append(ChatUtil.getColor(r, g, b)).append(main.charAt(finalI));
            final String finalTitlePart = titlePart.toString();
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player p : gameManager.getWorld().getPlayers()) {
                        p.sendTitle(finalTitlePart, "", 0, 20, 1);
                        if (main.charAt(finalI) != ' ')
                            p.playSound(p, Sound.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 0.5f, (float) (1.6 + ThreadLocalRandom.current().nextDouble() * 0.4));
                        else
                            p.playSound(p, Sound.BLOCK_GROWING_PLANT_CROP, SoundCategory.PLAYERS, 0.5f, 0);
                    }
                }
            }.runTaskLater(gameManager.getPlugin(), time);
            time += delay;
        }
        final String finalTitlePart = titlePart.toString();
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : gameManager.getWorld().getPlayers()) {
                    p.sendTitle(finalTitlePart, sub, 0, 80, 20);
                    p.playSound(p, Sound.AMBIENT_CAVE, 2, 0);
                    p.playSound(p, Sound.BLOCK_GROWING_PLANT_CROP, SoundCategory.PLAYERS, 1, 0);
                }
            }
        }.runTaskLater(gameManager.getPlugin(), time + delay);
    }
}
