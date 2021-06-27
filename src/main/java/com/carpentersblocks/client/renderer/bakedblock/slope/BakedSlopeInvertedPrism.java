package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.RenderPkg;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopeInvertedPrism extends AbstractBakedSlope {
	
	public BakedSlopeInvertedPrism(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		fillInvertedPrism(renderPkg, 0);
	}
	
}