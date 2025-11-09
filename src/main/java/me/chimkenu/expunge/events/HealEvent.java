package me.chimkenu.expunge.events;

import me.chimkenu.expunge.entities.survivor.Survivor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HealEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Survivor survivor;
    private int health;
    private int absorption;

    public HealEvent(Survivor survivor, int health, int absorption) {
        this.survivor = survivor;
        this.health = health;
        this.absorption = absorption;
    }

    public Survivor getSurvivor() {
        return survivor;
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
