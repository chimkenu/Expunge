package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.game.Dialogue;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Tutorial implements CommandExecutor {
    public static final ArrayList<Player> inTutorial = new ArrayList<>();

    private ArrayList<Location> getLocations() {
        ArrayList<Location> locations = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        locations.add(new Location(world,58.5, -59, 13.5, 0, 0));
        locations.add(new Location(world,46.5, -53, 35.5, 0, 60));
        locations.add(new Location(world,-1.5, -53, 35.5, 0, 40));
        locations.add(new Location(world,-13.5, -53, 9.5, 0, 40));
        locations.add(new Location(world,34.5, -55, 10.5, 0, 45));
        locations.add(new Location(world,19.5, -55, 36.5, -45, 30));
        locations.add(new Location(world,7.5, -55, 10.5, -45, 30));
        locations.add(new Location(world,-25.5, -53, 9.5, 0, 40));
        locations.add(new Location(world,-37.5, -55, 11.5, 0, 37.69f));
        locations.add(new Location(world,-49.5, -59, 13.5, 0, 0));
        return locations;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can execute this command.");
            return true;
        }
        if (Expunge.playing.getKeys().contains(player) || Expunge.inQueue.contains(player)) {
            sender.sendMessage(ChatColor.RED + "You should've done this before joining!");
            return true;
        }
        if (!inTutorial.contains(player)) {
            inTutorial.add(player);
        }

        int index = 0;
        if (args.length > 0) {
            try {
                index = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
        }
        if (index > getLocations().size() - 1) index = 0;

        if (index < 0) {
            inTutorial.remove(player);
            player.teleport(Expunge.currentMap.getScenes().get(Expunge.currentSceneIndex).startLocation());
            player.sendMessage(ChatColor.GREEN + "Finished tutorial! Sending you back...");
            return true;
        }

        // clear chat
        for (int i = 0; i < 10; i++) {
            player.sendMessage("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
        }

        player.teleport(getLocations().get(index));
        player.setAllowFlight(true);
        player.setFlying(true);
        return true;
    }
}
