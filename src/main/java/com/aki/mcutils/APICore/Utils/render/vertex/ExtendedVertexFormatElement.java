package com.aki.mcutils.APICore.Utils.render.vertex;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;

public interface ExtendedVertexFormatElement {

	void setVertexFormat(VertexFormat vertexFormat);

	VertexFormat getVertexFormat();

	void setOffset(int offset);

	int getOffset();

	void setNext(VertexFormatElement next);

	VertexFormatElement getNext();

	VertexConsumer getVertexConsumer();

}
