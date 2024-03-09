package com.aki.mcutils.mixin.renderhooks;

import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.asm.Optifine;
import com.aki.mcutils.renderer.entity.EntityRendererManager;
import com.aki.mcutils.renderer.render_util.BoundingBoxHelper;
import com.aki.mcutils.renderer.tileentity.TileEntityRendererManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(value = RenderGlobal.class, priority = MCUtils.ModPriority)
public class MixinRenderGlobal {
    @Inject(method = "setupTerrain", at = @At("HEAD"))
    public void setupCamera(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator, CallbackInfo info) {
        GLUtils.updateCamera();
    }

    @Inject(method = "renderEntities", at = @At("HEAD"))
    public void preEntities(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        if (MinecraftForgeClient.getRenderPass() == 0) {
            EntityRendererManager.setup(camera, (float) partialTicks);
            TileEntityRendererManager.setup(camera, (float) partialTicks);
        }
    }

    @Inject(method = "renderEntities", at = @At("RETURN"))
    public void postEntities(Entity renderViewEntity, ICamera camera, float partialTicks, CallbackInfo info) {
        if (MinecraftForgeClient.getRenderPass() == 1) {
            EntityRendererManager.reset();
            TileEntityRendererManager.reset();
        }

        if (Minecraft.getMinecraft().getRenderManager().debugBoundingBox
                && MinecraftForgeClient.getRenderPass() == 1
                && (!Optifine.isShadowPass())) {
            BoundingBoxHelper.getInstance().drawRenderBoxes(partialTicks);
        }
    }

    @Inject(method = "updateTileEntities", cancellable = true, at = @At("HEAD"))
    public void updateTileEntities(Collection<TileEntity> tileEntitiesToRemove, Collection<TileEntity> tileEntitiesToAdd, CallbackInfo info) {
        info.cancel();
    }
}
