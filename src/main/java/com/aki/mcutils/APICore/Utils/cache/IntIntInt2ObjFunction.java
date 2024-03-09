package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface IntIntInt2ObjFunction<R> {

    R apply(int x, int y, int z);

}
