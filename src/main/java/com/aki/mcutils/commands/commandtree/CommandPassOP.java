package com.aki.mcutils.commands.commandtree;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.commands.commandtree.Ops.CommandPassDeOp;
import com.aki.mcutils.commands.commandtree.Ops.CommandSetPass;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandEntityData;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * もし、間違えてDeOPを実行してしまったときに、パスワードを打ち込めばOPを取り戻せるコマンド。
 * (Pass->Default:minecraft)
 * PlayerのNBTに保存("OPPass", "DefPass")
 * */
public class CommandPassOP extends CommandTreeBase {
    public CommandPassOP() {
        super.addSubcommand(new CommandPassDeOp());
        super.addSubcommand(new CommandSetPass());
        super.addSubcommand(new CommandTreeHelp(this));
    }

    @Override
    public String getName() {
        return "pass_op";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.mcu.pass_op.usage";
    }



    /**
     * (/mcu pass_op) <op/deop/setpass> <Entity/password> <password>
     * */
    /*@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        String s;
        if(args.length > 2) {
            if(args[0].toLowerCase().equals("setpass")) {

            } else {

            }
        }
        return Collections.emptyList();//super.getTabCompletions(server, sender, args, targetPos);
    }

    public boolean CheckS3Code(ICommandSender sender, String s3, Entity entity) {
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
