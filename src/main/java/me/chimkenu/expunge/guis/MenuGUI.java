package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
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
            new QueueGUI(lobby).open(player1);
        });
        setItem(12, newGUIItem(Material.WOODEN_HOE, Component.text("Tutorial", NamedTextColor.GOLD)), player1 -> {
            player1.closeInventory();
            player1.performCommand("expunge tutorial");
        });
        setItem(13, newGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, Component.empty()));

        // TODO: player skull has player stats
        ItemStack stats = Utils.getSkull(player);
        List<Component> lore = new ArrayList<>();
        stats.lore(lore);
        setItem(15, stats);
    }
}
