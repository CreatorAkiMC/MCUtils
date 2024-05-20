package com.aki.mcutils;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;

public class MCUtilsConfig {
    public static Configuration cfg;

    public static String category = "mod";

    public static int tileEntityCachedBoundingBoxUpdateInterval = 100;
    public static boolean tileEntityCachedBoundingBoxEnabled = true;
    public static List<ResourceLocation> tileEntityCachedBoundingBoxBlacklistImpl = new ArrayList<>();

    public static float EntityRenderDistDivide = 1.0F;

    public static long OneTickNanoBase = 50000000;// -> 50ms (1/20_1Tick)
    //public static boolean show_debug_bounding_box = false;

    public static void PreInit(FMLPreInitializationEvent event) {
        cfg = new Configuration(event.getSuggestedConfigurationFile());

        SyncConfig();
    }

    public static void SettingConfig() {
        if(cfg != null) {
            cfg.load();
            cfg.addCustomCategoryComment(category, "Mod Settings");
            cfg.setCategoryRequiresMcRestart(category, false);
        }
    }

    public static void SyncConfig() {
        SettingConfig();
        tileEntityCachedBoundingBoxUpdateInterval = cfg.getInt("AABBUpdateInterval", category, 100, 1, 100000, "Frequency of calculating hit detection of TileEntity");
        tileEntityCachedBoundingBoxEnabled = cfg.getBoolean("CachedBoundingBox", category, true, "Most tile entities have static bounding boxes and thus they can be cached. Tile entities whose bounding boxes are likely to change every frame or so should be added to the blacklist. Tile entities whose bounding only change every once in a while should be covered by cache updates (update speed adjustable through tileEntityCachedBoundingBoxUpdateInterval)");
        OneTickNanoBase = cfg.getInt("OneTickNanoBase", category, 50000000, 0, Integer.MAX_VALUE, "This is the standard for measuring Update Speed. [Unit Nano]");
        for(String tile : cfg.getStringList("TileEntityBlackList", category, new String[]{}, "TileEntity Cached Bounding Box BlackList")) {
            String[] SplitTile = tile.split(":");
            ResourceLocation resourceLocation = tile.contains(":") ? new ResourceLocation(SplitTile[0], SplitTile[1]) : new ResourceLocation(tile);
            tileEntityCachedBoundingBoxBlacklistImpl.add(resourceLocation);
        }
        EntityRenderDistDivide = cfg.getFloat("Divide_N", category, 1.5F, 1.0F, 1000.0F, "Distance at which the entity will be drawn. dist = [16 * ChunkRenderDist / Divide_N]");
        //show_debug_bounding_box = cfg.getBoolean("Show_Bounding_Box", category, false, "Entity and TileEntity bounding box");
        cfg.save();
    }
}
