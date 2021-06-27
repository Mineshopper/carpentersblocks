package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.RenderPkg;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopeObliqueExterior extends AbstractBakedSlope {
	
	public BakedSlopeObliqueExterior(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		fillObliqueExterior(renderPkg);
	}
	
}