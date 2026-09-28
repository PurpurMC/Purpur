package org.purpurmc.purpur.event.inventory;

import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when anvil slots change, triggering the result slot to be updated
 */
@NullMarked
public class AnvilUpdateResultEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();

    @ApiStatus.Internal
    public AnvilUpdateResultEvent(InventoryView view) {
        super(view);
    }

    @Override
    public AnvilInventory getInventory() {
        return (AnvilInventory) super.getInventory();
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
