package com.aki.mcutils.renderer.render_util;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

public interface ITileEntityRendererCache {

    default boolean hasRenderer() {
        return getRenderer() != null;
    }

    @Nullable
    <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer();

    @Nullable
    default <T extends TileEntity> TileEntitySpecialRenderer<T> loadRenderer(TileEntity tileEntity) {
        return TileEntityRendererDispatcher.instance.getRenderer(tileEntity);
    }

    boolean isChunkLoaded();

    void setChunkLoaded(boolean isChunkLoaded);

    void updateCachedBoundingBox(double partialTicks);

    MutableAABB getCachedBoundingBox();
}
