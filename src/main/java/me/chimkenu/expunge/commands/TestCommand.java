package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;

public class TestCommand implements CommandExecutor {
    private final ArrayList<String> components = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        if (args.length > 1) {
            for (String s : components) {
                Expunge.instance.getLogger().log(Level.INFO, s);
            }
            return true;
        }
        String s = player.getX() + ", " + player.getY() + ", " + player.getZ();
        player.sendMessage(Component.text(s));
        components.add(s);
        return true;
    }
}
