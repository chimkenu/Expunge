package me.chimkenu.expunge.items;

import me.chimkenu.expunge.game.GameManager;
import org.bukkit.entity.LivingEntity;

public interface Utility {
    boolean use(GameManager manager, LivingEntity entity);
}
