package me.chimkenu.expunge;

import me.chimkenu.expunge.commands.*;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.utils.ResourceCopy;
import org.bukkit.*;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;

public final class Expunge extends JavaPlugin {
    private Lobby lobby;

    @Override
    public void onEnable() {
        try {
            ResourceCopy.copyFromJar(this.getClass(), "Maps", getDataFolder().toPath());
        } catch (URISyntaxException | IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Couldn't copy the maps to the plugin directory!");
            throw new RuntimeException(e);
        }

        registerCommand("join", new Join());
        registerCommand("values", new Values());
        registerCommand("bye", new Bye());
        registerCommand("startgame", new StartGame(this));
        registerCommand("stats", new Stats());
        registerCommand("getgun", new GetGun());
        registerCommand("getutility", new GetUtility());
        registerCommand("getmelee", new GetMelee());
        registerCommand("tutorial", new Tutorial());
        registerCommand("spawn", new Spawn());
        registerCommand("test", new TestCommand(this));

        getServer().getPluginManager().registerEvents(getLobby(), this);
    }

    @Override
    public void onDisable() {
        for (Queue queue : getLobby().getQueues()) {
            queue.stop(true);
        }
        for (GameManager gameManager : getLobby().getGames()) {
            gameManager.stop(true);
        }
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
        if (lobby == null) {
            lobby = new Lobby(this, Bukkit.getWorld("world"), new Vector(0, 0, 0));
        }
        return lobby;
    }
}
