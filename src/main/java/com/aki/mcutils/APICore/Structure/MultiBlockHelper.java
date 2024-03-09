package com.aki.mcutils.APICore.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiBlockHelper {
    /**
     * pos==中心座標
     * 例:
     * ReplaceCode==["nnn.add(new BlockPos(#1,#2,#3), #4)" -> "nnn.add(new BlockPos(0, 10, 2), stone)"]のように#nを対応する番号と置き換える。
     * IsAirToRemove==空気を含むか(構造として、取り入れるか)
     * */
    public static String getMultiBlockCode(World world, BlockPos center, BlockPos pos1, BlockPos pos2, String ReplaceCode, boolean Air) {
        String s = "";
        BlockPos MinPos = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        BlockPos MaxPos = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
        for(int z = MinPos.getZ(); z <= MaxPos.getZ(); z++) {
            for(int y = MinPos.getY(); y <= MaxPos.getY(); y++) {
                for(int x = MinPos.getX(); x <= MaxPos.getX(); x++) {
                    BlockPos pos = new BlockPos(x,y,z).subtract(center);
                    Block block = world.getBlockState(new BlockPos(x,y,z)).getBlock();
                    if(Air || (world.getBlockState(new BlockPos(x,y,z)).getMaterial() != Material.AIR))
                        s += ReplaceCode.replace("#1", String.valueOf(pos.getX())).replace("#2", String.valueOf(pos.getY())).replace("#3", String.valueOf(pos.getZ())).replace("#4", block.getRegistryName().getPath()) + "\n";
                }
            }
        }
        return s;
    }
}
