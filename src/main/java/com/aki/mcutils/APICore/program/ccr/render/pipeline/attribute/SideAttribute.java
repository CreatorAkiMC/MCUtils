package com.aki.mcutils.APICore.program.ccr.render.pipeline.attribute;

import com.aki.mcutils.APICore.Utils.VectorUtils;
import com.aki.mcutils.APICore.program.ccr.render.CCRenderState;
import com.aki.mcutils.APICore.program.ccr.render.pipeline.VertexAttribute;

/**
 * Sets the side state in CCRS based on the provided model. If the model does not have side data it requires normals.
 */
public class SideAttribute extends VertexAttribute<int[]> {

    public static final AttributeKey<int[]> attributeKey = new AttributeKey<int[]>() {
        @Override
        public int[] newArray(int length) {
            return new int[length];
        }
    };

    private int[] sideRef;

    @Override
    public int[] newArray(int length) {
        return new int[length];
    }

    @Override
    public String getAttribName() {
        return "sideAttrib";
    }

    @Override
    public boolean load(CCRenderState state) {
        sideRef = state.model.getAttributes(SideAttribute.attributeKey);
        if (state.model.hasAttribute(SideAttribute.attributeKey)) {
            return sideRef != null;
        }

        state.pipeline.addDependency(state.normalAttrib);
        return true;
    }

    @Override
    public void operate(CCRenderState state) {
        if (sideRef != null) {
            state.side = sideRef[state.vertexIndex];
        } else {
            state.side = VectorUtils.findSide(state.normal);
        }
    }
}
