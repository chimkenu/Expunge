package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyGUI extends GUI {
    public LobbyGUI(Lobby lobby) {
        super(27, ChatUtil.format("&0&bQueues..."), true);

        setItem(26, newGUIItem(Material.PAINTING, ChatUtil.format("&7Start a Queue")), player -> {
            player.closeInventory();

            if (!player.getWorld().equals(lobby.getWorld())) {
                ChatUtil.sendError(player, "You have to be in the lobby to do this.");
                return;
            }

            for (Queue q : lobby.getQueues()) {
                for (Player p : q.getQueue()) {
                    if (player == p) {
                        ChatUtil.sendError(player, "You're already in a queue.");
                        return;
                    }
                }
            }

            player.closeInventory();
            new QueueGUI(lobby, lobby.createQueue(Campaign.List.THE_DEPARTURE, Difficulty.EASY, player)).open(player);
        });

        setItem(25, newGUIItem(Material.GRAY_STAINED_GLASS_PANE, ChatUtil.format("Reload")), player -> loadQueues(lobby));

        loadQueues(lobby);
    }

    private void loadQueues(Lobby lobby) {
        int i = 0;
        for (Queue queue : lobby.getQueues()) {
            ItemStack skull = Utils.getSkull(queue.getCreator());
            ItemMeta meta = skull.getItemMeta();
            if (meta != null) {
                String name = "&4" + queue.getCreator().getDisplayName() + "&7's queue &8(" +
                        queue.getQueue().size() + "/" + queue.getMaxPlayers() + ")";
                meta.setDisplayName(ChatUtil.format(name));
                skull.setItemMeta(meta);
            }

            setItem(i, skull, player -> {
                player.closeInventory();

                if (!player.getWorld().equals(lobby.getWorld())) {
                    ChatUtil.sendError(player, "You have to be in the lobby to do this.");
                    return;
                }

                for (Queue q : lobby.getQueues()) {
                    for (Player p : q.getQueue()) {
                        if (player == p && queue != q) {
                            ChatUtil.sendError(player, "You're already in another queue.");
                            return;
                        }
                    }
                }

                if (queue.join(player)) {
                    queue.getQueue().forEach(p -> ChatUtil.sendFormatted(p, "&a" + player.getDisplayName() + " &7joined the queue."));
                } else if (queue.quit(player)) {
                    queue.getQueue().forEach(p -> ChatUtil.sendFormatted(p, "&a" + player.getDisplayName() + " &7left the queue."));
                    ChatUtil.sendInfo(player, "Left the queue.");
                } else {
                    ChatUtil.sendError(player, "Queue is full.");
                }
            });

            i++;
            if (i >= 24) break;
        }

        // clear out the rest
        for (; i < 24; i++) {
            setItem(i, new ItemStack(Material.AIR));
        }
    }
}
