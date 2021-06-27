package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.RenderPkg;
import com.carpentersblocks.client.renderer.helper.RenderHelper;
import com.carpentersblocks.client.renderer.helper.RenderHelperCollapsible;
import com.carpentersblocks.util.metadata.CollapsibleMetadata;

import net.minecraft.client.renderer.vertex.VertexFormat;

public class BakedCollapsibleBlock extends AbstractBakedModel {
	
	public BakedCollapsibleBlock(VertexFormat vertexFormat) {
		super(vertexFormat);
	}
	
	@Override
	protected void fillInventoryQuads(RenderPkg renderPkg) {
		renderPkg.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_solid).offset(0.0D, -1.0D, 0.0D));
		renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_solid));
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		CollapsibleMetadata util = new CollapsibleMetadata(renderPkg.getCbMetadata());
		util.computeOffsets();
    	if (!util.isFullCube()) {
    		renderPkg.add(RenderHelperCollapsible.getQuadYPosZNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadYPosZPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadXNegYPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadXPosYPos(util, TextureAtlasSprites.sprite_uncovered_solid));
		} else {
			renderPkg.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_solid));
		}
    	renderPkg.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadZNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadZPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadXNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadXPos(util, TextureAtlasSprites.sprite_uncovered_solid));
	}
	
}
