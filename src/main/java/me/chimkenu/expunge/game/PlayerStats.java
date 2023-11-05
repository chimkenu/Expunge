package me.chimkenu.expunge.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PlayerStats {
    private double health;
    private double absorption;
    private ItemStack[] hotbar;
    private boolean isAlive;
    private int lives;
    private int livesFromLastSave;

    public PlayerStats(ItemStack[] hotbar) {
        health = 20d;
        absorption = 0d;
        this.hotbar = hotbar;
        isAlive = true;
        lives = 3;
    }

    public PlayerStats() {
        this(new ItemStack[5]);
        Arrays.fill(hotbar, new ItemStack(Material.AIR));
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getAbsorption() {
        return absorption;
    }

    public void setAbsorption(double absorption) {
        this.absorption = absorption;
    }

    public ItemStack[] getHotbar() {
        return hotbar;
    }

    public void setHotbar(ItemStack[] hotbar) {
        this.hotbar = hotbar;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLivesFromLastSave() {
        return livesFromLastSave;
    }

    public void setLivesFromLastSave(int livesFromLastSave) {
        this.livesFromLastSave = livesFromLastSave;
    }
}
