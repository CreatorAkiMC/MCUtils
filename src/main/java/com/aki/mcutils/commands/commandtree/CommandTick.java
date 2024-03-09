package com.aki.mcutils.commands.commandtree;

import com.aki.mcutils.MCUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHelp;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandTick extends CommandBase {
    @Override
    public String getName() {
        return "tick";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.mcu.tick.usage";
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }


    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    /**
     * Stepを実装してもいいかも
     * */
    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 0) {
            sender.sendMessage(new TextComponentString("EntityTick: " + MCUtils.EntityUpdateTick).setStyle(new Style().setColor(MCUtils.EntityUpdateTick >= MCUtils.BaseTick ? TextFormatting.AQUA : TextFormatting.RED)));
            sender.sendMessage(new TextComponentString("TileTick: " + MCUtils.TileUpdateTick).setStyle(new Style().setColor(MCUtils.TileUpdateTick >= MCUtils.BaseTick ? TextFormatting.AQUA : TextFormatting.RED)));
            sender.sendMessage(new TextComponentString("BaseTick: " + MCUtils.TileUpdateTick).setStyle(new Style().setColor(TextFormatting.GOLD)));
        } else {
            String arg0 = args[0];
            if(arg0.equalsIgnoreCase("reset")) {// /mcu tick reset
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("entity")) {// /mcu tick reset entity
                        MCUtils.EntityUpdateTick = MCUtils.BaseTick;
                        sender.sendMessage(new TextComponentString("The entity tick has been reset to " + MCUtils.BaseTick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                    } else if(args[1].equalsIgnoreCase("tile")) {// /mcu tick reset tile
                        MCUtils.TileUpdateTick = MCUtils.BaseTick;
                        sender.sendMessage(new TextComponentString("The tile tick has been reset to " + MCUtils.BaseTick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                    } else {// /mcu tick reset ?
                        sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                    }
                } else if(args.length == 1) {
                    MCUtils.EntityUpdateTick = MCUtils.BaseTick;
                    MCUtils.TileUpdateTick = MCUtils.BaseTick;
                    sender.sendMessage(new TextComponentString("The entity tick and the tile tick has been reset to " + MCUtils.BaseTick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                } else {
                    sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            } else if(arg0.equalsIgnoreCase("set")) {
                if(args.length > 1) {// 2
                    String arg1 = args[1];
                    try {
                        double tick = Double.parseDouble(arg1);
                        MCUtils.EntityUpdateTick = tick;
                        MCUtils.TileUpdateTick = tick;
                        sender.sendMessage(new TextComponentString("The entity tick and the tile tick has been set to " + tick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                    } catch (NumberFormatException e) {
                        if(args.length == 3) {
                            if (arg1.equalsIgnoreCase("entity")) {
                                try {
                                    double tick = Double.parseDouble(args[2]);
                                    MCUtils.EntityUpdateTick = tick;
                                    sender.sendMessage(new TextComponentString("The entity tick has been set to " + tick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                                } catch (NumberFormatException ignored) {

                                }
                            } else if (arg1.equalsIgnoreCase("tile")) {
                                try {
                                    double tick = Double.parseDouble(args[2]);
                                    MCUtils.TileUpdateTick = tick;
                                    sender.sendMessage(new TextComponentString("The tile tick has been set to " + tick +".").setStyle(new Style().setColor(TextFormatting.AQUA)));
                                } catch (NumberFormatException ignored) {

                                }
                            } else {
                                //tile でも entity でも無い。
                                sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                            }
                        } else {
                            //字数が多かったり、足りない。
                            sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                    }
                } else {
                    sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            } else {
                sender.sendMessage(new TextComponentString("Unknown error").setStyle(new Style().setColor(TextFormatting.RED)));
            }
        }
    }
}
