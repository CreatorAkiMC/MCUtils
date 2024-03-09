package com.aki.mcutils.APICore.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ClientUtils {
    public static final GlowInfo NO_GLOW = new GlowInfo(0, 0, false);

    public static TextureAtlasSprite getSprite(ResourceLocation rl)
    {
        return mc().getTextureMapBlocks().getAtlasSprite(rl.toString());
    }

    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public void draw3DBlock(Block block)
    {
        final BlockRendererDispatcher blockRenderer = mc().getBlockRendererDispatcher();
        IBlockState state = block.getDefaultState();
        IBakedModel model = blockRenderer.getBlockModelShapes().getModelForState(state);

        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if(Minecraft.isAmbientOcclusionEnabled())
            GlStateManager.shadeModel(7425);
        else
            GlStateManager.shadeModel(7424);
        blockRenderer.getBlockModelRenderer().renderModelBrightness(model, state, .75f, false);
    }

    @Nonnull
    public static GlowInfo enableGlow() {
        return enableGlow(15);
    }

    @Nonnull
    public static GlowInfo enableGlow(int glow) {
        if (!FMLClientHandler.instance().hasOptifine() && glow > 0) {
            GlowInfo info = new GlowInfo(OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY, true);
            float glowStrength = (glow / 15F) * 240F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, Math.min(glowStrength + info.lightmapLastX, 240), Math.min(glowStrength + info.lightmapLastY, 240));
            return info;
        }
        return NO_GLOW;
    }

    @Nonnull
    public static GlowInfo enableGlow(@Nullable FluidStack fluid) {
        return fluid == null ? NO_GLOW : enableGlow(fluid.getFluid().getLuminosity(fluid));
    }

    @Nonnull
    public static GlowInfo enableGlow(@Nullable Fluid fluid) {
        return fluid == null ? NO_GLOW : enableGlow(fluid.getLuminosity());
    }

    public static void disableGlow(@Nonnull GlowInfo info) {
        if (info.glowEnabled) {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, info.lightmapLastX, info.lightmapLastY);
        }
    }


    public static class GlowInfo {

        private final boolean glowEnabled;
        private final float lightmapLastX;
        private final float lightmapLastY;

        public GlowInfo(float lightmapLastX, float lightmapLastY, boolean glowEnabled) {
            this.lightmapLastX = lightmapLastX;
            this.lightmapLastY = lightmapLastY;
            this.glowEnabled = glowEnabled;
        }
    }
}
