package me.chimkenu.expunge.game.campaign;

import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.GameState;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.game.PlayerStatsable;
import me.chimkenu.expunge.utils.ChatUtil;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CampaignGameState extends GameState implements PlayerStatsable {
    private final Map<Player, PlayerStats> players;

    private Difficulty difficulty;
    private int campaignMapIndex;
    private int attempts;

    public CampaignGameState(Set<Player> players, Difficulty difficulty, int campaignMapIndex) {
        super();
        this.players = new HashMap<>();
        this.difficulty = difficulty;
        this.campaignMapIndex = campaignMapIndex;
        attempts = 0;

        players.forEach(p -> this.players.put(p, new PlayerStats()));
    }

    @Override
    public Set<Player> getPlayers() {
        return players.keySet();
    }

    @Override
    public PlayerStats getPlayerStat(Player player) {
        return players.get(player);
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getCampaignMapIndex() {
        return campaignMapIndex;
    }
    public void setCampaignMapIndex(int campaignMapIndex) {
        this.campaignMapIndex = campaignMapIndex;
    }
    public void incrementCampaignMapIndex() {
        campaignMapIndex++;
    }

    public int getAttempts() {
        return attempts;
    }
    public void resetAttempts() {
        attempts = 0;
    }
    public void incrementAttempts() {
        attempts++;
    }

    public int getTotalKills() {
        int total = 0;
        for (PlayerStats s : players.values()) {
            total += s.getCommonKills();
            total += s.getSpecialKills();
        }
        return total;
    }

    public String displayStats() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        StringBuilder stats = new StringBuilder("&7&lGame Stats:");
        for (Player p : getPlayers()) {
            var stat = getPlayerStat(p);
            stats.append("    ")
                    .append(p.getDisplayName())
                    .append('\n')
                    .append("&7        Kills: ").append(stat.getCommonKills()).append(" common, ").append(stat.getSpecialKills()).append(" special")
                    .append('\n')
                    .append("&7        Accuracy: ").append(decimalFormat.format(stat.getAccuracy() * 100)).append("% (").append(decimalFormat.format(stat.getHeadshotAccuracy() * 100)).append("% headshot)")
                    .append('\n');
        }

        return ChatUtil.format(stats.toString());
    }
}
