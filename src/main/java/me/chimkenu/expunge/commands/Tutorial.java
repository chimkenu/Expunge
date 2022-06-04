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
                "&b[Tutorial] » This is a &eFirst-Person-Shooter Survival &fgame where you have to get through the &2Zombie Apocalpse&f!",
                "&b[Tutorial] » Try to &eget to safety&f alone or with friends using weapons and items found throughout the map!"
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
                "&b[Tutorial] » Progress through the game by reaching safe-zones.",
                "&b[Tutorial] » But be careful, zombies will try to stop you at every opportunity!"
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
                "&b[Tutorial] » Throughout the map, you will find &cweapons&r, &bitems&r, and &9ammo piles&r.",
                "&b[Tutorial] » To pick up items, hold sneak near the item. To get ammo from ammo piles, right click the ammo pile.",
                "&b[Tutorial] » &eNote: There is a limit to the certain amount of items you can hold."
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
                "&b[Tutorial] » You can find &chealing items&r and &6throwable items &rthroughout the map.",
                "&b[Tutorial] » To use them, simply right click holding the item. Some items will take time to use, and some will require you to stand still."
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
                "&b[Tutorial] » Guns. The amount of items represent the clip, and the xp bar shows your total ammo left.",
                "&b[Tutorial] » Right click to shoot, left click to reload. For laggy people, you may have to aim ahead rather than where the enemy currently is.",
                "&b[Tutorial] » &eNote: if the gun gets stuck reloading, drop it. It should fix it."
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
                "&b[Tutorial] » Melee weapons. Right click to use."
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
                "&b[Tutorial] » Shove. When you left click a zombie, it will shove them.",
                "&b[Tutorial] » Shoving knocks zombies back and stuns them for a short duration.",
                "&b[Tutorial] » This is useful when dealing with hordes, however repeatedly shoving will increase its cooldown.",
                "&b[Tutorial] » &eNote: You can shove while holding a gun, but if you are not aiming at a zombie it will make the gun reload."
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
                "&b[Tutorial] » You are given 3 lives. You get knocked down the first two times you are killed, giving a chance to your teammates (if any) to revive you.",
                "&b[Tutorial] » While knocked down, you are given a pistol to shoot zombies. You will have to be saved by a teammate.",
                "&b[Tutorial] » To save a knocked down teammate, simply hold sneak near them.",
                "&b[Tutorial] » &eNote: Using a medkit will restore your 3 lives.",
                "&b[Tutorial] » In the unfortunate event that you do die, it is not the end! If one of your teammates find a defibrillator, they can use it to revive you.",
                "&b[Tutorial] » Additionally, if your teammates reach the next safe-zone without you, you will respawn at their location."
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
                "&b[Tutorial] » Safe-zones. These are your checkpoints in the game.",
                "&b[Tutorial] » When you and all your teammates die, you will all be sent back to the last safe-zone you reached.",
                "&b[Tutorial] » Safe-zones with &6spruce doors&f will have good weapons and healing items for you to use before going out again.",
                "&b[Tutorial] » To reach a safe-zone, &epress the button&r inside while all your teammates (that are alive) are within the safe-zone."
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
                "&b[Tutorial] » This concludes the tutorial. To begin, press the &a[Finish]&r button then type the command &e/join&r to join the current game or to start a new one."
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
