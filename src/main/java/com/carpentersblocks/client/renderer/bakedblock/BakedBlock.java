package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.ReferenceQuads;
import com.carpentersblocks.client.renderer.helper.RenderHelper;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedBlock extends AbstractBakedModel {
	
	public BakedBlock(VertexFormat vertexFormat) {
		super(vertexFormat);
	}

	@Override
	protected void fillInventoryQuads(ReferenceQuads referenceQuads) {
		fillQuads(referenceQuads);
	}
	
	@Override
	protected void fillQuads(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		referenceQuads.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_quartered));
		referenceQuads.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		referenceQuads.add(RenderHelper.getQuadZPos(TextureAtlasSprites.sprite_uncovered_quartered));
		referenceQuads.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		referenceQuads.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_quartered));
    }
	
}
