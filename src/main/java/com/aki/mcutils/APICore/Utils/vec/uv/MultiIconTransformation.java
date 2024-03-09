package com.aki.mcutils.APICore.Utils.vec.uv;

import com.aki.mcutils.APICore.Utils.vec.IrreversibleTransformationException;
import com.aki.mcutils.APICore.program.ccr.render.CCRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class MultiIconTransformation extends UVTransformation {

    public TextureAtlasSprite[] icons;
    private TextureAtlasSprite icon;

    public MultiIconTransformation(TextureAtlasSprite... icons) {
        this.icons = icons;
    }

    @Override
    public void operate(CCRenderState state) {
        super.operate(state);
        state.sprite = icon;
    }

    @Override
    public void apply(UV uv) {
        icon = icons[uv.tex % icons.length];
        uv.u = icon.getInterpolatedU(uv.u * 16);
        uv.v = icon.getInterpolatedV(uv.v * 16);
    }

    @Override
    public UVTransformation inverse() {
        throw new IrreversibleTransformationException(this);
    }
}
