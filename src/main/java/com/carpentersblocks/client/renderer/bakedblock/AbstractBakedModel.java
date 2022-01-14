package com.carpentersblocks.client.renderer.bakedblock;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.client.renderer.ReferenceQuads;
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
    
    /**
     * Class constructor.
     * 
     * @param vertexFormat the vertex format
     */
    public AbstractBakedModel(VertexFormat vertexFormat) {
    	this._vertexFormat = vertexFormat;
    }
    
    /**
     * Used for gathering non-inventory quads.
     * 
     * @param blockState the block state
     * @param side the side
     * @param rand a random
     * @param extraData model data
     */
    @Override
    public List<BakedQuad> getQuads(BlockState blockState, Direction side, Random rand, IModelData extraData) {
    	if (side == null && !(extraData instanceof EmptyModelData)) {
	    	return doGetQuads(blockState, side, rand, extraData);
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
    	return doGetQuads(blockState, side, rand, null);
   	}
    
    /**
     * Handles getting quads based on presence of extra data.
     * 
     * @param blockState the block state
     * @param side the side
     * @param rand a random
     * @param extraData model data
     * @return
     */
    private List<BakedQuad> doGetQuads(BlockState blockState, Direction side, Random rand, @Nullable IModelData extraData) {
    	RenderPkg renderPkg;
    	if (extraData == null) {
    		renderPkg = new RenderPkg(_vertexFormat, rand);
    	} else {
    		renderPkg = new RenderPkg(_vertexFormat, extraData, blockState, rand);
    	}
    	RenderPkg.THREAD_LOCAL_RENDER_PKG.set(renderPkg);
    	if (extraData == null) {
    		fillInventoryQuads(renderPkg.getReferenceQuads());
    	} else {
    		fillQuads(renderPkg.getReferenceQuads());
    	}
    	renderPkg.lockReferenceQuads();
    	List<BakedQuad> bakedQuads = extraData == null ? renderPkg.getBlockItemBakedQuads() : renderPkg.getBlockBakedQuads();
    	RenderPkg.THREAD_LOCAL_RENDER_PKG.remove();
    	return bakedQuads;
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
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }
    
    @Override
	public boolean usesBlockLight() {
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
     * Fills reference quads with all quads.
     * <p>
     * Any world block transformations, like rotation, should
     * also in this step.
     * 
     * @param quadContainer the quad container
     */
    protected abstract void fillQuads(ReferenceQuads referenceQuads);
    
    /**
     * Fills reference quads with all inventory quads.
     * 
     * @param quadContainer the quad container
     */
    protected abstract void fillInventoryQuads(ReferenceQuads referenceQuads);
    
}