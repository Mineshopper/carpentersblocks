package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.ReferenceQuads;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopeWedge extends AbstractBakedSlope {
	
	public BakedSlopeWedge(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(ReferenceQuads referenceQuads) {
		fillWedge(referenceQuads);
	}
	
}