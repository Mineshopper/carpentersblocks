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
    		quadContainer.add(_renderHelper.getQuadYPosZNeg(SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(_renderHelper.getQuadYPosZPos(SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(_renderHelper.getQuadXNegYPos(SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(_renderHelper.getQuadXPosYPos(SpriteRegistry.sprite_uncovered_solid));
		} else {
			quadContainer.add(_renderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_solid));
		}
    	quadContainer.add(_renderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(_renderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(_renderHelper.getQuadZPos(SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(_renderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(_renderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_solid));
	}
	
}
