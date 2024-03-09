package com.aki.mcutils.APICore.Particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFallingDust;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class ParticleCustomColor extends Particle {
    public ParticleCustomColor(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Color color, int maxage, float scale)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.motionX *= 0.30000001192092896D;
        this.motionY *= 0.30000001192092896D;
        this.motionZ *= 0.30000001192092896D;
        this.particleRed = color.getRed();
        this.particleGreen = color.getGreen();
        this.particleBlue = color.getBlue();
        this.setSize(0.15F, 0.15F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = maxage;
        this.particleScale = scale;
    }

    //age = 20 == 1sec
    public ParticleCustomColor(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int maxage)
    {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        this.particleRed = 1.0F;
        this.particleGreen = 1.0F;
        this.particleBlue = 1.0F;
        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.particleMaxAge = maxage;
        this.particleScale = .375f;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
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
    }

    /*public void setFluidTexture(FluidStack fluid)
    {
        this.setParticleTexture(ClientUtils.getSprite(fluid.getFluid().getStill(fluid)));
        int argb = fluid.getFluid().getColor(fluid);
        this.particleAlpha = ((argb>>24)&255)/255f;
        this.particleRed = ((argb>>16)&255)/255f;
        this.particleRed = ((argb>>8&255))/255f;
        this.particleRed = (argb&255)/255f;
    }*/

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
            return new ParticleCustomColor(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Color.LIGHT_GRAY, 10, 0.375f);
        }
    }
}
