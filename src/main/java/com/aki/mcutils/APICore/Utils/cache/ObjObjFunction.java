package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface ObjObjFunction<T, U> {
    void apply(T t, U u);
}
