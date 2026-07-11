package org.purpurmc.purpur.network;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class RegionPacketBroadcaster {
    private static final RegionPacketBroadcaster INSTANCE = new RegionPacketBroadcaster();
    private final AtomicReference<Consumer<PacketBroadcastContext>> lookControllerInterceptor = new AtomicReference<>();

    private RegionPacketBroadcaster() {
    }

    public static RegionPacketBroadcaster instance() {
        return INSTANCE;
    }

    public void setLookControllerInterceptor(final Consumer<PacketBroadcastContext> interceptor) {
        this.lookControllerInterceptor.set(interceptor);
    }

    public void clearLookControllerInterceptor() {
        this.lookControllerInterceptor.set(null);
    }

    public void broadcastPacket(final Entity entity, final Packet<?> packet) {
        Objects.requireNonNull(entity, "entity");
        Objects.requireNonNull(packet, "packet");
        if (entity.isRemoved()) {
            return;
        }

        entity.getBukkitEntity().taskScheduler.scheduleOrExecute(scheduledEntity -> this.broadcastInRegion(scheduledEntity, packet));
    }

    private void broadcastInRegion(final Entity entity, final Packet<?> packet) {
        if (entity.isRemoved() || !(entity.level() instanceof ServerLevel level)) {
            return;
        }

        final PacketBroadcastContext context = new PacketBroadcastContext(entity, packet);
        final Consumer<PacketBroadcastContext> interceptor = this.lookControllerInterceptor.get();
        if (interceptor != null) {
            interceptor.accept(context);
        }
        if (!context.cancelled()) {
            this.sendToTrackingPlayers(level, entity, context.packet());
        }
    }

    @SuppressWarnings("unchecked")
    private void sendToTrackingPlayers(final ServerLevel level, final Entity entity, final Packet<?> packet) {
        level.getChunkSource().sendToTrackingPlayers(entity, (Packet<? super ClientGamePacketListener>) packet);
    }
}
