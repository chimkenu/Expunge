package me.chimkenu.expunge;

import me.chimkenu.expunge.commands.*;
import me.chimkenu.expunge.enums.Weapons;
import me.chimkenu.expunge.game.Director;
import me.chimkenu.expunge.game.SetSet;
import me.chimkenu.expunge.game.listeners.*;
import me.chimkenu.expunge.game.maps.*;
import me.chimkenu.expunge.guns.guns.Gun;
import me.chimkenu.expunge.guns.guns.Pistol;
import me.chimkenu.expunge.guns.listeners.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

public final class Expunge extends JavaPlugin {

    public static JavaPlugin instance;
    public static final HashSet<Player> spectators = new HashSet<>();
    public static final HashSet<Player> inQueue = new HashSet<>();
    public static final SetSet playing = new SetSet();

    public static boolean isCountdownRunning;
    public static boolean isGameRunning;
    public static boolean isSpawningEnabled;
    public static Map currentMap;
    public static int currentSceneIndex;
    public static final int difficulty = 1;
    public static Director runningDirector;

    @Override
    public void onEnable() {
        instance = this;
        isGameRunning = false;
        currentMap = new TheDeparture();
        currentSceneIndex = 0;

        getServer().getPluginManager().registerEvents(new JoinLeave(), instance);
        getServer().getPluginManager().registerEvents(new Shove(), instance);
        getServer().getPluginManager().registerEvents(new NextScene(), instance);
        getServer().getPluginManager().registerEvents(new DeathRevive(), instance);
        getServer().getPluginManager().registerEvents(new PickUp(), instance);

        getServer().getPluginManager().registerEvents(new Shoot(), this);
        getServer().getPluginManager().registerEvents(new Reload(), this);
        getServer().getPluginManager().registerEvents(new Swing(), this);
        getServer().getPluginManager().registerEvents(new UtilityListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getCommand("join").setExecutor(new Join());
        getCommand("values").setExecutor(new Values());
        getCommand("bye").setExecutor(new Bye());
        getCommand("startgame").setExecutor(new StartGame());

        getCommand("getgun").setExecutor(new GetGun());
        getCommand("getutility").setExecutor(new GetUtility());
        getCommand("getmelee").setExecutor(new GetMelee());

        getCommand("tutorial").setExecutor(new Tutorial());
    }

    public static void setSpectator(Player player) {
        playing.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(instance, p);
            p.showPlayer(instance, player);

            if (playing.getKeys().contains(p)) {
                p.hidePlayer(instance, player);
            }
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setHealth(20d);
        player.setAbsorptionAmount(0d);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setPlayerListName(ChatColor.GRAY + player.getName());
        spectators.add(player);
    }

    public static void setPlaying(Player player) {
        spectators.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            player.showPlayer(instance, p);
            p.showPlayer(instance, player);

            if (spectators.contains(p)) {
                player.hidePlayer(instance, p);
            }
        }

        player.setGameMode(GameMode.ADVENTURE);
        player.setPlayerListName(ChatColor.YELLOW + player.getName());
        newPlayerStats(player);
    }

    public static void newPlayerStats(Player player) {
        ItemStack[] itemStacks = SetSet.getDefaultHotbar();
        if (playing.getKeys().contains(player)) {
            // this is reached when the player is already in the game but will respawn
            // give player lower tier items (if they had an item to begin with)
            if (playing.getHotbar(player)[0] != null && playing.getHotbar(player)[0].getType() != Material.AIR) {
                ArrayList<Gun> guns = Utils.getTier1Guns();
                itemStacks[0] = guns.get(ThreadLocalRandom.current().nextInt(0, guns.size())).getGun();
            }
            if (playing.getHotbar(player)[1] != null && playing.getHotbar(player)[1].getType() != Material.AIR) {
                itemStacks[1] = new Pistol().getGun();
            }
        }
        playing.putDefaults(player);
        playing.putHotbar(player, itemStacks);
    }

    public static void savePlayerStats(Player player) {
        playing.putHealth(player, new Double[] {player.getHealth(), player.getAbsorptionAmount()});
        ItemStack[] itemStacks = playing.getHotbar(player);
        for (int i = 0; i < 5; i++) {
            itemStacks[i] = player.getInventory().getItem(i);
        }
        playing.putHotbar(player, itemStacks);
        playing.putLives(player, DeathRevive.currentLives.get(player));

        HashMap<Weapons.Guns, Integer> ammoMap = new HashMap<>();
        for (Weapons.Guns gun : Shoot.getAmmo(player).keySet()) {
            ammoMap.put(gun, Shoot.getAmmo(player, gun));
        }
        playing.putAmmo(player, ammoMap);
    }

    public static void loadPlayerStats(Player player) {
        player.setHealth(playing.getHealth(player)[0]);
        player.setAbsorptionAmount(playing.getHealth(player)[1]);

        HashMap<Weapons.Guns, Integer> ammoMap = new HashMap<>();
        for (Weapons.Guns gun : Expunge.playing.getAmmo(player).keySet()) {
            ammoMap.put(gun, Expunge.playing.getAmmo(player).get(gun));
        }
        Shoot.setAmmo(player, ammoMap);

        for (int i = 0; i < 5; i++) {
            player.getInventory().setItem(i, playing.getHotbar(player)[i]);
        }

        DeathRevive.currentLives.put(player, playing.getLives(player));
    }

    public static void startScene(Scene scene) {
        isSpawningEnabled = true;
        for (Player p : spectators) {
            if (!(p.getGameMode() == GameMode.CREATIVE)) {
                p.leaveVehicle();
                p.teleport(scene.startLocation());
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
            }
        }

        // load player stats
        for (Player p : playing.getKeys()) {
            p.removePotionEffect(PotionEffectType.GLOWING);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4, false, false, false));
            p.leaveVehicle();
            p.teleport(scene.startLocation());
            // for some reason, the code above does not teleport players that are riding vehicles, hence the code below
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.teleport(scene.startLocation());
                }
            }.runTaskLater(instance, 1);

            p.getInventory().clear();
            p.setGameMode(GameMode.ADVENTURE);

            loadPlayerStats(p);
            playing.setIsAlive(p, true);
        }

        // reload events
        if (scene.crescendoEvent() != null) HandlerList.unregisterAll(scene.crescendoEvent());
        if (scene.crescendoEvent() != null) instance.getServer().getPluginManager().registerEvents(scene.crescendoEvent(), instance);
        if (scene.happenings() != null) {
            for (Listener happening : scene.happenings()) {
                HandlerList.unregisterAll(happening);
                instance.getServer().getPluginManager().registerEvents(happening, instance);
            }
        }

        // director
        runningDirector.generateItems();
        runningDirector.spawnStartingMobs();

        // run dialogue && other stuff
        if (scene.runAtStart() != null) scene.runAtStart().run(null);
        if (scene.dialogueSolo() != null && playing.getKeys().size() == 1) scene.dialogueSolo().displayDialogue(playing.getKeys());
        else if (scene.dialogueCoOp() != null && playing.getKeys().size() > 1) scene.dialogueCoOp().displayDialogue(playing.getKeys());
    }

    public static void endScene(Scene scene) {
        isSpawningEnabled = false;
        if (scene.runAtEnd() != null) scene.runAtEnd().run(null);

        if (scene.crescendoEvent() != null) HandlerList.unregisterAll(scene.crescendoEvent());
        if (scene.happenings() != null) {
            for (Listener happening : scene.happenings()) {
                HandlerList.unregisterAll(happening);
            }
        }

        runningDirector.resetSceneAttempts();
        runningDirector.clearEntities();

        // save player stats
        for (Player p : playing.getKeys()) {
            if (!playing.isAlive(p)) {
                // player had died but others reached the safe-zone
                newPlayerStats(p);
            }
            savePlayerStats(p);
        }
    }

    public static void restartScene(Scene scene) {
        isSpawningEnabled = false;
        runningDirector.clearEntities();
        runningDirector.incrementSceneAttempts();
        startScene(scene);
    }

    public static void startGame(boolean doCountdown) {
        if (isGameRunning) {
            return;
        }
        if (doCountdown) {
            if (isCountdownRunning) {
                // the countdown is already running!
                return;
            }
            inQueue.clear();
            isCountdownRunning = true;
            new BukkitRunnable() {
                int count = 31;
                @Override
                public void run() {
                    count--;
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eGame starting in §b" + count + "§e. Do §b/join §eto join!"));
                    }
                    if (count < 1) {
                        isCountdownRunning = false;
                        if (!Expunge.isGameRunning) {
                            for (Player p : inQueue) {
                                setPlaying(p);
                            }
                            startGame();
                        }
                        inQueue.clear();
                        this.cancel();
                    }
                }
            }.runTaskTimer(instance, 1, 20);
        } else startGame();
    }

    public static void startGame() {
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Starting game...");
        if (playing.getKeys().size() < 1) {
            Bukkit.broadcastMessage(ChatColor.RED + "Cancelled, not enough players to start the game.");
            return;
        }

        currentMap = new TheDeparture();
        currentSceneIndex = 0;

        isCountdownRunning = false;
        isGameRunning = true;
        runningDirector = new Director();
        HandlerList.unregisterAll(runningDirector);
        instance.getServer().getPluginManager().registerEvents(runningDirector, instance);
        runningDirector.runTaskTimer(instance, 1, 1);

        startScene(currentMap.getScenes().get(currentSceneIndex));
    }

    public static void stopGame() {
        if (!isGameRunning) {
            return;
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getGameMode() != GameMode.CREATIVE) {
                p.setHealth(20d);
                p.setAbsorptionAmount(0d);
                p.setFoodLevel(20);
                p.getInventory().clear();
                setSpectator(p);
                p.teleport(currentMap.getScenes().get(0).startLocation());
            }
        }

        playing.clear();
        currentSceneIndex = 0;
        isSpawningEnabled = false;
        runningDirector.clearEntities();
        runningDirector.cancel();
        HandlerList.unregisterAll(runningDirector);
        runningDirector = null;
        isGameRunning = false;
        isCountdownRunning = false;
    }
}
