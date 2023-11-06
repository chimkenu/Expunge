package me.chimkenu.expunge;

import me.chimkenu.expunge.commands.*;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.*;
import me.chimkenu.expunge.listeners.game.*;
import me.chimkenu.expunge.utils.ResourceCopy;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;

public final class Expunge extends JavaPlugin {
    public static Expunge instance;
    private Lobby lobby;

    @Override
    public void onEnable() {
        instance = this;
        try {
            ResourceCopy.copyFromJar(this.getClass(), "Maps", getDataFolder().toPath());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        registerGameEvents();

        registerCommand("join", new Join());
        registerCommand("values", new Values());
        registerCommand("bye", new Bye());
        registerCommand("startgame", new StartGame(this));
        registerCommand("stats", new Stats());
        registerCommand("getgun", new GetGun());
        registerCommand("getutiltiy", new GetUtility());
        registerCommand("getmelee", new GetMelee());
        registerCommand("tutorial", new Tutorial());
        registerCommand("spawn", new Spawn());
        registerCommand("test", new TestCommand());

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
        getServer().getPluginManager().registerEvents(new UtilityListener(), this);
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
