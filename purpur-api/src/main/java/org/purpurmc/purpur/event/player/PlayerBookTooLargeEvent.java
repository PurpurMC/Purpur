package org.purpurmc.purpur.event.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a player tries to bypass book limitations
 */
@NullMarked
public class PlayerBookTooLargeEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack book;
    private boolean kickPlayer = true;

    /**
     * @param player The player
     * @param book The book
     */
    @ApiStatus.Internal
    public PlayerBookTooLargeEvent(Player player, ItemStack book) {
        super(player, !Bukkit.isPrimaryThread());
        this.book = book;
    }

    /**
     * Get the book containing the wanted edits
     *
     * @return The book
     */
    public ItemStack getBook() {
        return book;
    }

    /**
     * Whether server should kick the player or not
     *
     * @return True to kick player
     */
    public boolean shouldKickPlayer() {
        return kickPlayer;
    }

    /**
     * Whether server should kick the player or not
     *
     * @param kickPlayer True to kick player
     */
    public void setShouldKickPlayer(boolean kickPlayer) {
        this.kickPlayer = kickPlayer;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
