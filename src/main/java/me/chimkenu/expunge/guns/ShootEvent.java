package me.chimkenu.expunge.guns;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ShootEvent extends Event {
    private final Player shooter;
    private final HashMap<LivingEntity, Boolean> hitEntities;

    public ShootEvent(Player shooter, HashMap<LivingEntity, Boolean> hitEntities) {
        this.shooter = shooter;
        this.hitEntities = hitEntities;
    }

    public Player getShooter() {
        return shooter;
    }

    public HashMap<LivingEntity, Boolean> getHitEntities() {
        return hitEntities;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return new HandlerList();
    }

    public static HandlerList getHandlerList() {
        return new HandlerList();
    }
}
