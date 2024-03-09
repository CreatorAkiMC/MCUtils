package com.aki.mcutils.APICore.Code;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EqualsUtils {
    public boolean MatchBlock(Block block1, Block block2) {
        if(block1.getRegistryName() == block2.getRegistryName() && block1.getRegistryType() == block2.getRegistryType()) {
            if (ItemStack.areItemStackShareTagsEqual(new ItemStack(block1, 1), new ItemStack(block2))) {
                return true;
            }
        }
        return false;
    }

    public boolean MatchBlock(Item item1, Item item2) {
        if(item1.getRegistryName() == item2.getRegistryName() && item1.getRegistryType() == item2.getRegistryType()) {
            if (ItemStack.areItemStackShareTagsEqual(new ItemStack(item1, 1), new ItemStack(item2))) {
                return true;
            }
        }
        return false;
    }
}
