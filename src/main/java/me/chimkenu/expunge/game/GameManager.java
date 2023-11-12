package me.chimkenu.expunge.game;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.director.Director;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public interface GameManager {
    void start();
    void stop(boolean isAbrupt);
    boolean isRunning();
    void startMap();
    void endMap();
    void restartMap();
    void loadNextMap();
    void moveToNextMap();
    Campaign getCampaign();
    int getCampaignMapIndex();
    CampaignMap getMap();
    World getWorld();
    Difficulty getDifficulty();
    Set<Player> getPlayers();
    PlayerStats getPlayerStat(Player player);
    JavaPlugin getPlugin();
    Director getDirector();
}
