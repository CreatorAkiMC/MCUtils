package com.aki.mcutils.APICore.Loot;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GetLoot {
    public static List<ItemStack> getRandomStackforLoot(ResourceLocation Loot, World world) {
        List<ItemStack> stackList = new ArrayList<>();
        LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(Loot);
        if ((FMLClientHandler.instance().getClient()).player instanceof EntityPlayerSP) {
            LootContext context = new LootContext(world.rand.nextFloat(), (WorldServer) world, world.getLootTableManager(), null, (FMLClientHandler.instance().getClient()).player, new DamageSource("predicate"));
            stackList.addAll(lootTable.generateLootForPools(world.rand, context));
        }
        return stackList;
    }

    public static List<ResourceLocation> getAllLootResourceLocation() {
        List<ResourceLocation> resourceLocations = new ArrayList<>();
        for(ResourceLocation resourceLocation : LootTableList.getAll()) {
            resourceLocations.add(resourceLocation);
        }
        return resourceLocations;
    }

    public static ResourceLocation getRandomLootLocation() {
        List<ResourceLocation> rl = getAllLootResourceLocation();
        return rl.get(new Random().nextInt(rl.size()));
    }
}
