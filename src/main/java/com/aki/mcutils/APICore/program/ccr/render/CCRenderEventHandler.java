package com.aki.mcutils.APICore.program.ccr.render;

import com.aki.mcutils.APICore.Utils.raytracer.CuboidRayTraceResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CCRenderEventHandler {

    public static int renderTime;
    public static float renderFrame;

    private static boolean hasInit = false;

    public static void init() {
        if (!hasInit) {
            MinecraftForge.EVENT_BUS.register(new CCRenderEventHandler());
            hasInit = true;
        }
    }

    @SubscribeEvent
    public void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == Phase.END) {
            renderTime++;
        }
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == Phase.START) {
            renderFrame = event.renderTickTime;
        }
    }

    @SideOnly (Side.CLIENT)
    @SubscribeEvent (priority = EventPriority.LOW)
    public void onBlockHighlight(DrawBlockHighlightEvent event) {
        BlockPos pos = event.getTarget().getBlockPos();

        //We have found a CuboidRayTraceResult, Lets render it properly..
        RayTraceResult hit = event.getTarget();
        if (hit.typeOfHit == Type.BLOCK && hit instanceof CuboidRayTraceResult) {
            event.setCanceled(true);
            RenderUtils.renderHitBox(event.getPlayer(), ((CuboidRayTraceResult) event.getTarget()).cuboid6.copy().add(pos), event.getPartialTicks());
        }
    }
}
