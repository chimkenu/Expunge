package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.campaigns.Campaign;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SelectCampaignGUI extends GUI {
    public SelectCampaignGUI(Lobby lobby, Queue queue) {
        super(9, Component.text("Select campaign...", NamedTextColor.BLACK, TextDecoration.BOLD), true);
        int i = 0;
        for (Campaign.List campaign : Campaign.List.values()) {
            setItem(i, newGUIItem(campaign.getGUIMaterial(), campaign.get().getName(), queue.getCampaign() == campaign), player -> {
                player.closeInventory();
                queue.setCampaign(campaign);
                new QueueGUI(lobby, queue).open(player);
            });
            i++;
        }
    }
}
