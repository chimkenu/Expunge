package me.chimkenu.expunge.items.interactables;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.entity.LivingEntity;

public interface Interactable {
    default boolean canBePickedUp() {
        return true;
    }

    void onInteract(GameManager gameManager, LivingEntity actor);
}
