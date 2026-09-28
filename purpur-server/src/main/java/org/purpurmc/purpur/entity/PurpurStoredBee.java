package org.purpurmc.purpur.entity;

import com.mojang.logging.LogUtils;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.ValueInput;
import org.bukkit.block.EntityBlockStorage;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import java.util.Locale;

public class PurpurStoredBee implements StoredEntity<Bee> {
    static final Logger LOGGER = LogUtils.getLogger();

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private final EntityBlockStorage<Bee> blockStorage;
    private final BeehiveBlockEntity.BeeData handle;
    private final CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(PurpurStoredBee.DATA_TYPE_REGISTRY);

    private Component customName;

    public PurpurStoredBee(BeehiveBlockEntity.BeeData data, EntityBlockStorage<Bee> blockStorage, final BeehiveBlockEntity blockEntity) {
        this.handle = data;
        this.blockStorage = blockStorage;

        CompoundTag customData = handle.occupant.entityData().copyTagWithEntityId();

        try (ProblemReporter.ScopedCollector scopedCollector = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), LOGGER)) {
            ValueInput valueInput = TagValueInput.create(scopedCollector, RegistryAccess.EMPTY, customData);

            net.minecraft.network.chat.Component customNameMinecraft = BlockEntity.parseCustomNameSafe(valueInput, "CustomName");
            this.customName = customNameMinecraft == null ? null : PaperAdventure.asAdventure(customNameMinecraft);

            if (customData.get("BukkitValues") instanceof CompoundTag compoundTag) {
                this.persistentDataContainer.putAll(compoundTag);
            }
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
        handle.occupant.entityData().copyTagWithEntityId().put("BukkitValues", this.persistentDataContainer.toTagCompound());
        if(customName == null) {
            handle.occupant.entityData().copyTagWithEntityId().remove("CustomName");
        } else {
            handle.occupant.entityData().copyTagWithEntityId().putString("CustomName", CraftChatMessage.toJSON(PaperAdventure.asVanilla(customName)));
        }
    }
}
