package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class QueueGUI extends GUI {
    public QueueGUI(Lobby lobby, Queue queue) {
        super(27, queue.getCreator().displayName().color(NamedTextColor.BLACK).decorate(TextDecoration.BOLD).append(Component.text("'s queue", NamedTextColor.BLACK, TextDecoration.BOLD)), true);

        for (int i = 0; i < 9; i++) {
            setItem(i, newGUIItem(Material.BLACK_STAINED_GLASS_PANE, Component.empty()));
            setItem(i + 18, newGUIItem(Material.BLACK_STAINED_GLASS_PANE, Component.empty()));
        }

        setItem(9, newGUIItem(Material.BLACK_STAINED_GLASS_PANE, Component.empty()));
        setItem(12, newGUIItem(Material.BLACK_STAINED_GLASS_PANE, Component.empty()));
        setItem(17, newGUIItem(Material.BLACK_STAINED_GLASS_PANE, Component.empty()));

        setItem(10, newGUIItem(queue.getCampaign().getGUIMaterial(), queue.getCampaign().get().getName()), player -> {
            if (player != queue.getCreator()) {
                player.sendMessage(Component.text("You cannot change the settings of this queue.", NamedTextColor.RED));
                return;
            }
            player.closeInventory();
            new SelectCampaignGUI(lobby, queue).open(player);
        });

        setItem(11, newGUIItem(queue.getDifficulty().material(), queue.getDifficulty().component()), player -> {
            if (player != queue.getCreator()) {
                player.sendMessage(Component.text("You cannot change the settings of this queue.", NamedTextColor.RED));
                return;
            }
            player.closeInventory();
            new SelectDifficultyGUI(lobby, queue).open(player);
        });

        int i = 0;
        for (Player p : queue.getQueue()) {
            ItemStack skull = Utils.getSkull(p);
            ItemMeta meta = skull.getItemMeta();
            meta.displayName(p.displayName().append(Component.text(" (Click to kick)", NamedTextColor.GRAY)));
            skull.setItemMeta(meta);
            setItem(i + 13, skull, player -> {
                if (player != queue.getCreator()) {
                    player.sendMessage(Component.text("You cannot kick this player.", NamedTextColor.RED));
                    return;
                }
                if (p == queue.getCreator()) {
                    player.sendMessage(Component.text("You can't kick yourself, silly!", NamedTextColor.RED));
                    return;
                }

                queue.quit(p);
                player.sendMessage(Component.text("Removed.", NamedTextColor.YELLOW));
                player.closeInventory();
                new QueueGUI(lobby, queue).open(player);
            });
            i++;
        }

        for (; i < 4; i++) {
            setItem(i + 13, newGUIItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("Invite your friends!", NamedTextColor.GRAY)));
        }

        setItem(24, newGUIItem(Material.GREEN_DYE, Component.text("Confirm", NamedTextColor.DARK_GREEN)), player -> {
            player.closeInventory();
            player.sendMessage(Component.text("Starting a game...", NamedTextColor.YELLOW));
            queue.stop(false);
        });

        setItem(25, newGUIItem(Material.RED_DYE, Component.text("Cancel", NamedTextColor.RED)), player -> {
            player.closeInventory();
            player.sendMessage(Component.text("Cancelling...", NamedTextColor.RED));
            queue.stop(true);
            new LobbyGUI(lobby).open(player);
        });
    }
}