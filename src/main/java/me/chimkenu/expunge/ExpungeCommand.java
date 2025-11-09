package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.commands.TestCommand;
import me.chimkenu.expunge.enums.Campaigns;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.game.campaign.CampaignGameManager;
import me.chimkenu.expunge.entities.MobType;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.guis.MenuGUI;
import me.chimkenu.expunge.items.GameItem;
import me.chimkenu.expunge.items.Interactable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ExpungeCommand implements CommandExecutor, TabCompleter {
    private final Expunge plugin;

    public ExpungeCommand(Expunge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("expunge")) {
            sendNoPermission(sender);
            return true;
        }

        if (args.length < 1) {
            if (sender instanceof Player player) {
                new MenuGUI(Expunge.getLobby(), player).open(player);
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
            case "test" -> new TestCommand(plugin).onCommand(sender, command, label, args);
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
                        for (Campaign campaign : Campaigns.values()) {
                            tabComplete.add(campaign.directoryName());
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
                    return tabComplete;
                }
                case "get" -> {
                    if (args.length > 2) {
                        break;
                    }
                    for (GameItem item : Expunge.getItems().list()) {
                        if (item.id().contains(args[1])) {
                            tabComplete.add(item.id());
                        }
                    }
                }
                case "spawn" -> {
                    if (args.length == 2) {
                        for (MobType infectedTypes : MobType.values()) {
                            tabComplete.add(infectedTypes.name());
                        }
                        for (GameItem interactable : Expunge.getItems().list(item -> item instanceof Interactable)) {
                            if (interactable.id().contains(args[1])) {
                                tabComplete.add(interactable.id());
                            }
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
        /* TODO bruh
        var gameManager = plugin.getLobby().getCurrentGame();
        sender.sendMessage("World: " + gameManager.getWorld().getName());
        sender.sendMessage("Difficulty: " + gameManager.getDifficulty());
        CampaignDirector director = gameManager.getDirector();
        sender.sendMessage("sceneTime: " + director.getSceneTime());
        sender.sendMessage("sceneAttempts: " + director.getSceneAttempts());
        sender.sendMessage("isSpawningEnabled: " + director.getMobHandler().isSpawningEnabled());
        sender.sendMessage("activeMobsSize: " + director.getMobHandler().getActiveMobs().size());
        sender.sendMessage("totalKills: " + director.getStatsHandler().getTotalKills());
         */
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

        Campaign campaign;
        try {
            campaign = Campaigns.valueOf(args[1]);
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
            Expunge.getLobby().changeMap(campaign);
            Expunge.getLobby().startNewGame(new CampaignGameManager(plugin, Expunge.getLobby().getGameWorld(), new HashSet<>(List.of(player)), campaign, difficulty, 0));
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
        Expunge.getLobby().changeMap(campaign);
        Expunge.getLobby().startNewGame(new CampaignGameManager(plugin, Expunge.getLobby().getGameWorld(), players, campaign, difficulty, 0));
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

        Expunge.getLobby().stopGame();
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

        Expunge.getItems().toGameItem(args[1]).ifPresentOrElse(gameItem -> {
            sender.spigot().sendMessage(new ComponentBuilder("Here you go.").color(ChatColor.GREEN).create());
            player.getInventory().addItem(gameItem.toItem());
        }, () -> {
            var error = new ComponentBuilder("Unknown item ").color(ChatColor.RED)
                    .append("'" + args[1] + "'").color(ChatColor.GRAY)
                    .create();
            sender.spigot().sendMessage(error);
        });
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

        GameManager gameManager = Expunge.getLobby().getCurrentGame();
        if (gameManager == null || !gameManager.isRunning()) {
            sendError(sender, "No active game.");
            return;
        }

        MobType infectedType = null;
        Interactable interactable = null;
        try {
            infectedType = MobType.valueOf(args[1]);
        } catch (IllegalArgumentException ignored) {
        }
        try {
            interactable = (Interactable) Expunge.getItems().toGameItem(args[1]).get();
        } catch (Exception ignored) {
        }

        if (infectedType != null) {
            gameManager.spawnInfected(infectedType, player.getLocation().toVector());
            sendInfo(sender, "Spawned.");
        } else if (interactable != null) {
            gameManager.addEntity(interactable.spawn(player.getWorld(), player.getLocation().toVector(), false));
            sendInfo(sender, "Spawned.");
        } else {
            sendError(sender, "Unknown entity.");
        }
    }

    private void handleSpwan(CommandSender sender, String[] args) {
        if (!sender.hasPermission("expunge.spawn")) {
            sendNoPermission(sender);
            return;
        }
        if (!(sender instanceof Player player)) {
            sendPlayerOnly(sender);
            return;
        }
        if (args.length < 3) {
            sendUsage(sender, "Insufficient arguments.", "/expunge spawn <entity> <type> <game-id>");
            return;
        }

        String entityName = args[1].toUpperCase();
        String goalName = args[2].toUpperCase();
        String gameId = args[3];
    }

    private void handleReload(CommandSender sender) {
        plugin.reloadConfig();
        sender.spigot().sendMessage(new ComponentBuilder("Reloaded config.").color(ChatColor.GREEN).create());
    }
}
