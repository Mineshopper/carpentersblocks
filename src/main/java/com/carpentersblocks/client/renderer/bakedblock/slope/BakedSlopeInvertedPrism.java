package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.ReferenceQuads;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopeInvertedPrism extends AbstractBakedSlope {
	
	public BakedSlopeInvertedPrism(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(ReferenceQuads referenceQuads) {
		fillInvertedPrism(referenceQuads, 0);
	}
	
}