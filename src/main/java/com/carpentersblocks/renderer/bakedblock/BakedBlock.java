package com.carpentersblocks.renderer.bakedblock;

import java.util.function.Function;

import com.carpentersblocks.renderer.AbstractBakedModel;
import com.carpentersblocks.renderer.RenderPkg;
import com.carpentersblocks.renderer.helper.RenderHelper;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.IModelState;

public class BakedBlock extends AbstractBakedModel {
	
    public BakedBlock(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		super(state, format, bakedTextureGetter);
	}
    
	@Override
	protected void fillQuads(RenderPkg renderPkg) {
		RenderHelper renderHelper = new RenderHelper();
		renderPkg.add(renderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(renderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(renderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(renderHelper.getQuadZPos(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(renderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_quartered));
		renderPkg.add(renderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_quartered));
    }
	
}
