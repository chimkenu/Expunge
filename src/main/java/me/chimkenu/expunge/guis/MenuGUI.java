package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.utils.ChatUtil;
import me.chimkenu.expunge.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuGUI extends GUI {
    public MenuGUI(Lobby lobby, Player player) {
        super(27, ChatUtil.format("&6Expunge"), true);
        setItem(11, newGUIItem(Material.IRON_HOE, ChatUtil.format("Play")), p -> {
            // TODO: start game
            p.sendMessage("indev");
            p.closeInventory();
        });
        setItem(12, newGUIItem(Material.WOODEN_HOE, ChatUtil.format("&6Tutorial")), p -> {
            p.closeInventory();
            // TODO: in-game check
            p.performCommand("expunge tutorial");
        });
        setItem(13, newGUIItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, ""));

        // TODO: player skull has player stats
        ItemStack stats = ItemUtil.getSkull(player);
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtil.format("sussy burger in my tummy i eat it when im hungry"));
        ItemMeta meta = stats.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            stats.setItemMeta(meta);
        }
        setItem(15, stats);
    }
}
