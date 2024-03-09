package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface ObjInt2IntFunction<T> {

    int apply(T t, int v);

}
