package org.purpurmc.purpur.entity.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.projectile.LlamaSpit;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.entity.EntityRemoveEvent;

public class DolphinSpit extends LlamaSpit {
    public LivingEntity dolphin;
    public int ticksLived;

    public DolphinSpit(EntityType<? extends LlamaSpit> type, Level world) {
        super(type, world);
    }

    public DolphinSpit(Level world, Dolphin dolphin) {
        this(EntityType.LLAMA_SPIT, world);
        this.setOwner(dolphin.getRider() != null ? dolphin.getRider() : dolphin);
        this.dolphin = dolphin;
        this.setPos(
                dolphin.getX() - (double) (dolphin.getBbWidth() + 1.0F) * 0.5 * (double) Mth.sin(dolphin.yBodyRot * (float) (Math.PI / 180.0)),
                dolphin.getEyeY() - 0.1F,
                dolphin.getZ() + (double) (dolphin.getBbWidth() + 1.0F) * 0.5 * (double) Mth.cos(dolphin.yBodyRot * (float) (Math.PI / 180.0)));
    }

    @Override
    public boolean canSaveToDisk() {
        return false;
    }

    public void tick() {
        projectileTick();

        Vec3 mot = this.getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        this.preHitTargetOrDeflectSelf(hitResult);

        double x = this.getX() + mot.x;
        double y = this.getY() + mot.y;
        double z = this.getZ() + mot.z;

        this.updateRotation();

        Vec3 motDouble = mot.scale(2.0);
        for (int i = 0; i < 5; i++) {
            ((ServerLevel) level()).sendParticlesSource(null, ParticleTypes.BUBBLE,
                    true, false,
                    getX() + random.nextFloat() / 2 - 0.25F,
                    getY() + random.nextFloat() / 2 - 0.25F,
                    getZ() + random.nextFloat() / 2 - 0.25F,
                    0, motDouble.x(), motDouble.y(), motDouble.z(), 0.1D);
        }

        if (++ticksLived > 20) {
            this.discard(EntityRemoveEvent.Cause.DISCARD);
        } else {
            this.setDeltaMovement(mot.scale(0.99D));
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.06D, 0.0D));
            }

            this.setPos(x, y, z);
        }
    }

    @Override
    public void shoot(double x, double y, double z, float speed, float inaccuracy) {
        setDeltaMovement(new Vec3(x, y, z).normalize().add(
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
                random.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
                .scale(speed));
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity shooter = this.getOwner();
        if (shooter instanceof LivingEntity) {
            entityHitResult.getEntity().hurt(entityHitResult.getEntity().damageSources().mobProjectile(this, (LivingEntity) shooter), level().purpurConfig.dolphinSpitDamage);
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        if (this.hitCancelled) {
            return;
        }
        BlockState state = this.level().getBlockState(blockHitResult.getBlockPos());
        state.onProjectileHit(this.level(), state, blockHitResult, this);
        this.discard(EntityRemoveEvent.Cause.DISCARD);
    }
}
