package org.purpurmc.purpur.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ServerboundBeehivePayload(BlockPos pos) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ServerboundBeehivePayload> STREAM_CODEC = CustomPacketPayload.codec(ServerboundBeehivePayload::write, ServerboundBeehivePayload::new);
    public static final Type<ServerboundBeehivePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("purpur", "beehive_c2s"));

    public ServerboundBeehivePayload(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBlockPos());
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(this.pos);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
