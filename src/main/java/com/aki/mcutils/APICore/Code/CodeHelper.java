package com.aki.mcutils.APICore.Code;

import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CodeHelper {
    public static <T> List<Class<? extends T>> createArrayList(Class<? extends T> classe) {
        List<Class<? extends T>> array = new ArrayList<>();
        return array;
    }
}
