package com.aki.mcutils.renderer.tileentity;

import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.APICore.asm.ASMUtil;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.asm.MCUtilsASMTransformer;
import com.aki.mcutils.asm.Optifine;
import net.minecraft.client.renderer.culling.ICamera;

public class TileEntityRendererManager {
    private static TileEntityRenderer tileEntityRenderer = Optifine.isOptifineDetected() ? new TileEntityRendererOptifine() : new TileEntityRenderer();

    public static void setup(ICamera frustum, float partialTicks) {
        if(!MCUtils.isRenderLib)
            tileEntityRenderer.setup(frustum, partialTicks, GLUtils.getCameraEntityX(), GLUtils.getCameraEntityY(), GLUtils.getCameraEntityZ());
    }

    public static void reset() {
        tileEntityRenderer.reset();
    }

    public static void renderTileEntities(float partialTicks) {
        tileEntityRenderer.renderTileEntities(partialTicks);
    }

    public static int renderedTileEntities() {
        return tileEntityRenderer.getRenderedTileEntities();
    }

    public static int occludedTileEntities() {
        return tileEntityRenderer.getOccludedTileEntities();
    }

    public static int totalTileEntities() {
        return tileEntityRenderer.getTotalTileEntities();
    }
}
