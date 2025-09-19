package me.chimkenu.expunge.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PlayerStats {
    private final int MAX_LIVES = 3;

    // game stats
    private double health;
    private double absorption;
    private ItemStack[] hotbar;
    private boolean isAlive;
    private int lives;
    private int livesFromLastSave;

    // skill stats
    private int commonInfectedKills;
    private int specialInfectedKills;
    private int shots;
    private int shotsHit;
    private int headshots;

    public PlayerStats(ItemStack[] hotbar) {
        health = 20d;
        absorption = 0d;
        this.hotbar = hotbar;
        isAlive = true;
        lives = MAX_LIVES;
        livesFromLastSave = MAX_LIVES;
    }

    public PlayerStats() {
        this(new ItemStack[5]);
        Arrays.fill(hotbar, new ItemStack(Material.AIR));
    }

    public void revive() {
        setHealth(10);
        setAbsorption(0);
        Arrays.fill(hotbar, new ItemStack(Material.AIR));
        setAlive(true);
        setLives(MAX_LIVES);
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
    public void resetLives() {
        this.lives = MAX_LIVES;
    }
    public int getLivesFromLastSave() {
        return livesFromLastSave;
    }
    public void setLivesFromLastSave(int livesFromLastSave) {
        this.livesFromLastSave = livesFromLastSave;
    }

    public int getCommonKills() {
        return commonInfectedKills;
    }
    public void setCommonKills(int kills) {
        commonInfectedKills = kills;
    }
    public void addCommonKill() {
        commonInfectedKills++;
    }
    public int getSpecialKills() {
        return specialInfectedKills;
    }
    public void setSpecialKills(int kills) {
        specialInfectedKills = kills;
    }
    public void addSpecialKill() {
        specialInfectedKills++;
    }

    public double getAccuracy() {
        if (shots == 0) return 0;
        return (double) shotsHit / shots;
    }

    public double getHeadshotAccuracy() {
        if (shotsHit == 0) return 0;
        return (double) headshots / shotsHit;
    }

    public void addShot(boolean shotHit, boolean headshot) {
        shots += 1;
        shotsHit += shotHit ? 1 : 0;
        headshots += headshot ? 1 : 0;
    }

    public void savePlayerStats(Player p) {
        setHealth(p.getHealth());
        setAbsorption(p.getAbsorptionAmount());
        ItemStack[] itemStacks = getHotbar();
        for (int i = 0; i < 5; i++) {
            itemStacks[i] = p.getInventory().getItem(i);
        }
        setHotbar(itemStacks);
        setLivesFromLastSave(getLives());
    }

    public void loadPlayerStats(Player p) {
        p.setHealth(getHealth());
        p.setAbsorptionAmount(getAbsorption());
        p.getInventory().clear();
        for (int i = 0; i < getHotbar().length; i++) {
            p.getInventory().setItem(i, getHotbar()[i]);
        }
        setLives(getLivesFromLastSave());
    }
}
