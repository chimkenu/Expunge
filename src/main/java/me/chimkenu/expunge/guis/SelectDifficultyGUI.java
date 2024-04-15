package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.enums.Difficulty;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class SelectDifficultyGUI extends GUI {
    public SelectDifficultyGUI(Lobby lobby, Queue queue) {
        super(9, Component.text("Select difficulty...", NamedTextColor.BLACK, TextDecoration.BOLD), true);
        int i = 2;
        for (Difficulty difficulty : Difficulty.values()) {
            setItem(i, newGUIItem(difficulty.material(), difficulty.component(), queue.getDifficulty() == difficulty), player -> {
                player.closeInventory();
                queue.setDifficulty(difficulty);
                new QueueGUI(lobby, queue).open(player);
            });
            i++;
        }
    }
}
