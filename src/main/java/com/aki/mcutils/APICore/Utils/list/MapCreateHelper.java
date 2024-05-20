package com.aki.mcutils.APICore.Utils.list;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.IntFunction;

public class MapCreateHelper {
    public static <K, V> HashMap<K, V> CreateHashMap(K[] Keys, IntFunction<V> ValueFunc) {
        HashMap<K, V> map = new HashMap<>();

        for(int i = 0; i < Keys.length; i++) {
            map.put(Keys[i], ValueFunc.apply(i));
        }

        return map;
    }

    public static <K, V> LinkedHashMap<K, V> CreateLinkedHashMap(K[] Keys, IntFunction<V> ValueFunc) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        for(int i = 0; i < Keys.length; i++) {
            map.put(Keys[i], ValueFunc.apply(i));
        }
        return map;
    }
}
