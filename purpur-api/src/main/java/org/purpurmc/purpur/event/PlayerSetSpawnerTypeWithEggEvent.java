package org.purpurmc.purpur.event;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlayerSetSpawnerTypeWithEggEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Block block;
    private final CreatureSpawner spawner;
    private EntityType type;
    private boolean cancel;

    @ApiStatus.Internal
    public PlayerSetSpawnerTypeWithEggEvent(Player player, Block block, CreatureSpawner spawner, EntityType type) {
        super(player);
        this.block = block;
        this.spawner = spawner;
        this.type = type;
    }

    /**
     * Get the spawner Block in the world
     *
     * @return Spawner Block
     */
    public Block getBlock() {
        return block;
    }

    /**
     * Get the spawner state
     *
     * @return Spawner state
     */
    public CreatureSpawner getSpawner() {
        return spawner;
    }

    /**
     * Gets the EntityType being set on the spawner
     *
     * @return EntityType being set
     */
    public EntityType getEntityType() {
        return type;
    }

    /**
     * Sets the EntityType being set on the spawner
     *
     * @param type EntityType to set
     */
    public void setEntityType(EntityType type) {
        this.type = type;
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
