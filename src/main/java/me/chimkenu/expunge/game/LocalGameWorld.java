package me.chimkenu.expunge.game;

import me.chimkenu.expunge.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class LocalGameWorld implements GameWorld {
    private final File sourceWorldFolder;
    private File activeWorldFolder;
    private World world;

    public LocalGameWorld(File sourceWorldFolder) {
        this.sourceWorldFolder = sourceWorldFolder;
    }

    @Override
    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(), sourceWorldFolder.getName() + "_" + System.currentTimeMillis());

        try {
            FileUtils.copyDirectory(sourceWorldFolder.toPath().toString(), activeWorldFolder.toPath().toString());
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to copy source to world folder " + activeWorldFolder.getName());
            e.printStackTrace();
            return false;
        }

        world = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
        if (world != null) world.setAutoSave(false);
        return isLoaded();
    }

    @Override
    public void unload() {
        if (world != null) Bukkit.unloadWorld(world, false);
        if (activeWorldFolder != null) {
            try {
                FileUtils.deleteDirectory(activeWorldFolder.toPath());
            } catch (IOException e) {
                Bukkit.getLogger().severe("Failed to delete " + activeWorldFolder.getName());
                e.printStackTrace();
            }
        }

        world = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean isLoaded() {
        return world != null;
    }

    @Override
    public World getWorld() {
        return world;
    }
}
