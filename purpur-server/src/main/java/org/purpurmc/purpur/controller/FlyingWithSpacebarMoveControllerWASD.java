package org.purpurmc.purpur.controller;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FlyingWithSpacebarMoveControllerWASD extends FlyingMoveControllerWASD {
    public FlyingWithSpacebarMoveControllerWASD(Mob entity) {
        super(entity);
    }

    public FlyingWithSpacebarMoveControllerWASD(Mob entity, float groundSpeedModifier) {
        super(entity, groundSpeedModifier);
    }

    @Override
    public void purpurTick(Player rider) {
        Input lastClientInput = ((ServerPlayer) rider).getLastClientInput();
        float forward = (lastClientInput.forward() == lastClientInput.backward() ? 0.0F : lastClientInput.forward() ? 1.0F : -1.0F);
        float strafe = (lastClientInput.left() == lastClientInput.right() ? 0.0F : lastClientInput.left() ? 1.0F : -1.0F) * 0.5F;
        float vertical = 0;

        if (forward < 0.0F) {
            forward *= 0.5F;
            strafe *= 0.5F;
        }

        float speed = (float) entity.getAttributeValue(Attributes.MOVEMENT_SPEED);

        if (entity.onGround) {
            speed *= groundSpeedModifier;
        }

        if (lastClientInput.jump() && spacebarEvent(entity) && !entity.onSpacebar()) {
            entity.setNoGravity(true);
            vertical = 1.0F;
        } else {
            entity.setNoGravity(false);
        }

        if (entity.getY() >= entity.getMaxY() || --tooHighCooldown > 0) {
            if (tooHighCooldown <= 0) {
                tooHighCooldown = 20;
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -0.2D, 0.0D));
            vertical = 0.0F;
        }

        setSpeedModifier(speed);
        entity.setSpeed((float) getSpeedModifier());
        entity.setVerticalMot(vertical);
        entity.setStrafeMot(strafe);
        entity.setForwardMot(forward);

        setForward(entity.getForwardMot());
        setStrafe(entity.getStrafeMot());

        Vec3 mot = entity.getDeltaMovement();
        if (mot.y > 0.2D) {
            entity.setDeltaMovement(mot.x, 0.2D, mot.z);
        }
    }
}
