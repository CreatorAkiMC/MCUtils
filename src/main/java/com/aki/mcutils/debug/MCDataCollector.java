package com.aki.mcutils.debug;

import com.aki.mcutils.MCUtils;
import com.aki.mcutils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MCDataCollector {
    public DebugDataWriter debugDataWriter = null;
    public MCDataCollector(DebugDataWriter debugDataWriter) {
        this.debugDataWriter = debugDataWriter;
    }

    public void Start() {
        ProgressManager.ProgressBar bar = ProgressManager.push("Minecraft`s Data Logging", 6);

        bar.step("Start Counts");
        debugDataWriter.StartWriting(MCUtils.McUtilsPath, "Counts#.txt");
        debugDataWriter.WriteDataAndTime("-----Minecraft-Data-Logging-Start-----");
        debugDataWriter.WriteDataAndTime("--Counts-Start--");
        debugDataWriter.WriteData("Items: " + Item.REGISTRY.getKeys().size());
        debugDataWriter.WriteData("Blocks: " + Block.REGISTRY.getKeys().size());
        debugDataWriter.WriteData("Entities: " + EntityList.getEntityNameList().size());
        debugDataWriter.WriteData("OreDicts: " + OreDictionary.getOreNames().length);
        debugDataWriter.WriteData("Mods: " + Loader.instance().getActiveModList().size());
        debugDataWriter.WriteDataAndTime("--Counts-End--");

        bar.step("Start Item Logging");
        {
            DebugDataWriter item = new DebugDataWriter();
            item.StartWriting(MCUtils.McUtilsPath, "Items#.txt");
            ProgressManager.ProgressBar ItemBar = ProgressManager.push("Logging Item", Item.REGISTRY.getKeys().size());
            for (Item value : Item.REGISTRY) {
                ItemStack itemStack = new ItemStack(value, 1);
                String[] ItemData = getItemStackToDate(itemStack);
                ItemBar.step("ID: " + ItemData[0] + ":" + ItemData[1]);
                item.WriteData("--Mod: " + ItemData[0] + ", ID: " + ItemData[1] + ", RegistryName: " + ItemData[2] + ", TranslationKey: " + ItemData[3] + ", MaxDamage: " + ItemData[4] + ", MaxStackSize: " + ItemData[5] + ", OreDictName: " + ItemData[6] + "--");
            }
            ProgressManager.pop(ItemBar);
            item.EndWriting();
        }

        bar.step("Start Block Logging");
        {
            DebugDataWriter block = new DebugDataWriter();
            block.StartWriting(MCUtils.McUtilsPath, "Blocks#.txt");
            ProgressManager.ProgressBar BlockBar = ProgressManager.push("Logging Block", Block.REGISTRY.getKeys().size());
            for (Block value : Block.REGISTRY) {
                ItemStack itemStack = new ItemStack(value, 1);
                String[] ItemData = getItemStackToDate(itemStack);
                BlockBar.step("ID: " + ItemData[0] + ":" + ItemData[1]);
                block.WriteData("--Mod: " + ItemData[0] + ", ID: " + ItemData[1] + ", RegistryName: " + ItemData[2] + ", TranslationKey: " + ItemData[3] + ", MaxDamage: " + ItemData[4] + ", MaxStackSize: " + ItemData[5] + ", OreDictName: " + ItemData[6] + "--");
            }
            ProgressManager.pop(BlockBar);
            block.EndWriting();
        }

        bar.step("Start Entity Logging");
        {
            DebugDataWriter entity = new DebugDataWriter();
            entity.StartWriting(MCUtils.McUtilsPath, "Entities#.txt");
            ProgressManager.ProgressBar EntityBar = ProgressManager.push("Logging Entity", EntityList.getEntityNameList().size());
            for(ResourceLocation resourceLocation : EntityList.getEntityNameList()) {
                String Mod = resourceLocation.toString().split(":")[0];
                EntityBar.step("ID: " + resourceLocation.toString());
                entity.WriteData("--Mod: " + Mod + ", RegistryName: " + resourceLocation.toString() + "--");
            }
            ProgressManager.pop(EntityBar);
            entity.EndWriting();
        }

        bar.step("Start OreDictionary Logging");
        {
            DebugDataWriter OreDict = new DebugDataWriter();
            OreDict.StartWriting(MCUtils.McUtilsPath, "OreDicts#.txt");
            ProgressManager.ProgressBar OreBar = ProgressManager.push("Logging OreDictionary", OreDictionary.getOreNames().length);
            for(String s : OreDictionary.getOreNames()) {
                OreBar.step("ID: " + s);
                OreDict.WriteData("--Name: " + s + " --");
            }
            ProgressManager.pop(OreBar);
            OreDict.EndWriting();
        }

        bar.step("Mod Logging");
        {
            DebugDataWriter Mods = new DebugDataWriter();
            Mods.StartWriting(MCUtils.McUtilsPath, "Mods#.txt");
            ProgressManager.ProgressBar ModBar = ProgressManager.push("Logging Mod", Loader.instance().getActiveModList().size());
            for(ModContainer modContainer : Loader.instance().getActiveModList()) {
                List<ArtifactVersion> artifactVersions = new ArrayList<>(modContainer.getRequirements());
                List<String> requirements = new ArrayList<>();
                ModBar.step("Id: " + modContainer.getModId() + ", Name: " + modContainer.getName());
                for(ArtifactVersion artifactVersion : artifactVersions) {
                    requirements.add(artifactVersion.getLabel() + "-Range: " + artifactVersion.getRangeString() + "--Version: " + artifactVersion.getVersionString());
                }
                Mods.WriteData("--{\n   ModId: " + modContainer.getModId() + ",\n   ModName: " + modContainer.getName() + ",\n   Version: " + modContainer.getVersion() + ",\n   OwnedPackages: " + Utils.getListPackage(modContainer.getOwnedPackages(), "      ") + ",\n   Requirements: " + Utils.getListPackage(requirements, "      ") + " }--");
            }
            ProgressManager.pop(ModBar);
            Mods.EndWriting();
        }

        debugDataWriter.WriteDataAndTime("-----Minecraft-Data-Logging-End-----");
        debugDataWriter.EndWriting();

        ProgressManager.pop(bar);

    }

    /**
     * 0:ModName
     * 1:ID
     * 2:RegistryName
     * 3:TranslationKey
     * 4:MaxDamage
     * 5:MaxStackSize
     * 6:OreDictName
     * */
    public String[] getItemStackToDate(ItemStack stack) {
        String[] strings = new String[7];
        Item item = stack.getItem();
        String RegistryName = item.getRegistryName().toString();
        String ModName = RegistryName.split(":")[0];
        strings[0] = ModName;
        strings[1] = String.valueOf(Item.getIdFromItem(item));
        strings[2] = RegistryName;
        strings[3] = item.getTranslationKey();
        strings[4] = String.valueOf(stack.getMaxDamage());
        strings[5] = String.valueOf(stack.getMaxStackSize());
        String DictName = "--None--";

        b:for(String s : OreDictionary.getOreNames()) {
            for(ItemStack stack1 : OreDictionary.getOres(s)) {
                if(stack1.getItem().getRegistryName() == stack.getItem().getRegistryName()) {
                    DictName = s;
                    break b;
                }
            }
        }

        strings[6] = DictName;
        return strings;
    }
}
