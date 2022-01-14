package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.block.IStateImplementor;
import com.carpentersblocks.client.renderer.ReferenceQuads;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedPressurePlate extends AbstractBakedModel {
	
	public BakedPressurePlate(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(ReferenceQuads referenceQuads) {
		referenceQuads.addAll(((IStateImplementor)CbBlocks.blockPressurePlate).getStateMap().getInventoryQuads());
	}
	
	@Override
	protected void fillQuads(ReferenceQuads referenceQuads) {
		//AbstractState state = (AbstractState) RenderPkg.getThreadedProperty(Property.CB_STATE);
		//referenceQuads.addAll(state.getQuads());
	}
	
}