package org.purpurmc.purpur.event.entity;

import org.bukkit.entity.Llama;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a Llama leaves a caravan
 */
@NullMarked
public class LlamaLeaveCaravanEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();

    @ApiStatus.Internal
    public LlamaLeaveCaravanEvent(Llama llama) {
        super(llama);
    }

    @Override
    public Llama getEntity() {
        return (Llama) entity;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
