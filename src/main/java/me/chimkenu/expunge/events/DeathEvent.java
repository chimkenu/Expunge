package me.chimkenu.expunge.events;

import me.chimkenu.expunge.entities.survivor.Survivor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DeathEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Survivor survivor;

    public DeathEvent(Survivor survivor) {
        this.survivor = survivor;
    }

    public Survivor getSurvivor() {
        return survivor;
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
