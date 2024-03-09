package com.aki.mcutils.mixin.renderhooks.render;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.renderer.render_util.IEntityRendererCache;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Render.class, priority = MCUtils.ModPriority)
public class MixinRender {

    @Inject(method = "shouldRender", cancellable = true, at = @At("HEAD"))
    public void shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> info) {
        if (!entity.isInRangeToRender3d(camX, camY, camZ)) {
            info.setReturnValue(false);
            return;
        }

        if (entity.ignoreFrustumCheck) {
            info.setReturnValue(true);
            return;
        }

        info.setReturnValue(((IEntityRendererCache) entity).getCachedBoundingBox().isVisible(camera));
    }

}
