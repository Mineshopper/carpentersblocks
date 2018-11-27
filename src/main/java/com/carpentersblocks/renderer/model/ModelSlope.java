package com.carpentersblocks.renderer.model;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import com.carpentersblocks.renderer.bakedblock.BakedSlope;
import com.carpentersblocks.util.registry.SpriteRegistry;
import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelSlope implements IModel {

	private String _type;
	
	public ModelSlope(String type) {
		this._type = type;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of(SpriteRegistry.RESOURCE_UNCOVERED_FULL);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new BakedSlope(state, format, bakedTextureGetter, _type);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

}
