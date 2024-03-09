package com.aki.mcutils.commands.commandtree;

import com.google.common.collect.Multimap;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandGetDebugItems extends CommandBase {

    @Override
    public String getName() {
        return "get_debug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.mcu.get_debug.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayer entityplayer = null;
        if(args.length > 0) {
            entityplayer = getPlayer(server, sender, args[0]);
            for (ItemStack itemstack : getDebugItems()) {
                boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);
                if (flag) {
                    entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    entityplayer.inventoryContainer.detectAndSendChanges();
                }
                if (flag && itemstack.isEmpty()) {
                    itemstack.setCount(1);
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                    EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);

                    if (entityitem1 != null) {
                        entityitem1.makeFakeItem();
                    }
                } else {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1 - itemstack.getCount());
                    EntityItem entityitem = entityplayer.dropItem(itemstack, false);

                    if (entityitem != null) {
                        entityitem.setNoPickupDelay();
                        entityitem.setOwner(entityplayer.getName());
                    }
                }
            }
        } else if(sender.getCommandSenderEntity() instanceof EntityPlayer) {
            entityplayer = (EntityPlayer) sender.getCommandSenderEntity();
            for (ItemStack itemstack : getDebugItems()) {
                boolean flag = entityplayer.inventory.addItemStackToInventory(itemstack);
                if (flag) {
                    entityplayer.world.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((entityplayer.getRNG().nextFloat() - entityplayer.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    entityplayer.inventoryContainer.detectAndSendChanges();
                }
                if (flag && itemstack.isEmpty()) {
                    itemstack.setCount(1);
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
                    EntityItem entityitem1 = entityplayer.dropItem(itemstack, false);

                    if (entityitem1 != null) {
                        entityitem1.makeFakeItem();
                    }
                } else {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1 - itemstack.getCount());
                    EntityItem entityitem = entityplayer.dropItem(itemstack, false);

                    if (entityitem != null) {
                        entityitem.setNoPickupDelay();
                        entityitem.setOwner(entityplayer.getName());
                    }
                }
            }
        }
    }

    public List<ItemStack> getDebugItems() {
        List<ItemStack> stackList = new ArrayList<>();
        stackList.add(new ItemStack(Blocks.COMMAND_BLOCK, 1));
        stackList.add(new ItemStack(Blocks.CHAIN_COMMAND_BLOCK, 1));
        stackList.add(new ItemStack(Blocks.REPEATING_COMMAND_BLOCK, 1));
        stackList.add(new ItemStack(Blocks.STRUCTURE_BLOCK, 1));


        ItemStack sword = BaseEnchantement(new ItemStack(Items.DIAMOND_SWORD, 1));
        ItemStack pickaxe = BaseEnchantement(new ItemStack(Items.DIAMOND_PICKAXE, 1));
        ItemStack head = BaseEnchantement(new ItemStack(Items.DIAMOND_HELMET, 1));
        ItemStack chest = BaseEnchantement(new ItemStack(Items.DIAMOND_CHESTPLATE, 1));
        ItemStack leg = BaseEnchantement(new ItemStack(Items.DIAMOND_LEGGINGS, 1));
        ItemStack boots = BaseEnchantement(new ItemStack(Items.DIAMOND_BOOTS, 1));
        ItemStack bow = BaseEnchantement(new ItemStack(Items.BOW, 1));
        ItemStack shield = BaseEnchantement(new ItemStack(Items.SHIELD, 1));


        head.addEnchantment(Enchantments.RESPIRATION, 100);
        head.addEnchantment(Enchantments.BLAST_PROTECTION, 100);
        head.addEnchantment(Enchantments.PROTECTION, 100);
        head.addEnchantment(Enchantments.FIRE_PROTECTION, 100);
        head.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 100);


        chest.addEnchantment(Enchantments.BLAST_PROTECTION, 100);
        chest.addEnchantment(Enchantments.PROTECTION, 100);
        chest.addEnchantment(Enchantments.FIRE_PROTECTION, 100);
        chest.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 100);


        leg.addEnchantment(Enchantments.DEPTH_STRIDER, 10);
        leg.addEnchantment(Enchantments.BLAST_PROTECTION, 100);
        leg.addEnchantment(Enchantments.PROTECTION, 100);
        leg.addEnchantment(Enchantments.FIRE_PROTECTION, 100);
        leg.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 100);


        boots.addEnchantment(Enchantments.DEPTH_STRIDER, 10);
        boots.addEnchantment(Enchantments.BLAST_PROTECTION, 100);
        boots.addEnchantment(Enchantments.PROTECTION, 100);
        boots.addEnchantment(Enchantments.FIRE_PROTECTION, 100);
        boots.addEnchantment(Enchantments.PROJECTILE_PROTECTION, 100);
        boots.addEnchantment(Enchantments.FEATHER_FALLING, 100);


        pickaxe.addEnchantment(Enchantments.EFFICIENCY, 100);
        pickaxe.addEnchantment(Enchantments.FORTUNE, 5);
        pickaxe.addEnchantment(Enchantments.AQUA_AFFINITY, 10);

        sword.addEnchantment(Enchantments.FIRE_ASPECT, 1);
        sword.addEnchantment(Enchantments.SHARPNESS, 100);
        sword.addEnchantment(Enchantments.KNOCKBACK, 5);
        sword.addEnchantment(Enchantments.SWEEPING, 100);

        bow.addEnchantment(Enchantments.FLAME, 1);
        bow.addEnchantment(Enchantments.POWER, 100);
        bow.addEnchantment(Enchantments.PUNCH, 5);
        bow.addEnchantment(Enchantments.INFINITY, 1);

        stackList.add(sword);
        stackList.add(pickaxe);
        stackList.add(shield);
        stackList.add(bow);


        stackList.add(head);
        stackList.add(chest);
        stackList.add(leg);
        stackList.add(boots);



        stackList.add(new ItemStack(Blocks.BARRIER, 1));
        stackList.add(new ItemStack(Blocks.STRUCTURE_VOID, 1));

        stackList.add(new ItemStack(Items.ARMOR_STAND, 1));
        return stackList;
    }

    public ItemStack BaseEnchantement(ItemStack stack) {
        Item item = stack.getItem();
        //item.setNoRepair();
        stack = new ItemStack(item, 1);
        stack.addEnchantment(Enchantments.UNBREAKING, 100);
        stack.addEnchantment(Enchantments.MENDING, 5);
        stack.addEnchantment(Enchantments.LOOTING, 5);
        return stack;
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        return Collections.emptyList();
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}
