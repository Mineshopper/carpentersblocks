package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.RenderPkg;
import com.carpentersblocks.client.renderer.helper.RenderHelper;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedBlock extends AbstractBakedModel {
	
	public BakedBlock(VertexFormat vertexFormat) {
		super(vertexFormat);
	}

	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		fillQuads(renderPkg);
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadZNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadZPos(TextureAtlasSprites.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadXNeg(TextureAtlasSprites.sprite_uncovered_quartered));
		renderPkg.add(RenderHelper.getQuadXPos(TextureAtlasSprites.sprite_uncovered_quartered));
    }
	
}
