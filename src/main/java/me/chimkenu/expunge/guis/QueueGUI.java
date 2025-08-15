package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QueueGUI extends GUI {
    public QueueGUI(Lobby lobby, Queue queue) {
        super(27, ChatUtil.format("&0&b" + queue.getCreator().getDisplayName() + "'s queue"), true);

        for (int i = 0; i < 9; i++) {
            setItem(i, newGUIItem(Material.BLACK_STAINED_GLASS_PANE));
            setItem(i + 18, newGUIItem(Material.BLACK_STAINED_GLASS_PANE));
        }

        setItem(9, newGUIItem(Material.BLACK_STAINED_GLASS_PANE));
        setItem(12, newGUIItem(Material.BLACK_STAINED_GLASS_PANE));
        setItem(17, newGUIItem(Material.BLACK_STAINED_GLASS_PANE));

        setItem(10, newGUIItem(queue.getCampaign().getGUIMaterial(), queue.getCampaign().get().getName()), player -> {
            if (player != queue.getCreator()) {
                ChatUtil.sendError(player, "You cannot change the settings of this queue.");
                return;
            }
            player.closeInventory();
            new SelectCampaignGUI(lobby, queue).open(player);
        });

        setItem(11, newGUIItem(queue.getDifficulty().material(), queue.getDifficulty().component()), player -> {
            if (player != queue.getCreator()) {
                ChatUtil.sendError(player, "You cannot change the settings of this queue.");
                return;
            }
            player.closeInventory();
            new SelectDifficultyGUI(lobby, queue).open(player);
        });

        int i = 0;
        for (Player p : queue.getQueue()) {
            ItemStack skull = Utils.getSkull(p);
            ItemMeta meta = skull.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(ChatUtil.format(p.getDisplayName() + " &7(Click to kick)"));
                skull.setItemMeta(meta);
            }

            setItem(i + 13, skull, player -> {
                if (player != queue.getCreator()) {
                    ChatUtil.sendError(player, "You cannot kick this player.");
                    return;
                }
                if (p == queue.getCreator()) {
                    ChatUtil.sendError(player, "You can't kick yourself, silly!");
                    return;
                }

                queue.quit(p);
                ChatUtil.sendInfo(player, "Removed.");
                player.closeInventory();
                new QueueGUI(lobby, queue).open(player);
            });
            i++;
        }

        for (; i < 4; i++) {
            setItem(i + 13, newGUIItem(Material.GRAY_STAINED_GLASS_PANE, ChatUtil.format("&7Invite your friends!")));
        }

        setItem(24, newGUIItem(Material.GREEN_DYE, ChatUtil.format("&2Confirm")), player -> {
            player.closeInventory();
            ChatUtil.sendInfo(player, "Starting a game...");
            queue.stop(false);
        });

        setItem(25, newGUIItem(Material.RED_DYE, ChatUtil.format("&aCancel")), player -> {
            player.closeInventory();
            ChatUtil.sendInfo(player, "Cancelling...");
            queue.stop(true);
            new LobbyGUI(lobby).open(player);
        });
    }
}