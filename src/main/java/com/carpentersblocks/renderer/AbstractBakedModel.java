package com.carpentersblocks.renderer;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.block.BlockUtil;
import com.carpentersblocks.util.registry.ConfigRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;

public abstract class AbstractBakedModel implements IBakedModel {
	
    private VertexFormat _vertexFormat;
    
    public AbstractBakedModel(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this._vertexFormat = vertexFormat;
    }
    
    public VertexFormat getVertexFormat() {
    	if (ConfigRegistry.enableOptifineCompatibility) {
    		return _vertexFormat;
    	} else {
	    	// Add TEX_2S to be able to adjust quad brightness; this breaks compatibility with Optifine
	    	return new VertexFormat(_vertexFormat).addElement(DefaultVertexFormats.TEX_2S);
    	}
    }
    
    public abstract List<BakedQuad> getInventoryQuads(RenderPkg renderPkg);
    
    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing facing, long rand) {
    	if (blockState == null) {
    		RenderPkg renderPkg = new RenderPkg(_vertexFormat, facing, rand);
    		return this.getInventoryQuads(renderPkg);
    	}
    	if (!BlockUtil.validateBlockState(blockState) || facing != null || MinecraftForgeClient.getRenderLayer() == null) {
    		// TODO: Look into at net.minecraftforge.client.ForgeHooksClient.getDamageModel(ForgeHooksClient.java:668)
    		// Damage is not currently rendering
    		return Collections.emptyList();
    	}
    	RenderPkg renderPkg = new RenderPkg(this.getVertexFormat(), blockState, facing, rand);
    	fillQuads(renderPkg);
    	return renderPkg.getQuads();
    }
    
    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
    	return SpriteRegistry.sprite_uncovered_full;
    }
    
    /**
     * Fills quad container with all block quads.
     * 
     * @param quadContainer the quad container
     */
    protected abstract void fillQuads(RenderPkg renderPkg);
    
    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
    	return Pair.of(this, IConstants.perspectiveMatrix[cameraTransformType.ordinal()]);
    }
    
}