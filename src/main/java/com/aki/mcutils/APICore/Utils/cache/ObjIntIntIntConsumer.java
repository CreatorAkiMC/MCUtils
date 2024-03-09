package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface ObjIntIntIntConsumer<T> {
    void accept(T t, int x, int y, int z);

}
