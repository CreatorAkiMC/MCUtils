package com.aki.mcutils.APICore.Handlers;

import com.aki.mcutils.APICore.Utils.IProcessMethod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class ProcessHandler {
    public static LinkedList<IProcessMethod> processMethods = new LinkedList<>();
    public static LinkedList<IProcessMethod> NewprocessMethods = new LinkedList<>();

    @SubscribeEvent
    public void OnServerTick(TickEvent.ServerTickEvent tickEvent) {
        if (tickEvent.phase == TickEvent.Phase.START) {
            Iterator<IProcessMethod> i = processMethods.iterator();

            while (i.hasNext()) {
                IProcessMethod method = i.next();
                if (method.IsDeadProcess()) {
                    i.remove();
                }
                else {
                    method.ServerUpdateProcess();
                }
            }
        }
    }

    @SubscribeEvent
    public void OnClientTick(TickEvent.ClientTickEvent tickEvent) {
        if (tickEvent.phase == TickEvent.Phase.START) {
            Iterator<IProcessMethod> i = processMethods.iterator();

            while (i.hasNext()) {
                IProcessMethod method = i.next();
                if (method.IsDeadProcess()) {
                    i.remove();
                }
                else {
                    method.ClientUpdateProcess();
                }
            }
            if(!NewprocessMethods.isEmpty()) {
                processMethods.addAll(NewprocessMethods);
                NewprocessMethods.clear();
            }
        }
    }

    public static void addProcessEvent(IProcessMethod method) {
        NewprocessMethods.add(method);
    }
}
