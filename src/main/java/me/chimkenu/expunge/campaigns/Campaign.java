package me.chimkenu.expunge.campaigns;

import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Set;

public abstract class Campaign {
    private final CampaignMap[] maps;

    public Campaign(CampaignMap[] maps) {
        this.maps = maps;
    }

    public abstract String name();
    public abstract String displayName();
    public abstract String directoryName();
    public abstract String builderNames();
    public abstract Material guiMaterial();
    public CampaignMap[] maps() {
        return maps;
    }

    public static void playCrescendoEventEffect(Set<Player> players) {
        for (Player p : players) {
            ChatUtil.sendInfo(p, "Here they come...");
            p.playSound(p, Sound.AMBIENT_CAVE, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_ATTACK_WOODEN_DOOR, SoundCategory.PLAYERS, 1f, 1f);
            p.playSound(p, Sound.ENTITY_ZOMBIE_AMBIENT, SoundCategory.PLAYERS, 1f, 1f);
        }
    }
}
