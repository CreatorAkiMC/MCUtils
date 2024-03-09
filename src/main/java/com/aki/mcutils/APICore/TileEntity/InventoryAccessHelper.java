package com.aki.mcutils.APICore.TileEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class InventoryAccessHelper {
    public IItemHandler handler;

    /**
     * 下に入れるときは、EnumFacing.down
     * 上に入れるときは、EnumFacing.up
     * */
    public ItemStack InsertItem(ItemStack stack, World world, BlockPos pos, EnumFacing facing) {
        //EnumFacing facing = EnumFacing.random(world.rand);
        TileEntity entity = world.getTileEntity(pos.offset(facing));
        this.handler = null;
        if(stack != null && entity != null) {
            if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
                this.handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                if(handler != null)
                    for (int j = 0; j < this.handler.getSlots(); j++) {
                        ItemStack left = this.handler.insertItem(j, stack, true);
                        if(!ItemStack.areItemStacksEqual(new ItemStack(stack.getItem(), 1, stack.getMetadata()), new ItemStack(left.getItem(), 1, left.getMetadata()))){
                            left = this.handler.insertItem(j, stack, false);
                            return left;
                        }
                    }
            }
        }
        return stack;
    }

    public ItemStack ExtractItem(World world, BlockPos pos, ItemStack filter, EnumFacing facing) {
        //EnumFacing facing = EnumFacing.random(world.rand);
        TileEntity entity = world.getTileEntity(pos.offset(facing));
        this.handler = null;
        if(entity != null) {
            if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
                this.handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                if(handler != null)
                    for (int j = 0; j < this.handler.getSlots(); j++) {
                        ItemStack left = this.handler.extractItem(j, this.handler.getStackInSlot(j).getCount(), true);
                        if(!ItemStack.areItemStacksEqual(new ItemStack(filter.getItem(), 1, filter.getMetadata()), new ItemStack(left.getItem(), 1, left.getMetadata()))){
                            left = this.handler.extractItem(j, this.handler.getStackInSlot(j).getCount(), false);
                            return left;
                        }
                    }
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack ExtractItem(World world, BlockPos pos, EnumFacing facing) {
        //EnumFacing facing = EnumFacing.random(world.rand);
        TileEntity entity = world.getTileEntity(pos.offset(facing));
        this.handler = null;
        if(entity != null) {
            if(entity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
                this.handler = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
                if(handler != null)
                    for (int j = 0; j < this.handler.getSlots(); j++) {
                        ItemStack left = this.handler.extractItem(j, this.handler.getStackInSlot(j).getCount(), true);
                        if(!ItemStack.areItemStacksEqual(new ItemStack(this.handler.getStackInSlot(j).getItem(), 1, this.handler.getStackInSlot(j).getMetadata()), new ItemStack(left.getItem(), 1, left.getMetadata()))){
                            left = this.handler.extractItem(j, this.handler.getStackInSlot(j).getCount(), false);
                            return left;
                        }
                    }
            }
        }
        return ItemStack.EMPTY;
    }
}
