package com.aki.mcutils.APICore.Utils.list;

import java.util.HashMap;

public class ManyObjectsMap<K, V> extends HashMap<K, V> {

    public ManyObjectsMap<K, V> addE(K k1, V v1) {
        this.put(k1,v1);
        return this;
    }
}
