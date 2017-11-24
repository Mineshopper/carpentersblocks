package com.carpentersblocks.renderer;

import static net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.IConstants;
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
import com.carpentersblocks.util.states.factory.AbstractState;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public abstract class AbstractBakedModel implements IBakedModel {

    private final static double SIDE_DEPTH = 1/16D;
    private final static double SNOW_SIDE_DEPTH = 1/8D;
    protected boolean _renderX;
    protected boolean _renderY;
    protected boolean _renderZ;
    protected AttributeHelper _cbAttrHelper;
    protected IBlockState _blockState;
    protected int _cbMetadata;
    protected BlockPos _blockPos;
    protected Boolean[] _renderFace;
    protected AbstractState _state;
    private long _rand;
    private QuadContainer _quadContainer;
    private BlockRenderLayer _uncoveredRenderLayer;
    private VertexFormat _vertexFormat;
    private boolean _isSideCover;
    private double _sideDepth;
    private boolean _isSnowCover;
    private EnumAttributeLocation _location;
    private IModelState _modelState;
    
    public AbstractBakedModel(IModelState modelState, VertexFormat vertexFormat, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
    	this._vertexFormat = new VertexFormat(vertexFormat).addElement(DefaultVertexFormats.TEX_2S);
    	this._modelState = modelState;
    }
    
    public VertexFormat getVertexFormat() {
    	return _vertexFormat;
    }
    
    public IModelState getModelState() {
    	return _modelState;
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
    	_state = ((IExtendedBlockState)blockState).getValue(Property.CB_STATE);
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute>) ((IExtendedBlockState)blockState).getValue(Property.ATTR_MAP));
    	_uncoveredRenderLayer = Minecraft.getMinecraft().world.getBlockState(_blockPos).getBlock().getBlockLayer();
    	_quadContainer = new QuadContainer(_vertexFormat, EnumAttributeLocation.HOST, false);
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
        boolean hasHostCoverOverride = _quadContainer.hasCoverOverride();

    	for (EnumAttributeLocation location : EnumAttributeLocation.values()) {

    		_location = location;
    		QuadContainer quadContainer = _quadContainer;
    		boolean hasLocationCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    		
    		if ((_isSideCover = !EnumAttributeLocation.HOST.equals(location)) && !hasLocationCover) {
    			continue;
    		}
    		boolean hasOverlay = _cbAttrHelper.hasAttribute(location, EnumAttributeType.OVERLAY);
	        boolean hasChiselDesign = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DESIGN_CHISEL);
	        
	    	
	        boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
	        int dyeColor = !hasDye ? IConstants.DEFAULT_RGB : DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel());
	        IBlockState attributeState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
	        
	    	// Set side cover depth
	        if (!EnumAttributeLocation.HOST.equals(location)) {
		    	if (attributeState != null) {
			    	if (isSnowState(attributeState)) {
			    		_sideDepth = SNOW_SIDE_DEPTH;
			    	} else {
			    		_sideDepth = SIDE_DEPTH;
			    	}
		    	}
		    	// TODO: Copy quad properties to new side quads
		    	quadContainer = _quadContainer.toSideLocation(location, _sideDepth);
	        }
	        
	        Map<EnumFacing, List<Quad>> coverQuadMap = getCoveredQuads(quadContainer, location, renderLayer, dyeColor);
	        
	        // Find required render layers for cover
	    	boolean renderAttribute = false;
	    	if (attributeState != null) {
	    		renderAttribute = attributeState.getBlock().canRenderInLayer(attributeState, renderLayer);
	    	}
	    	boolean canRenderBaseQuads = quadContainer.getRenderLayers(renderAttribute).contains(renderLayer);
	        BlockRenderLayer outermostCoverRenderLayer = _uncoveredRenderLayer;
	        if (attributeState != null && hasLocationCover) {
	        	for (BlockRenderLayer layer : BlockRenderLayer.values()) {
	        		if (attributeState.getBlock().canRenderInLayer(attributeState, layer)) {
	        			if (layer.equals(renderLayer)) {
	        				canRenderBaseQuads = true;
	        			}
	        			outermostCoverRenderLayer = layer;
	        		}
	        	}
	        } else {
	        	canRenderBaseQuads |= renderLayer.equals(_uncoveredRenderLayer);
	        }
	        // Check quads for render layer overrides
	        if (!canRenderBaseQuads) {
	            for (EnumFacing facing : EnumFacing.VALUES) {
	            	if (coverQuadMap.containsKey(facing)) {
	            		for (Quad quad : coverQuadMap.get(facing)) {
	            			if (renderLayer.equals(quad.getRenderLayer())) {
	            				canRenderBaseQuads = true;
	            				break;
	            			}
	            		}
	            	}
	            	if (canRenderBaseQuads) {
	            		break;
	            	}
	            }
	        }
	        
	        // Find render layer for overlay (must be outermost)
	        BlockRenderLayer overlayRenderLayer = CUTOUT_MIPPED;
	        if (hasOverlay) {
	        	overlayRenderLayer = TRANSLUCENT;
	        } else if (CUTOUT_MIPPED.ordinal() < outermostCoverRenderLayer.ordinal()) {
	        	overlayRenderLayer = outermostCoverRenderLayer;
	        }
	        BlockRenderLayer chiselDesignRenderLayer = TRANSLUCENT;

	        // Render cover layer
            for (EnumFacing facing : EnumFacing.VALUES) {
            	if (coverQuadMap.containsKey(facing)) {
            		for (Quad quad : coverQuadMap.get(facing)) {
            			quads.add(quad.bake(_vertexFormat, location));
            		}
            	}
            }
	        
	        // Add chisel quads
	        if (hasChiselDesign && chiselDesignRenderLayer.equals(renderLayer)) {
        		String design = ((AttributeString)_cbAttrHelper.getAttribute(location, EnumAttributeType.DESIGN_CHISEL)).getModel();
        		TextureAtlasSprite chiselSprite = SpriteRegistry.sprite_design_chisel.get(DesignHandler.listChisel.indexOf(design));
        		for (EnumFacing facing : EnumFacing.VALUES) {
        			quads.addAll(getQuadsForSide(quadContainer, facing, chiselSprite, IConstants.DEFAULT_RGB, false));
        		}
	        }
	        
	        // Add overlay quads
	        if (hasOverlay && overlayRenderLayer.equals(renderLayer)) {
	        	Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
	        	int overlayColor = IConstants.DEFAULT_RGB;
	        	if (Overlay.GRASS.equals(overlay)) {
	        		IBlockState overlayBlockState = Blocks.GRASS.getDefaultState();
	        		overlayColor = blockColors.colorMultiplier(overlayBlockState, Minecraft.getMinecraft().world, _blockPos, ForgeHooksClient.getWorldRenderPass());
	        	}        	
	        	for (EnumFacing facing : EnumFacing.VALUES) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(overlay, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, overlayColor, false));
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
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, null, false));
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
    
    private Map<EnumFacing, List<Quad>> getCoveredQuads(QuadContainer quadContainer, EnumAttributeLocation location, BlockRenderLayer renderLayer, int dyeRgb) {
    	Map<EnumFacing, List<Quad>> map = new HashMap<EnumFacing, List<Quad>>();
    	boolean canRenderCover = false;
    	boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    	BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
    	IBlockState blockState = null;
    	
    	// Discover dynamic quads to donate texture and tint color
    	Map<EnumFacing, List<BakedQuad>> bakedQuads = new HashMap<EnumFacing, List<BakedQuad>>();
    	if (hasCover) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(coverStack);
		    	blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
		    	canRenderCover = blockState.getBlock().canRenderInLayer(blockState, renderLayer);
		    	if (blockState.getBlock().canRenderInLayer(blockState, renderLayer)) {
		    		for (EnumFacing facing : EnumFacing.values()) {
			    		bakedQuads.put(facing, itemModel.getQuads(blockState, facing, _rand));
			    	}
		    	}
	    	}
    	}
    	
    	int attrRgb = -1;
    	if (blockState != null) {
    		attrRgb = blockColors.colorMultiplier(blockState, Minecraft.getMinecraft().world, _blockPos, ForgeHooksClient.getWorldRenderPass());
    	}
    	
    	// If can be covered in any layer, do not render uncovered
    	boolean canRenderUncovered = hasCover && !quadContainer.isCoverable(renderLayer) || !hasCover;
    	
    	// Apply cover properties to quads
    	for (EnumFacing facing : EnumFacing.values()) {
    		map.put(facing, new ArrayList<Quad>());
    		if (bakedQuads.get(facing) != null) {
				for (BakedQuad bakedQuad : bakedQuads.get(facing)) {
					for (Quad quad : quadContainer.getQuads(facing)) {
	    	    		if (quad.canCover()) {
	    	    			quad.setRgb(dyeRgb);
	    	    			if (hasCover) {
	    	    				if (canRenderCover) {
	    	    					if (bakedQuad.getTintIndex() > -1 && dyeRgb != IConstants.DEFAULT_RGB) {
	    	    						quad.setRgb(attrRgb);
	    	    					}
									quad.setSprite(bakedQuad.getSprite());
									map.get(facing).add(quad);
									continue;
	    	    				}
	    	    			} else if (canRenderUncovered && quad.getRenderLayer().equals(renderLayer)) { // Uncovered quads
	    	    				map.get(facing).add(quad);
	    	    			}
	    	    		} else if (canRenderUncovered && quad.getRenderLayer().equals(renderLayer)){
	    	    			map.get(facing).add(quad);
	    	    		}
					}
				}
	    	} else {
	    		for (Quad quad : quadContainer.getQuads(facing)) {
	    			quad.setRgb(dyeRgb);
		    		if (canRenderUncovered && quad.getRenderLayer().equals(renderLayer)){
		    			map.get(facing).add(quad);
		    		}
	    		}
	    	}
    	}
    
    	return map;
    }

    private List<BakedQuad> getQuadsForSide(QuadContainer quadContainer, EnumFacing facing, TextureAtlasSprite spriteOverride, Integer rgbOverride, boolean isCover) {
    	List<Quad> srcQuads = quadContainer.getQuads(facing);
    	List<Quad> destQuads = new ArrayList<Quad>(srcQuads.size());
    	// Sprite override applies to all parts... might need property in state file
    	for (Quad quad : srcQuads) {
    		Quad newQuad = new Quad(quad);
			if (quad.canCover() && spriteOverride != null) {
				newQuad.setSprite(spriteOverride);
			}
    		if (rgbOverride != null) {
    			newQuad.setRgb(rgbOverride);
    		}
    		destQuads.add(newQuad);
    	}
    	return quadContainer.bakeQuads(destQuads);
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
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    public int getData() {
    	return _cbMetadata;
    }
    
    protected List<Quad> transform(List<BakedQuad> quads) {
    	List<Quad> outQuads = new ArrayList<Quad>();
    	for (BakedQuad bakedQuad : quads) {
    		//outQuads.add(transform(bakedQuad));
    	}
    	return outQuads;
    }
    
    /**
     * Transforms BakedQuad into Quad.
     * 
     * @param bakedQuad the baked quad to transform
     * @return a new Quad
     */
    protected Quad transform(BakedQuad bakedQuad, EnumFacing facing) {
		int[] data = bakedQuad.getVertexData();
		float xMin = Float.MAX_VALUE;
		float xMax = Float.MIN_VALUE;
		float yMin = Float.MAX_VALUE;
		float yMax = Float.MIN_VALUE;
		float zMin = Float.MAX_VALUE;
		float zMax = Float.MIN_VALUE;
        for (int i = 0; i < 4; ++i) {
            float x = Float.intBitsToFloat(data[i * 7]);
            float y = Float.intBitsToFloat(data[i * 7 + 1]);
            float z = Float.intBitsToFloat(data[i * 7 + 2]);
            xMin = Math.min(xMin, x);
            yMin = Math.min(yMin, y);
            zMin = Math.min(zMin, z);
            xMax = Math.max(xMax, x);
            yMax = Math.max(yMax, y);
            zMax = Math.max(zMax, z);
        }
        switch (facing) {
	        case DOWN:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMin, zMax), new Vec3d(xMin, yMin, zMin), new Vec3d(xMax, yMin, zMin), new Vec3d(xMax, yMin, zMax));
	        case UP:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMin), new Vec3d(xMin, yMax, zMax), new Vec3d(xMax, yMax, zMax), new Vec3d(xMax, yMax, zMin));
	        case NORTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMax, yMax, zMin), new Vec3d(xMax, yMin, zMin), new Vec3d(xMin, yMin, zMin), new Vec3d(xMin, yMax, zMin));
	        case SOUTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMax), new Vec3d(xMin, yMin, zMax), new Vec3d(xMax, yMin, zMax), new Vec3d(xMax, yMax, zMax));
	        case WEST:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMin, yMax, zMin), new Vec3d(xMin, yMin, zMin), new Vec3d(xMin, yMin, zMax), new Vec3d(xMin, yMax, zMax));
	        case EAST:
	        default:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vec3d(xMax, yMax, zMax), new Vec3d(xMax, yMin, zMax), new Vec3d(xMax, yMin, zMin), new Vec3d(xMax, yMax, zMin));
        }
	}
    
    /**
     * Fills quad container with all block quads.
     * 
     * @param quadContainer the quad container
     */
    protected abstract void fillQuads(QuadContainer quadContainer);
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
    	return SpriteRegistry.sprite_uncovered_full;
    }
    
}