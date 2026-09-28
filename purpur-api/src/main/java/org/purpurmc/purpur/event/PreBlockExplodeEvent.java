package org.purpurmc.purpur.event;

import org.bukkit.ExplosionResult;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExplodeEvent;
import org.jetbrains.annotations.ApiStatus;
import java.util.Collections;
import org.jspecify.annotations.NullMarked;

/**
 * Called before a block's explosion is processed
 */
@NullMarked
public class PreBlockExplodeEvent extends BlockExplodeEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private final float yield;

    @ApiStatus.Internal
    public PreBlockExplodeEvent(final Block what, final float yield, BlockState explodedBlockState, ExplosionResult result) {
        super(what, explodedBlockState, Collections.emptyList(), yield, result);
        this.yield = yield;
        this.cancelled = false;
    }

    /**
     * Returns the percentage of blocks to drop from this explosion
     *
     * @return The yield.
     */
    public float getYield() {
        return yield;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
