package me.chimkenu.expunge.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerSet {
    final ArrayList<Player> keys;
    final HashMap<Player, Double[]> health;
    final HashMap<Player, ItemStack[]> hotbar;
    final HashMap<Player, Boolean> isAlive;
    final HashMap<Player, Integer> lives;

    public PlayerSet() {
        keys = new ArrayList<>();
        health = new HashMap<>();
        hotbar = new HashMap<>();
        isAlive = new HashMap<>();
        lives = new HashMap<>();
    }

    public static Double[] getDefaultHealth() {
        Double[] defaultHealth = new Double[2];
        defaultHealth[0] = 20d;
        defaultHealth[1] = 0d;
        return defaultHealth;
    }

    public static ItemStack[] getDefaultHotbar() {
        ItemStack[] defaultHotbar = new ItemStack[5];
        for (int i = 0; i < 5; i++) {
            defaultHotbar[i] = new ItemStack(Material.AIR);
        }
        return defaultHotbar;
    }

    public static int getDefaultLives() {
        return 3;
    }

    public void putDefaults(Player player) {
        if (!keys.contains(player)) {
            keys.add(player);
        }
        health.put(player, getDefaultHealth());
        hotbar.put(player, getDefaultHotbar());
        lives.put(player, getDefaultLives());
        isAlive.put(player, true);
    }

    public void putHealth(Player player, Double[] newHealth) {
        if (!keys.contains(player)) {
            putDefaults(player);
        }
        health.put(player, newHealth);
    }

    public void putHotbar(Player player, ItemStack[] newHotbar) {
        if (!keys.contains(player)) {
            putDefaults(player);
        }
        hotbar.put(player, newHotbar);
    }

    public void setIsAlive(Player player, Boolean bool) {
        if (!keys.contains(player)) {
            putDefaults(player);
        }
        isAlive.put(player, bool);
    }

    public void putLives(Player player, int newLives) {
        if (!keys.contains(player)) {
            putDefaults(player);
        }
        lives.put(player, newLives);
    }

    public ArrayList<Player> getKeys() {
        return keys;
    }

    public Double[] getHealth(Player player) {
        health.putIfAbsent(player, getDefaultHealth());
        return health.get(player);
    }

    public ItemStack[] getHotbar(Player player) {
        hotbar.putIfAbsent(player, getDefaultHotbar());
        return hotbar.get(player);
    }

    public boolean isAlive(Player player) {
        isAlive.putIfAbsent(player, true);
        return isAlive.get(player);
    }

    public int getLives(Player player) {
        lives.putIfAbsent(player, getDefaultLives());
        return lives.get(player);
    }

    public void remove(Player player) {
        keys.remove(player);
        health.remove(player);
        hotbar.remove(player);
        isAlive.remove(player);
        lives.remove(player);
    }

    public void clear() {
        keys.clear();
        health.clear();
        hotbar.clear();
        isAlive.clear();
        lives.clear();
    }
}
