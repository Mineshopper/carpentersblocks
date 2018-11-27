package com.carpentersblocks.renderer.bakedblock;

import java.util.List;
import java.util.function.Function;

import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.RenderPkg;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.renderer.helper.RenderHelperCollapsible;
import com.carpentersblocks.util.block.CollapsibleUtil;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BakedCollapsibleBlock extends AbstractBakedModel {

	private static List<BakedQuad> _inventoryBakedQuads;
	
    public BakedCollapsibleBlock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
    
	@Override
	public List<BakedQuad> getInventoryQuads(RenderPkg renderPkg) {
		if (_inventoryBakedQuads == null) {
			renderPkg.add(RenderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_solid).offset(0.0D, -1.0D, 0.0D));
			renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_solid));
			_inventoryBakedQuads = renderPkg.getInventoryQuads();
		}
		return _inventoryBakedQuads;
	}
	
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		CollapsibleUtil util = new CollapsibleUtil(renderPkg.getData());
		util.computeOffsets();
    	if (!util.isFullCube()) {
    		renderPkg.add(RenderHelperCollapsible.getQuadYPosZNeg(util, SpriteRegistry.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadYPosZPos(util, SpriteRegistry.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadXNegYPos(util, SpriteRegistry.sprite_uncovered_solid));
    		renderPkg.add(RenderHelperCollapsible.getQuadXPosYPos(util, SpriteRegistry.sprite_uncovered_solid));
		} else {
			renderPkg.add(RenderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_solid));
		}
    	renderPkg.add(RenderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadZNeg(util, SpriteRegistry.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadZPos(util, SpriteRegistry.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadXNeg(util, SpriteRegistry.sprite_uncovered_solid));
    	renderPkg.add(RenderHelperCollapsible.getQuadXPos(util, SpriteRegistry.sprite_uncovered_solid));
	}
	
}
