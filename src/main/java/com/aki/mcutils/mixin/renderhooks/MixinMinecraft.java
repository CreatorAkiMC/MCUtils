package com.aki.mcutils.mixin.renderhooks;

import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.MCUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Minecraft.class, priority = MCUtils.ModPriority)
public class MixinMinecraft {
    @Shadow private boolean isGamePaused;

    @Shadow
    public float renderPartialTicksPaused;

    @Shadow @Final private Timer timer;

    @Shadow public WorldClient world;

    @ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickStart(F)V", remap = false))
    public float onRenderTickStart(float partialTick) {
        return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V", args = "ldc=gameRenderer"))
    public void runGameLoop(CallbackInfo info) {
        GLUtils.update(isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks);
    }

    @ModifyArg(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;onRenderTickEnd(F)V", remap = false))
    public float onRenderTickEnd(float partialTick) {
        return isGamePaused ? renderPartialTicksPaused : timer.renderPartialTicks;
    }

    @Inject(method = "isFramerateLimitBelowMax", cancellable = true, at = @At("HEAD"))
    public void isFramerateLimitBelowMax(CallbackInfoReturnable<Boolean> info) {
        if (world == null) {
            info.setReturnValue(true);
        }
    }

    @Redirect(method = "launchIntegratedServer", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;sleep(J)V"))
    public void launchIntegratedServer_sleep(long millis) {
        Display.sync(20);
    }
}