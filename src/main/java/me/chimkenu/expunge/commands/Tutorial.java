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

    private ArrayList<Dialogue> getDialogues() {
        ArrayList<Dialogue> dialogues = new ArrayList<>();
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed]");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 1"));
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to &aExpunge&r!",
                "&b[Tutorial] » This is a &eFirst-Person-Shooter Survival &rgame where you have to survive the &2Zombie Apocalpse&r!"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 2"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 0"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 1 1 1 1"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 3"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 1"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 2 2"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 4"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 2"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 3 3 3"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 5"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 3"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 4"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 6"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 4"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 5 5 5"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 7"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 5"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 6 6 6 6"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 8"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 6"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 7 7 7"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Proceed] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 9"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 7"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 8"
        ));
        dialogues.add(new Dialogue(player -> {
            TextComponent proceed = new TextComponent("[Finish] ");
            proceed.setColor(net.md_5.bungee.api.ChatColor.GREEN);
            proceed.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial -1"));
            TextComponent back = new TextComponent("[Back]");
            back.setColor(net.md_5.bungee.api.ChatColor.GOLD);
            back.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tutorial 8"));
            proceed.addExtra(back);
            player.spigot().sendMessage(proceed);
        },
                "&b[Tutorial] » Welcome to Expunge!",
                "&b[Tutorial] » test test 9 9"
        ));
        return dialogues;
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

        if (index < 0) {
            inTutorial.remove(player);
            player.teleport(Expunge.currentMap.getScenes().get(Expunge.currentSceneIndex).startLocation());
            player.sendMessage(ChatColor.GREEN + "Finished tutorial! Sending you back...");
            return true;
        }

        // clear chat
        for (int i = 0; i < 500; i++) {
            player.sendMessage("");
        }
        ArrayList<Dialogue> dialogues = getDialogues();
        ArrayList<Location> locations = getLocations();
        if (index > dialogues.size() - 1) index = 0;

        player.teleport(locations.get(index));
        dialogues.get(index).displayDialogue(new ArrayList<>(List.of(player)));
        player.setFlying(true);
        return true;
    }
}
