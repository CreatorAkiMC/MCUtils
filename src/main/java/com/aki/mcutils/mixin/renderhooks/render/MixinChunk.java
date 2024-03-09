package com.aki.mcutils.mixin.renderhooks.render;

import com.aki.mcutils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(value = Chunk.class, priority = MCUtils.ModPriority)
public class MixinChunk {
    @Shadow
    private World world;
    @Shadow
    private Map<BlockPos, TileEntity> tileEntities;

    @Inject(method = "getTileEntity", cancellable = true, at = @At("HEAD"))
    public void getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType creationMode, CallbackInfoReturnable<TileEntity> info) {
        if (world.isRemote && !Minecraft.getMinecraft().isCallingFromMinecraftThread()) {
            TileEntity tileentity = tileEntities.get(pos);
            info.setReturnValue(tileentity != null && !tileentity.isInvalid() ? tileentity : null);
        }
    }
}
