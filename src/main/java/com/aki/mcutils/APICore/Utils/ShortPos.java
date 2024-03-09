package com.aki.mcutils.APICore.Utils;

import net.minecraft.util.math.BlockPos;

public class ShortPos {

    private BlockPos relativeTo;

    public ShortPos(BlockPos relativeTo) {
        this.relativeTo = relativeTo;
    }

    public void setRelativeTo(BlockPos relativeTo) {
        this.relativeTo = relativeTo;
    }

    public BlockPos getRelativeTo() {
        return relativeTo;
    }

    public int getIntPos(BlockPos pos) {
        return getIntPos(pos, relativeTo);
    }

    public int getIntPos(Vec3D pos) {
        return getIntPos(pos, relativeTo);
    }

    public BlockPos getActualPos(int intPos) {
        return getBlockPos(intPos, relativeTo);
    }

    /**
     * This is the main method for converting a relative position to an integer.<br>
     * Max allowed x/z difference between the two positions is 2048. Anything greater than this will result in a broken output but will not throw an error.<br><br>
     *
     * <pre>
     * Byte Format:
     *  Y 8 bits     X 12 bits       Z 12 bits
     * [11111111] [11111111 1111] [1111 11111111]
     * </pre>
     *
     * @param position The position.
     * @param relativeTo The the position to calculate the relative pos from. You will need this pos to convert the output from this method back to an actual block pos.
     * @return the relative deference between the inputs as an integer.
     * */
    public static int getIntPos(BlockPos position, BlockPos relativeTo) {
        if (position.getY() > 255) {
            position = new BlockPos(position.getX(), 255, position.getY());
        }
        else if (position.getY() < 0) {
            position = new BlockPos(position.getX(), 0, position.getY());
        }
        int xp = (position.getX() - relativeTo.getX()) + 2048;
        int yp = position.getY();
        int zp = (position.getZ() - relativeTo.getZ()) + 2048;

        return (yp << 24) | (xp << 12) | zp;
    }

    public static int getIntPos(Vec3D position, BlockPos relativeTo) {
        if (position.y > 255) {
            position.y = 255;
        }
        else if (position.y < 0) {
            position.y = 0;
        }
        int xp = (position.floorX() - relativeTo.getX()) + 2048;
        int yp = position.floorY();
        int zp = (position.floorZ() - relativeTo.getZ()) + 2048;

        return (yp << 24) | (xp << 12) | zp;
    }

    /**
     * This is the main method for converting from a relative integer position back to an actual block position.
     *
     * @param intPos The integer position calculated by getIntPos
     * @param relativeTo the position to calculate the output pos relative to. This should be the same pos that was used when calculating the intPos to get back the original absolute pos.
     * @return The absolute block position.
     */
    public static BlockPos getBlockPos(int intPos, BlockPos relativeTo) {
        int yp = (intPos >> 24 & 0xFF);
        int xp = (intPos >> 12 & 0xFFF) - 2048;
        int zp = (intPos & 0xFFF) - 2048;

        return new BlockPos(relativeTo.getX() + xp, yp, relativeTo.getZ() + zp);
    }
}
