package org.purpurmc.purpur.controller;


import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.player.Player;

public class LookControllerWASD extends LookControl {
    protected final Mob entity;
    private float yOffset = 0;
    private float xOffset = 0;

    public LookControllerWASD(Mob entity) {
        super(entity);
        this.entity = entity;
    }

    // tick
    @Override
    public void tick() {
        if (entity.getRider() != null && entity.isControllable()) {
            purpurTick(entity.getRider());
        } else {
            vanillaTick();
        }
    }

    protected void purpurTick(Player rider) {
        setYawPitch(rider.getYRot(), rider.getXRot());
    }

    public void vanillaTick() {
        super.tick();
    }

    public void setYawPitch(float yRot, float xRot) {
        entity.setXRot(normalizePitch(xRot + xOffset));
        entity.setYRot(normalizeYaw(yRot + yOffset));
        entity.setYHeadRot(entity.getYRot());
        entity.xRotO = entity.getXRot();
        entity.yRotO = entity.getYRot();

        ClientboundMoveEntityPacket.PosRot entityPacket = new ClientboundMoveEntityPacket.PosRot(
            entity.getId(),
            (short) 0, (short) 0, (short) 0,
            (byte) Mth.floor(entity.getYRot() * 256.0F / 360.0F),
            (byte) Mth.floor(entity.getXRot() * 256.0F / 360.0F),
            entity.onGround
        );
        ((ServerLevel) entity.level()).getChunkSource().broadcast(entity, entityPacket);
    }

    public void setOffsets(float yaw, float pitch) {
        yOffset = yaw;
        xOffset = pitch;
    }

    public float normalizeYaw(float yaw) {
        yaw %= 360.0f;
        if (yaw >= 180.0f) {
            yaw -= 360.0f;
        } else if (yaw < -180.0f) {
            yaw += 360.0f;
        }
        return yaw;
    }

    public float normalizePitch(float pitch) {
        if (pitch > 90.0f) {
            pitch = 90.0f;
        } else if (pitch < -90.0f) {
            pitch = -90.0f;
        }
        return pitch;
    }
}
