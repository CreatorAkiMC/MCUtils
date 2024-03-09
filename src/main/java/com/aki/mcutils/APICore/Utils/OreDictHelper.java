package com.aki.mcutils.APICore.Utils;

import com.aki.mcutils.MCUtils;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OreDictHelper {
    public static Set<String> OreDictInitials = new HashSet<>();

    public static void Init() {
        for(String s : OreDictionary.getOreNames()) {
            if (s.length() > 1) {
                //String s1 = String.valueOf(s.charAt(0));
                if(Character.isLowerCase(s.charAt(0))) {
                    for(int i = 1; i < s.length(); i++) {
                        if(Character.isUpperCase(s.charAt(i))) {
                            String OreDict = "";
                            for(int i2 = 0; i2 < i; i2++) {
                                OreDict += String.valueOf(s.charAt(i2));
                            }
                            OreDictInitials.add(OreDict);
                            break;
                        }
                    }
                }
            }
        }

        MCUtils.logger.debug("---OreDictNames:" + OreDictInitials.stream().collect(Collectors.toList()) + "---");
    }



    public static boolean IsOreDictRegister(ItemStack stack, String DictName) {
        for(String s : getOreDictNames_Stack(stack)) {
            if(s.contains(DictName)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getOreDictNames_Stack(ItemStack stack) {
        List<String> strings = new ArrayList<>();
        ItemStack stack2 = stack.copy();
        stack2.setCount(1);
        if(!stack2.isEmpty()) {
            for (int i : OreDictionary.getOreIDs(stack2)) {
                strings.add(OreDictionary.getOreName(i));
            }
        }
        return strings;
    }
}
