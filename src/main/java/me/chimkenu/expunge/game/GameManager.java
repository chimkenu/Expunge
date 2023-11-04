package me.chimkenu.expunge.game;

public interface GameManager {
    void start();
    void stop(boolean isAbrupt);
    boolean isRunning();
}
