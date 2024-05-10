package com.aki.mcutils.utils.save_load;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.threads.MCUtilsThreadManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

//並列で処理
public class QuickSaveLoadSystem {
    private static final List<Chunk> SaveChunksQueue = new ArrayList<>();
    public static void AddQuickSaveChunkQueue(Chunk chunk) {
        SaveChunksQueue.add(chunk);
    }

    private synchronized static Chunk getNextProcessChunk() {
        return SaveChunksQueue.remove(0);
    }

    private synchronized static boolean IsNextProcessChunk() {
        return SaveChunksQueue.size() > 0;
    }

    public static void StartQuickSave(World world) {

        //File = [WorldName + YYYY-MM-DD-hh-mm-ss-[ChunkX_ChunkZ].json]
        for(int i = 0; i < Math.ceil(SaveChunksQueue.size() / 2.0d); i++) {
            LocalDate date = LocalDate.now();
            LocalTime time = LocalTime.now();
            String TimeString = date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + "-" + time.getHour() + "-" + time.getMinute() + "-" + time.getSecond();
            MCUtilsThreadManager.executor.execute(() -> {
                while (IsNextProcessChunk()) {
                    Chunk chunk = getNextProcessChunk();
                    int ChunkPosX = chunk.x;
                    int ChunkPosZ = chunk.z;
                    NBTTagCompound MainData = new NBTTagCompound();
                    NBTTagCompound ChunkData = new NBTTagCompound();
                    MainData.setTag("Level", ChunkData);
                    MainData.setInteger("DataVersion", 1343);
                    net.minecraftforge.fml.common.FMLCommonHandler.instance().getDataFixer().writeVersionData(MainData);
                    writeChunkToNBT(chunk, world, ChunkData);
                    net.minecraftforge.common.ForgeChunkManager.storeChunkNBT(chunk, ChunkData);
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkDataEvent.Save(chunk, MainData));
                    try {
                        String FileName = "[" + world.getWorldInfo().getWorldName() + "]-" + TimeString + "-[" + ChunkPosX + "_" + ChunkPosZ + "].json";
                        File file = new File(MCUtils.McUtilsPath + "SaveDataFolder");
                        if(!file.exists()) {
                            Files.createDirectory(file.toPath());
                        }
                        CompressedStreamTools.writeCompressed(MainData, Files.newOutputStream(Paths.get(file + "\\" + FileName)));//GZip使用
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private static void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound compound)
    {
        compound.setInteger("xPos", chunkIn.x);
        compound.setInteger("zPos", chunkIn.z);
        compound.setLong("LastUpdate", worldIn.getTotalWorldTime());
        compound.setIntArray("HeightMap", chunkIn.getHeightMap());
        compound.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        compound.setBoolean("LightPopulated", chunkIn.isLightPopulated());
        compound.setLong("InhabitedTime", chunkIn.getInhabitedTime());
        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = worldIn.provider.hasSkyLight();

        for (ExtendedBlockStorage extendedblockstorage : aextendedblockstorage)
        {
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE)
            {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
                byte[] abyte = new byte[4096];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = extendedblockstorage.getData().getDataForNBT(abyte, nibblearray);
                nbttagcompound.setByteArray("Blocks", abyte);
                nbttagcompound.setByteArray("Data", nibblearray.getData());

                if (nibblearray1 != null)
                {
                    nbttagcompound.setByteArray("Add", nibblearray1.getData());
                }

                nbttagcompound.setByteArray("BlockLight", extendedblockstorage.getBlockLight().getData());

                if (flag)
                {
                    nbttagcompound.setByteArray("SkyLight", extendedblockstorage.getSkyLight().getData());
                }
                else
                {
                    nbttagcompound.setByteArray("SkyLight", new byte[extendedblockstorage.getBlockLight().getData().length]);
                }

                nbttaglist.appendTag(nbttagcompound);
            }
        }

        compound.setTag("Sections", nbttaglist);
        compound.setByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (int i = 0; i < chunkIn.getEntityLists().length; ++i)
        {
            for (Entity entity : chunkIn.getEntityLists()[i])
            {
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();

                try
                {
                    if (entity.writeToNBTOptional(nbttagcompound2))
                    {
                        chunkIn.setHasEntities(true);
                        nbttaglist1.appendTag(nbttagcompound2);
                    }
                }
                catch (Exception e)
                {
                    net.minecraftforge.fml.common.FMLLog.log.error("An Entity type {} has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                            entity.getClass().getName(), e);
                }
            }
        }

        compound.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();

        for (TileEntity tileentity : chunkIn.getTileEntityMap().values())
        {
            try
            {
                NBTTagCompound nbttagcompound3 = tileentity.writeToNBT(new NBTTagCompound());
                nbttaglist2.appendTag(nbttagcompound3);
            }
            catch (Exception e)
            {
                net.minecraftforge.fml.common.FMLLog.log.error("A TileEntity type {} has throw an exception trying to write state. It will not persist. Report this to the mod author",
                        tileentity.getClass().getName(), e);
            }
        }

        compound.setTag("TileEntities", nbttaglist2);
        List<NextTickListEntry> list = worldIn.getPendingBlockUpdates(chunkIn, false);

        if (list != null)
        {
            long j = worldIn.getTotalWorldTime();
            NBTTagList nbttaglist3 = new NBTTagList();

            for (NextTickListEntry nextticklistentry : list)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                ResourceLocation resourcelocation = Block.REGISTRY.getNameForObject(nextticklistentry.getBlock());
                nbttagcompound1.setString("i", resourcelocation == null ? "" : resourcelocation.toString());
                nbttagcompound1.setInteger("x", nextticklistentry.position.getX());
                nbttagcompound1.setInteger("y", nextticklistentry.position.getY());
                nbttagcompound1.setInteger("z", nextticklistentry.position.getZ());
                nbttagcompound1.setInteger("t", (int)(nextticklistentry.scheduledTime - j));
                nbttagcompound1.setInteger("p", nextticklistentry.priority);
                nbttaglist3.appendTag(nbttagcompound1);
            }

            compound.setTag("TileTicks", nbttaglist3);
        }

        if (chunkIn.getCapabilities() != null)
        {
            try
            {
                compound.setTag("ForgeCaps", chunkIn.getCapabilities().serializeNBT());
            }
            catch (Exception exception)
            {
                net.minecraftforge.fml.common.FMLLog.log.error("A capability provider has thrown an exception trying to write state. It will not persist. Report this to the mod author", exception);
            }
        }
    }
}