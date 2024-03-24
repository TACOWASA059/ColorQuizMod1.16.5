package com.github.tacowasa059.colorquizmod.client.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

public class ModRenderType extends RenderType{

    public ModRenderType(String name, VertexFormat format, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable runnablePre, Runnable runnablePost) {
        super(name, format, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, runnablePre, runnablePost);
    }


    public static final RenderType OVERLAY_FACE = makeType("overlay_faces",
            DefaultVertexFormats.POSITION_COLOR, GL11.GL_QUADS, 256,
                    RenderType.State.getBuilder().line(new LineState(OptionalDouble.of(1.0)))
                    .layer(RenderState.POLYGON_OFFSET_LAYERING)
                    .transparency(TRANSLUCENT_TRANSPARENCY)
                    .texture(NO_TEXTURE)
                    .depthTest(RenderState.DEPTH_LEQUAL)
                    .cull(CULL_DISABLED)
                    .lightmap(LIGHTMAP_DISABLED)
                    .writeMask(COLOR_WRITE)
                    .build(false));
}
