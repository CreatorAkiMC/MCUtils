package com.aki.mcutils.renderer.tileentity;

import com.aki.mcutils.asm.Optifine;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRendererOptifine extends TileEntityRenderer {

    @Override
    protected void preRenderTileEntity(TileEntity tileEntity) {
        if (Optifine.isShaders()) {
            Optifine.nextBlockEntity(tileEntity);
        }
        super.preRenderTileEntity(tileEntity);
    }

}
