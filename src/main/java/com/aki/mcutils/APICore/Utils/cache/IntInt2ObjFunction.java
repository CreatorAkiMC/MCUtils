package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface IntInt2ObjFunction<R> {

    R apply(int x, int y);

}
