package com.carpentersblocks.client.renderer.bakedblock.slope;

import com.carpentersblocks.client.renderer.RenderPkg;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedSlopePrismWedge extends AbstractBakedSlope {
	
	public BakedSlopePrismWedge(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		fillPrismWedge(renderPkg);
	}
	
}