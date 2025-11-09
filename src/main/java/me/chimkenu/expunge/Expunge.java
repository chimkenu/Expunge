package me.chimkenu.expunge;

import me.chimkenu.expunge.items.Items;
import me.chimkenu.expunge.listeners.global.GUIListener;
import me.chimkenu.expunge.listeners.global.ItemGlowListener;
import me.chimkenu.expunge.utils.ResourceCopy;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.logging.Level;

public final class Expunge extends JavaPlugin {
    private static Expunge instance;
    private static Lobby lobby;
    private static Items items;

    public static Expunge getInstance() {
        if (instance == null) {
            throw new RuntimeException("Expunge instance is null?");
        }
        return instance;
    }

    public static Lobby getLobby() {
        if (lobby == null) {
            lobby = new Lobby(getInstance(), getInstance().getConfig().getString("lobby-world"), getInstance().getConfig().getString("game-world"));
        }
        return lobby;
    }

    public static Items getItems() {
        if (items == null) {
            items = new Items(getInstance(), "items.yml");
        }
        if (items.list().isEmpty()) {
            items.reload();
        }
        return items;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            try {
                ResourceCopy.copyFromJar(this.getClass(), "Maps", getDataFolder().toPath());
            } catch (URISyntaxException | IOException e) {
                getLogger().log(Level.SEVERE, "Couldn't copy the maps to the plugin directory!");
                throw new RuntimeException(e);
            }
        }

        saveDefaultConfig();

        Objects.requireNonNull(getCommand("expunge")).setExecutor(new ExpungeCommand(this));
        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new ItemGlowListener(), this);

        instance = this;
        lobby = new Lobby(this, getConfig().getString("lobby-world"), getConfig().getString("game-world-prefix"));
        getItems();
    }

    @Override
    public void onDisable() {
        if (lobby != null) {
            lobby.unload();
        }
    }

    @Override
    public void reloadConfig() {
        if (lobby != null) {
            getLogger().info("RELOADING LOBBY!!!");
            lobby.unload();
        }
        super.reloadConfig();
    }
}
