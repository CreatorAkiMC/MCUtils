package com.aki.mcutils.mixin.renderhooks.render;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.renderer.render_util.EntityUtil;
import com.aki.mcutils.renderer.render_util.IEntityRendererCache;
import com.aki.mcutils.renderer.render_util.ITileEntityRendererCache;
import com.aki.mcutils.renderer.render_util.TileEntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = MCUtils.ModPriority)
public class MixinEntityRenderer {

    /** {@link EntityRenderer#renderWorld(float, long)} */
    @Inject(method = "renderWorld", at = @At("HEAD"))
    public void renderWorld(float partialTicks, long finishTimeNano, CallbackInfo info) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.world == null)
            return;

        for (Entity e : EntityUtil.entityIterable(mc.world.loadedEntityList)) {
            IEntityRendererCache entity = ((IEntityRendererCache) e);
            if (entity.hasRenderer() && entity.isChunkLoaded())
                entity.updateCachedBoundingBox(partialTicks);
        }

        TileEntityUtil.processTileEntities(mc.world, tileEntity -> {
            ITileEntityRendererCache tile = ((ITileEntityRendererCache) tileEntity);
            if (tile.isChunkLoaded())
                tile.updateCachedBoundingBox(partialTicks);
        });
    }

}
