package org.purpurmc.purpur.entity;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.bukkit.block.EntityBlockStorage;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class PurpurStoredBee implements StoredEntity<Bee> {
    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private final EntityBlockStorage<Bee> blockStorage;
    private final BeehiveBlockEntity.BeeData handle;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(PurpurStoredBee.DATA_TYPE_REGISTRY);

    private Component customName;

    public PurpurStoredBee(BeehiveBlockEntity.BeeData data, EntityBlockStorage<Bee> blockStorage) {
        this.handle = data;
        this.blockStorage = blockStorage;

        CompoundTag customData = handle.occupant.entityData().copyTag();
        this.customName = customData.contains("CustomName")
                ? PaperAdventure.asAdventure(net.minecraft.network.chat.Component.Serializer.fromJson(customData.getString("CustomName"), MinecraftServer.getDefaultRegistryAccess()))
                : null;

        if(customData.contains("BukkitValues", Tag.TAG_COMPOUND)) {
            this.persistentDataContainer.putAll(customData.getCompound("BukkitValues"));
        }
    }

    public BeehiveBlockEntity.BeeData getHandle() {
        return handle;
    }

    @Override
    public @Nullable Component customName() {
        return customName;
    }

    @Override
    public void customName(@Nullable Component customName) {
        this.customName = customName;
    }

    @Override
    public @Nullable String getCustomName() {
        return PaperAdventure.asPlain(customName, Locale.US);
    }

    @Override
    public void setCustomName(@Nullable String name) {
        customName(name != null ? Component.text(name) : null);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return persistentDataContainer;
    }

    @Override
    public boolean hasBeenReleased() {
        return !blockStorage.getEntities().contains(this);
    }

    @Override
    public @Nullable Bee release() {
        return blockStorage.releaseEntity(this);
    }

    @Override
    public @Nullable EntityBlockStorage<Bee> getBlockStorage() {
        if(hasBeenReleased()) {
            return null;
        }

        return blockStorage;
    }

    @Override
    public @NotNull EntityType getType() {
        return EntityType.BEE;
    }

    @Override
    public void update() {
        handle.occupant.entityData().copyTag().put("BukkitValues", this.persistentDataContainer.toTagCompound());
        if(customName == null) {
            handle.occupant.entityData().copyTag().remove("CustomName");
        } else {
            handle.occupant.entityData().copyTag().putString("CustomName", net.minecraft.network.chat.Component.Serializer.toJson(PaperAdventure.asVanilla(customName), MinecraftServer.getDefaultRegistryAccess()));
        }
    }
}
