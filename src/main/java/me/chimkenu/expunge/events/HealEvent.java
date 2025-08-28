package me.chimkenu.expunge.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HealEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private int health;
    private int absorption;

    public HealEvent(Player player, int health, int absorption) {
        this.player = player;
        this.health = health;
        this.absorption = absorption;
    }

    public Player getPlayer() {
        return player;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAbsorption() {
        return absorption;
    }

    public void setAbsorption(int absorption) {
        this.absorption = absorption;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
