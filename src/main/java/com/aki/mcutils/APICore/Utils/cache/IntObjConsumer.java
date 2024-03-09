package com.aki.mcutils.APICore.Utils.cache;

@FunctionalInterface
public interface IntObjConsumer<T> {

    void accept(int i, T t);

}
