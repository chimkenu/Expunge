package me.chimkenu.expunge.guis;

import me.chimkenu.expunge.Lobby;
import me.chimkenu.expunge.Queue;
import me.chimkenu.expunge.enums.Difficulty;
import me.chimkenu.expunge.utils.ChatUtil;

public class SelectDifficultyGUI extends GUI {
    public SelectDifficultyGUI(Lobby lobby, Queue queue) {
        super(9, ChatUtil.format("&0&bSelect difficulty..."), true);
        int i = 2;
        for (Difficulty difficulty : Difficulty.values()) {
            setItem(i, newGUIItem(difficulty.material(), ChatUtil.format(difficulty.name()), queue.getDifficulty() == difficulty), player -> {
                player.closeInventory();
                queue.setDifficulty(difficulty);
                new QueueGUI(lobby, queue).open(player);
            });
            i++;
        }
    }
}
