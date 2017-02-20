package com.carpentersblocks.renderer;

import static net.minecraft.util.BlockRenderLayer.CUTOUT;
import static net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED;
import static net.minecraft.util.BlockRenderLayer.SOLID;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.attribute.AbstractAttribute;
import com.carpentersblocks.util.attribute.AbstractAttribute.Key;
import com.carpentersblocks.util.attribute.AttributeHelper;
import com.carpentersblocks.util.attribute.AttributeItemStack;
import com.carpentersblocks.util.attribute.AttributeString;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.attribute.EnumAttributeType;
import com.carpentersblocks.util.block.BlockUtil;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;
import com.carpentersblocks.util.registry.SpriteRegistry;
import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class AbstractBakedModel implements IBakedModel {

	private static final int NO_COLOR = 0xffffff;
    private final static List<BlockRenderLayer> LAYERS = Arrays.asList(new BlockRenderLayer[] { SOLID, CUTOUT_MIPPED, CUTOUT, TRANSLUCENT }); // These are arranged in render order
    private final static double SIDE_DEPTH = 1/16D;
    private final static double SNOW_SIDE_DEPTH = 1/8D;
    protected boolean _renderY;
    protected boolean _renderX;
    protected boolean _renderZ;
    protected AttributeHelper _cbAttrHelper;
    protected IBlockState _blockState;
    protected int _cbMetadata;
    protected BlockPos _blockPos;
    protected Boolean[] _renderFace;
    private long _rand;
    private QuadContainer _quadContainer;
    private BlockRenderLayer _uncoveredRenderLayer;
    private VertexFormat _format;
    private boolean _isSideCover;
    private double _sideDepth;
    private boolean _isSnowCover;
    EnumAttributeLocation _location;
    
    public AbstractBakedModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this._format = format;
    }
    
    public VertexFormat getFormat() {
    	return _format;
    }
    
    public IBlockState appendAttributeBlockState(CbTileEntity cbTileEntity, IBlockState blockState, EnumAttributeLocation location, EnumAttributeType type) {
    	if (cbTileEntity.getAttributeHelper().hasAttribute(location, type)) {
    		ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, type)).getModel();
    		Block block = Block.getBlockFromItem(itemStack.getItem());
    		IBlockState attrBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, type);
    		return ((IExtendedBlockState)blockState).withProperty(Property.ATTR_BLOCKSTATE, attrBlockState);
    	}
    	return blockState;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState blockState, EnumFacing facing, long rand) {
    	if (!BlockUtil.validateBlockState(blockState) || facing != null) {
    		return Collections.emptyList();
    	}
    	_rand = rand;
    	_blockState = blockState;
    	_cbMetadata = ((IExtendedBlockState)blockState).getValue(Property.CB_METADATA);
    	_blockPos = ((IExtendedBlockState)blockState).getValue(Property.BLOCK_POS);
    	_renderFace = ((IExtendedBlockState)blockState).getValue(Property.RENDER_FACE);
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute>) ((IExtendedBlockState)blockState).getValue(Property.ATTR_MAP));
    	_uncoveredRenderLayer = Minecraft.getMinecraft().theWorld.getBlockState(_blockPos).getBlock().getBlockLayer();
    	_quadContainer = new QuadContainer(_format, EnumAttributeLocation.HOST, false);
    	boolean hasSideCoverYN = _cbAttrHelper.hasAttribute(EnumAttributeLocation.DOWN, EnumAttributeType.COVER);
    	boolean hasSideCoverYP = _cbAttrHelper.hasAttribute(EnumAttributeLocation.UP, EnumAttributeType.COVER);
    	boolean hasSideCoverZN = _cbAttrHelper.hasAttribute(EnumAttributeLocation.NORTH, EnumAttributeType.COVER);
    	boolean hasSideCoverZP = _cbAttrHelper.hasAttribute(EnumAttributeLocation.SOUTH, EnumAttributeType.COVER);
    	boolean hasSideCoverXN = _cbAttrHelper.hasAttribute(EnumAttributeLocation.WEST, EnumAttributeType.COVER);
    	boolean hasSideCoverXP = _cbAttrHelper.hasAttribute(EnumAttributeLocation.EAST, EnumAttributeType.COVER);
    	_renderY = hasSideCoverZN || hasSideCoverZP || hasSideCoverXN || hasSideCoverXP;
    	_renderX = hasSideCoverYN || hasSideCoverYP || hasSideCoverZN || hasSideCoverZP;
    	_renderZ = hasSideCoverYN || hasSideCoverYP || hasSideCoverXN || hasSideCoverXP;
    	fillQuads(_quadContainer);
    	return getQuads();
    }
    
    /**
     * Gets a final list of baked quads for rendering.
     * 
     * @return a list of baked quads
     */
    private List<BakedQuad> getQuads() {
    	List<BakedQuad> quads = new ArrayList<BakedQuad>();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        BlockRenderLayer renderLayer = MinecraftForgeClient.getRenderLayer();
        IBlockState hostBlockState = _cbAttrHelper.hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER) ?
        		BlockUtil.getAttributeBlockState(_cbAttrHelper, EnumAttributeLocation.HOST, EnumAttributeType.COVER) : null;

    	for (EnumAttributeLocation location : EnumAttributeLocation.values()) {

    		_location = location;
    		QuadContainer quadContainer = _quadContainer;
    		boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    		_isSideCover = !EnumAttributeLocation.HOST.equals(location);
    		if (_isSideCover && !hasCover) {
    			continue;
    		}
    		boolean hasOverlay = _cbAttrHelper.hasAttribute(location, EnumAttributeType.OVERLAY);
	        boolean hasChiselDesign = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DESIGN_CHISEL);
    	
	    	Map<EnumFacing, List<BakedQuad>> quadMap = getCoverQuads(quadContainer, location);
	        boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
	        int dyeColor = !hasDye ? NO_COLOR : DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel());
	        IBlockState attributeState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);

	    	// Set side cover depth
	    	if (!EnumAttributeLocation.HOST.equals(location)) {
		    	if (isSnowState(attributeState)) {
		    		_sideDepth = SNOW_SIDE_DEPTH;
		    	} else {
		    		_sideDepth = SIDE_DEPTH;
		    	}
		    	quadContainer = _quadContainer.toSideLocation(location, _sideDepth);
	    	}
	        
	        // Find current render layer for cover, and outermost layer
	        boolean canRenderCover = false;
	        BlockRenderLayer outermostCoverRenderLayer = _uncoveredRenderLayer;
	        if (hasCover) {
	        	for (BlockRenderLayer layer : LAYERS) {
	        		if (attributeState.getBlock().canRenderInLayer(attributeState, layer)) {
	        			if (layer.equals(renderLayer)) {
	        				canRenderCover = true;
	        			}
	        			outermostCoverRenderLayer = layer;
	        		}
	        	}
	        } else {
	        	canRenderCover = renderLayer.equals(_uncoveredRenderLayer);
	        }
	        // Find render layer for overlay (must be outermost)
	        BlockRenderLayer overlayRenderLayer = CUTOUT_MIPPED;
	        if (hasOverlay) {
	        	overlayRenderLayer = TRANSLUCENT;
	        } else if (LAYERS.indexOf(CUTOUT_MIPPED) < LAYERS.indexOf(outermostCoverRenderLayer)) {
	        	overlayRenderLayer = outermostCoverRenderLayer;
	        }
	        BlockRenderLayer chiselDesignRenderLayer = TRANSLUCENT;

	        // Render cover layer
	        if (canRenderCover) {
			    if (hasCover) {
		        	// Add cover quads
	                for (EnumFacing facing : EnumFacing.VALUES) {
	                	if (quadMap.containsKey(facing)) {
	                		for (BakedQuad bakedQuad : quadMap.get(facing)) {
	                			int color = hasDye ? dyeColor : NO_COLOR;
	                			if (!hasDye && bakedQuad.hasTintIndex()) {
	                				color = blockColors.colorMultiplier(attributeState, Minecraft.getMinecraft().theWorld, _blockPos, 0);
	                			}
	                			quads.addAll(getQuadsForSide(quadContainer, facing, bakedQuad.getSprite(), color));
	                		}
	                	}
	                }
	        	} else if (EnumAttributeLocation.HOST.equals(location)) {
	        		// Add uncovered quads
	                for (EnumFacing facing : EnumFacing.VALUES) {
	                	quads.addAll(getQuadsForSide(quadContainer, facing, getUncoveredSprite(), dyeColor));
	                }
		        }
	        }
	        
	        // Add chisel quads
	        if (hasChiselDesign && chiselDesignRenderLayer.equals(renderLayer)) {
        		String design = ((AttributeString)_cbAttrHelper.getAttribute(location, EnumAttributeType.DESIGN_CHISEL)).getModel();
        		TextureAtlasSprite chiselSprite = SpriteRegistry.sprite_design_chisel.get(DesignHandler.listChisel.indexOf(design));
        		for (EnumFacing facing : EnumFacing.VALUES) {
        			quads.addAll(getQuadsForSide(quadContainer, facing, chiselSprite, NO_COLOR));
        		}
	        }
	        
	        // Add overlay quads
	        if (hasOverlay && overlayRenderLayer.equals(renderLayer)) {
	        	Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
	        	int overlayColor = NO_COLOR;
	        	if (Overlay.GRASS.equals(overlay)) {
	        		IBlockState overlayBlockState = Blocks.GRASS.getDefaultState();
	        		overlayColor = blockColors.colorMultiplier(overlayBlockState, Minecraft.getMinecraft().theWorld, _blockPos, ForgeHooksClient.getWorldRenderPass());
	        	}        	
	        	for (EnumFacing facing : EnumFacing.VALUES) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(overlay, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, overlayColor));
	        		}
	        	}
	        }
	        
	        // Add snow override
	        Key upKey = AbstractAttribute.generateKey(EnumAttributeLocation.UP, EnumAttributeType.COVER);
	        IBlockState upBlockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, upKey.getLocation(), upKey.getType());
	        if (EnumAttributeLocation.HOST.equals(location)
	        		&& _cbAttrHelper.hasAttribute(upKey)
	        		&& isSnowState(upBlockState)
	        		&& (hostBlockState != null && !isSnowState(hostBlockState))
	        		&& overlayRenderLayer.equals(renderLayer)) {
	        	for (EnumFacing facing : EnumFacing.VALUES) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(Overlay.SNOW, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, NO_COLOR));
	        		}
	        	}
	        }
    	}
        return quads;
    }
    
    private boolean isSnowState(IBlockState blockState) {
    	return blockState.getBlock().equals(Blocks.SNOW) ||
    			blockState.getBlock().equals(Blocks.SNOW_LAYER);
    }

    private List<BakedQuad> getQuadsForSide(QuadContainer quadContainer, EnumFacing facing, TextureAtlasSprite sprite, int rgb) {
    	return quadContainer.getBakedQuads(facing, sprite, rgb);
    }
    
    private Map<EnumFacing, List<BakedQuad>> getCoverQuads(QuadContainer quadContainer, EnumAttributeLocation location) {
    	Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();    	
    	if (_cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER)) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(coverStack);
		    	IBlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
		    	for (EnumFacing facing : EnumFacing.VALUES) {
		    		map.put(facing, itemModel.getQuads(blockState, facing, _rand));
		    	}
	    	}
    	} else {
    		for (EnumFacing facing : EnumFacing.VALUES) {
    			map.put(facing, getQuadsForSide(quadContainer, facing, getUncoveredSprite(), NO_COLOR));
    		}
    	}
    	return map;
    }

    /**
     * While rendering raw quads, can be overridden to not draw
     * quads for side depending on block state or other factors.
     * 
     * @param facing the facing
     * @return <code>true</code> if side should render
     */
    protected boolean canRenderSide(EnumFacing facing) {
    	return true;
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
        return getUncoveredSprite();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    public int getData() {
    	return _cbMetadata;
    }
    
    /**
     * Fills quad container with all block quads.
     * 
     * @param quadContainer the quad container
     */
    protected abstract void fillQuads(QuadContainer quadContainer);
    
    protected abstract TextureAtlasSprite getUncoveredSprite();
    
}