package org.purpurmc.purpur.entity.ai;

import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class HorseHasRider extends HasRider {
    public final AbstractHorse horse;

    public HorseHasRider(AbstractHorse entity) {
        super(entity);
        this.horse = entity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && horse.isSaddled();
    }
}
