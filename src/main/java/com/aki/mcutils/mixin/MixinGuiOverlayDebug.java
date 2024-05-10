package com.aki.mcutils.mixin;

import com.aki.mcutils.APICore.Utils.GuiDebugHelper;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.MCUtilsConfig;
import com.aki.mcutils.renderer.entity.EntityRendererManager;
import com.aki.mcutils.renderer.tileentity.TileEntityRendererManager;
import com.aki.mcutils.utils.InformationCollector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = GuiOverlayDebug.class, priority = MCUtils.ModPriority)
public class MixinGuiOverlayDebug {
    @Inject(method = "call", at = @At("RETURN"))
    public void AddModDataCall(CallbackInfoReturnable<List<String>> cir) {
        try {
            List<String> stringList = cir.getReturnValue();

            int TotalRendered = EntityRendererManager.renderedEntities() + TileEntityRendererManager.renderedTileEntities();
            int TotalOccluded = EntityRendererManager.occludedEntities() + TileEntityRendererManager.occludedTileEntities();
            int TotalSchedule = EntityRendererManager.totalEntities() + TileEntityRendererManager.totalTileEntities();

            stringList.add("MCUtils Render Entities: ");
            stringList.add("Total Rendered: " + TotalRendered + ", Occluded: " + TotalOccluded + ", Schedule: " + TotalSchedule);
            if(Minecraft.getMinecraft().player.isSneaking()) {
                stringList.add("Entity Rendered: " + EntityRendererManager.renderedEntities() + ", Occluded: " + EntityRendererManager.occludedEntities() + ", Schedule: " + EntityRendererManager.totalEntities());
                stringList.add("TileEntity Rendered: " + TileEntityRendererManager.renderedTileEntities() + ", Occluded: " + TileEntityRendererManager.occludedTileEntities() + ", Schedule: " + TileEntityRendererManager.totalTileEntities());
            }
            stringList.add("MCUtils TileEntity Tick Info (Update every second): ");
            stringList.add("  TickBase: " + MCUtilsConfig.OneTickNanoBase + "ns, " + "OneTickTime: " + InformationCollector.getOneTickTime() + ", LateTickTime: " + InformationCollector.getLateTime());
            stringList.add("  LateTileEntities: " + InformationCollector.getLateTileEntities() + ", MaxLateCycle: " + InformationCollector.getMaxLateCycle());
            stringList.add("Chunk Here");
            stringList.add("  TileEntities: " + InformationCollector.getPlayerChunkTiles());

            GuiDebugHelper.getMinecraftDebugReplaceConsumers().forEach(action -> action.accept(stringList));
            GuiDebugHelper.getStringList().forEach(stringList::addAll);
        } catch (Exception e) {
            MCUtils.logger.error("MCUtils Mixin Error: GuiOverlayDebug Data");
            GuiDebugHelper.getStringList().forEach(list -> list.forEach(MCUtils.logger::error));
            MCUtils.logger.error("MCUtils Mixin Error: Data End");
            Minecraft.getMinecraft().crashed(new CrashReport("MCUtils Mixin GuiOverlayDebug Error", e));
        }
        //cir.setReturnValue(stringList);
    }
}
