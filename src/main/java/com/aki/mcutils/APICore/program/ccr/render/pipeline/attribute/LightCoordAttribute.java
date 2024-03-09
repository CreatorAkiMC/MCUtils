package com.aki.mcutils.APICore.program.ccr.render.pipeline.attribute;

import com.aki.mcutils.APICore.Utils.vec.Transformation;
import com.aki.mcutils.APICore.Utils.vec.Vector3;
import com.aki.mcutils.APICore.lighting.LC;
import com.aki.mcutils.APICore.program.ccr.render.CCRenderState;
import com.aki.mcutils.APICore.program.ccr.render.pipeline.VertexAttribute;

/**
 * Uses the position of the lightmatrix to compute LC if not provided
 */
public class LightCoordAttribute extends VertexAttribute<LC[]> {

    public static final AttributeKey<LC[]> attributeKey = new AttributeKey<LC[]>() {
        @Override
        public LC[] newArray(int length) {
            return new LC[length];
        }
    };

    private LC[] lcRef;
    private Vector3 vec = new Vector3();//for computation
    private Vector3 pos = new Vector3();

    @Override
    public LC[] newArray(int length) {
        return new LC[length];
    }

    @Override
    public String getAttribName() {
        return "lightCoordAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        lcRef = state.model.getAttributes(LightCoordAttribute.attributeKey);
        if (state.model.hasAttribute(LightCoordAttribute.attributeKey)) {
            return lcRef != null;
        }

        pos.set(state.lightMatrix.pos);
        state.pipeline.addDependency(state.sideAttrib);
        state.pipeline.addRequirement(Transformation.operationIndex);
        return true;
    }

    @Override
    public void operate(CCRenderState state) {
        if (lcRef != null) {
            state.lc.set(lcRef[state.vertexIndex]);
        } else {
            state.lc.compute(vec.set(state.vert.vec).subtract(pos), state.side);
        }
    }
}
