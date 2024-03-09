package com.aki.mcutils.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EntityArrow.class)
public abstract class MixinFixArrow {
    @Shadow public abstract boolean getIsCritical();

    private Block inTile;
    protected boolean inGround;
    public EntityArrow.PickupStatus pickupStatus;
    public int arrowShake;
    public Entity shootingEntity;

    public double motionX;
    public double motionY;
    public double motionZ;
    public double damage;
    public Random rand;

    @Inject(method = "<init>(Lnet/minecraft/world/World;)V", at = @At("RETURN"))
    private static void FixInit1(World worldIn, CallbackInfo ci) {

    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)V", at = @At("RETURN"))
    private static void FixInit2(World worldIn, EntityLivingBase shooter, CallbackInfo ci) {

    }

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDD)V", at = @At("RETURN"))
    private static void FixInit3(World worldIn, double x, double y, double z, CallbackInfo ci) {

    }

    /*
    @Overwrite
    protected void onHit(RayTraceResult raytraceResultIn) {
        Entity entity = raytraceResultIn.entityHit;

        if (entity != null)
        {
            float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            int i = MathHelper.ceil((double)f * this.damage);

            if (this.getIsCritical())
            {
                i += this.rand.nextInt(i / 2 + 2);
            }

            DamageSource damagesource;

            if (this.shootingEntity == null)
            {
                damagesource = DamageSource.causeArrowDamage(this, this);
            }
            else
            {
                damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
            }

            if (this.isBurning() && !(entity instanceof EntityEnderman))
            {
                entity.setFire(5);
            }

            if (entity.attackEntityFrom(damagesource, (float)i))
            {
                if (entity instanceof EntityLivingBase)
                {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity;

                    if (!this.world.isRemote)
                    {
                        entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                    }

                    if (this.knockbackStrength > 0)
                    {
                        float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

                        if (f1 > 0.0F)
                        {
                            entitylivingbase.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)f1);
                        }
                    }

                    if (this.shootingEntity instanceof EntityLivingBase)
                    {
                        EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                    }

                    this.arrowHit(entitylivingbase);

                    if (this.shootingEntity != null && entitylivingbase != this.shootingEntity && entitylivingbase instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
                    {
                        ((EntityPlayerMP)this.shootingEntity).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
                    }
                }

                this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                if (!(entity instanceof EntityEnderman))
                {
                    this.setDead();
                }
            }
            else
            {
                this.motionX *= -0.10000000149011612D;
                this.motionY *= -0.10000000149011612D;
                this.motionZ *= -0.10000000149011612D;
                this.rotationYaw += 180.0F;
                this.prevRotationYaw += 180.0F;
                this.ticksInAir = 0;

                if (!this.world.isRemote && this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ < 0.0010000000474974513D)
                {
                    if (this.pickupStatus == EntityArrow.PickupStatus.ALLOWED)
                    {
                        this.entityDropItem(this.getArrowStack(), 0.1F);
                    }

                    this.setDead();
                }
            }
        }
        else
        {
            BlockPos blockpos = raytraceResultIn.getBlockPos();
            this.xTile = blockpos.getX();
            this.yTile = blockpos.getY();
            this.zTile = blockpos.getZ();
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            this.inTile = iblockstate.getBlock();
            this.inData = this.inTile.getMetaFromState(iblockstate);
            this.motionX = (double)((float)(raytraceResultIn.hitVec.x - this.posX));
            this.motionY = (double)((float)(raytraceResultIn.hitVec.y - this.posY));
            this.motionZ = (double)((float)(raytraceResultIn.hitVec.z - this.posZ));
            float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
            this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
            this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
            this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
            this.inGround = true;
            this.arrowShake = 7;
            this.setIsCritical(false);

            if (iblockstate.getMaterial() != Material.AIR)
            {
                this.inTile.onEntityCollision(this.world, blockpos, iblockstate, this);
            }
        }
    }*/
}
