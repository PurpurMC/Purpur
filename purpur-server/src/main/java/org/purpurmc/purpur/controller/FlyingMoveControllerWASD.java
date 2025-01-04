package org.purpurmc.purpur.controller;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;

public class FlyingMoveControllerWASD extends MoveControllerWASD {
    protected final float groundSpeedModifier;
    protected final float flyingSpeedModifier;
    protected int tooHighCooldown = 0;
    protected boolean setNoGravityFlag;

    public FlyingMoveControllerWASD(Mob entity) {
        this(entity, 1.0F);
    }

    public FlyingMoveControllerWASD(Mob entity, float groundSpeedModifier) {
        this(entity, groundSpeedModifier, 1.0F, true);
    }

    public FlyingMoveControllerWASD(Mob entity, float groundSpeedModifier, float flyingSpeedModifier) {
        this(entity, groundSpeedModifier, flyingSpeedModifier, true);
    }

    public FlyingMoveControllerWASD(Mob entity, float groundSpeedModifier, float flyingSpeedModifier, boolean setNoGravityFlag) {
        super(entity);
        this.groundSpeedModifier = groundSpeedModifier;
        this.flyingSpeedModifier = flyingSpeedModifier;
        this.setNoGravityFlag = setNoGravityFlag;
    }

    @Override
    public void purpurTick(Player rider) {
        Input lastClientInput = ((ServerPlayer) rider).getLastClientInput();
        float forward = lastClientInput.forward() == lastClientInput.backward() ? 0.0F : lastClientInput.forward() ? 1.0F : 0.0F;
        float vertical = forward == 0.0F ? 0.0F : -(rider.xRotO / 45.0F);
        float strafe = (lastClientInput.left() == lastClientInput.right() ? 0.0F : lastClientInput.left() ? 1.0F : -1.0F);

        if (lastClientInput.jump() && spacebarEvent(entity)) {
            entity.onSpacebar();
        }

        if (entity.getY() >= entity.getMaxY() || --tooHighCooldown > 0) {
            if (tooHighCooldown <= 0) {
                tooHighCooldown = 20;
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -0.05D, 0.0D));
            vertical = 0.0F;
        }

        setSpeedModifier(entity.getAttributeValue(Attributes.MOVEMENT_SPEED));
        float speed = (float) getSpeedModifier();

        if (entity.onGround) {
            speed *= groundSpeedModifier; // TODO = fix this!
        } else {
            speed *= flyingSpeedModifier;
        }

        if (setNoGravityFlag) {
            entity.setNoGravity(forward > 0);
        }

        entity.setSpeed(speed);
        entity.setVerticalMot(vertical);
        entity.setStrafeMot(strafe);
        entity.setForwardMot(forward);

        setForward(entity.getForwardMot());
        setStrafe(entity.getStrafeMot());
    }
}
