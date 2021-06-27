package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.RenderPkg;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopeWedgeExterior extends AbstractBakedSlope {
	
	public BakedSlopeWedgeExterior(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		fillWedgeExterior(renderPkg);
	}
	
}