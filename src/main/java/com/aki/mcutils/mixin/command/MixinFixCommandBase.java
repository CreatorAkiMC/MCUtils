package com.aki.mcutils.mixin.command;

import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CommandBase.class)
public class MixinFixCommandBase {
    /**
     * @author Aki
     * @reason Replaced TextToItem -> StringToItem
     * */
    @Overwrite
    public static Item getItemByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        Item item = null;
        ResourceLocation resourcelocation = new ResourceLocation(id);
        if(!id.matches("[+-]?\\d*(\\.\\d+)?")) {
            item = Item.REGISTRY.getObject(resourcelocation);
        } else {
            item = Item.REGISTRY.getObjectById(Integer.parseInt(id));
            resourcelocation = item.getRegistryName();
        }

        if (item == null)
        {
            throw new NumberInvalidException("commands.give.item.notFound", new Object[] {resourcelocation});
        }
        else
        {
            return item;
        }
    }

    /**
     * @author Aki
     * @reason Replaced TextToBlock -> StringToBlock
     * */
    @Overwrite
    public static Block getBlockByText(ICommandSender sender, String id) throws NumberInvalidException
    {
        ResourceLocation resourcelocation = new ResourceLocation(id);
        Block block = null;

        if(!id.matches("[+-]?\\d*(\\.\\d+)?")) {
            if(Block.REGISTRY.containsKey(resourcelocation))
                block = Block.REGISTRY.getObject(resourcelocation);
        } else {
            block = Block.REGISTRY.getObjectById(Integer.parseInt(id));
            resourcelocation = block.getRegistryName();
        }

        if (block == null)
        {
            throw new NumberInvalidException("commands.give.block.notFound", new Object[] {resourcelocation});
        }
        else
        {
            return block;
        }
    }
}
