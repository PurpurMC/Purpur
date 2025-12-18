package org.purpurmc.purpur.task;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import org.purpurmc.purpur.network.ClientboundBeehivePayload;
import org.purpurmc.purpur.network.ServerboundBeehivePayload;
import org.purpurmc.purpur.util.MinecraftInternalPlugin;

public class BeehiveTask implements PluginMessageListener {

    private static BeehiveTask instance;

    public static BeehiveTask instance() {
        if (instance == null) {
            instance = new BeehiveTask();
        }
        return instance;
    }

    private final PluginBase plugin = new MinecraftInternalPlugin();

    private BeehiveTask() {
    }

    public void register() {
        Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, ClientboundBeehivePayload.TYPE.id().toString());
        Bukkit.getMessenger().registerIncomingPluginChannel(this.plugin, ServerboundBeehivePayload.TYPE.id().toString(), this);
    }

    public void unregister() {
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this.plugin, ClientboundBeehivePayload.TYPE.id().toString());
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this.plugin, ServerboundBeehivePayload.TYPE.id().toString());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes) {
        FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.copiedBuffer(bytes));
        ServerboundBeehivePayload payload = ServerboundBeehivePayload.STREAM_CODEC.decode(byteBuf);

        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        // targeted block info max range specified in client at net.minecraft.client.gui.hud.DebugHud#render
        if (!payload.pos().getCenter().closerThan(serverPlayer.position(), 20)) return; // Targeted Block info max range is 20
        if (serverPlayer.level().getChunkIfLoaded(payload.pos()) == null) return;

        BlockEntity blockEntity = serverPlayer.level().getBlockEntity(payload.pos());
        if (!(blockEntity instanceof BeehiveBlockEntity beehive)) {
            return;
        }

        ClientboundBeehivePayload customPacketPayload = new ClientboundBeehivePayload(payload.pos(), beehive.getOccupantCount());
        FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundBeehivePayload.STREAM_CODEC.encode(friendlyByteBuf, customPacketPayload);
        byte[] byteArray = new byte[friendlyByteBuf.readableBytes()];
        friendlyByteBuf.readBytes(byteArray);
        player.sendPluginMessage(this.plugin, customPacketPayload.type().id().toString(), byteArray);
    }
}
