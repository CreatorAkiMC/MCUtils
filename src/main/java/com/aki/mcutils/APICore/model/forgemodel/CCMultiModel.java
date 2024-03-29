package com.aki.mcutils.APICore.model.forgemodel;

import com.aki.mcutils.APICore.model.bakedmodels.ModelProperties;
import com.aki.mcutils.APICore.model.bakedmodels.ModelProperties.PerspectiveProperties;
import com.aki.mcutils.APICore.model.bakedmodels.PerspectiveAwareMultiModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * Created by covers1624 on 16/12/2016.
 */
public class CCMultiModel implements IModel {

    private final IModel base;
    private final ModelProperties baseProperties;
    private final List<IModel> subModels;

    public CCMultiModel(IModel base, ModelProperties baseProperties, List<IModel> subModels) {
        this.base = base;
        this.baseProperties = baseProperties;
        this.subModels = subModels;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        List<ResourceLocation> deps = new ArrayList<>();
        if (base != null) {
            deps.addAll(base.getDependencies());
        }
        for (IModel subModel : subModels) {
            deps.addAll(subModel.getDependencies());
        }
        return deps;
    }

    @Override
    public Collection<ResourceLocation> getTextures() {
        List<ResourceLocation> deps = new ArrayList<>();
        if (base != null) {
            deps.addAll(base.getTextures());
        }
        for (IModel subModel : subModels) {
            deps.addAll(subModel.getTextures());
        }
        return deps;
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        IBakedModel baseBakedModel = null;
        if (base != null) {
            baseBakedModel = base.bake(state, format, bakedTextureGetter);
        }
        List<IBakedModel> subBakedModels = new ArrayList<>();
        for (IModel subModel : subModels) {
            IBakedModel bakedSubModel = subModel.bake(subModel.getDefaultState(), format, bakedTextureGetter);
            subBakedModels.add(bakedSubModel);
        }

        return new PerspectiveAwareMultiModel(baseBakedModel, subBakedModels, new PerspectiveProperties(state, baseProperties));
    }

    @Override
    public IModelState getDefaultState() {
        return base.getDefaultState();
    }
}
