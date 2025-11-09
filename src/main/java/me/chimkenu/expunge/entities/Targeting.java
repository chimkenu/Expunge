package me.chimkenu.expunge.entities;

import java.util.Optional;
import java.util.Set;

public interface Targeting {
    Optional<GameEntity> getTarget(Set<GameEntity> entities);
    void setTarget(GameEntity target);
}
