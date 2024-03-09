package com.aki.mcutils.APICore.Utils.render;

import net.minecraft.util.BlockRenderLayer;

public enum ChunkRenderPass {
    SOLID,
    CUTOUT_MIPPED,
    CUTOUT,
    TRANSLUCENT;

    public static final ChunkRenderPass[] ALL = ChunkRenderPass.values();

    public static ChunkRenderPass ConvVanillaRenderPass(BlockRenderLayer layer) {
        return ALL[layer.ordinal()];
    }
}
