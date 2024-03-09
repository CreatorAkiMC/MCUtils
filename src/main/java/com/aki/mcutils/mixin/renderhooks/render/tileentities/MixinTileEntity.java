package com.aki.mcutils.mixin.renderhooks.render.tileentities;

import com.aki.mcutils.APICore.Utils.rand.mersenne.MersenneTwister;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.MCUtilsConfig;
import com.aki.mcutils.renderer.integrate.ValkyrienSkies;
import com.aki.mcutils.renderer.render_util.ITileEntityRendererCache;
import com.aki.mcutils.renderer.render_util.MutableAABB;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Random;

@Mixin(value = TileEntity.class, priority = MCUtils.ModPriority)
public class MixinTileEntity implements ITileEntityRendererCache {
    @Unique
    private TileEntitySpecialRenderer<TileEntity> renderer;
    @Unique
    private boolean rendererInitialized;
    @Unique
    private static final Random RAND = new MersenneTwister();
    @Unique
    private final MutableAABB cachedBoundingBox = new MutableAABB();
    @Unique
    private boolean initialized;
    @Unique
    private boolean isChunkLoaded = true;
    @Shadow
    private BlockPos pos;
    @Shadow
    private World world;

    @SuppressWarnings("unchecked")
    @Unique
    @Override
    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer() {
        if (!rendererInitialized) {
            renderer = loadRenderer((TileEntity) (Object) this);
            rendererInitialized = true;
        }
        return (TileEntitySpecialRenderer<T>) renderer;
    }

    @Unique
    @Override
    public void updateCachedBoundingBox(double partialTicks) {
        if (!initialized
                || !MCUtilsConfig.tileEntityCachedBoundingBoxEnabled
                //|| MCUtilsConfig.tileEntityCachedBoundingBoxBlacklistImpl.contains()
                || MCUtilsConfig.tileEntityCachedBoundingBoxUpdateInterval == 1
                || RAND.nextInt(MCUtilsConfig.tileEntityCachedBoundingBoxUpdateInterval) == 0
                || MCUtils.isValkyrienSkiesInstalled && ValkyrienSkies.isOnShip((TileEntity) (Object) this)) {
            cachedBoundingBox.set(MCUtils.isValkyrienSkiesInstalled ? ValkyrienSkies.getAABB((TileEntity) (Object) this) : ((TileEntity) (Object) this).getRenderBoundingBox());
            /*Vec3d v = RenderLibConfig.tileEntityBoundingBoxGrowthListImpl.get((TileEntity) (Object) this);
            if (v != null) {
                cachedBoundingBox.grow(v.x, v.y, v.x, v.x, v.z, v.x);
            }*/
            initialized = true;
        }
    }

    @Unique
    @Override
    public MutableAABB getCachedBoundingBox() {
        return this.cachedBoundingBox;
    }

    @Override
    public boolean isChunkLoaded() {
        return isChunkLoaded;
    }

    @Override
    public void setChunkLoaded(boolean isChunkLoaded) {
        this.isChunkLoaded = isChunkLoaded;
    }

    /**
     * @author Aki
     * @reason Replace Bounding Box Getter Method
     */
    @SideOnly(Side.CLIENT)
    @Overwrite(remap = false)
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = this.pos;
        try {
            World world = this.world;
            return world.getBlockState(pos).getBoundingBox(world, pos).offset(pos);
        } catch (Exception e) {
            return new AxisAlignedBB(pos);
        }
    }
}
