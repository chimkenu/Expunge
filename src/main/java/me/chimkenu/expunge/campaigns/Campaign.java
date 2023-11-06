package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;

public abstract class Campaign {
    private final String name;
    private final CampaignMap[] maps;
    private final String mainDirectory;

    public Campaign(String name, CampaignMap[] maps, String mainDirectory) {
        this.name = name;
        this.maps = maps;
        this.mainDirectory = mainDirectory;
    }

    public String getName() {
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
        };
        public abstract Campaign get();
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
