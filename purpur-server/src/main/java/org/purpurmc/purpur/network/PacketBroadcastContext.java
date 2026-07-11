package org.purpurmc.purpur.network;

import java.util.Objects;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.Entity;

public final class PacketBroadcastContext {
    private final Entity entity;
    private Packet<?> packet;
    private boolean cancelled;

    public PacketBroadcastContext(final Entity entity, final Packet<?> packet) {
        this.entity = Objects.requireNonNull(entity, "entity");
        this.packet = Objects.requireNonNull(packet, "packet");
    }

    public Entity entity() {
        return this.entity;
    }

    public Packet<?> packet() {
        return this.packet;
    }

    public void packet(final Packet<?> packet) {
        this.packet = Objects.requireNonNull(packet, "packet");
    }

    public boolean cancelled() {
        return this.cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
