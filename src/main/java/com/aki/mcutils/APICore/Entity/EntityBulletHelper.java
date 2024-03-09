package com.aki.mcutils.APICore.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityBulletHelper {
    public static double getHeadShotDamageFactor(double speed, Entity Target, double y, double Base, World world) {
        AxisAlignedBB boundingBox = Target.getEntityBoundingBox().expand(0, 0.0625, 0);
        if(boundingBox.maxY - y <= 8.0 * 0.0625)
        {
            int rand = world.rand.nextInt(10) + 1;
            return (Base * (Math.abs(rand - (speed / (Base - (speed - (rand * 2)))))));
        }
        return 1.0D;
    }
}
