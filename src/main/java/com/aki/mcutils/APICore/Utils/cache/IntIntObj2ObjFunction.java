package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface IntIntObj2ObjFunction<T, R> {

    R apply(int x, int y, T t);

}
