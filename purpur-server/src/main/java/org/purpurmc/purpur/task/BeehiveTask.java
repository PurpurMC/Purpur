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

/**
 * Handles beehive information requests from the client.
 * Receives a position from the client and responds with the number of bees inside the hive.
 */
public class BeehiveTask implements PluginMessageListener {

    private static BeehiveTask instance;

    public static BeehiveTask instance() {
        if (instance == null) {
            instance = new BeehiveTask();
        }
        return instance;
    }

    private final PluginBase plugin = new MinecraftInternalPlugin();

    private BeehiveTask() {}

    public void register() {
        final String outgoing = ClientboundBeehivePayload.TYPE.id().toString();
        final String incoming = ServerboundBeehivePayload.TYPE.id().toString();

        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, outgoing);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, incoming, this);
    }

    public void unregister() {
        final String outgoing = ClientboundBeehivePayload.TYPE.id().toString();
        final String incoming = ServerboundBeehivePayload.TYPE.id().toString();

        Bukkit.getMessenger().unregisterOutgoingPluginChannel(plugin, outgoing);
        Bukkit.getMessenger().unregisterIncomingPluginChannel(plugin, incoming);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] bytes
    ) {
        final FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.copiedBuffer(bytes));
        final ServerboundBeehivePayload payload = ServerboundBeehivePayload.STREAM_CODEC.decode(buf);

        final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        // Max range for targeted block info (client-side limit)
        if (!payload.pos().getCenter().closerThan(serverPlayer.position(), 20)) {
            return;
        }

        if (serverPlayer.level().getChunkIfLoaded(payload.pos()) == null) {
            return;
        }

        final BlockEntity blockEntity = serverPlayer.level().getBlockEntity(payload.pos());
        if (!(blockEntity instanceof BeehiveBlockEntity beehive)) {
            return;
        }

        final ClientboundBeehivePayload response = new ClientboundBeehivePayload(payload.pos(), beehive.getOccupantCount());

        final FriendlyByteBuf out = new FriendlyByteBuf(Unpooled.buffer());
        ClientboundBeehivePayload.STREAM_CODEC.encode(out, response);

        final byte[] data = new byte[out.readableBytes()];
        out.readBytes(data);

        player.sendPluginMessage(plugin, response.type().id().toString(), data);
    }
}
