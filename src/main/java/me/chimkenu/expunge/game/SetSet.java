package me.chimkenu.expunge.game;

import me.chimkenu.expunge.enums.Weapons;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class SetSet {
    final ArrayList<Player> keys;
    final HashMap<Player, Double[]> health;
    final HashMap<Player, ItemStack[]> hotbar;
    final HashMap<Player, Boolean> isAlive;
    final HashMap<Player, Integer> lives;
    final HashMap<Player, HashMap<Weapons.Guns, Integer>> ammo;

    public SetSet() {
        keys = new ArrayList<>();
        health = new HashMap<>();
        hotbar = new HashMap<>();
        isAlive = new HashMap<>();
        lives = new HashMap<>();
        ammo = new HashMap<>();
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

    public static HashMap<Weapons.Guns, Integer> getDefaultAmmo() {
        HashMap<Weapons.Guns, Integer> ammoMap = new HashMap<>();
        for (Weapons.Guns gun : Weapons.Guns.values()) {
            ammoMap.put(gun, gun.getGun().getMaxAmmo());
        }
        return ammoMap;
    }

    public void putDefaults(Player player) {
        if (!keys.contains(player)) {
            keys.add(player);
        }
        health.put(player, getDefaultHealth());
        hotbar.put(player, getDefaultHotbar());
        lives.put(player, getDefaultLives());
        ammo.put(player, getDefaultAmmo());
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

    public void putLives(Player player, Integer newLives) {
        if (!keys.contains(player)) {
            putDefaults(player);
        }
        lives.put(player, newLives);
    }

    public void putAmmo(Player player, HashMap<Weapons.Guns, Integer> ammoMap) {
        ammo.put(player, ammoMap);
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

    public HashMap<Weapons.Guns, Integer> getAmmo(Player player) {
        ammo.putIfAbsent(player, getDefaultAmmo());
        return ammo.get(player);
    }

    public void remove(Player player) {
        keys.remove(player);
        health.remove(player);
        hotbar.remove(player);
        isAlive.remove(player);
        lives.remove(player);
        ammo.remove(player);
    }

    public void clear() {
        keys.clear();
        health.clear();
        hotbar.clear();
        isAlive.clear();
        lives.clear();
        ammo.clear();
    }
}
