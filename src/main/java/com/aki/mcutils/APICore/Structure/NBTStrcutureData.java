package com.aki.mcutils.APICore.Structure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NBTStrcutureData {
    public Vec3d Size = Vec3d.ZERO;

    public BlockPos BasePos = BlockPos.ORIGIN;
    public BlockPos MinPos = BlockPos.ORIGIN;
    public BlockPos MaxPos = BlockPos.ORIGIN;

    public int AllBlock = 0;
    public int BuildingBlocks = 0;
    public int AirBlocks = 0;
    public int AllEntity = 0;

    public String version = "1.12.2";
    public String DimensionName = "";
    public String Time = "";
    public String PlayerName = "";

    public NBTStrcutureData(Vec3d size, BlockPos basePos, BlockPos minPos, BlockPos maxPos, int allBlocks, int buildingBlocks, int airBlocks, int allEntity, String version, String dimensionName, String time, String playerName) {
        this.Size = size;
        this.BasePos = basePos;
        this.MinPos = minPos;
        this.MaxPos = maxPos;
        this.AllBlock = allBlocks;
        this.BuildingBlocks = buildingBlocks;
        this.AirBlocks = airBlocks;
        this.AllEntity = allEntity;
        this.version = version;
        this.DimensionName = dimensionName;
        this.Time = time;
        this.PlayerName = playerName;
    }
}
