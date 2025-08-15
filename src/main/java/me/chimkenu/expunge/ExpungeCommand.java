package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.enums.InfectedTypes;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.guis.MenuGUI;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.interactables.Interactable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ExpungeCommand implements CommandExecutor, TabCompleter {
    private final JavaPlugin plugin;
    private final Lobby lobby;

    public ExpungeCommand(JavaPlugin plugin, Lobby lobby) {
        this.plugin = plugin;
        this.lobby = lobby;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("expunge")) {
            sendNoPermission(sender);
            return true;
        }

        if (args.length < 1) {
            if (sender instanceof Player player) {
                new MenuGUI(lobby, player).open(player);
                return true;
            }

            sendUsage(sender, "Insufficient arguments.", "/expunge [tutorial|stats|start|stop|get|spawn|reload]");
            return true;
        }

        String subcommand = args[0].toLowerCase();

        switch (subcommand) {
            case "tutorial" -> handleTutorial(sender);
            case "stats" -> handleStats(sender);
            case "start" -> handleStart(sender, args);
            case "stop" -> handleStop(sender, args);
            case "get" -> handleGet(sender, args);
            case "spawn" -> handleSpawn(sender, args);
            case "reload" -> handleReload(sender);
            default -> sendUsage(sender, "Insufficient arguments.", "/expunge [tutorial|stats|start|stop|get|spawn|reload]");
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!command.getName().equalsIgnoreCase("expunge")) {
            return null;
        }

        List<String> tabComplete = new ArrayList<>(List.of("tutorial", "stats", "start", "stop", "get", "spawn", "reload"));
        tabComplete.removeIf(s -> !sender.hasPermission("expunge." + s + ((s.equals("stats") ? ".player" : ""))));

        if (args.length > 1) {
            tabComplete = new ArrayList<>();
            switch (args[0]) {
                case "stats" -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        tabComplete.add(player.getName());
                    }
                }
                case "start" -> {
                    if (args.length < 3) {
                        for (Campaign.List campaign : Campaign.List.values()) {
                            tabComplete.add(campaign.toString());
                        }
                    } else if (args.length < 4) {
                        for (Difficulty difficulty : Difficulty.values()) {
                            tabComplete.add(difficulty.toString());
                        }
                    } else {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            tabComplete.add(player.getName());
                        }
                    }
                }
                case "stop" -> {
                    for (GameManager gameManager : lobby.getGames()) {
                        tabComplete.add(gameManager.getUUID().toString());
                    }
                }
                case "get" -> {
                    if (args.length > 2) {
                        break;
                    }
                    for (GameItems gameItems : GameItems.values()) {
                        tabComplete.add(gameItems.name());
                    }
                }
                case "spawn" -> {
                    if (args.length == 2) {
                        for (InfectedTypes infectedTypes : InfectedTypes.values()) {
                            tabComplete.add(infectedTypes.name());
                        }
                        for (GameItems interactable : GameItems.getInteractables()) {
                            tabComplete.add(interactable.name());
                        }
                    } else if (args.length == 3) {
                        for (GameManager gameManager : lobby.getGames()) {
                            tabComplete.add(gameManager.getUUID().toString());
                        }
                    }
                }
            }
        }

        return tabComplete;
    }

    private void sendInfo(CommandSender sender, String info) {
        sender.spigot().sendMessage(new ComponentBuilder(info).color(ChatColor.YELLOW).create());
    }

    private void sendError(CommandSender sender, String error) {
        sender.spigot().sendMessage(new ComponentBuilder(error).color(ChatColor.RED).create());
    }

    private void sendNoPermission(CommandSender sender) {
        sendError(sender, "Insufficient permissions.");
    }

    private void sendUsage(CommandSender sender, String error, String usage) {
        var message = new ComponentBuilder(error + " ").color(ChatColor.RED)
                .append(usage).color(ChatColor.GRAY)
                .create();
        sender.spigot().sendMessage(message);
    }

    private void sendPlayerOnly(CommandSender sender) {
        sender.spigot().sendMessage(new ComponentBuilder("Only players can execute this command.").color(ChatColor.RED).create());
    }

    private void handleTutorial(CommandSender sender) {
        sender.sendMessage("not implemented yet");
    }

    private void handleStats(CommandSender sender) {
        for (GameManager gameManager : lobby.getGames()) {
            sender.sendMessage("Game: " + gameManager.getUUID());
            sender.sendMessage("World: " + gameManager.getWorld().getName());
            sender.sendMessage("Difficulty: " + gameManager.getDifficulty());
            Director director = gameManager.getDirector();
            sender.sendMessage("sceneTime: " + director.getSceneTime());
            sender.sendMessage("sceneAttempts: " + director.getSceneAttempts());
            sender.sendMessage("isSpawningEnabled: " + director.getMobHandler().isSpawningEnabled());
            sender.sendMessage("activeMobsSize: " + director.getMobHandler().getActiveMobs().size());
            sender.sendMessage("totalKills: " + director.getStatsHandler().getTotalKills());
        }
    }

    private void handleStart(CommandSender sender, String[] args) {
        if (!sender.hasPermission("expunge.start")) {
            sendNoPermission(sender);
            return;
        }
        if (args.length < 3) {
            sendUsage(sender, "Insufficient arguments.", "/expunge start <campaign> <difficulty> [players...]");
            sender.spigot().sendMessage(
                    new ComponentBuilder("Note that if players are specified you have to include yourself!")
                            .color(ChatColor.DARK_GRAY).create()
            );
            return;
        }

        Campaign.List campaign;
        try {
            campaign = Campaign.List.valueOf(args[1]);
        } catch (IllegalArgumentException e) {
            var error = new ComponentBuilder("Unknown campaign ").color(ChatColor.RED)
                    .append("'" + args[1] + "'").color(ChatColor.GRAY)
                    .create();
            sender.spigot().sendMessage(error);
            return;
        }
        Difficulty difficulty;
        try {
            difficulty = Difficulty.valueOf(args[2]);
        } catch (IllegalArgumentException e) {
            var error = new ComponentBuilder("Unknown difficulty").color(ChatColor.RED)
                    .append("'" + args[2] + "'").color(ChatColor.GRAY)
                    .create();
            sender.spigot().sendMessage(error);
            return;
        }

        if (args.length == 3) {
            if (!(sender instanceof Player player)) {
                sendError(sender, "Please include a list of players!");
                return;
            }

            sendInfo(sender, "Starting a game...");
            lobby.createGame(plugin, campaign.get(), difficulty, new HashSet<>(List.of(player)));
            return;
        }

        HashSet<Player> players = new HashSet<>();
        for (int i = 3; i < args.length; i++) {
            Player player = Bukkit.getPlayer(args[i]);
            if (player == null || !player.isOnline()) {
                var error = new ComponentBuilder(args[i]).color(ChatColor.YELLOW)
                        .append(" isn't online.").color(ChatColor.RED)
                        .create();
                sender.spigot().sendMessage(error);
                return;
            }
            players.add(player);
        }

        sender.spigot().sendMessage(new ComponentBuilder("Starting a game...").color(ChatColor.YELLOW).create());
        lobby.createGame(plugin, campaign.get(), difficulty, players);
    }

    private void handleStop(CommandSender sender, String[] args) {
        if (!sender.hasPermission("expunge.stop")) {
            sendNoPermission(sender);
            return;
        }
        if (args.length < 2) {
            sendUsage(sender, "Provide a game ID.", "/expunge stop <game-id>");
            return;
        }

        UUID id;
        GameManager gameManager = null;
        try {
            id = UUID.fromString(args[1]);
            for (GameManager gm : lobby.getGames()) {
                if (gm.getUUID().equals(id)) {
                    gameManager = gm;
                }
            }
            if (gameManager == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            sender.spigot().sendMessage(new ComponentBuilder("No game with that ID exists.").color(ChatColor.RED).create());
            return;
        }

        gameManager.stop(true);
    }

    private void handleGet(CommandSender sender, String[] args) {
        if (!sender.hasPermission("expunge.get")) {
            sendNoPermission(sender);
            return;
        }
        if (!(sender instanceof Player player)) {
            sendPlayerOnly(sender);
            return;
        }
        if (args.length < 2) {
            sendUsage(sender, "Insufficient arguments.", "/expunge get <item>");
            return;
        }

        GameItem gameItem;
        try {
            gameItem = GameItems.valueOf(args[1]).getGameItem();
        } catch (IllegalArgumentException e) {
            var error = new ComponentBuilder("Unknown item ").color(ChatColor.RED)
                    .append("'" + args[1] + "'").color(ChatColor.GRAY)
                    .create();
            sender.spigot().sendMessage(error);
            return;
        }

        sender.spigot().sendMessage(new ComponentBuilder("Here you go.").color(ChatColor.GREEN).create());
        player.getInventory().addItem(gameItem.get());
    }

    private void handleSpawn(CommandSender sender, String[] args) {
        if (!sender.hasPermission("expunge.spawn")) {
            sendNoPermission(sender);
            return;
        }
        if (!(sender instanceof Player player)) {
            sendPlayerOnly(sender);
            return;
        }
        if (args.length < 3) {
            sendUsage(sender, "Insufficient arguments.", "/expunge spawn <entity> <game-id>");
            return;
        }

        UUID id;
        GameManager gameManager = null;
        try {
            id = UUID.fromString(args[2]);
            for (GameManager gm : lobby.getGames()) {
                if (gm.getUUID().equals(id)) {
                    gameManager = gm;
                }
            }
            if (gameManager == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            sendError(sender, "No game with that ID exists.");
            return;
        }

        InfectedTypes infectedType = null;
        Interactable interactable = null;
        try {
            infectedType = InfectedTypes.valueOf(args[1]);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            interactable = (Interactable) GameItems.valueOf(args[1]).getGameItem();
        } catch (Exception ignored) {
        }

        if (infectedType != null) {
            gameManager.getDirector().getMobHandler().addMob(infectedType.spawn(plugin, gameManager, player.getLocation().toVector(), gameManager.getDifficulty()));
            sendInfo(sender, "Spawned.");
        } else if (interactable != null) {
            gameManager.getDirector().getItemHandler().addEntity(interactable.spawn(player.getLocation()));
            sendInfo(sender, "Spawned.");
        } else {
            sendError(sender, "Unknown entity.");
        }
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.spigot().sendMessage(new ComponentBuilder("Reloaded config.").color(ChatColor.GREEN).create());
    }
}
