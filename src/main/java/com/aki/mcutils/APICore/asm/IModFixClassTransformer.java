package com.aki.mcutils.APICore.asm;

public interface IModFixClassTransformer {

	byte[] transform(String obfName, String name, byte[] basicClass);

}
