package com.aki.mcutils.mixin.renderhooks.render.entities;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.renderer.render_util.IEntityRendererCache;
import com.aki.mcutils.renderer.render_util.MutableAABB;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(value = Entity.class, priority = MCUtils.ModPriority)
public class MixinEntity implements IEntityRendererCache {
    @Unique
    private Render<Entity> renderer;
    @Unique
    private boolean rendererInitialized;
    @Unique
    private boolean isChunkLoaded = true;
    @Unique
    private final MutableAABB cachedBoundingBox = new MutableAABB();

    @SuppressWarnings("unchecked")
    @Unique
    @Override
    @Nullable
    public <T extends Entity> Render<T> getRenderer() {
        if (!rendererInitialized) {
            if (!((Entity) (Object) this instanceof AbstractClientPlayer)
                    || (((AbstractClientPlayer) (Entity) (Object) this).playerInfo != null
                    && ((AbstractClientPlayer) (Entity) (Object) this).playerInfo.skinType != null)) {
                rendererInitialized = true;
            }
            renderer = loadRenderer((Entity) (Object) this);
        }
        return (Render<T>) renderer;
    }

    @Override
    public boolean isChunkLoaded() {
        return isChunkLoaded;
    }

    @Override
    public void setChunkLoaded(boolean isChunkLoaded) {
        this.isChunkLoaded = isChunkLoaded;
    }

    @Unique
    @Override
    public void updateCachedBoundingBox(double partialTicks) {
        cachedBoundingBox.set(((Entity) (Object) this).getRenderBoundingBox());
        cachedBoundingBox.grow(0.5D);
        cachedBoundingBox.offset(
                -(((Entity) (Object) this).posX - ((Entity) (Object) this).lastTickPosX) * (1.0D - partialTicks),
                -(((Entity) (Object) this).posY - ((Entity) (Object) this).lastTickPosY) * (1.0D - partialTicks),
                -(((Entity) (Object) this).posZ - ((Entity) (Object) this).lastTickPosZ) * (1.0D - partialTicks));
        /*Vec3d v = RenderLibConfig.entityBoundingBoxGrowthListImpl.get((Entity) (Object) this);
        if (v != null) {
            cachedBoundingBox.grow(v.x, v.y, v.x, v.x, v.z, v.x);
        }*/
    }

    @Unique
    @Override
    public MutableAABB getCachedBoundingBox() {
        return this.cachedBoundingBox;
    }
}
