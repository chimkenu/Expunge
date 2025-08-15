package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuGUI extends GUI {
    public MenuGUI(Lobby lobby, Player player) {
        super(27, ChatUtil.format("&6Expunge"), true);
        setItem(11, newGUIItem(Material.IRON_HOE, ChatUtil.format("Play")), player1 -> {
            player1.closeInventory();
            Queue queue = getPlayerQueue(lobby, player1);
            if (queue != null) {
                new QueueGUI(lobby, queue).open(player1);
            } else {
                new LobbyGUI(lobby).open(player1);
            }
        });
        setItem(12, newGUIItem(Material.WOODEN_HOE, ChatUtil.format("&6Tutorial")), player1 -> {
            player1.closeInventory();
            if (getPlayerQueue(lobby, player1) != null) {
                ChatUtil.sendError(player1, "Leave your current queue before going to the tutorial.");
                return;
            }
            player1.performCommand("expunge tutorial");
        });
        setItem(13, newGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ""));

        // TODO: player skull has player stats
        ItemStack stats = Utils.getSkull(player);
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtil.format("sussy burger in my tummy i eat it when im hungry"));
        ItemMeta meta = stats.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            stats.setItemMeta(meta);
        }
        setItem(15, stats);
    }

    private Queue getPlayerQueue(Lobby lobby, Player player) {
        for (Queue queue : lobby.getQueues()) {
            for (Player p : queue.getQueue()) {
                if (p == player) return queue;
            }
        }
        return null;
    }
}
