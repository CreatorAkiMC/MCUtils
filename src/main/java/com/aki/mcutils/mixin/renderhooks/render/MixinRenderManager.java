package com.aki.mcutils.mixin.renderhooks.render;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.renderer.render_util.IEntityRendererCache;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = RenderManager.class, priority = MCUtils.ModPriority)
public abstract class MixinRenderManager {

    @Redirect(method = "isRenderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
    public Render<Entity> isRenderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
        return ((IEntityRendererCache) entityIn).getRenderer();
    }

    @Redirect(method = "shouldRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
    public Render<Entity> shouldRender_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
        return ((IEntityRendererCache) entityIn).getRenderer();
    }

    @Redirect(method = "renderMultipass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderManager;getEntityRenderObject(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;"))
    public Render<Entity> renderMultipass_getEntityRenderObject(RenderManager renderManager, Entity entityIn) {
        return ((IEntityRendererCache) entityIn).getRenderer();
    }

}
