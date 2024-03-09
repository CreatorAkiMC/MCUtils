package com.aki.mcutils.APICore.Structure;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPosData {
    public BlockPos pos = BlockPos.ORIGIN;
    public IBlockState state;
    public NBTTagCompound StateNBT = new NBTTagCompound();

    /**
     * World.setBlockState()をした後は、必ずSetTileEntityDataを実行する。
     * */
    public BlockPosData(BlockPos pos, IBlockState state, NBTTagCompound StateNBT) {
        this.pos = pos;
        this.state = state;
        this.StateNBT  = StateNBT;
    }

    /**
     * そのブロックにエンティティデータがない(null)の場合false;
     * あって、ブロックに適用できた場合true。
     * pos = 空間座標(0基準)、pos1=座標
     * */
    public boolean SetTileEntityData(World world, BlockPos pos1) {
        TileEntity entity = world.getTileEntity(pos1);
        //System.out.print("bbb: " + entity.getTileData().getString());
        /**
         * tileentityのreadが機能していない
         * */
        if(entity != null) {
            if (entity instanceof TileEntityLockableLoot) {
                StateNBT.setLong("LootTableSeed", world.rand.nextLong());
            }
            /**
             * ccc: {BlockState:{Properties:{waterlogged:"false",facing:"west",type:"single"},Name:"minecraft:chest"},Items:[{Slot:13b,id:"minecraft:observer",Count:1b}],BlockPos:[{SaveBasePosZ:0,SaveBasePosX:0,SaveBasePosY:0}],id:"minecraft:chest",LootTableSeed:491385203217690091L}
             * */

            entity.readFromNBT(StateNBT);
            //System.out.print("ccc: " + ((CompoundNBT)StateNBT).getString() + " entity: " + entity.getTileData().getString());
            //System.out.print("aaa");

            /*BlockState blockstate1 = world.getBlockState(pos1);
            BlockState blockstate3 = Block.(blockstate1, world, pos1);
            if (blockstate1 != blockstate3) {
                world.setBlockState(pos1, blockstate3, 2 & -2 | 16);
            }

            world.updateBlock(pos1, blockstate3.getBlock());*/
            /*TileEntity entity1 = world.getTileEntity(pos1);
            if(entity1 != null) {
                entity1.markDirty();
            }*/

            world.setTileEntity(pos1, entity);
            return true;
        }
        //System.out.print("ccc: " + entity.getTileData().getString());
        return false;
    }

    /**
     *
     * [,]は、一ブロックの情報の区切り。
     * [.]は、情報の区切り。
     *
     * */
    /*public String getWritenableDataMake(TileEntity tile) {
        String data = ",\n{";
        if(this.pos != null && this.state != null) {
            int x = this.pos.getX();
            int y = this.pos.getY();
            int z = this.pos.getZ();
            if(this.NBT != null) {
                this.NBT.write();
            }
            //StructureBlockTileEntity
            //FurnaceBlock
            //List<Property<?>> properties = this.state.getProperties().stream().collect(Collectors.toList());
            /*for(Property<> property : properties) {
                if(property.getName().toLowerCase(Locale.ROOT).equals("facing".toLowerCase(Locale.ROOT))) {
                    property.getPairWithValue()
                }
            }*/

            /*StructureBlockTileEntity
            if (tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()) {
                IItemHandler iItemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
                iItemHandler.
            }
        }
        return data + "\n" + "}";
    }*/
}
