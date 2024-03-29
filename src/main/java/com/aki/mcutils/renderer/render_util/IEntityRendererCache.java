package com.aki.mcutils.renderer.render_util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface IEntityRendererCache {

    default boolean hasRenderer() {
        return getRenderer() != null;
    }

    @Nullable
    <T extends Entity> Render<T> getRenderer();

    @Nullable
    default <T extends Entity> Render<T> loadRenderer(Entity entity) {
        Render<T> renderer = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entity);
        return renderer.getClass() == RenderEntity.class ? null : renderer;
    }

    boolean isChunkLoaded();

    void setChunkLoaded(boolean isChunkLoaded);

    void updateCachedBoundingBox(double partialTicks);

    MutableAABB getCachedBoundingBox();
}
