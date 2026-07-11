package org.purpurmc.purpur.network;

import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

public final class RegionPacketBroadcaster {
    private final Plugin plugin;
    private final RegionScheduler regionScheduler;
    private final AtomicReference<Consumer<PacketBroadcastContext>> lookControllerInterceptor = new AtomicReference<>();

    public RegionPacketBroadcaster(final Plugin plugin, final RegionScheduler regionScheduler) {
        this.plugin = Objects.requireNonNull(plugin, "plugin");
        this.regionScheduler = Objects.requireNonNull(regionScheduler, "regionScheduler");
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

        final Location location = entity.getBukkitEntity().getLocation();
        this.regionScheduler.execute(this.plugin, location, () -> this.broadcastInRegion(entity, packet));
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
