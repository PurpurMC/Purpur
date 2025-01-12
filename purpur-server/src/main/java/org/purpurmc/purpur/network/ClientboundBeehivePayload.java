package org.purpurmc.purpur.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundBeehivePayload(BlockPos pos, int numOfBees) implements CustomPacketPayload {
    public static final StreamCodec<FriendlyByteBuf, ClientboundBeehivePayload> STREAM_CODEC = CustomPacketPayload.codec(ClientboundBeehivePayload::write, ClientboundBeehivePayload::new);
    public static final Type<ClientboundBeehivePayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath("purpur", "beehive_s2c"));

    public ClientboundBeehivePayload(FriendlyByteBuf friendlyByteBuf) {
        this(friendlyByteBuf.readBlockPos(),  friendlyByteBuf.readInt());
    }

    private void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(this.pos);
        friendlyByteBuf.writeInt(this.numOfBees);
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
