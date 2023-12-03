package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.campaigns.Campaign;
import me.chimkenu.expunge.enums.Difficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;

public class GameSettingsGUI extends GUI {
    public GameSettingsGUI(Lobby lobby, Campaign.List selectedCampaign, Difficulty selectedDifficulty) {
        super(18, Component.text("Settings...", NamedTextColor.BLACK, TextDecoration.BOLD), true);

        int i = 0;
        for (Campaign.List campaign : Campaign.List.values()) {
            setItem(i, newGUIItem(campaign.getGUIMaterial(), campaign.get().getName(), campaign == selectedCampaign), player -> {
                player.closeInventory();
                new GameSettingsGUI(lobby, campaign, selectedDifficulty).open(player);
            });

            i++;
            if (i >= 9) return;
        }

        for (Difficulty difficulty : Difficulty.values()) {
            setItem(10 + difficulty.ordinal(), newGUIItem(difficulty.material(), difficulty.component(), difficulty == selectedDifficulty), player -> {
                player.closeInventory();
                new GameSettingsGUI(lobby, selectedCampaign, difficulty).open(player);
            });
        }

        setItem(15, newGUIItem(Material.GREEN_DYE, Component.text("Confirm", NamedTextColor.DARK_GREEN)), player -> {
            player.closeInventory();
            player.sendMessage(Component.text("Created a queue. Please wait 30 seconds for other players to join."));
            lobby.createQueue(30, selectedCampaign.get(), selectedDifficulty, player);
        });
        setItem(16, newGUIItem(Material.RED_DYE, Component.text("Cancel", NamedTextColor.RED)), player -> {
            player.closeInventory();
            new QueueGUI(lobby).open(player);
        });
    }
}
