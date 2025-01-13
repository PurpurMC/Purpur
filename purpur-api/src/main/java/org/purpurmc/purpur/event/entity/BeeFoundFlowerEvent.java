package org.purpurmc.purpur.event.entity;

import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Called when a bee targets a flower
 */
@NullMarked
public class BeeFoundFlowerEvent extends EntityEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Location location;

    @ApiStatus.Internal
    public BeeFoundFlowerEvent(Bee bee, @Nullable Location location) {
        super(bee);
        this.location = location;
    }

    @Override
    public Bee getEntity() {
        return (Bee) super.getEntity();
    }

    /**
     * Returns the location of the flower that the bee targets
     *
     * @return The location of the flower
     */
    @Nullable
    public Location getLocation() {
        return location;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
