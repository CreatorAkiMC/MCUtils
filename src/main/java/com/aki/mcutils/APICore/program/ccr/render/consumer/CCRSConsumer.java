package com.aki.mcutils.APICore.program.ccr.render.consumer;

import com.aki.mcutils.APICore.Utils.vec.Vector3;
import com.aki.mcutils.APICore.colour.Colour;
import com.aki.mcutils.APICore.program.ccr.render.CCRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

/**
 * Created by covers1624 on 15/01/2017.
 * TODO, Maybe merge with CCRS??
 */
public class CCRSConsumer implements IVertexConsumer {

    private final CCRenderState ccrs;
    private Vector3 offset = Vector3.zero;

    public CCRSConsumer(CCRenderState ccrs) {
        this.ccrs = ccrs;
    }

    @Override
    public VertexFormat getVertexFormat() {
        return ccrs.getVertexFormat();
    }

    @Override
    public void setTexture(TextureAtlasSprite texture) {
        ccrs.sprite = texture;
    }

    @Override
    public void put(int e, float... data) {
        VertexFormat format = getVertexFormat();

        VertexFormatElement fmte = format.getElement(e);
        switch (fmte.getUsage()) {
            case POSITION:
                ccrs.vert.vec.set(data).add(offset);
                break;
            case UV:
                if (fmte.getIndex() == 0) {
                    ccrs.vert.uv.set(data[0], data[1]);
                } else {
                    ccrs.brightness = (int) (data[1] * 0xFFFF / 2) << 16 | (int) (data[0] * 0xFFFF / 2);
                }
                break;
            case COLOR:
                ccrs.colour = Colour.packRGBA(data);
                break;
            case NORMAL:
                ccrs.normal.set(data);
                break;
            case PADDING:
                break;
            default:
                throw new UnsupportedOperationException("Generic vertex format element");
        }
        if (e == format.getElementCount() - 1) {
            ccrs.writeVert();
        }
    }

    public void setOffset(Vector3 offset) {
        this.offset = offset;
    }

    public void setOffset(BlockPos offset) {
        this.offset = Vector3.fromBlockPos(offset);
    }

    @Override
    public void setQuadTint(int tint) {
    }

    @Override
    public void setQuadOrientation(EnumFacing orientation) {
    }

    @Override
    public void setApplyDiffuseLighting(boolean diffuse) {
    }
}
