package me.chimkenu.expunge.commands;

import me.chimkenu.expunge.Expunge;
import me.chimkenu.expunge.Queue;
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

public class Tutorial implements CommandExecutor {
    public static final ArrayList<Player> inTutorial = new ArrayList<>();

    private ArrayList<String[]> getTutorialLines() {
        ArrayList<String[]> lines = new ArrayList<>();
        lines.add(new String[]{
                "Welcome to &aExpunge&f!",
                "This is a &eFirst-Person-Shooter Survival &fgame where you have to get through the &2Zombie Apocalpse&f!",
                "Try to &eget to safety&f alone or with friends using weapons and items found throughout the map!"
        });
        lines.add(new String[]{
                "Progress through the game by reaching safe-zones.",
                "But be careful, zombies will try to stop you at every opportunity!"
        });
        lines.add(new String[]{
                "Throughout the map, you will find &cweapons&r, &bitems&r, and &9ammo piles&r.",
                "To pick up items, sneak near the item.",
                "To get ammo from ammo piles, simply right click the ammo pile.",
                "&eNote: There is a limit to the certain amount of items you can hold."
        });
        lines.add(new String[]{
                "You can find &chealing items&r and &6throwable items &rthroughout the map.",
                "To use them, left click holding the item. Some items will take time to use, and some will require you to stand &7&o(somewhat)&r still."
        });
        lines.add(new String[]{
                "Guns. The &eamount of items&r represent the clip, and the &exp bar&r shows your total ammo left.",
                "Left click to shoot, sneak + left click to reload.",
                "For laggy people, you may have to aim &eahead&r rather than where the enemy currently is.",
                "&eNote: if the gun gets stuck reloading, drop it. It should fix it."
        });
        lines.add(new String[]{
                "Melee weapons. Left click to use."
        });
        lines.add(new String[]{
                "Shove. When you right click a zombie, it will shove them.",
                "Shoving &eknocks&r zombies back and &estuns&r them for a short duration.",
                "This is useful when dealing with hordes, however repeatedly shoving will &eincrease its cooldown&r.",
                "&eNote: You can shove while holding items."
        });
        lines.add(new String[]{
                "You are given &e3 lives&r. You get &eknocked down&r the first two times you are killed, giving a chance for your teammates &7&o(if any)&r to revive you.",
                "While knocked down, you are given a pistol to shoot zombies. You will have to be saved by a teammate.",
                "To save a knocked down teammate, simply &ehold sneak near them&r.",
                "&eNote: Using a medkit will restore your 3 lives.",
                "In the unfortunate event that you do die, it is not the end! If one of your teammates find a defibrillator, they can use it to revive you.",
                "Additionally, if your teammates reach the next safe-zone without you, you will respawn at their location."
        });
        lines.add(new String[]{
                "Safe-zones. These are your checkpoints in the game.",
                "When you and all your teammates die, you will all be sent back to the last safe-zone you reached.",
                "Safe-zones with &6spruce doors&f will have good weapons and healing items for you to use before going out again.",
                "To reach a safe-zone, &epress the button&r inside while all your teammates &7&o(that are alive)&r are within the safe-zone."
        });
        lines.add(new String[]{
                "This concludes the tutorial. To begin, press the &a[Finish]&r button then type the command &e/join&r to join the current game or to start a new one."
        });
        return lines;
    }

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
        if (Expunge.playing.getKeys().contains(player) || Queue.contains(player)) {
            sender.sendMessage(ChatColor.RED + "You should've done this before joining!");
            return true;
        }
        if (!inTutorial.contains(player)) {
            inTutorial.add(player);
        }

        int index = 0;
        int part = 0;
        if (args.length > 0) {
            try {
                index = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {}
            try {
                part = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        if (index < 0 || part < 0) {
            inTutorial.remove(player);
            player.teleport(Expunge.currentMap.getMaps().get(Expunge.currentSceneIndex).startLocation());
            player.sendMessage(ChatColor.GREEN + "Finished tutorial! Sending you back...");
            return true;
        }

        if (index > getLocations().size() - 1) index = 0;
        if (part > getTutorialLines().get(index).length - 1) part = 0;

        if (part == 0) {
            // clear chat
            for (int i = 0; i < 100; i++) {
                player.sendMessage("");
            }
            // teleport
            player.teleport(getLocations().get(index));
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[Tutorial] &8»&f " + getTutorialLines().get(index)[part]));
        TextComponent component = new TextComponent("");
        if (part == 0 && index - 1 >= 0) {
            TextComponent back = new TextComponent("[Back] ");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial " + (index - 1) + " 0"));
            component.addExtra(back);
        }
        if (part + 1 < getTutorialLines().get(index).length) {
            TextComponent next = new TextComponent("[Next] ");
            next.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            next.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial " + index + " " + (part + 1)));
            component.addExtra(next);
        }
        else if (index + 1 < getLocations().size()) {
            TextComponent proceed = new TextComponent("[Next] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial " + (index + 1) + " 0"));
            component.addExtra(proceed);
        } else {
            TextComponent finish = new TextComponent("[Finish]");
            finish.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            finish.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial " + "-1 " + "-1"));
            component.addExtra(finish);
        }

        player.spigot().sendMessage(component);
        player.setAllowFlight(true);
        player.setFlying(true);
        return true;
    }
}
