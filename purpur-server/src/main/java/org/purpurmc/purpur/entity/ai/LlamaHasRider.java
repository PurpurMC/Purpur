package org.purpurmc.purpur.entity.ai;

import net.minecraft.world.entity.animal.horse.Llama;

public class LlamaHasRider extends HasRider {
    public final Llama llama;

    public LlamaHasRider(Llama entity) {
        super(entity);
        this.llama = entity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && llama.isSaddled() && llama.isControllable();
    }
}
