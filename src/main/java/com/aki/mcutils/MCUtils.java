package com.aki.mcutils;

import com.aki.mcutils.APICore.Handlers.ProcessHandler;
import com.aki.mcutils.APICore.Utils.OreDictHelper;
import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.commands.MCUtilsCommand;
import com.aki.mcutils.debug.DebugDataWriter;
import com.aki.mcutils.debug.MCDataCollector;
import com.aki.mcutils.proxy.ModProxy;
import com.aki.mcutils.threads.MCUtilsThreadManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Mod(
        modid = MCUtils.MOD_ID,
        name = MCUtils.MOD_NAME,
        version = MCUtils.VERSION
)
public class MCUtils {

    public static final String MOD_ID = "mcutils";
    public static final String MOD_NAME = "mcutils";
    public static final String VERSION = "v3.8-1.12.2";

    public static final int ModPriority = 2389;

    /**
     * n(Path) + /
     * */
    public static String SaveStructurePath = "";
    public static String minecraftPath = "";
    public static String McUtilsPath = "";

    public static String Default_DOPPassWord = "minecraft";

    public static TextureAtlasSprite laserIcon;

    public static DebugDataWriter debugDataWriter = new DebugDataWriter();

    public List<Integer> Month = new ArrayList<>();
    public List<Integer> Day = new ArrayList<>();

    public static int OldMonth = 0;
    public static int OldDay = 0;

    public String[] FileNames = new String[] {"Debug#.txt", "Blocks#.txt", "Items#.txt", "Entities#.txt", "OreDicts#.txt", "Mods#.txt"};

    public static boolean isFairyLightsInstalled;
    public static boolean isValkyrienSkiesInstalled;
    public static boolean isRenderLib;

    //割合を使って処理
    //整数になった時にforを回す。
    //21の時は
    public static double EntityUpdateTick = 20.0d;
    public static double TileUpdateTick = 20.0d;
    public static final double BaseTick = 20.0d;

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static MCUtils INSTANCE;

    public static Logger logger;

    @SidedProxy(clientSide = "com.aki.mcutils.proxy.ClientProxy", serverSide = "com.aki.mcutils.proxy.CommonProxy")
    public static ModProxy proxy;


    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        isRenderLib = Loader.isModLoaded("renderlib");
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        // コンフィグが変更された時に呼ばれる。
        if (event.getModID().equals(MCUtils.MOD_ID)) {
            ConfigManager.sync(MOD_ID, Config.Type.INSTANCE);
            MCUtilsConfig.SyncConfig();
        }
    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        logger = event.getModLog();

        MCUtilsConfig.PreInit(event);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String[] Days = sdf.format(cal.getTime()).split("/");
        OldMonth = Integer.parseInt(Days[1]);
        OldDay = Integer.parseInt(Days[2]);
        Month.add(OldMonth);
        Day.add(OldDay);

        String YMD2 = "";//"-" + Integer.parseInt(Days[0]) + "-" + OldMonth + "-" + OldDay;



        minecraftPath = event.getSuggestedConfigurationFile().getPath().replace("config\\" + MOD_ID.toLowerCase(Locale.ROOT) + ".cfg", "");
        McUtilsPath = minecraftPath + "MCUtils\\";
        System.out.print(minecraftPath);

        for(String s : FileNames) {
            try {
                Path path = Paths.get(McUtilsPath + s.replace("#", YMD2));
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Path path = Paths.get(minecraftPath + "SaveStructure");
            SaveStructurePath = minecraftPath + "SaveStructure\\";
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }

            Path path1 = Paths.get(minecraftPath + "MCUtils");
            if (!Files.exists(path1)) {
                Files.createDirectory(path1);
            }
        } catch (IOException e) {
            System.out.print("CreateDirectory   Error: " + e.getLocalizedMessage());
            e.getStackTrace();
        }

        if (event.getSide() == Side.CLIENT) {
            /*debugDataWriter.StartWriting(McUtilsPath, "Debug#.txt");
            debugDataWriter.WriteData("Minecraft Path:  " + minecraftPath);
            debugDataWriter.WriteData("Version: " + Minecraft.getMinecraft().getVersion());
            debugDataWriter.WriteWaitingData();*/
        }
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ProcessHandler());
        GLUtils.init();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new MCUtilsCommand());

        MCDataCollector dataCollector = new MCDataCollector(debugDataWriter);
        dataCollector.Start();

        OreDictHelper.Init();

        isFairyLightsInstalled = Loader.isModLoaded("fairylights");
        isValkyrienSkiesInstalled = Loader.isModLoaded("valkyrienskies");
    }

    @Mod.EventHandler
    public void ServerStartingEvent(FMLServerStartingEvent event) {
        if (event.getSide() == Side.CLIENT) {
            //debugDataWriter.WriteDataAndTime("Server Started");
            //debugDataWriter.WriteWaitingData();
        }
    }

    @Mod.EventHandler
    public void ServerStoppingEvent(FMLServerStoppingEvent event) {
        if (event.getSide() == Side.CLIENT) {
            MCUtilsThreadManager.Shutdown();
            //debugDataWriter.WriteDataAndTime("Server Stopped");
            //debugDataWriter.EndWriting();
            try {
                AllFilesCompressedZip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @SubscribeEvent
    public void TickEvent(TickEvent.ClientTickEvent event) {
        /*if (event.side == Side.CLIENT) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
            String[] Days = sdf.format(cal.getTime()).split("/");
            int M = Integer.parseInt(Days[0]);
            int D = Integer.parseInt(Days[1]);

            if(OldMonth + OldDay != M + D) {
                debugDataWriter.EndWriting();

                debugDataWriter.StartWriting(minecraftPath + "MCUtils/", minecraftPath + "MCUtils/Debug#.txt");
                debugDataWriter.WriteData("-----(Continuation of " + M + "/" + D + ")----");
                debugDataWriter.WriteData("Minecraft Path:  " + minecraftPath);
                debugDataWriter.WriteData("Version: " + Minecraft.getMinecraft().getVersion());
                OldMonth = M;
                OldDay = D;
                Month.add(M);
                Day.add(D);
            }

            if (debugDataWriter != null && debugDataWriter.WriteWaiting.size() > 20) {
                debugDataWriter.WriteWaitingData();
            }
        }*/
    }

    public void AllFilesCompressedZip() throws IOException {
        /*Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String YMD = sdf.format(cal.getTime());

        //SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");
        int Y = Integer.parseInt(sdf.format(cal.getTime()).split("-")[0]);

        Path result = Paths.get(McUtilsPath + "Debug-" + YMD +".zip");
        //Path inputPath = Paths.get(McUtilsPath.replace("\\", ""));
        List<String> loadFileList = new ArrayList<>();
        for(String s : FileNames) {
            for(Integer Month : Month) {
                for(Integer Day : Day) {
                    String YMD2 = "-" + Y + "-" + Month.toString() + "-" + Day.toString();
                    if(Files.exists(Paths.get(McUtilsPath + s.replace("#", YMD2))))
                        loadFileList.add(McUtilsPath + s.replace("#", YMD2));
                }
            }
        }

        //zipファイルを作成
        try(FileOutputStream fos = new FileOutputStream(result.toString());
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ZipOutputStream zos = new ZipOutputStream(bos)){

            //ファイルの数分、ループする。
            for (String s : loadFileList){
                Path filePath = Paths.get(s);
                //テキストファイルの名前を取得。
                ZipEntry entry = new ZipEntry(filePath.getFileName().toString());
                zos.putNextEntry(entry);

                //ファイルの中身をbyte配列で取得し、書き込み。
                byte[] data = Files.readAllBytes(filePath);
                zos.write(data);

                Files.delete(filePath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre event) {
        laserIcon = event.getMap().registerSprite(new ResourceLocation(MOD_ID, "blocks/laser"));

    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
