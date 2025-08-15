package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.utils.ChatUtil;

public class SelectCampaignGUI extends GUI {
    public SelectCampaignGUI(Lobby lobby, Queue queue) {
        super(9, ChatUtil.format("&0&bSelect campaign..."), true);
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
