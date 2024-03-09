package com.aki.mcutils.APICore.Particle;

import com.aki.mcutils.APICore.Utils.ClientUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleFluidSplash extends Particle
{
    public boolean splash = false;

    public ParticleFluidSplash(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, boolean splash)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

        this.splash = splash;

        this.motionX *= 0.30000001192092896D;
        if(splash)
            this.motionY = Math.random() * 0.20000000298023224D + 0.10000000149011612D;
        else
            this.motionY *= 0.30000001192092896D;
        this.motionZ *= 0.30000001192092896D;
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        if(splash)
            this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        else this.particleMaxAge = (int)(ySpeedIn * 2.5);
        this.particleScale = .375f;
        this.setParticleTexture(ClientUtils.getSprite(FluidRegistry.WATER.getStill()));
    }

    //age = 20 == 1sec
    public ParticleFluidSplash(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int age)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.splash = false;

        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = age;
        this.particleScale = .375f;
        this.setParticleTexture(ClientUtils.getSprite(FluidRegistry.WATER.getStill()));
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if(splash)
            this.motionY -= (double)this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        if(splash)
            this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if(this.particleMaxAge-- <= 0)
            this.setExpired();

        if (this.onGround)
        {
            if(Math.random() < 0.5D)
                this.setExpired();
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        IBlockState iblockstate = this.world.getBlockState(blockpos);
        Material material = iblockstate.getMaterial();

        if(material.isLiquid() || material.isSolid())
        {
            double d0;
            if(iblockstate.getBlock() instanceof IFluidBlock) {
                d0 = ((IFluidBlock)iblockstate.getBlock()).getFilledPercentage(world, blockpos);
            }

            if(iblockstate.getBlock() instanceof BlockLiquid)
                d0 = (double)(1.0F - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL).intValue()));
            else if(iblockstate.getBlock() instanceof IFluidBlock) {
                d0 = 1.0F - ((IFluidBlock)iblockstate.getBlock()).getFilledPercentage(world, blockpos);
            } else d0 = iblockstate.getBoundingBox(this.world, blockpos).maxY;
            double d1 = (double) MathHelper.floor(this.posY) + d0;
            if(this.posY < d1)
                this.setExpired();
        }
    }

    public void setFluidTexture(FluidStack fluid)
    {
        this.setParticleTexture(ClientUtils.getSprite(fluid.getFluid().getStill(fluid)));
        int argb = fluid.getFluid().getColor(fluid);
        this.particleAlpha = ((argb>>24)&255)/255f;
        this.particleRed = ((argb>>16)&255)/255f;
        this.particleRed = ((argb>>8&255))/255f;
        this.particleRed = (argb&255)/255f;
    }

    @Override
    public int getFXLayer()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
        {
            return new ParticleFluidSplash(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, true);
        }
    }
}
