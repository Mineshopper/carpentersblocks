package com.carpentersblocks.util.states;

import net.minecraft.client.renderer.RenderType;

enum RenderLayerType {

	SOLID(RenderType.solid()),
	CUTOUT_MIPPED(RenderType.cutoutMipped()),
	CUTOUT(RenderType.cutout()),
	TRANSLUCENT(RenderType.translucent());
	
	private RenderType renderType;
	
	private RenderLayerType(RenderType renderType) {
		this.renderType = renderType;
	}
	
	public RenderType getRenderType() {
		return this.renderType;
	}
	
}
