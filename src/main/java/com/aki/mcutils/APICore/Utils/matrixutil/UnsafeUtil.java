package com.aki.mcutils.APICore.Utils.matrixutil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.*;
import java.util.Objects;
import java.util.stream.Stream;

import com.oracle.nio.BufferSecrets;
import sun.misc.Unsafe;

public class UnsafeUtil {

	public static final Unsafe UNSAFE;

	static {
		try {
			Field field = Stream.of(Unsafe.class.getDeclaredFields())
					.filter(f -> f.getType() == Unsafe.class)
					.filter(f -> Modifier.isStatic(f.getModifiers()))
					.filter(f -> Modifier.isFinal(f.getModifiers()))
					.findFirst()
					.orElseThrow(NullPointerException::new);
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new UnsupportedOperationException("Failed to find sun.misc.Unsafe instance");
		}
	}
}
