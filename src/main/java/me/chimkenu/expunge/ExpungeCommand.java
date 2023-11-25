package me.chimkenu.expunge;

import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.enums.GameItems;
import me.chimkenu.expunge.game.GameManager;
import me.chimkenu.expunge.game.director.Director;
import me.chimkenu.expunge.guis.MenuGUI;
import me.chimkenu.expunge.items.GameItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
            sender.sendMessage(Component.text("Insufficient permissions."));
            return true;
        }

        if (args.length < 1) {
            if (sender instanceof Player player) {
                new MenuGUI(lobby, player).open(player);
                return true;
            }

            sender.sendMessage(Component.text("Insufficient arguments. ", NamedTextColor.RED).append(Component.text("/expunge [tutorial|stats|start|stop|get|spawn|reload]")));
            return true;
        }

        String arg = args[0].toLowerCase();

        switch (arg) {
            case "tutorial" -> {

            }
            case "stats" -> {
                if (lobby.getGames().isEmpty()) {
                    return true;
                }
                for (GameManager gameManager : lobby.getGames()) {
                    sender.sendMessage("Game: " + gameManager.getUUID());
                    sender.sendMessage("World: " + gameManager.getWorld().getName());
                    sender.sendMessage("Difficulty: " + gameManager.getDifficulty());
                    Director director = gameManager.getDirector();
                    sender.sendMessage("sceneTime: " + director.getSceneTime());
                    sender.sendMessage("sceneAttempts: " + director.getSceneAttempts());
                    sender.sendMessage("isSpawningEnabled: " + director.getMobHandler().isSpawningEnabled());
                    sender.sendMessage("activeMobsSize" + director.getMobHandler().getActiveMobs().size());
                }
            }
            case "start" -> {
                if (!sender.hasPermission("expunge.start")) {
                    sender.sendMessage(Component.text("Insufficient permissions.", NamedTextColor.RED));
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage(Component.text("Insufficient arguments. ", NamedTextColor.RED).append(Component.text("/expunge start <campaign> <difficulty> [players...] ", NamedTextColor.GRAY)).append(Component.text("Note that if players are specified you have to include yourself!", NamedTextColor.DARK_GRAY)));
                    return true;
                }

                Campaign.List campaign;
                Difficulty difficulty;
                try {
                    campaign = Campaign.List.valueOf(args[1]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("No such campaign exists, did you type it correctly?", NamedTextColor.RED));
                    return true;
                }
                try {
                    difficulty = Difficulty.valueOf(args[2]);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("No such difficulty exists, did you type it correctly?", NamedTextColor.RED));
                    return true;
                }

                if (args.length == 3) {
                    if (!(sender instanceof Player player)) {
                        sender.sendMessage(Component.text("Please include a list of players!", NamedTextColor.RED));
                        return true;
                    }

                    sender.sendMessage(Component.text("Starting a game...", NamedTextColor.YELLOW));
                    lobby.createGame(plugin, campaign.get(), difficulty, new HashSet<>(List.of(player)));
                    return true;
                }

                HashSet<Player> players = new HashSet<>();
                for (int i = 3; i < args.length; i++) {
                    Player player = Bukkit.getPlayer(args[i]);
                    if (player == null || !player.isOnline()) {
                        sender.sendMessage(Component.text(args[i], NamedTextColor.YELLOW).append(Component.text(" isn't online.", NamedTextColor.RED)));
                        return true;
                    }
                    players.add(player);
                }

                sender.sendMessage(Component.text("Starting a game...", NamedTextColor.YELLOW));
                lobby.createGame(plugin, campaign.get(), difficulty, players);
            }
            case "stop" -> {
                if (!sender.hasPermission("expunge.stop")) {
                    sender.sendMessage(Component.text("Insufficient permissions.", NamedTextColor.RED));
                    return true;
                }
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Insufficient arguments.", NamedTextColor.RED));
                    return true;
                }

                UUID id;
                GameManager gameManager = null;
                try {
                    id = UUID.fromString(args[1]);
                    for (GameManager gm : lobby.getGames()) {
                        if (gm.getUUID() == id) {
                            gameManager = gm;
                        }
                    }
                    if (gameManager == null) throw new IllegalArgumentException();
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("No game with that ID exists.", NamedTextColor.RED));
                    return true;
                }

                gameManager.stop(true);
            }
            case "get" -> {
                if (!sender.hasPermission("expunge.get")) {
                    sender.sendMessage(Component.text("Insufficient permissions.", NamedTextColor.RED));
                    return true;
                }

                if (!(sender instanceof Player player)) {
                    sender.sendMessage(Component.text("Only players can execute this command.", NamedTextColor.RED));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(Component.text("Insufficient arguments. ", NamedTextColor.RED).append(Component.text("/expunge get <item>", NamedTextColor.GRAY)));
                    return true;
                }

                GameItem gameItem;
                try {
                    gameItem = GameItems.valueOf(args[1]).getGameItem();
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Couldn't find that item, did you type it correctly?", NamedTextColor.RED));
                    return true;
                }
                sender.sendMessage(Component.text("Here you go.", NamedTextColor.GREEN));
                player.getInventory().addItem(gameItem.get());
                return true;
            }
            case "reload" -> {
                plugin.reloadConfig();
                sender.sendMessage(Component.text("Reloaded config.", NamedTextColor.GREEN));
            }
            default -> sender.sendMessage(Component.text("Unknown argument. ", NamedTextColor.RED).append(Component.text("/expunge [tutorial|stats|start|stop|get|spawn|reload]", NamedTextColor.GRAY)));
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
                    for (GameItems gameItems : GameItems.values()) {
                        tabComplete.add(gameItems.name());
                    }
                }
            }
        }

        return tabComplete;
    }
}
