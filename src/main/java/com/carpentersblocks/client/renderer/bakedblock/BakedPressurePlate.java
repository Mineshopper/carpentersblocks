package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.block.IStateImplementor;
import com.carpentersblocks.client.renderer.RenderPkg;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedPressurePlate extends AbstractBakedModel {
	
	public BakedPressurePlate(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		renderPkg.addAll(((IStateImplementor)CbBlocks.blockPressurePlate).getStateMap().getInventoryQuads());
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		//AbstractState state = (AbstractState) RenderPkg.getThreadedProperty(Property.CB_STATE);
		//renderPkg.addAll(state.getQuads());
	}
	
}