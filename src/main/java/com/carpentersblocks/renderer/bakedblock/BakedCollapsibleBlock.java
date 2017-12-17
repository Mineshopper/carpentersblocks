package com.carpentersblocks.renderer.bakedblock;

import java.util.function.Function;

import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.QuadContainer;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.renderer.helper.RenderHelperCollapsible;
import com.carpentersblocks.util.block.CollapsibleUtil;
import com.carpentersblocks.util.registry.SpriteRegistry;

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
		CollapsibleUtil util = new CollapsibleUtil(_cbMetadata);
		util.computeOffsets();
    	if (!util.isFullCube()) {
    		quadContainer.add(RenderHelperCollapsible.getQuadYPosZNeg(util, SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(RenderHelperCollapsible.getQuadYPosZPos(util, SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(RenderHelperCollapsible.getQuadXNegYPos(util, SpriteRegistry.sprite_uncovered_solid));
    		quadContainer.add(RenderHelperCollapsible.getQuadXPosYPos(util, SpriteRegistry.sprite_uncovered_solid));
		} else {
			quadContainer.add(RenderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_solid));
		}
    	quadContainer.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(RenderHelperCollapsible.getQuadZNeg(util, SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(RenderHelperCollapsible.getQuadZPos(util, SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(RenderHelperCollapsible.getQuadXNeg(util, SpriteRegistry.sprite_uncovered_solid));
        quadContainer.add(RenderHelperCollapsible.getQuadXPos(util, SpriteRegistry.sprite_uncovered_solid));
	}
	
}
