package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface ObjObjObjObjConsumer<T, U, V, W> {
    void accept(T t, U u, V v, W w);
}
