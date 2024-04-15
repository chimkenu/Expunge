package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuGUI extends GUI {
    public MenuGUI(Lobby lobby, Player player) {
        super(27, Component.text("Expunge", NamedTextColor.GOLD), true);
        setItem(11, newGUIItem(Material.IRON_HOE, Component.text("Play", NamedTextColor.WHITE)), player1 -> {
            player1.closeInventory();
            Queue queue = getPlayerQueue(lobby, player1);
            if (queue != null) {
                new QueueGUI(lobby, queue).open(player1);
            } else {
                new LobbyGUI(lobby).open(player1);
            }
        });
        setItem(12, newGUIItem(Material.WOODEN_HOE, Component.text("Tutorial", NamedTextColor.GOLD)), player1 -> {
            player1.closeInventory();
            if (getPlayerQueue(lobby, player1) != null) {
                player1.sendMessage(Component.text("Leave your current queue before going to the tutorial.", NamedTextColor.RED));
                return;
            }
            player1.performCommand("expunge tutorial");
        });
        setItem(13, newGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Component.empty()));

        // TODO: player skull has player stats
        ItemStack stats = Utils.getSkull(player);
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("sussy burger in my tummy i eat it when im hungry"));
        stats.lore(lore);
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
