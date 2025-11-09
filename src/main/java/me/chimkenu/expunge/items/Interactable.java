package me.chimkenu.expunge.items;

import me.chimkenu.expunge.entities.GameEntity;
import me.chimkenu.expunge.game.GameManager;

public sealed interface Interactable extends GameItem permits Explosive {
    void onDamage(GameManager manager, GameEntity interactable, GameEntity actor);
    void onInteract(GameManager manager, GameEntity interactable, GameEntity actor);
}
