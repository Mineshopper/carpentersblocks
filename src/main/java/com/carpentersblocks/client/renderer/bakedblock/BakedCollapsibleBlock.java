package com.carpentersblocks.client.renderer.bakedblock;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.ReferenceQuads;
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
	protected void fillInventoryQuads(ReferenceQuads referenceQuads) {
		referenceQuads.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_solid).offset(0.0D, -1.0D, 0.0D));
		referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_solid));
	}
	
	@Override
	protected void fillQuads(ReferenceQuads referenceQuads) {
		CollapsibleMetadata util = new CollapsibleMetadata(RenderPkg.get().getCbMetadata());
		util.computeOffsets();
    	if (!util.isFullCube()) {
    		referenceQuads.add(RenderHelperCollapsible.getQuadYPosZNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    		referenceQuads.add(RenderHelperCollapsible.getQuadYPosZPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    		referenceQuads.add(RenderHelperCollapsible.getQuadXNegYPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    		referenceQuads.add(RenderHelperCollapsible.getQuadXPosYPos(util, TextureAtlasSprites.sprite_uncovered_solid));
		} else {
			referenceQuads.add(RenderHelper.getQuadYPos(TextureAtlasSprites.sprite_uncovered_solid));
		}
    	referenceQuads.add(RenderHelper.getQuadYNeg(TextureAtlasSprites.sprite_uncovered_solid));
    	referenceQuads.add(RenderHelperCollapsible.getQuadZNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    	referenceQuads.add(RenderHelperCollapsible.getQuadZPos(util, TextureAtlasSprites.sprite_uncovered_solid));
    	referenceQuads.add(RenderHelperCollapsible.getQuadXNeg(util, TextureAtlasSprites.sprite_uncovered_solid));
    	referenceQuads.add(RenderHelperCollapsible.getQuadXPos(util, TextureAtlasSprites.sprite_uncovered_solid));
	}
	
}
