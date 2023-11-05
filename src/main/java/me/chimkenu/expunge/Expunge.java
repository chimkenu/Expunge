package me.chimkenu.expunge;

import me.chimkenu.expunge.commands.*;
import me.chimkenu.expunge.enums.Achievements;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.BreakGlass;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.LocalGameManager;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.game.PlayerStats;
import me.chimkenu.expunge.game.listeners.*;
import me.chimkenu.expunge.campaigns.CampaignMap;
import me.chimkenu.expunge.campaigns.thedeparture.TheDeparture;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.guns.listeners.*;
import me.chimkenu.expunge.guns.weapons.guns.Gun;
import me.chimkenu.expunge.guns.weapons.guns.Pistol;
import me.chimkenu.expunge.mobs.MobListener;
import me.chimkenu.expunge.utils.ResourceCopy;
import me.chimkenu.expunge.utils.Utils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public final class Expunge extends JavaPlugin {
    private Lobby lobby;

    @Override
    public void onEnable() {
        try {
            ResourceCopy.copyFromJar(this.getClass(), "Maps", getDataFolder().toPath());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        registerGameEvents();

        registerCommand("join", new Join());
        registerCommand("values", new Values());
        registerCommand("bye", new Bye());
        registerCommand("startgame", new StartGame());
        registerCommand("stats", new Stats());
        registerCommand("getgun", new GetGun());
        registerCommand("getutiltiy", new GetUtility());
        registerCommand("getmelee", new GetMelee());
        registerCommand("tutorial", new Tutorial());
        registerCommand("spawn", new Spawn());

        lobby = new Lobby(this, Bukkit.getWorld("world"), new Location(Bukkit.getWorld("world"), 0, 0, 0));
        getServer().getPluginManager().registerEvents(lobby, this);
    }

    @Override
    public void onDisable() {
        for (Queue queue : lobby.getQueues()) {
            queue.stop(true);
        }
        for (GameManager gameManager : lobby.getGames()) {
            gameManager.stop(true);
        }
    }

    public void registerGameEvents() {
        HandlerList.unregisterAll(this);
        getServer().getPluginManager().registerEvents(new JoinLeave(), this);
        getServer().getPluginManager().registerEvents(new Shove(), this);
        getServer().getPluginManager().registerEvents(new NextScene(), this);
        getServer().getPluginManager().registerEvents(new DeathRevive(), this);
        getServer().getPluginManager().registerEvents(new PickUp(), this);
        getServer().getPluginManager().registerEvents(new Shoot(), this);
        getServer().getPluginManager().registerEvents(new Reload(), this);
        getServer().getPluginManager().registerEvents(new Swing(), this);
        getServer().getPluginManager().registerEvents(new UtilityListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        getServer().getPluginManager().registerEvents(new MobListener(), this);
    }

    private void registerCommand(String commandName, CommandExecutor executioner) {
        PluginCommand command = getCommand(commandName);
        if (command == null) {
            return;
        }

        command.setExecutor(executioner);
        if (executioner instanceof TabCompleter) {
            command.setTabCompleter((TabCompleter) executioner);
        }
    }

    public Lobby getLobby() {
        return lobby;
    }
}
