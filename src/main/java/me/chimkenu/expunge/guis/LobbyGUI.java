package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyGUI extends GUI {
    public LobbyGUI(Lobby lobby) {
        super(27, Component.text("Queues...", NamedTextColor.BLACK, TextDecoration.BOLD), true);

        setItem(26, newGUIItem(Material.PAINTING, Component.text("Start a Queue", NamedTextColor.GRAY)), player -> {
            player.closeInventory();

            if (!player.getWorld().equals(lobby.getWorld())) {
                player.sendMessage(Component.text("You have to be in the lobby to do this.", NamedTextColor.RED));
                return;
            }

            for (Queue q : lobby.getQueues()) {
                for (Player p : q.getQueue()) {
                    if (player == p) {
                        player.sendMessage(Component.text("You're already in a queue.", NamedTextColor.RED));
                        return;
                    }
                }
            }

            player.closeInventory();
            new QueueGUI(lobby, lobby.createQueue(Campaign.List.THE_DEPARTURE, Difficulty.EASY, player)).open(player);
        });

        setItem(25, newGUIItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("Reload")), player -> loadQueues(lobby));

        loadQueues(lobby);
    }

    private void loadQueues(Lobby lobby) {
        int i = 0;
        for (Queue queue : lobby.getQueues()) {
            ItemStack skull = Utils.getSkull(queue.getCreator());
            ItemMeta meta = skull.getItemMeta();
            meta.displayName(queue.getCreator().displayName().color(NamedTextColor.RED)
                    .append(Component.text("'s queue", NamedTextColor.GRAY))
                    .append(Component.text(" (" + queue.getQueue().size() + "/" + queue.getMaxPlayers() + ")", NamedTextColor.DARK_GRAY)));
            skull.setItemMeta(meta);
            setItem(i, skull, player -> {
                player.closeInventory();

                if (!player.getWorld().equals(lobby.getWorld())) {
                    player.sendMessage(Component.text("You have to be in the lobby to do this.", NamedTextColor.RED));
                    return;
                }

                for (Queue q : lobby.getQueues()) {
                    for (Player p : q.getQueue()) {
                        if (player == p && queue != q) {
                            player.sendMessage(Component.text("You're already in another queue.", NamedTextColor.RED));
                            return;
                        }
                    }
                }

                if (queue.join(player)) {
                    for (Player playersInQueue : queue.getQueue()) {
                        playersInQueue.sendMessage(player.displayName().color(NamedTextColor.RED).append(Component.text(" joined the queue.", NamedTextColor.GRAY)));
                    }
                } else if (queue.quit(player)) {
                    for (Player playersInQueue : queue.getQueue()) {
                        playersInQueue.sendMessage(player.displayName().color(NamedTextColor.RED).append(Component.text(" left the queue.", NamedTextColor.GRAY)));
                    }
                    player.sendMessage(Component.text("Left the queue.", NamedTextColor.GRAY));
                } else {
                    player.sendMessage(Component.text("Queue is full.", NamedTextColor.RED));
                }
            });

            i++;
            if (i >= 24) break;
        }
    }
}
