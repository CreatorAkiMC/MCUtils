package com.aki.mcutils.mixin;

import com.aki.mcutils.MCUtils;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.MCVersion(ForgeVersion.mcVersion)
@IFMLLoadingPlugin.SortingIndex(12000)
@IFMLLoadingPlugin.TransformerExclusions({"com.aki.mcutils.asm", "com.aki.mcutils.mixin.MCUtilsLoadPlugins"})
public class MCUtilsLoadPlugins implements IFMLLoadingPlugin {
    public MCUtilsLoadPlugins() {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins." + MCUtils.MOD_ID + ".json");
    }

    @Override
    public String[] getASMTransformerClass() { return new String[] {"com.aki.mcutils.asm.MCUtilsASMTransformer"}; }

    @Override
    public String getModContainerClass() { return null; }

    @Nullable
    @Override
    public String getSetupClass() { return null; }

    @Override
    public void injectData(Map<String,Object> data) {}

    @Override
    public String getAccessTransformerClass() { return null; }
}
