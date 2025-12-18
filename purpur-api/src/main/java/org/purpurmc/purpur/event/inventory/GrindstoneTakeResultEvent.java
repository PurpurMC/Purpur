package org.purpurmc.purpur.event.inventory;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player takes the result item out of a Grindstone
 */
@NullMarked
public class GrindstoneTakeResultEvent extends InventoryEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemStack result;
    private int experienceAmount;

    @ApiStatus.Internal
    public GrindstoneTakeResultEvent(HumanEntity player, InventoryView view, ItemStack result, int experienceAmount) {
        super(view);
        this.player = (Player) player;
        this.result = result;
        this.experienceAmount = experienceAmount;
    }

    public Player getPlayer() {
        return player;
    }

    public ItemStack getResult() {
        return result;
    }

    @Override
    public GrindstoneInventory getInventory() {
        return (GrindstoneInventory) super.getInventory();
    }

    /**
     * Get the amount of experience this transaction will give
     * (takes priority over and uses result from {@link org.bukkit.event.block.BlockExpEvent})
     *
     * @return Amount of experience to give
     */
    public int getExperienceAmount() {
        return this.experienceAmount;
    }

    /**
     * Set the amount of experience this transaction will give
     * (takes priority over {@link org.bukkit.event.block.BlockExpEvent})
     *
     * @param experienceAmount Amount of experience to give
     */
    public void setExperienceAmount(int experienceAmount) {
        this.experienceAmount = experienceAmount;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
