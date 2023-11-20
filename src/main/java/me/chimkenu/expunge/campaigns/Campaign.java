package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Set;

public abstract class Campaign {
    private final Component name;
    private final CampaignMap[] maps;
    private final String mainDirectory;

    public Campaign(String name, Color color1, Color color2, CampaignMap[] maps, String mainDirectory) {
        Component nameComponent = Component.empty();
        for (int i = 0; i < name.length(); i++) {
            double a = i / (name.length() - 1d);
            int r = (int) ((1 - a) * color1.getRed() + a * color2.getRed());
            int g = (int) ((1 - a) * color1.getGreen() + a * color2.getGreen());
            int b = (int) ((1 - a) * color1.getBlue() + a * color2.getBlue());
            nameComponent = nameComponent.append(Component.text(name.charAt(i), TextColor.color(r, g, b)));
        }
        this.name = nameComponent;
        this.maps = maps;
        this.mainDirectory = mainDirectory;
    }

    public Component getName() {
        return name;
    }

    public CampaignMap[] getMaps() {
        return maps;
    }

    public String getMainDirectory() {
        return mainDirectory;
    }

    public enum List {
        THE_DEPARTURE {
            @Override
            public Campaign get() {
                return new TheDeparture();
            }

            @Override
            public Material getGUIMaterial() {
                return Material.WOODEN_HOE;
            }
        };
        public abstract Campaign get();
        public abstract Material getGUIMaterial();
    }

    public static void playCrescendoEventEffect(Set<Player> players) {
        for (Player p : players) {
            p.sendRichMessage("<Yellow>Here they come...");
            p.playSound(p, Sound.AMBIENT_CAVE, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
}
