package com.aki.mcutils.renderer.render_util;

import com.aki.mcutils.APICore.Utils.matrixutil.Matrix4f;
import com.aki.mcutils.APICore.Utils.memory.NIOBufferUtil;
import com.aki.mcutils.APICore.Utils.render.GLUtils;
import com.aki.mcutils.APICore.program.shader.ShaderHelper;
import com.aki.mcutils.APICore.program.shader.ShaderObject;
import com.aki.mcutils.APICore.program.shader.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class BoundingBoxHelper {

    private static final byte[] VERTEX_DATA = {
            0, 0, 0,
            0, 0, 1,
            0, 1, 0,
            0, 1, 1,
            1, 0, 0,
            1, 0, 1,
            1, 1, 0,
            1, 1, 1
    };
    private static final byte[] INDICES = {
            0, 4, 5, 1,
            3, 7, 6, 2,
            4, 0, 2, 6,
            1, 5, 7, 3,
            0, 1, 3, 2,
            5, 4, 6, 7
    };
    private static final String A_POS = "a_Pos";
    private static final String U_MATRIX = "u_ModelViewProjectionMatrix";
    private static BoundingBoxHelper instance;
    private final int cubeVertexBuffer;
    private final int quadsCubeIndexBuffer;

    private final ShaderProgram program = getProgram();

    public ShaderProgram getProgram() {
        ShaderProgram Cprogram = new ShaderProgram();
        try
        {
            Cprogram.attachShader(new ShaderObject(ShaderObject.ShaderType.VERTEX, ShaderHelper.readShader(ShaderHelper.getStream("/assets/mcutils/shaders/debug_box.vert"))));
            Cprogram.attachShader(new ShaderObject(ShaderObject.ShaderType.FRAGMENT, ShaderHelper.readShader(ShaderHelper.getStream("/assets/mcutils/shaders/debug_box.frag"))));
        } catch(IOException e)
        {
            e.printStackTrace();
        }

        return Cprogram;
    }

    public BoundingBoxHelper() {
        cubeVertexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVertexBuffer);
        NIOBufferUtil.tempByteBuffer(VERTEX_DATA, buffer -> GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        quadsCubeIndexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadsCubeIndexBuffer);
        NIOBufferUtil.tempByteBuffer(INDICES, buffer -> GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW));
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public static BoundingBoxHelper getInstance() {
        if (instance == null) {
            instance = new BoundingBoxHelper();
        }
        return instance;
    }

    public void drawRenderBoxes(double partialTicks) {
        this.setupRenderState();
        program.useShader(cache -> {
            cache.glUniform1I(A_POS, 0);
        });

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, quadsCubeIndexBuffer);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, cubeVertexBuffer);
        GL20.glVertexAttribPointer(program.getAttributeLocation(A_POS), 3, GL11.GL_BYTE, false, 0, 0);
        GL20.glEnableVertexAttribArray(program.getAttributeLocation(A_POS));

        Minecraft mc = Minecraft.getMinecraft();
        EntityUtil.entityIterable(mc.world.loadedEntityList).forEach(entity -> {
            if (entity == mc.getRenderViewEntity()) {
                return;
            }

            MutableAABB aabb = ((IEntityRendererCache) entity).getCachedBoundingBox();
            if (!aabb.isVisible(GLUtils.getFrustum()) || aabb.getLongestDistance() > 1024) {
                return;
            }

            Matrix4f matrix = GLUtils.getProjectionModelViewMatrix().copy();
            matrix.translate(
                    (float) (aabb.minX() - GLUtils.getCameraEntityX()),
                    (float) (aabb.minY() - GLUtils.getCameraEntityY()),
                    (float) (aabb.minZ() - GLUtils.getCameraEntityZ()));
            matrix.scale((float) aabb.sizeX(), (float) aabb.sizeY(), (float) aabb.sizeZ());
            GLUtils.setMatrix(program.getUniformLocation(U_MATRIX), matrix);

            GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        });

        TileEntityUtil.processTileEntities(mc.world, tileEntity -> {
            MutableAABB aabb = ((ITileEntityRendererCache) tileEntity).getCachedBoundingBox();
            if (!aabb.isVisible(GLUtils.getFrustum()) || aabb.getLongestDistance() > 1024) {
                return;
            }

            Matrix4f matrix = GLUtils.getProjectionModelViewMatrix().copy();
            matrix.translate(
                    (float) (aabb.minX() - GLUtils.getCameraEntityX()),
                    (float) (aabb.minY() - GLUtils.getCameraEntityY()),
                    (float) (aabb.minZ() - GLUtils.getCameraEntityZ()));
            matrix.scale((float) aabb.sizeX(), (float) aabb.sizeY(), (float) aabb.sizeZ());
            GLUtils.setMatrix(program.getUniformLocation(U_MATRIX), matrix);

            GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glDrawElements(GL11.GL_QUADS, 24, GL11.GL_UNSIGNED_BYTE, 0);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        });

        GL20.glDisableVertexAttribArray(program.getAttributeLocation(A_POS));
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        program.releaseShader();

        this.clearRenderState();
    }

    private void setupRenderState() {
        GLUtils.saveShaderGLState();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        GlStateManager.enableDepth();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(false);

        GlStateManager.disableCull();

        GlStateManager.colorMask(true, true, true, true);
    }

    private void clearRenderState() {
        GLUtils.restoreShaderGLState();
    }

}
