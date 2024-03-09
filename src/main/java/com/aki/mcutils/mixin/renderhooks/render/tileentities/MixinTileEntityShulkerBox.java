package com.aki.mcutils.mixin.renderhooks.render.tileentities;

import com.aki.mcutils.MCUtils;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = TileEntityShulkerBox.class, priority = MCUtils.ModPriority)
public class MixinTileEntityShulkerBox extends TileEntity {

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        BlockPos pos = this.getPos();
        double x0 = pos.getX();
        double y0 = pos.getY();
        double z0 = pos.getZ();
        double x1 = pos.getX() + 1.0D;
        double y1 = pos.getY() + 1.0D;
        double z1 = pos.getZ() + 1.0D;
        if (this.hasWorld()) {
            World world = this.getWorld();
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockShulkerBox) {
                EnumFacing facing = state.getValue(BlockShulkerBox.FACING);
                EnumFacing.Axis axis = facing.getAxis();
                if (axis != EnumFacing.Axis.X) {
                    x0 -= 0.25D;
                    x1 += 0.25D;
                }
                if (axis != EnumFacing.Axis.Y) {
                    y0 -= 0.25D;
                    y1 += 0.25D;
                }
                if (axis != EnumFacing.Axis.Z) {
                    z0 -= 0.25D;
                    z1 += 0.25D;
                }
                switch (facing) {
                    case DOWN:
                        y0 -= 0.5D;
                        break;
                    case UP:
                        y1 += 0.5D;
                        break;
                    case NORTH:
                        z0 -= 0.5D;
                        break;
                    case SOUTH:
                        z1 += 0.5D;
                        break;
                    case WEST:
                        x0 -= 0.5D;
                        break;
                    case EAST:
                        x1 += 0.5D;
                        break;
                }
            }
        }
        return new AxisAlignedBB(x0, y0, z0, x1, y1, z1);
    }

}
