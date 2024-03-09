package com.aki.mcutils.asm;

import com.aki.mcutils.APICore.asm.ASMUtil;
import com.aki.mcutils.APICore.asm.HashMapClassNodeClassTransformer;
import com.aki.mcutils.APICore.asm.IClassTransformerRegistry;
import com.aki.mcutils.MCUtils;
import com.aki.mcutils.mixin.MCUtilsLoadPlugins;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class MCUtilsASMTransformer extends HashMapClassNodeClassTransformer implements IClassTransformer {
    public static final boolean OPTIFINE_INSIDE;
    static {
        boolean flag = false;
        try {
            Class.forName("optifine.OptiFineClassTransformer", false, MCUtilsLoadPlugins.class.getClassLoader());
            flag = true;
        } catch (ClassNotFoundException e) {
            // ignore
        }
        OPTIFINE_INSIDE = flag;
    }

    @Override
    protected void registerTransformers(IClassTransformerRegistry registry) {
        // @formatter:off
        registry.add("net.minecraft.client.renderer.RenderGlobal", "renderEntities", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/client/renderer/culling/ICamera;F)V", "a", "(Lvg;Lbxy;F)V", ClassWriter.COMPUTE_FRAMES, methodNode -> {
            AbstractInsnNode targetNode1 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKESTATIC, "com/google/common/collect/Lists", "newArrayList", "()Ljava/util/ArrayList;").find();
            targetNode1 = ASMUtil.prev(targetNode1).type(LabelNode.class).find();
            AbstractInsnNode popNode1 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V", "bwx", "preDrawBatch", "()V").find();
            popNode1 = ASMUtil.prev(popNode1).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/profiler/Profiler", "endStartSection", "(Ljava/lang/String;)V", "rl", "c", "(Ljava/lang/String;)V").find();
            if (OPTIFINE_INSIDE) {
                popNode1 = ASMUtil.prev(popNode1).methodInsn(Opcodes.INVOKESTATIC, "net/optifine/shaders/Shaders", "endEntities", "()V").find();
                popNode1 = ASMUtil.prev(popNode1).type(JumpInsnNode.class).find();
            }
            popNode1 = ASMUtil.prev(popNode1).type(LabelNode.class).find();

            AbstractInsnNode targetNode2 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "preDrawBatch", "()V", "bwx", "preDrawBatch", "()V").find();
            if (OPTIFINE_INSIDE) {
                targetNode2 = ASMUtil.next(targetNode2).methodInsn(Opcodes.INVOKESTATIC, "net/minecraft/client/renderer/tileentity/TileEntitySignRenderer", "updateTextRenderDistance", "()V", "bxf", "updateTextRenderDistance", "()V").find();
            }
            targetNode2 = ASMUtil.next(targetNode2).type(LabelNode.class).find();
            AbstractInsnNode popNode2 = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/tileentity/TileEntityRendererDispatcher", "drawBatch", "(I)V", "bwx", "drawBatch", "(I)V").find();
            if (OPTIFINE_INSIDE) {
                popNode2 = ASMUtil.prev(popNode2).methodInsn(Opcodes.INVOKEVIRTUAL, "net/optifine/reflect/ReflectorMethod", "exists", "()Z").find();
            }
            popNode2 = ASMUtil.prev(popNode2).type(LabelNode.class).find();

            if(!MCUtils.isRenderLib) {
                methodNode.instructions.insert(targetNode1, ASMUtil.listOf(
                        new VarInsnNode(Opcodes.FLOAD, 3),
                        new MethodInsnNode(Opcodes.INVOKESTATIC, "com/aki/mcutils/renderer/entity/EntityRendererManager", "renderEntities", "(F)V", false),
                        new InsnNode(Opcodes.ICONST_0),
                        new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode1)
                ));

                methodNode.instructions.insert(targetNode2, ASMUtil.listOf(
                        new VarInsnNode(Opcodes.FLOAD, 3),
                        new MethodInsnNode(Opcodes.INVOKESTATIC, "com/aki/mcutils/renderer/tileentity/TileEntityRendererManager", "renderTileEntities", "(F)V", false),
                        new InsnNode(Opcodes.ICONST_0),
                        new JumpInsnNode(Opcodes.IFEQ, (LabelNode) popNode2)
                ));
            }
        });
        registry.add("net.minecraft.client.renderer.entity.RenderManager", "renderEntity", "(Lnet/minecraft/entity/Entity;DDDFFZ)V", "a", "(Lvg;DDDFFZ)V", 0, methodNode -> {
            AbstractInsnNode targetNode = ASMUtil.first(methodNode).methodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraft/client/renderer/entity/RenderManager", "getEntityRenderObject", "(Lnet/minecraft/entity/Entity;)Lnet/minecraft/client/renderer/entity/Render;", "bzf", "a", "(Lvg;)Lbzg;").find();

            if(!MCUtils.isRenderLib) {
                methodNode.instructions.insert(targetNode, ASMUtil.listOf(
                        new InsnNode(Opcodes.SWAP),
                        new InsnNode(Opcodes.POP),
                        new TypeInsnNode(Opcodes.CHECKCAST, "com/aki/mcutils/renderer/render_util/IEntityRendererCache"),
                        new MethodInsnNode(Opcodes.INVOKEINTERFACE, "com/aki/mcutils/renderer/render_util/IEntityRendererCache", "getRenderer", "()Lnet/minecraft/client/renderer/entity/Render;", true)
                ));
                methodNode.instructions.remove(targetNode);
            }
        });
    }
}
