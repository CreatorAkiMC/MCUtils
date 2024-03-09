package com.aki.mcutils.APICore.Registry;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class GetBlockItemsRegistry {
    public static List<Item> getItemList() {
        List<Item> itemList = new ArrayList<>();
        for(Item item : Item.REGISTRY) {
            itemList.add(item);
        }
        return itemList;
    }

    public static List<Block> getBlockList() {
        List<Block> itemList = new ArrayList<>();
        for(Block item : Block.REGISTRY) {
            itemList.add(item);
        }
        return itemList;
    }
}
