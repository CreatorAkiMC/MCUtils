package com.aki.mcutils.APICore.Utils;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import static net.minecraft.util.EnumFacing.Axis.Y;

public class Vec3D {

    public double x;
    public double y;
    public double z;

    public Vec3D() {
    }

    public Vec3D(Entity entity) {
        this.x = entity.posX;
        this.y = entity.posY;
        this.z = entity.posZ;
    }

    public Vec3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(Vec3D vec3I) {
        this.x = vec3I.x;
        this.y = vec3I.y;
        this.z = vec3I.z;
    }

    /*public Vec3D(Vector3 vector3) {
        this.x = vector3.x;
        this.y = vector3.y;
        this.z = vector3.z;
    }*/

    public Vec3D(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    public Vec3D set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vec3D set(Vec3D vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }

    public Vec3D set(BlockPos pos) {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
        return this;
    }

    public Vec3D add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vec3D add(Vec3D vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }

    public Vec3D add(BlockPos pos) {
        this.x += pos.getX();
        this.y += pos.getY();
        this.z += pos.getZ();
        return this;
    }

    public Vec3D subtract(BlockPos pos) {
        this.x -= pos.getX();
        this.y -= pos.getY();
        this.z -= pos.getZ();
        return this;
    }

    public Vec3D subtract(Vec3D vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }

    public Vec3D subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vec3D multiply(Vec3D vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }

    public Vec3D multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vec3D copy() {
        return new Vec3D(this);
    }

    public BlockPos getPos() {
        return new BlockPos(x, y, z);
    }

    //public Vector3 toVector3() { return new Vector3(x, y, z); }

    @Override
    public String toString() {
        return String.format("Vec3D: [x: %s, y: %s, z: %s]", x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Vec3D other = (Vec3D) obj;

        if (x == other.x && y == other.y && z == other.z) return true;

        return false;
    }

    @Override
    public int hashCode() {
        return ((int) y + (int) z * 31) * 31 + (int) x;
    }

    public Vec3D offset(EnumFacing direction, double offsetDistance) {
        this.x += direction.getXOffset() * offsetDistance;
        this.y += direction.getYOffset() * offsetDistance;
        this.z += direction.getZOffset() * offsetDistance;
        return this;
    }

    public Vec3D offset(Vec3D direction, double offsetDistance) {
        this.x += direction.x * offsetDistance;
        this.y += direction.y * offsetDistance;
        this.z += direction.z * offsetDistance;
        return this;
    }

    public Vec3D radialOffset(EnumFacing.Axis axis, double sin, double cos, double offsetAmount) {
        x += ((axis == EnumFacing.Axis.X ? 0 : axis == Y ? sin : sin) * offsetAmount);
        y += ((axis == EnumFacing.Axis.X ? sin : axis == Y ? 0 : cos) * offsetAmount);
        z += ((axis == EnumFacing.Axis.X ? cos : axis == Y ? cos : 0) * offsetAmount);
        return this;
    }

    /**
     * Calculates a directional vector between the two given points
     * This can be used for example if you have an entity at pos1 and you want to
     * apply motion so hat is moves towards pos2
     */
    public static Vec3D getDirectionVec(Vec3D vecFrom, Vec3D vecTo) {
        double distance = getDistanceAtoB(vecFrom, vecTo);
        if (distance == 0) {
            distance = 0.1;
        }
        Vec3D offset = vecTo.copy();
        offset.subtract(vecFrom);
        return new Vec3D(offset.x / distance, offset.y / distance, offset.z / distance);
    }

    public static double getDistanceAtoB(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dy * dy + dz * dz));
    }

    public static double getDistanceAtoB(Vec3D pos1, Vec3D pos2) {
        return getDistanceAtoB(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public static double getDistanceAtoB(double x1, double z1, double x2, double z2) {
        double dx = x1 - x2;
        double dz = z1 - z2;
        return Math.sqrt((dx * dx + dz * dz));
    }

    /**
     * Calculates a directional vector between the two given points
     * This can be used for example if you have an entity at pos1 and you want to
     * apply motion so hat is moves towards pos2
     */
    /*public static Vector3 getDirectionVec(Vector3 vecFrom, Vector3 vecTo) {
        double distance = getDistanceAtoB(vecFrom.x, vecFrom.y, vecFrom.z, vecTo.x, vecTo.y, vecTo.z);
        if (distance == 0) {
            distance = 0.1;
        }
        Vector3 offset = vecTo.copy();
        offset.subtract(vecFrom);
        return new Vector3(offset.x / distance, offset.y / distance, offset.z / distance);
    }*/

    public static Vec3D getCenter(BlockPos pos) {
        return new Vec3D(pos).add(0.5, 0.5, 0.5);
    }

    public static Vec3D getCenter(TileEntity tile) {
        return getCenter(tile.getPos());
    }

    public double distXZ(Vec3D vec3D) {
        return getDistanceAtoB(x, z, vec3D.x, vec3D.z);
    }

    public double distance(Vec3D vec3D) {
        return getDistanceAtoB(this, vec3D);
    }

    public double distanceSq(Vec3D v) {
        return getDistanceSq(x, y, z, v.x, v.y, v.z);
    }

    public static double getDistanceSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }

    public int floorX() {
        return MathHelper.floor(x);
    }

    public int floorY() {
        return MathHelper.floor(y);
    }

    public int floorZ() {
        return MathHelper.floor(z);
    }
}
