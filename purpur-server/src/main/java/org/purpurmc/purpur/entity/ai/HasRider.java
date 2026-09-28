package org.purpurmc.purpur.entity.ai;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HasRider extends Goal {
    public final Mob entity;

    public HasRider(Mob entity) {
        this.entity = entity;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET, Flag.UNKNOWN_BEHAVIOR));
    }

    @Override
    public boolean canUse() {
        return entity.getRider() != null && entity.isControllable();
    }
}
