package org.purpurmc.purpur.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Triggered when a ridable mob moves with a rider
 */
@NullMarked
public class RidableMoveEvent extends EntityEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean canceled;
    private final Player rider;
    private Location from;
    private Location to;

    @ApiStatus.Internal
    public RidableMoveEvent(Mob entity, Player rider, Location from, Location to) {
        super(entity);
        this.rider = rider;
        this.from = from;
        this.to = to;
    }

    @Override
    public Mob getEntity() {
        return (Mob) entity;
    }

    public Player getRider() {
        return rider;
    }

    public boolean isCancelled() {
        return canceled;
    }

    public void setCancelled(boolean cancel) {
        canceled = cancel;
    }

    /**
     * Gets the location this entity moved from
     *
     * @return Location the entity moved from
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Sets the location to mark as where the entity moved from
     *
     * @param from New location to mark as the entity's previous location
     */
    public void setFrom(Location from) {
        validateLocation(from);
        this.from = from;
    }

    /**
     * Gets the location this entity moved to
     *
     * @return Location the entity moved to
     */
    public Location getTo() {
        return to;
    }

    /**
     * Sets the location that this entity will move to
     *
     * @param to New Location this entity will move to
     */
    public void setTo(Location to) {
        validateLocation(to);
        this.to = to;
    }

    private void validateLocation(Location loc) {
        Preconditions.checkArgument(loc != null, "Cannot use null location!");
        Preconditions.checkArgument(loc.getWorld() != null, "Cannot use null location with null world!");
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
