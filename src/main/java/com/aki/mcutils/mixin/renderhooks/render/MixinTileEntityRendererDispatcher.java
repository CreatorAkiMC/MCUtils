package com.aki.mcutils.mixin.renderhooks.render;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.renderer.render_util.ITileEntityRendererCache;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityRendererDispatcher.class, priority = MCUtils.ModPriority)
public class MixinTileEntityRendererDispatcher {

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;DDDFIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher;getRenderer(Lnet/minecraft/tileentity/TileEntity;)Lnet/minecraft/client/renderer/tileentity/TileEntitySpecialRenderer;"))
    public TileEntitySpecialRenderer<TileEntity> render_getRenderer(TileEntityRendererDispatcher tileEntityRenderDispatcher, TileEntity tileEntity) {
        if (tileEntity == null)
            return null;
        if (tileEntity.isInvalid())
            return null;
        return ((ITileEntityRendererCache) tileEntity).getRenderer();
    }

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getDistanceSq(DDD)D"))
    public double render_getDistanceSq(TileEntity tileEntity, double x, double y, double z) {
        return 0.0D;
    }

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getMaxRenderDistanceSquared()D"))
    public double render_getMaxRenderDistanceSquared(TileEntity tileEntity) {
        return 1.0D;
    }

    @Redirect(method = "render(Lnet/minecraft/tileentity/TileEntity;FI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isBlockLoaded(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    public boolean render_isBlockLoaded(World world, BlockPos pos, boolean allowEmpty) {
        return true;
    }
}
