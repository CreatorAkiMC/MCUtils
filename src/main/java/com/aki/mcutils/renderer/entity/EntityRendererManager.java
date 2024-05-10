package com.aki.mcutils.renderer.entity;

import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.asm.MCUtilsASMTransformer;
import com.aki.mcutils.asm.Optifine;
import net.minecraft.client.renderer.culling.ICamera;

public class EntityRendererManager {
    private static final EntityRenderer entityRenderer = Optifine.isOptifineDetected() ? new EntityRendererOptifine() : new EntityRenderer();

    public static void setup(ICamera frustum, float partialTicks) {
        if(!MCUtils.isRenderLib)
            entityRenderer.setup(frustum, partialTicks, GLUtils.getCameraEntityX(), GLUtils.getCameraEntityY(), GLUtils.getCameraEntityZ());
    }

    public static void reset() {
        entityRenderer.reset();
    }

    public static void renderEntities(float partialTicks) {
        entityRenderer.renderEntities(partialTicks);
    }

    public static int renderedEntities() {
        return entityRenderer.getRenderedEntities();
    }

    public static int occludedEntities() {
        return entityRenderer.getOccludedEntities();
    }

    public static int totalEntities() {
        return entityRenderer.getTotalEntities();
    }
}
