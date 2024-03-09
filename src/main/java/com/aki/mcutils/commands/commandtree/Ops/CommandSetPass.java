package com.aki.mcutils.commands.commandtree.Ops;

import com.aki.mcutils.MCUtils;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandSetPass extends CommandBase {
    @Override
    public String getName() {
        return "setpass";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.mcu.pass_op.setpass.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        /**
         * (/pass_op)を含めない。
         * */
        if(args.length > 0) {
            //op---deop
            Entity entity = getEntity(server, sender, args[0]);
            if(entity instanceof EntityPlayer) {
                if(!(args[1] == null || args[1] == "" || args[1] == " ")) {
                    NBTTagCompound tagCompound = entity.writeToNBT(new NBTTagCompound());
                    tagCompound.setString("OPPass", args[1]);
                    entity.readFromNBT(tagCompound);
                }
                /*GameProfile gameprofile = server.getPlayerList().getOppedPlayers().getGameProfileFromName(args[0]);
                if(gameprofile != null) {
                    if (CheckS3Code(sender, args[1], entity)) {
                        server.getPlayerList().removeOp(gameprofile);
                        notifyCommandListener(sender, this, "commands.deop.success", new Object[] {args[1]});
                    }
                }*/
            }
        }
    }

    /**
     * (/mcu pass_op) <op/deop/setpass>) <Entity> <password>
     * */
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if(args.length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getPlayerList().getOppedPlayerNames());
        } else if (args.length == 2){
            return Lists.newArrayList(args[1]);
        }
        return Collections.emptyList();//super.getTabCompletions(server, sender, args, targetPos);
    }

    /*public boolean CheckS3Code(ICommandSender sender, String s3, Entity entity) {
        if(entity instanceof EntityPlayer) {
            sender.sendMessage(new TextComponentString("DefaultPassWord-> " + MCUtils.Default_DOPPassWord));
            if(s3 == null || s3 == "" || s3 == " ") {
                sender.sendMessage(new TextComponentString("Please enter your password."));
            } else if(((EntityPlayer)entity).writeToNBT(new NBTTagCompound()).hasKey("OPPass") && ((EntityPlayer)entity).writeToNBT(new NBTTagCompound()).getString("OPPass").equals(s3)) {
                return true;
            }
        }
        return false;
    }*/
}
