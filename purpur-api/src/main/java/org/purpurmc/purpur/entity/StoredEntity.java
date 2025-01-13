package org.purpurmc.purpur.entity;

import org.bukkit.Nameable;
import org.bukkit.block.EntityBlockStorage;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an entity stored in a block
 *
 * @see org.bukkit.block.EntityBlockStorage
 */
public interface StoredEntity<T extends Entity> extends PersistentDataHolder, Nameable {
    /**
     * Checks if this entity has been released yet
     *
     * @return if this entity has been released
     */
    boolean hasBeenReleased();

    /**
     * Releases the entity from its stored block
     *
     * @return the released entity, or null if unsuccessful (including if this entity has already been released)
     */
    @Nullable
    T release();

    /**
     * Returns the block in which this entity is stored
     *
     * @return the EntityBlockStorage in which this entity is stored, or null if it has been released
     */
    @Nullable
    EntityBlockStorage<T> getBlockStorage();

    /**
     * Gets the entity type of this stored entity
     *
     * @return the type of entity this stored entity represents
     */
    @NotNull
    EntityType getType();

    /**
     * Writes data to the block entity snapshot. {@link EntityBlockStorage#update()} must be run in order to update the block in game.
     */
    void update();
}
