package org.purpurmc.purpur.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PlayerAFKEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final boolean setAfk;
    private boolean shouldKick;
    private @Nullable String broadcast;
    private boolean cancel;

    @ApiStatus.Internal
    public PlayerAFKEvent(Player player, boolean setAfk, boolean shouldKick, @Nullable String broadcast, boolean async) {
        super(player, async);
        this.setAfk = setAfk;
        this.shouldKick = shouldKick;
        this.broadcast = broadcast;
    }

    /**
     * Whether player is going afk or coming back
     *
     * @return True if going afk. False is coming back
     */
    public boolean isGoingAfk() {
        return setAfk;
    }

    public boolean shouldKick() {
        return shouldKick;
    }

    public void setShouldKick(boolean shouldKick) {
        this.shouldKick = shouldKick;
    }

    @Nullable
    public String getBroadcastMsg() {
        return broadcast;
    }

    public void setBroadcastMsg(@Nullable String broadcast) {
        this.broadcast = broadcast;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
