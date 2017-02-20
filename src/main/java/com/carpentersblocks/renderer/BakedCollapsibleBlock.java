package com.carpentersblocks.renderer;

import com.carpentersblocks.renderer.helper.RenderHelperCollapsible;
import com.carpentersblocks.util.block.CollapsibleUtil;
import com.carpentersblocks.util.registry.SpriteRegistry;
import com.google.common.base.Function;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedCollapsibleBlock extends AbstractBakedModel {

    public BakedCollapsibleBlock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
	
	@Override
	protected void fillQuads(QuadContainer quadContainer) {
		RenderHelperCollapsible _renderHelper = new RenderHelperCollapsible(this);
		CollapsibleUtil util = _renderHelper.getUtil();
    	if (!util.isFullCube()) {
    		quadContainer.add(_renderHelper.getQuadYPosZNeg());
    		quadContainer.add(_renderHelper.getQuadYPosZPos());
    		quadContainer.add(_renderHelper.getQuadXNegYPos());
    		quadContainer.add(_renderHelper.getQuadXPosYPos());
		} else {
			quadContainer.add(_renderHelper.getQuadYPos());
		}
    	quadContainer.add(_renderHelper.getQuadYNeg());
        quadContainer.add(_renderHelper.getQuadZNeg());
        quadContainer.add(_renderHelper.getQuadZPos());
        quadContainer.add(_renderHelper.getQuadXNeg());
        quadContainer.add(_renderHelper.getQuadXPos());
	}

	@Override
	protected TextureAtlasSprite getUncoveredSprite() {
		return SpriteRegistry.sprite_uncovered_solid;
	}
	
}
