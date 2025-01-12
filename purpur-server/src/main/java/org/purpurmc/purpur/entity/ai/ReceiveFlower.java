package org.purpurmc.purpur.entity.ai;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.EnumSet;
import java.util.UUID;

public class ReceiveFlower extends Goal {
    private final IronGolem irongolem;
    private ServerPlayer target;
    private int cooldown;

    public ReceiveFlower(IronGolem entity) {
        this.irongolem = entity;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.irongolem.getOfferFlowerTick() > 0) {
            return false;
        }
        if (!this.irongolem.isAngry()) {
            return false;
        }
        UUID uuid = this.irongolem.getPersistentAngerTarget();
        if (uuid == null) {
            return false;
        }
        Entity target = ((ServerLevel) this.irongolem.level()).getEntity(uuid);
        if (!(target instanceof ServerPlayer player)) {
            return false;
        }
        InteractionHand hand = getPoppyHand(player);
        if (hand == null) {
            return false;
        }
        removeFlower(player, hand);
        this.target = player;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.cooldown > 0;
    }

    @Override
    public void start() {
        this.cooldown = 100;
        this.irongolem.stopBeingAngry();
        this.irongolem.offerFlower(true);
    }

    @Override
    public void stop() {
        this.irongolem.offerFlower(false);
        this.target = null;
    }

    @Override
    public void tick() {
        this.irongolem.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        --this.cooldown;
    }

    private InteractionHand getPoppyHand(ServerPlayer player) {
        if (isPoppy(player.getMainHandItem())) {
            return InteractionHand.MAIN_HAND;
        }
        if (isPoppy(player.getOffhandItem())) {
            return InteractionHand.OFF_HAND;
        }
        return null;
    }

    private void removeFlower(ServerPlayer player, InteractionHand hand) {
        player.setItemInHand(hand, ItemStack.EMPTY);
    }

    private boolean isPoppy(ItemStack item) {
        return item.getItem() == Blocks.POPPY.asItem();
    }
}
