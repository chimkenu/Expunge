package me.chimkenu.expunge.game;

import org.bukkit.entity.Player;

import java.util.Set;

public abstract class GameState {
    private long sysTimeStart;
    private long timeTicks;
    private long gameTicks;

    public GameState() {
        sysTimeStart = -1;
        timeTicks = 0;
        gameTicks = 0;
    }

    public long getSysTimeStart() {
        return sysTimeStart;
    }
    public void setSysTimeStart() {
        sysTimeStart = System.currentTimeMillis();
    }
    public long getTimeTicks() {
        return timeTicks;
    }
    public long getGameTicks() {
        return gameTicks;
    }
    public void incrementTimeTicks() {
        timeTicks++;
        gameTicks++;
    }
    public void resetGameTicks() {
        gameTicks = 0;
    }

    public abstract Set<Player> getPlayers();
}
