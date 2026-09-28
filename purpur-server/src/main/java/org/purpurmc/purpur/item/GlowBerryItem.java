package org.purpurmc.purpur.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.bukkit.event.entity.EntityPotionEffectEvent;

public class GlowBerryItem extends BlockItem {
    public GlowBerryItem(Block block, Properties settings) {
        super(block, settings);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        ItemStack result = super.finishUsingItem(stack, world, user);
        if (world.purpurConfig.glowBerriesEatGlowDuration > 0 && user instanceof ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.GLOWING, world.purpurConfig.glowBerriesEatGlowDuration), EntityPotionEffectEvent.Cause.FOOD);
        }
        return result;
    }
}
