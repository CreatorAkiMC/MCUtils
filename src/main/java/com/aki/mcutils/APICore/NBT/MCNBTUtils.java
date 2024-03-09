package com.aki.mcutils.APICore.NBT;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class MCNBTUtils {
    public static NBTTagCompound saveAllItems(NBTTagCompound tag, List<ItemStack> list, boolean saveEmpty) {
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < list.size(); i++)
        {
            if (!list.get(i).isEmpty())
            {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setInteger("Slot", i);
                list.get(i).writeToNBT(itemTag);
                nbtTagList.appendTag(itemTag);
            }
        }
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("Items", nbtTagList);
        nbt.setInteger("Size", list.size());

        return tag;
    }

    public static List<ItemStack> loadAllItems(NBTTagCompound nbt) {
        List<ItemStack> stackList = new ArrayList<>();
        for(int i = 0; i < nbt.getInteger("Size"); i++) {
            stackList.add(ItemStack.EMPTY);
        }
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound itemTags = tagList.getCompoundTagAt(i);
            int slot = itemTags.getInteger("Slot");

            if (slot >= 0 && slot < stackList.size())
            {
                stackList.set(slot, new ItemStack(itemTags));
            }
        }
        return stackList;
    }
}
