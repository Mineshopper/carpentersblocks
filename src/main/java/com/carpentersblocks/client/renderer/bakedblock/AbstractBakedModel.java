package com.carpentersblocks.client.renderer.bakedblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.RenderConstants;
import com.carpentersblocks.client.renderer.RenderPkg;
import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.model.TransformationHelper;

public abstract class AbstractBakedModel implements IDynamicBakedModel {
	
	private final VertexFormat _vertexFormat;
	
	private List<BakedQuad> _cachedInventoryQuads;
    
    /**
     * Class constructor.
     * 
     * @param vertexFormat the vertex format
     */
    public AbstractBakedModel(VertexFormat vertexFormat) {
    	this._vertexFormat = vertexFormat;
    	this._cachedInventoryQuads = new ArrayList<>();
    }
    
    /**
     * Used for gathering non-inventory quads.
     * 
     * @param blockState the block state
     * @param side the side
     * @param rand the random
     * @param extraData model data
     */
    @Override
    public List<BakedQuad> getQuads(BlockState blockState, Direction side, Random rand, IModelData extraData) {
    	if (side == null && !(extraData instanceof EmptyModelData)) {
	    	RenderPkg renderPkg = new RenderPkg(_vertexFormat, extraData, blockState, side, rand);
	    	fillQuads(renderPkg);
	    	return renderPkg.getQuads();
    	}
    	return Collections.emptyList();
    }
    
    /**
     * Used for gathering inventory quads.
     * <p>
     * These quads are cached.
     * 
     * @param blockState the block state
     * @param side the direction
     * @param rand a random
     */
    @Override
   	public List<BakedQuad> getQuads(BlockState blockState, Direction side, Random rand) {
    	if (!_cachedInventoryQuads.isEmpty()) {
    		return _cachedInventoryQuads;
    	}
    	RenderPkg renderPkg = new RenderPkg(_vertexFormat, side, rand);
    	fillInventoryQuads(renderPkg);
    	_cachedInventoryQuads.addAll(renderPkg.getInventoryQuads());
    	return _cachedInventoryQuads;
   	}
    
    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.EMPTY;
    }
    
    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }
    
    @Override
	public boolean usesBlockLight() {
    	// TODO: figure out what this is
		return false;
	}
    
    @Override
    public TextureAtlasSprite getParticleIcon() {
    	return TextureAtlasSprites.sprite_uncovered_full;
    }
	
	@Override
	public IBakedModel handlePerspective(TransformType transformType, MatrixStack mat) {
		TransformationHelper.toTransformation(RenderConstants.itemCameraTransforms.getTransform(transformType)).push(mat);
		return getBakedModel();
	}
	
    /**
     * Fills quad container with all quads.
     * 
     * @param renderPkg the quad container
     */
    protected abstract void fillQuads(RenderPkg renderPkg);
    
    /**
     * Fills quad container with all inventory quads.
     * 
     * @param renderPkg the quad container
     */
    protected abstract void fillInventoryQuads(RenderPkg renderPkg);
    
}