package me.chimkenu.expunge;

import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.listeners.global.GUIListener;
import me.chimkenu.expunge.utils.ResourceCopy;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;

public final class Expunge extends JavaPlugin {
    private Lobby lobby;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            try {
                ResourceCopy.copyFromJar(this.getClass(), "Maps", getDataFolder().toPath());
            } catch (URISyntaxException | IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Couldn't copy the maps to the plugin directory!");
                throw new RuntimeException(e);
            }
        }

        saveDefaultConfig();

        Objects.requireNonNull(getCommand("expunge")).setExecutor(new ExpungeCommand(this, getLobby()));
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
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

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        lobby = null;
    }

    public Lobby getLobby() {
        if (lobby == null) {
            lobby = new Lobby(this, Bukkit.getWorld("world"), new Vector(getConfig().getDouble("lobby-spawn.x"), getConfig().getDouble("lobby-spawn.y"), getConfig().getDouble("lobby-spawn.z")));
        }
        return lobby;
    }
}
