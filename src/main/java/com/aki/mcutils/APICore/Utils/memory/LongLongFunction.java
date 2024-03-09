package com.aki.mcutils.APICore.Utils.memory;

@FunctionalInterface
public interface LongLongFunction<T> {

	T apply(long x, long y);

}
