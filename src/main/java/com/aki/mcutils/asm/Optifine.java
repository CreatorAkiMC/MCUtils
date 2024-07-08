package com.aki.mcutils.asm;

import com.aki.mcutils.APICore.Utils.reflectors.ReflectionConstructor;
import com.aki.mcutils.APICore.Utils.reflectors.ReflectionField;
import com.aki.mcutils.APICore.Utils.reflectors.ReflectionMethod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.render.RenderEnv;

import javax.vecmath.Matrix4f;
import java.util.List;

public class Optifine {

    private static final ReflectionMethod<Boolean> IS_SHADERS = new ReflectionMethod<>("Config", "isShaders", "isShaders");
    private static final ReflectionMethod<Boolean> IS_NATURALTEXTURES = new ReflectionMethod<>("Config", "isNaturalTextures", "isNaturalTextures");
    private static final ReflectionMethod<Boolean> IS_CONNECTED_TEXTURES = new ReflectionMethod<>("Config", "isConnectedTextures", "isConnectedTextures");
    private static final ReflectionField<Boolean> IS_SHADOW_PASS = new ReflectionField<>("net.optifine.shaders.Shaders", "isShadowPass", "isShadowPass");
    private static final ReflectionMethod<Void> NEXT_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextEntity", "nextEntity", Entity.class);
    private static final ReflectionMethod<BakedQuad> GetNaturalTexture = new ReflectionMethod<>("net.optifine.NaturalTextures", "getNaturalTexture", "getNaturalTexture", BlockPos.class, BakedQuad.class);
    private static final ReflectionMethod<Boolean> IsBreakingAnimation = new ReflectionMethod<>("net.optifine.render.RenderEnv", "isBreakingAnimation", "isBreakingAnimation", BakedQuad.class);
    private static final ReflectionMethod<BakedQuad[]> GetArrayQuadsCtm = new ReflectionMethod<>("net.optifine.render.RenderEnv", "getArrayQuadsCtm", "getArrayQuadsCtm", BakedQuad.class);
    private static final ReflectionMethod<RenderEnv> GetRenderEnv = new ReflectionMethod<>("net.minecraft.client.renderer.BufferBuilder", "getRenderEnv", "getRenderEnv", IBlockAccess.class, IBlockState.class, BlockPos.class);
    private static final ReflectionMethod<List<BakedQuad>> GetNaturalBakedArray = new ReflectionMethod<>("net.optifine.model.BlockModelCustomizer", "getRenderQuads", "getRenderQuads", List.class, IBlockAccess.class, IBlockState.class, BlockPos.class, EnumFacing.class, long.class, RenderEnv.class);
    private static final ReflectionMethod<IBakedModel> GetBakedModel = new ReflectionMethod<>("net.optifine.model.BlockModelCustomizer", "getRenderModel", "getRenderModel", IBakedModel.class, IBlockState.class, RenderEnv.class);
    private static final ReflectionMethod<BakedQuad[]> GetConnectedTexture = new ReflectionMethod<>("net.optifine.ConnectedTextures", "getConnectedTexture", "getConnectedTexture", IBlockAccess.class, IBlockState.class, BlockPos.class, BakedQuad.class, RenderEnv.class);
    private static final ReflectionField<Entity> RENDERED_ENTITY = new ReflectionField<>(RenderGlobal.class, "renderedEntity", "renderedEntity");
    private static final ReflectionMethod<Boolean> IS_FAST_RENDER = new ReflectionMethod<>("Config", "isFastRender", "isFastRender");
    private static final ReflectionMethod<Boolean> IS_ANTIALIASING = new ReflectionMethod<>("Config", "isAntialiasing", "isAntialiasing");
    private static final ReflectionMethod<Void> BEGIN_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "beginEntitiesGlowing", "beginEntitiesGlowing");
    private static final ReflectionMethod<Void> END_ENTITIES_GLOWING = new ReflectionMethod<>("net.optifine.shaders.Shaders", "endEntitiesGlowing", "endEntitiesGlowing");
    private static final ReflectionMethod<Void> NEXT_BLOCK_ENTITY = new ReflectionMethod<>("net.optifine.shaders.Shaders", "nextBlockEntity", "nextBlockEntity", TileEntity.class);

    public static boolean isOptifineDetected() {
        return MCUtilsASMTransformer.OPTIFINE_INSIDE;
    }

    public static boolean isShaders() {
        return IS_SHADERS.invoke(null);
    }

    public static boolean isNaturalTextures() {
        return IS_NATURALTEXTURES.invoke(null);
    }
    public static boolean isConnectedTextures() {
        return IS_CONNECTED_TEXTURES.invoke(null);
    }
    public static boolean isBreakingAnimation(RenderEnv renderEnv, BakedQuad quad) {
        return IsBreakingAnimation.invoke(renderEnv, quad);
    }

    public static boolean isShadowPass() {
        return IS_SHADOW_PASS.getBoolean(null);
    }

    public static void nextEntity(Entity entity) {
        NEXT_ENTITY.invoke(null, entity);
    }

    public static BakedQuad getNaturalTexture(BlockPos pos, BakedQuad quad) {
        return GetNaturalTexture.invoke(null, pos, quad);
    }

    public static BakedQuad[] getArrayQuadsCtm(RenderEnv renderEnv, BakedQuad quad) {
        return GetArrayQuadsCtm.invoke(renderEnv, quad);
    }

    public static BakedQuad[] getConnectedTextures(IBlockAccess access, IBlockState state, BlockPos pos, BakedQuad quad, RenderEnv renderEnv) {
        return GetConnectedTexture.invoke(null, access, state, pos, quad, renderEnv);
    }

    public static RenderEnv getRenderEnv(IBlockAccess world, IBlockState state, BlockPos pos) {
        return GetRenderEnv.invoke(null, world, state, pos);
    }

    public static List<BakedQuad> getNaturalBakedArray(List<BakedQuad> quads, IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing, long rand, RenderEnv env) {
        return GetNaturalBakedArray.invoke(null, quads, world, state, pos, facing, rand, env);
    }

    public static IBakedModel getBakedModel(IBakedModel model, IBlockState state, RenderEnv renderEnv) {
        return GetBakedModel.invoke(null, model, state, renderEnv);
    }

    public static void setRenderedEntity(Entity entity) {
        RENDERED_ENTITY.set(Minecraft.getMinecraft().renderGlobal, entity);
    }

    public static boolean isFastRender() {
        return IS_FAST_RENDER.invoke(null);
    }

    public static boolean isAntialiasing() {
        return IS_ANTIALIASING.invoke(null);
    }

    public static void beginEntitiesGlowing() {
        BEGIN_ENTITIES_GLOWING.invoke(null);
    }

    public static void endEntitiesGlowing() {
        END_ENTITIES_GLOWING.invoke(null);
    }

    public static void nextBlockEntity(TileEntity tileEntity) {
        NEXT_BLOCK_ENTITY.invoke(null, tileEntity);
    }

}
