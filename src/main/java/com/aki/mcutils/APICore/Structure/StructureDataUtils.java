package com.aki.mcutils.APICore.Structure;

import com.aki.mcutils.APICore.DataUtils.DataUtils;
import com.aki.mcutils.MCUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StructureDataUtils {
    /**
     * basepos = 建物を保存するブロックの座標。
     * 保存できれば、保存先[.nbt付き]を、できなければ"ERROR"を。
     * 独自のディレクトリを使用する場合、[filename]"foldername/filename"のようにする。
     * loadする場合も同じです。
     * */
    public static String SaveBuild(String filename, World world, EntityPlayer player, BlockPos basepos, BlockPos pos1, BlockPos pos2) {
        BlockPos POS1 = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        BlockPos POS2 = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
        //List<NBTTagCompound> nbts = new ArrayList<>();
        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setTag("BlockPosData", new NBTTagList());
        NBTTagList NBTTagList1 = new NBTTagList();
        int allblocks = 0;
        int buildingblocks = 0;
        int airblocks = 0;

        boolean ok = true;
        for(int x = POS1.getX(); x <= POS2.getX(); x++) {
            for(int y = POS1.getY(); y <= POS2.getY(); y++) {
                for(int z = POS1.getZ(); z <= POS2.getZ(); z++) {

                    allblocks++;

                    BlockPos savepos0 = new BlockPos(x,y,z);
                    IBlockState state = world.getBlockState(savepos0);
                    NBTTagCompound nbt = new NBTTagCompound();
                    TileEntity tile = world.getTileEntity(savepos0);
                    if(tile != null) {
                        if(tile.writeToNBT(new NBTTagCompound()) != null) {
                            nbt = tile.writeToNBT(new NBTTagCompound());
                        }
                    }

                    if(state.getMaterial() != Material.AIR) {
                        buildingblocks++;
                    } else {
                        airblocks++;
                    }

                    BlockPos savepos = new BlockPos(getBasePosfromBlockPos(x,basepos.getX()), getBasePosfromBlockPos(y,basepos.getY()), getBasePosfromBlockPos(z,basepos.getZ()));
                    //BlockPosData data = new BlockPosData(savepos, state, nbt);
                    //NBTTagList2.add(new NBTTagCompound().set("BlockState", NBTUtil.writeBlockState(state)));

                    nbt.setTag("BlockState", NBTUtil.writeBlockState(new NBTTagCompound(), state));
                    nbt.removeTag("x");
                    nbt.removeTag("y");
                    nbt.removeTag("z");
                    nbt.removeTag("LootTable");
                    nbt.removeTag("LootTableSeed");

                    nbt.setTag("BlockPos", new NBTTagList());
                    NBTTagList nbt2 = new NBTTagList();

                    NBTTagCompound nbt3 = new NBTTagCompound();
                    nbt3.setInteger("SaveBasePosX", savepos.getX());
                    nbt3.setInteger("SaveBasePosY", savepos.getY());
                    nbt3.setInteger("SaveBasePosZ", savepos.getZ());
                    nbt2.appendTag(nbt3);
                    nbt.setTag("BlockPos", nbt2);
                    //if(state.getMaterial() != Material.AIR)
                    NBTTagList1.appendTag(nbt);
                    //NBTTagList2.add(NBTUtil.writeBlockState(state));
                }
            }
        }
        nbt1.setTag("entitys", new NBTTagList());
        NBTTagList entitynbt = new NBTTagList();
        AxisAlignedBB alignedBB = new AxisAlignedBB(pos1, pos2.up());
        List<Entity> entity2 = new ArrayList<>();
        for(Entity entity : world.getEntitiesWithinAABB(Entity.class, alignedBB)) {
            if(!(entity instanceof EntityPlayer)) {
                NBTTagCompound entityCNBT = new NBTTagCompound();
                entityCNBT.setTag("Pos", new NBTTagList());
                entity2.add(entity);
                entity.writeToNBT(entityCNBT);
                entityCNBT.removeTag("UUID");
                double x = getDoubleBasePosfromPos(entity.posX, basepos.getX());
                double y = getDoubleBasePosfromPos(entity.posY, basepos.getY());
                double z = getDoubleBasePosfromPos(entity.posZ, basepos.getZ());
                NBTTagList posnbt = new NBTTagList();
                NBTTagCompound pos = new NBTTagCompound();
                pos.setDouble("PosX", x);
                pos.setDouble("PosY", y);
                pos.setDouble("PosZ", z);
                posnbt.appendTag(pos);
                entityCNBT.setTag("Pos", posnbt);
                entitynbt.appendTag(entityCNBT);
            }
        }

        int entitysize = entity2.size();

        nbt1.setTag("Profile", new NBTTagList());
        NBTTagList profile = new NBTTagList();
        NBTTagCompound profiles = new NBTTagCompound();



        profiles.setTag("Size", new NBTTagList());
        profiles.setTag("Pos", new NBTTagList());
        profiles.setTag("EntityBlock", new NBTTagList());
        profiles.setTag("WTPP", new NBTTagList());



        NBTTagCompound sizenbt = new NBTTagCompound();
        sizenbt.setInteger("XSize", POS2.getX() - POS1.getX());
        sizenbt.setInteger("YSize", POS2.getY() - POS1.getY());
        sizenbt.setInteger("ZSize", POS2.getZ() - POS1.getZ());

        NBTTagList size = new NBTTagList();
        size.appendTag(sizenbt);
        profiles.setTag("Size", size);


        NBTTagCompound POSNBT = new NBTTagCompound();
        POSNBT.setInteger("BasePosX", basepos.getX());
        POSNBT.setInteger("BasePosY", basepos.getY());
        POSNBT.setInteger("BasePosZ", basepos.getZ());
        POSNBT.setInteger("MinPosX", POS1.getX() - basepos.getX());
        POSNBT.setInteger("MinPosY", POS1.getY() - basepos.getY());
        POSNBT.setInteger("MinPosZ", POS1.getZ() - basepos.getZ());
        POSNBT.setInteger("MaxPosX", POS2.getX() - basepos.getX());
        POSNBT.setInteger("MaxPosY", POS2.getY() - basepos.getY());
        POSNBT.setInteger("MaxPosZ", POS2.getZ() - basepos.getZ());

        NBTTagList pos = new NBTTagList();
        pos.appendTag(POSNBT);
        profiles.setTag("Pos", pos);


        NBTTagCompound EntityBlockProfile = new NBTTagCompound();
        EntityBlockProfile.setInteger("AllBlocks", allblocks);
        EntityBlockProfile.setInteger("BuildingBlocks", buildingblocks);
        EntityBlockProfile.setInteger("AirBlocks", airblocks);
        EntityBlockProfile.setInteger("Entitys", entitysize);

        NBTTagList EntityBlock = new NBTTagList();
        EntityBlock.appendTag(EntityBlockProfile);
        profiles.setTag("EntityBlock", EntityBlock);


        NBTTagCompound WorldTimePlayerProfile = new NBTTagCompound();
        /**
         * 時間取得、ワールド名取得
         * */
        LocalDateTime ldt = LocalDateTime.now();
        WorldTimePlayerProfile.setString("WorldName", world.getWorldInfo().getWorldName());
        WorldTimePlayerProfile.setString("Version", Objects.requireNonNull(player.getServer()).getMinecraftVersion());
        WorldTimePlayerProfile.setString("DimensionName", world.getWorldType().getName());
        WorldTimePlayerProfile.setString("Time", String.valueOf(ldt));
        WorldTimePlayerProfile.setString("PlayerName", player.getDisplayNameString());

        NBTTagList WTTP = new NBTTagList();
        WTTP.appendTag(WorldTimePlayerProfile);
        profiles.setTag("WTPP", WTTP);

        profile.appendTag(profiles);

        nbt1.setTag("Profile", profile);
        nbt1.setTag("entitys", entitynbt);
        nbt1.setTag("BlockPosData", NBTTagList1);

        //nbt1.set("Blocks", NBTTagList2);
        try {
            DataOutputStream dataOutputStream = DataUtils.getDataOutput(MCUtils.SaveStructurePath + filename + ".nbt");
            CompressedStreamTools.writeCompressed(nbt1, dataOutputStream);
        } catch (IOException e) {
            MCUtils.logger.error(e.getMessage());
        }

        if(!ok)
            return null;
        return MCUtils.SaveStructurePath + filename + ".nbt";
    }

    /**
     * 基準点を中心とした、座標の移動。(計算が簡単で、向きを変えるのにうってつけ。)
     * */
    public static int getBasePosfromBlockPos(int posxyz, int baseposxyz) {
        return posxyz - baseposxyz;
    }

    public static double getDoubleBasePosfromPos(double posxyz, int baseposxyz) {
        return posxyz - (double) baseposxyz;
    }

    /**
     * 読み込み(String Saveで取得した保存先)
     * */
    public static NBTTagCompound LoadBuild(String location1) {
        NBTTagCompound nbt = new NBTTagCompound();
        String location = MCUtils.SaveStructurePath + location1 + ".nbt";

        try {
            DataInputStream dataInputStream = DataUtils.getDataInput(location);
            nbt = CompressedStreamTools.readCompressed(dataInputStream);
        } catch (IOException e) {
            MCUtils.logger.error(e.getMessage());
        }
        return nbt;
    }

    /*private static INBT read(DataInset inset, int depth, NBTSizeTracker accounter) throws IOException {
        byte b0 = inset.readByte();
        accounter.read(8); // Forge: Count everything!
        if (b0 == 0) {
            return EndNBT.INSTANCE;
        } else {
            //accounter.readUTF(inset.readUTF()); //Forge: Count this string.
            accounter.readUTF(inset.readUTF());
            accounter.read(32); //Forge: 4 extra bytes for the object allocation.

            try {
                return NBTTypes.getGetTypeByID(b0).readNBT(inset, depth, accounter);
            } catch (IOException ioexception) {
                CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
                crashreportcategory.addDetail("Tag type", b0);
                throw new ReportedException(crashreport);
            }
        }
    }*/

    public static List<BlockPosData> getNBTtoBlock(NBTTagCompound nbt) {
        List<BlockPosData> posDataList = new ArrayList<>();
        if(nbt.getTag("BlockPosData") instanceof NBTTagList) {
            for(NBTBase blockposdata : (NBTTagList)nbt.getTag("BlockPosData")) {
                NBTTagCompound BlockPosData1 = ((NBTTagCompound) blockposdata);
                if (NBTUtil.readBlockState((NBTTagCompound) Objects.requireNonNull(BlockPosData1.getTag("BlockState"))) != null) {
                    IBlockState state = NBTUtil.readBlockState((NBTTagCompound) Objects.requireNonNull(BlockPosData1.getTag("BlockState")));

                    int x = 0, y = 0, z = 0;
                    if (BlockPosData1.getTag("BlockPos") instanceof NBTTagList) {
                        for (NBTBase PosData : (NBTTagList) BlockPosData1.getTag("BlockPos")) {
                            NBTTagCompound nbt1 = (NBTTagCompound) PosData;
                            x = nbt1.getInteger("SaveBasePosX");
                            y = nbt1.getInteger("SaveBasePosY");
                            z = nbt1.getInteger("SaveBasePosZ");
                        }
                    }
                    BlockPos pos = new BlockPos(x, y, z);
                    posDataList.add(new BlockPosData(pos, state, BlockPosData1));
                    //System.out.print("posX: " + pos.getX() + " posY: " + pos.getY() + " posZ: " + pos.getZ() + " StateName: " + state.getBlock().getRegistryName().toString());
                }
            }
        }
        return posDataList;
    }

    public static List<Entity> getNBTtoEntity(NBTTagCompound nbt, World world) {
        List<Entity> EntityList = new ArrayList<>();
        double x = 0;
        double y = 0;
        double z = 0;
        if(nbt.getTag("entitys") instanceof NBTTagList) {
            NBTTagList entitys = (NBTTagList) nbt.getTag("entitys");
            for(NBTBase inbt : entitys) {
                NBTTagCompound nbt1 = (NBTTagCompound) inbt;
                for(NBTBase inbt1 : (NBTTagList)nbt1.getTag("Pos")) {
                    NBTTagCompound nbt2 = (NBTTagCompound) inbt1;
                    x = nbt2.getDouble("PosX");
                    y = nbt2.getDouble("PosY");
                    z = nbt2.getDouble("PosZ");
                }
                NBTTagCompound entitynbt = nbt1.copy();
                NBTTagList listPos = new NBTTagList();
                listPos.appendTag(new NBTTagDouble(x));
                listPos.appendTag(new NBTTagDouble(y));
                listPos.appendTag(new NBTTagDouble(z));
                entitynbt.setTag("Pos", listPos);
                entitynbt.removeTag("UUID");
                //System.out.print("ok: X: " + x + " Y: " + y + " Z: " + z);
                Entity entity = net.minecraft.entity.EntityList.createEntityFromNBT(entitynbt, world);
                entity.setPosition(x,y,z);
                System.out.print("EntityName: " + entity.getDisplayName().getUnformattedComponentText());
                EntityList.add(entity);
            }
        }

        return EntityList;
    }

    public static NBTStrcutureData getNBTtoProfile(NBTTagCompound nbt) {

        int XSize = 0;
        int YSize = 0;
        int ZSize = 0;
        int BasePosX = 0;
        int BasePosY = 0;
        int BasePosZ = 0;
        int MinPosX = 0;
        int MinPosY = 0;
        int MinPosZ = 0;
        int MaxPosX = 0;
        int MaxPosY = 0;
        int MaxPosZ = 0;
        int AllBlocks = 0;
        int BuildingBlocks = 0;
        int AirBlocks = 0;
        int AllEntitys = 0;
        String version = "1.16.5";
        String DimensionName = "";
        String Time = "";
        String PlayerName = "";

        if (nbt.getTag("Profile") instanceof NBTTagList) {
            for (NBTBase inbt : (NBTTagList) nbt.getTag("Profile")) {
                NBTTagCompound nbt1 = (NBTTagCompound) inbt;
                /**
                 *         profiles.set("Size", new NBTTagList());
                 *         profiles.set("Pos", new NBTTagList());
                 *         profiles.set("EntityBlock", new NBTTagList());
                 *         profiles.set("WTPP", new NBTTagList());
                 * */
                if (nbt1.getTag("Size") instanceof NBTTagList) {
                    for (NBTBase inbt1 : (NBTTagList) nbt1.getTag("Size")) {
                        NBTTagCompound nbt2 = (NBTTagCompound) inbt1;
                        //System.out.print("nbt2: " + nbt2.getString());
                        XSize = nbt2.getInteger("XSize");
                        YSize = nbt2.getInteger("YSize");
                        ZSize = nbt2.getInteger("ZSize");
                    }
                }

                //System.out.print("Size");

                if (nbt1.getTag("Pos") instanceof NBTTagList) {
                    for (NBTBase inbt1 : (NBTTagList) nbt1.getTag("Pos")) {
                        NBTTagCompound nbt2 = (NBTTagCompound) inbt1;
                        BasePosX = nbt2.getInteger("BasePosX");
                        BasePosY = nbt2.getInteger("BasePosY");
                        BasePosZ = nbt2.getInteger("BasePosZ");
                        MinPosX = nbt2.getInteger("MinPosX");
                        MinPosY = nbt2.getInteger("MinPosY");
                        MinPosZ = nbt2.getInteger("MinPosZ");
                        MaxPosX = nbt2.getInteger("MaxPosX");
                        MaxPosY = nbt2.getInteger("MaxPosY");
                        MaxPosZ = nbt2.getInteger("MaxPosZ");
                    }
                }

                //System.out.print("Pos");

                if (nbt1.getTag("EntityBlock") instanceof NBTTagList) {
                    for (NBTBase inbt1 : (NBTTagList) nbt1.getTag("EntityBlock")) {
                        NBTTagCompound nbt2 = (NBTTagCompound) inbt1;
                        AllBlocks = nbt2.getInteger("AllBlocks");
                        BuildingBlocks = nbt2.getInteger("BuildingBlocks");
                        AirBlocks = nbt2.getInteger("AirBlocks");
                        AllEntitys = nbt2.getInteger("Entitys");
                    }
                }

                //System.out.print("EntityBlock");

                if (nbt1.getTag("WTPP") instanceof NBTTagList) {
                    for (NBTBase inbt1 : (NBTTagList) nbt1.getTag("WTPP")) {
                        NBTTagCompound nbt2 = (NBTTagCompound) inbt1;
                        version = nbt2.getString("Version");
                        DimensionName = nbt2.getString("DimensionName");
                        Time = nbt2.getString("Time");
                        PlayerName = nbt2.getString("PlayerName");
                    }
                }

                //System.out.print("WTPP");
            }
        }

        return new NBTStrcutureData(new Vec3d(XSize, YSize, ZSize),
                new BlockPos(BasePosX, BasePosY, BasePosZ),
                new BlockPos(MinPosX, MinPosY, MinPosZ),
                new BlockPos(MaxPosX, MaxPosY, MaxPosZ),
                AllBlocks, BuildingBlocks, AirBlocks, AllEntitys, version, DimensionName,
                Time, PlayerName);
    }

    /**
     * MyFolderNameは、saveの保存先に何も変更を加えていなければ、"";
     * 加えていれば、
     * 例:
     * ~~/SaveStructure/フォルダの名前/ファイルの名前.nbt
     * の場合、
     * MyFolderNameには,("フォルダの名前/")と、入れる。
     * */
    public static List<File> getListFilesDirectory(String MyFolderName) {
        return dumpFile(new File(MCUtils.SaveStructurePath + MyFolderName));
    }

    /**
     * 範囲を空っぽにするための処理。
     * クリエイティブ用
     * */
    public static void setAirFilled(World world, NBTStrcutureData strcutureData, BlockPos baseblockpos) {
        List<BlockPos> blockPosList = getFilledBlockPos(strcutureData.MinPos, strcutureData.MaxPos);
        /*//System.out.print(" MaxPos: " + strcutureData.MaxPos.toString() + " MinPos: " + strcutureData.MinPos.toString() + "     Poss: " + blockPosList.toString());
        for(BlockPosData data : blockPosDataList) {
            if(blockPosList.contains(data.pos)) {
                blockPosList.remove(data.pos);
            }
        }*/

        for(BlockPos pos : blockPosList) {
            world.setBlockToAir(pos.add(baseblockpos));
            world.removeTileEntity(pos.add(baseblockpos));
        }
    }

    /**
     * 空間の空気の部分の座標を割り出す
     * 帰ってくるList<BlockPos>は、0,0,0を基準とした座標
     * */
    public static List<BlockPos> getAirBlockPos(NBTStrcutureData strcutureData, List<BlockPosData> blockPosDataList) {
        if(strcutureData != null) {
            List<BlockPos> blockPosList = getFilledBlockPos(strcutureData.MinPos, strcutureData.MaxPos);

            for(BlockPosData data : blockPosDataList) {
                if(blockPosList.contains(data.pos)) {
                    blockPosDataList.remove(data.pos);
                }
            }
            return blockPosList;
        }
        return null;
    }

    /**
     * ブロックの部分の座標を割り出す
     * 帰ってくるList<BlockPos>は、0,0,0を基準とした座標
     * */
    public static List<BlockPos> getReplaceBlockPosList(NBTStrcutureData strcutureData, List<BlockPosData> blockPosDataList) {
        if(strcutureData != null) {
            List<BlockPos> blockPosList = new ArrayList<>();//getFilledBlockPos(strcutureData.MinPos, strcutureData.MaxPos);
            for(BlockPosData data : blockPosDataList) {
                blockPosList.add(data.pos);
            }
            return blockPosList;
        }
        return null;
    }

    public static List<BlockPos> getFilledBlockPos(BlockPos pos1, BlockPos pos2) {
        List<BlockPos> blockPosList = new ArrayList<>();

        pos1 = new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
        pos2 = new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));

        for (int l = pos1.getZ(); l <= pos2.getZ(); ++l)
            for (int i1 = pos1.getY(); i1 <= pos2.getY(); ++i1)
                for (int j1 = pos1.getX(); j1 <= pos2.getX(); ++j1) {
                    BlockPos blockpos4 = new BlockPos(j1, i1, l);
                    blockPosList.add(blockpos4);
                }

        return blockPosList;
    }

    private static List<File> dumpFile(File file){

        // ファイル一覧取得
        File[] files = file.listFiles();
        List<File> files1 = new ArrayList<>();

        if(files == null){
            return files1;
        }

        for (File tmpFile : files) {

            // ディレクトリの場合
            if(tmpFile.isDirectory()){

                // 再帰呼び出し
                dumpFile(tmpFile);

                // ファイルの場合
            }else{
                files1.add(tmpFile);
            }
        }
        return files1;
    }
}
