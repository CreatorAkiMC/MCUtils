package com.aki.mcutils.APICore.Utils.vec.uv;

import com.aki.mcutils.APICore.Utils.vec.ITransformation;
import com.aki.mcutils.APICore.program.ccr.render.CCRenderState;
import com.aki.mcutils.APICore.program.ccr.render.pipeline.IVertexOperation;

/**
 * Abstract supertype for any UV transformation
 */
public abstract class UVTransformation extends ITransformation<UV, UVTransformation> implements IVertexOperation {

    public static final int operationIndex = CCRenderState.registerOperation();

    public UVTransformation at(UV point) {
        return new UVTransformationList(new UVTranslation(-point.u, -point.v), this, new UVTranslation(point.u, point.v));
    }

    public UVTransformationList with(UVTransformation t) {
        return new UVTransformationList(this, t);
    }

    @Override
    public boolean load(CCRenderState state) {
        return !isRedundant();
    }

    @Override
    public void operate(CCRenderState state) {
        apply(state.vert.uv);
        state.sprite = null;
    }

    @Override
    public int operationID() {
        return operationIndex;
    }
}


