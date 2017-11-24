package com.carpentersblocks.renderer;

import java.util.function.Function;

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
	protected void fillQuads(QuadContainer quadContainer) {
		RenderHelper renderHelper = new RenderHelper();
		quadContainer.add(renderHelper.getQuadYNeg(SpriteRegistry.sprite_uncovered_full));
		quadContainer.add(renderHelper.getQuadYPos(SpriteRegistry.sprite_uncovered_full));
		quadContainer.add(renderHelper.getQuadZNeg(SpriteRegistry.sprite_uncovered_full));
		quadContainer.add(renderHelper.getQuadZPos(SpriteRegistry.sprite_uncovered_full));
		quadContainer.add(renderHelper.getQuadXNeg(SpriteRegistry.sprite_uncovered_full));
		quadContainer.add(renderHelper.getQuadXPos(SpriteRegistry.sprite_uncovered_full));
    }
	
}
