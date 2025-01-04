package org.purpurmc.purpur.controller;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import org.purpurmc.purpur.event.entity.RidableSpacebarEvent;

public class MoveControllerWASD extends MoveControl {
    protected final Mob entity;
    private final double speedModifier;

    public MoveControllerWASD(Mob entity) {
        this(entity, 1.0D);
    }

    public MoveControllerWASD(Mob entity, double speedModifier) {
        super(entity);
        this.entity = entity;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean hasWanted() {
        return entity.getRider() != null ? strafeForwards != 0 || strafeRight != 0 : super.hasWanted();
    }

    @Override
    public void tick() {
        if (entity.getRider() != null && entity.isControllable()) {
            purpurTick(entity.getRider());
        } else {
            vanillaTick();
        }
    }

    public void vanillaTick() {
        super.tick();
    }

    public void purpurTick(Player rider) {
        Input lastClientInput = ((ServerPlayer) rider).getLastClientInput();
        float forward = (lastClientInput.forward() == lastClientInput.backward() ? 0.0F : lastClientInput.forward() ? 1.0F : -1.0F) * 0.5F;
        float strafe = (lastClientInput.left() == lastClientInput.right() ? 0.0F : lastClientInput.left() ? 1.0F : -1.0F) * 0.25F;

        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        float yawOffset = 0;
        if (strafe != 0) {
            if (forward == 0) {
                yawOffset += strafe > 0 ? -90 : 90;
                forward = Math.abs(strafe * 2);
            } else {
                yawOffset += strafe > 0 ? -30 : 30;
                strafe /= 2;
                if (forward < 0) {
                    yawOffset += strafe > 0 ? -110 : 110;
                    forward *= -1;
                }
            }
        } else if (forward < 0) {
            yawOffset -= 180;
            forward *= -1;
        }

        ((LookControllerWASD) entity.getLookControl()).setOffsets(yawOffset, 0);

        if (lastClientInput.jump() && spacebarEvent(entity) && !entity.onSpacebar() && entity.onGround) {
            entity.jumpFromGround();
        }

        setSpeedModifier(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * speedModifier);

        entity.setSpeed((float) getSpeedModifier());
        entity.setForwardMot(forward);

        setForward(entity.getForwardMot());
        setStrafe(entity.getStrafeMot());
    }

    public static boolean spacebarEvent(Mob entity) {
        if (RidableSpacebarEvent.getHandlerList().getRegisteredListeners().length > 0) {
            return new RidableSpacebarEvent(entity.getBukkitEntity()).callEvent();
        } else {
            return true;
        }
    }
}
