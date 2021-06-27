package com.carpentersblocks.client.renderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.nbt.AttributeHelper;
import com.carpentersblocks.nbt.CbTileEntity;
import com.carpentersblocks.nbt.attribute.AbstractAttribute;
import com.carpentersblocks.nbt.attribute.AbstractAttribute.Key;
import com.carpentersblocks.nbt.attribute.AttributeItemStack;
import com.carpentersblocks.nbt.attribute.AttributeString;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.nbt.attribute.EnumAttributeType;
import com.carpentersblocks.util.BlockUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.IModelData;

public class RenderPkg {

	public static ThreadLocal<RenderPkg> THREAD_LOCAL_RENDER_PKG = new ThreadLocal<RenderPkg>();
	private VertexFormat _vertexFormat;
    private Random _rand;
    private AttributeHelper _cbAttrHelper;
    private BlockState _blockState;
	private BlockPos _blockPos;
    private int _cbMetadata;
    private QuadContainer _quadContainer;
    private RenderType _uncoveredRenderType;
    private boolean _isSideCover;
    private double _sideDepth;
    private boolean _isSnowCover;
    private EnumAttributeLocation _location;
    private final static double SIDE_DEPTH = 1/16D;
    private final static double SNOW_SIDE_DEPTH = 1/8D;
    
    private enum OrderedRenderType {
    	
    		TRANSLUCENT(RenderType.translucent()),
    		CUTOUT_MIPPED(RenderType.cutoutMipped()),
    		CUTOUT(RenderType.cutout()),
    		SOLID(RenderType.solid());
    	
    	private RenderType _renderType;
    	
    	private OrderedRenderType(RenderType renderType) {
			_renderType = renderType;
		}
    	
    	public RenderType getRenderType() {
    		return this._renderType;
    	}
    	
    	public static OrderedRenderType fromRenderType(RenderType renderTypeIn) {
    		for (OrderedRenderType renderTypeOrderLayer : values()) {
    			if (renderTypeOrderLayer._renderType.equals(renderTypeIn)) {
    				return renderTypeOrderLayer;
    			}
    		}
    		return null;
    	}
    	
    }
    
    public RenderPkg(VertexFormat vertexFormat, IModelData extraData, BlockState blockState, Direction facing, Random rand) {
    	_vertexFormat = vertexFormat;
    	_blockPos = extraData.getData(CbTileEntity.MODEL_BLOCK_POS);
    	_cbMetadata = extraData.getData(CbTileEntity.MODEL_METADATA);
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute<?>>) extraData.getData(CbTileEntity.MODEL_ATTRIBUTES));
    	_blockState = blockState;
    	_uncoveredRenderType = RenderConstants.DEFAULT_RENDER_LAYER;
    	_quadContainer = new QuadContainer(EnumAttributeLocation.HOST);
    	_rand = rand;
    	THREAD_LOCAL_RENDER_PKG.set(this);
    }
    
	public RenderPkg(VertexFormat vertexFormat, Direction facing, Random rand) {
		_vertexFormat = vertexFormat;
    	_rand = rand;
    	_quadContainer = new QuadContainer(EnumAttributeLocation.HOST);
    	THREAD_LOCAL_RENDER_PKG.set(this);
    }
    
	public void addAll(Collection<Quad> collection) {
		for (Quad quad : collection) {
			if (quad != null) {
				_quadContainer.add(quad);
			}
		}
	}
    
    public void add(Quad quad) {
    	if (quad != null) {
    		_quadContainer.add(quad);
    	}
    }
    
	/**
	 * Rotates quads about facing axis.
	 * 
	 * @param facing defines the axis of rotation
	 * @param rotation the rotation enum
	 */
	public void rotate(CbRotation rotation) {
		_quadContainer.rotate(rotation);
	}
    
    public BlockState appendAttributeBlockState(CbTileEntity cbTileEntity, BlockState blockState, EnumAttributeLocation location, EnumAttributeType type) {
    	if (cbTileEntity.getAttributeHelper().hasAttribute(location, type)) {
    		ItemStack itemStack = ((AttributeItemStack)cbTileEntity.getAttributeHelper().getAttribute(location, type)).getModel();
    		BlockState attrBlockState = BlockUtil.getAttributeBlockState(cbTileEntity.getAttributeHelper(), location, type);
    		return attrBlockState;
    	}
    	return blockState;
    }
    
    public List<BakedQuad> getInventoryQuads() {
    	return _quadContainer.bakeQuads();
    }
    
    /**
     * Gets a final list of baked quads for rendering.
     * 
     * @return a list of baked quads
     */
    public List<BakedQuad> getQuads() {
    	List<BakedQuad> quads = new ArrayList<BakedQuad>();
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        RenderType renderType = MinecraftForgeClient.getRenderLayer();
        BlockState hostBlockState = _cbAttrHelper.hasAttribute(EnumAttributeLocation.HOST, EnumAttributeType.COVER) ?
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
	        
	    	// Set side cover depth
	        if (!EnumAttributeLocation.HOST.equals(location)) {
		    	_sideDepth = getSideCoverDepth(location);
		    	quadContainer = _quadContainer.toSideLocation(null, location, _sideDepth);
	        }
	        
	        Map<Direction, List<Quad>> coverQuadMap = getCoveredQuads(quadContainer, location, renderType);
	        
	        // Find required render layers for cover
	    	boolean renderAttribute = false;
	    	ItemStack attributeItemStack = BlockUtil.getCover(_cbAttrHelper, _blockState, location);
	    	RenderType outermostCoverRenderType = _uncoveredRenderType;
	    	RenderType attributeRenderType = RenderTypeLookup.getRenderType(attributeItemStack, false);
	    	renderAttribute = renderType.equals(attributeRenderType);
	    	boolean canRenderBaseQuads = quadContainer.getRenderTypes(renderAttribute).contains(renderType);
	        if (hasLocationCover) {
	        	for (OrderedRenderType layerOrder : OrderedRenderType.values()) {
	        		if (layerOrder.getRenderType().equals(attributeRenderType)) {
	        			canRenderBaseQuads = true;
	        			outermostCoverRenderType = layerOrder.getRenderType();
	        		}
	        	}
	        } else {
	        	canRenderBaseQuads |= renderType.equals(_uncoveredRenderType);
	        }
	        // Check quads for render layer overrides
	        if (!canRenderBaseQuads) {
	            for (Direction facing : Direction.values()) {
	            	if (coverQuadMap.containsKey(facing)) {
	            		for (Quad quad : coverQuadMap.get(facing)) {
	            			if (renderType.equals(quad.getRenderType())) {
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
	        RenderType overlayRenderLayer = RenderType.cutoutMipped();
	        if (hasOverlay) {
	        	overlayRenderLayer = RenderType.translucent();
	        } else if (OrderedRenderType.CUTOUT.ordinal() < OrderedRenderType.fromRenderType(outermostCoverRenderType).ordinal()) {
	        	overlayRenderLayer = outermostCoverRenderType;
	        }
	        RenderType chiselDesignRenderLayer = RenderType.translucent();

	        // Render cover layer
            for (Direction facing : Direction.values()) {
            	if (coverQuadMap.containsKey(facing)) {
            		for (Quad quad : coverQuadMap.get(facing)) {
            			quads.add(quad.bake(location));
            		}
            	}
            }
	        
	        // Add chisel quads
	        if (hasChiselDesign && chiselDesignRenderLayer.equals(renderType)) {
        		String design = ((AttributeString)_cbAttrHelper.getAttribute(location, EnumAttributeType.DESIGN_CHISEL)).getModel();
        		TextureAtlasSprite chiselSprite = TextureAtlasSprites.sprite_design_chisel.get(DesignHandler.listChisel.indexOf(design));
        		for (Direction facing : Direction.values()) {
        			quads.addAll(getQuadsForSide(quadContainer, facing, chiselSprite, RenderConstants.DEFAULT_RGB, false));
        		}
	        }
	        
	        // Add overlay quads
	        if (hasOverlay && overlayRenderLayer.equals(renderType)) {
	        	Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
	        	int overlayColor = RenderConstants.DEFAULT_RGB;
	        	if (Overlay.GRASS.equals(overlay)) {
	        		BlockState overlayBlockState = Blocks.GRASS.defaultBlockState();
	        		overlayColor = blockColors.getColor(overlayBlockState, Minecraft.getInstance().level, _blockPos);
	        	}        	
	        	for (Direction facing : Direction.values()) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(overlay, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, overlayColor, false));
	        		}
	        	}
	        }
	        
	        // Add snow override
	        Key upKey = AbstractAttribute.generateKey(EnumAttributeLocation.UP, EnumAttributeType.COVER);
	        BlockState upBlockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, upKey.getLocation(), upKey.getType());
	        if (EnumAttributeLocation.HOST.equals(location)
	        		&& _cbAttrHelper.hasAttribute(upKey)
	        		&& isSnowState(upBlockState)
	        		&& (hostBlockState != null && !isSnowState(hostBlockState))
	        		&& overlayRenderLayer.equals(renderType)) {
	        	for (Direction facing : Direction.values()) {
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(Overlay.SNOW, facing);
	        		if (overlaySprite != null) {
	        			quads.addAll(getQuadsForSide(quadContainer, facing, overlaySprite, null, false));
	        		}
	        	}
	        }
    	}
    	
    	// Cleanup
    	THREAD_LOCAL_RENDER_PKG.remove();
    	
        return quads;
    }
    
    private boolean isSnowState(BlockState blockState) {
    	return blockState.getBlock().equals(Blocks.SNOW) ||
    			blockState.getBlock().equals(Blocks.SNOW_BLOCK);
    }
    
    private Map<Direction, List<Quad>> getCoveredQuads(QuadContainer quadContainer, EnumAttributeLocation location, RenderType renderType) {
    	Map<Direction, List<Quad>> map = new HashMap<Direction, List<Quad>>();
    	boolean canRenderCover = false;
    	boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
    	int dyeRgb = 0;
    	if (hasDye) {
    		dyeRgb = DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel());
    	}
    	boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    	BlockColors blockColors = Minecraft.getInstance().getBlockColors();
    	BlockState blockState = null;
    	
    	// Discover cover quads to donate texture and tint color
    	Map<Direction, List<BakedQuad>> bakedQuads = new HashMap<Direction, List<BakedQuad>>();
    	if (hasCover) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(coverStack);
		    	blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
		    	canRenderCover = RenderTypeLookup.canRenderInLayer(blockState, renderType);
	    		for (Direction facing : Direction.values()) {
		    		bakedQuads.put(facing, itemModel.getQuads(blockState, facing, _rand));
		    	}
	    	}
    	}
    	
    	int attrRgb = -1;
    	if (blockState != null) {
    		attrRgb = blockColors.getColor(blockState, Minecraft.getInstance().level, _blockPos);
    	}
    	
    	// Add quads
    	for (Direction facing : Direction.values()) {
    		map.put(facing, new ArrayList<Quad>());
    		for (Quad quad : quadContainer.getQuads(blockState, facing)) {
	    		if (quad.canCover()) { // Quad can be covered
	    			if (hasCover) {
	    				if (canRenderCover && bakedQuads.get(facing) != null) {
	    					for (BakedQuad bakedQuad : bakedQuads.get(facing)) {
	    						Quad newQuad = new Quad(quad);
	    						if (Blocks.GRASS.equals(blockState.getBlock())) {
    								if (isTintedGrassSprite(bakedQuad.getSprite())) {
    									newQuad.setRgb(attrRgb);
    								} else if (hasDye) {
    									newQuad.setRgb(dyeRgb);
    								}
    							} else if (bakedQuad.getTintIndex() > -1) {
    								if (hasDye) {
    									newQuad.setRgb(dyeRgb);
    								} else {
    									newQuad.setRgb(attrRgb);
    								}
	    				    	} else if (hasDye) {
	    				    		newQuad.setRgb(dyeRgb);
	    				    	}
    	    					newQuad.setTextureAtlasSprite(bakedQuad.getSprite());
								map.get(facing).add(newQuad);
	    					}
	    				}
	    				continue;
	    			}
	    		}
	    		if (quad.getRenderType().equals(renderType)) {
	    			if (hasDye) {
	    				quad.setRgb(dyeRgb);
	    			}
	    			map.get(facing).add(quad);
	    		}
	    	}
    	}
    
    	return map;
    }
    
    private boolean isTintedGrassSprite(TextureAtlasSprite sprite) {
    	return "minecraft:blocks/grass_top".equalsIgnoreCase(sprite.getName().toString()) || "minecraft:blocks/grass_side_overlay".equalsIgnoreCase(sprite.getName().toString());
    }
    
    private List<BakedQuad> getQuadsForSide(QuadContainer quadContainer, Direction facing, TextureAtlasSprite spriteOverride, Integer rgbOverride, boolean isCover) {
    	List<Quad> srcQuads = quadContainer.getQuads(null, facing);
    	List<Quad> destQuads = new ArrayList<Quad>(srcQuads.size());
    	for (Quad quad : srcQuads) {
    		Quad newQuad = new Quad(quad);
			if (quad.canCover() && spriteOverride != null) {
				newQuad.setTextureAtlasSprite(spriteOverride);
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
    protected boolean canRenderSide(Direction facing) {
    	return true;
    }
    
    /**
     * Transforms BakedQuad into Quad.
     * 
     * @param bakedQuad the baked quad to transform
     * @return a new Quad
     */
    protected Quad transform(BakedQuad bakedQuad, Direction facing) {
		int[] data = bakedQuad.getVertices();
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
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMin, yMin, zMax), new Vector3d(xMin, yMin, zMin), new Vector3d(xMax, yMin, zMin), new Vector3d(xMax, yMin, zMax));
	        case UP:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMin, yMax, zMin), new Vector3d(xMin, yMax, zMax), new Vector3d(xMax, yMax, zMax), new Vector3d(xMax, yMax, zMin));
	        case NORTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMax, yMax, zMin), new Vector3d(xMax, yMin, zMin), new Vector3d(xMin, yMin, zMin), new Vector3d(xMin, yMax, zMin));
	        case SOUTH:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMin, yMax, zMax), new Vector3d(xMin, yMin, zMax), new Vector3d(xMax, yMin, zMax), new Vector3d(xMax, yMax, zMax));
	        case WEST:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMin, yMax, zMin), new Vector3d(xMin, yMin, zMin), new Vector3d(xMin, yMin, zMax), new Vector3d(xMin, yMax, zMax));
	        case EAST:
	        default:
	        	return Quad.getQuad(facing, bakedQuad.getSprite(), bakedQuad.getTintIndex(), new Vector3d(xMax, yMax, zMax), new net.minecraft.util.math.vector.Vector3d(xMax, yMin, zMax), new Vector3d(xMax, yMin, zMin), new Vector3d(xMax, yMax, zMin));
        }
	}
    
    private double getSideCoverDepth(EnumAttributeLocation location) {
    	BlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
    	if (EnumAttributeLocation.UP.equals(location) && blockState != null && isSnowState(blockState)) {
    		return SNOW_SIDE_DEPTH;
    	} else {
    		return SIDE_DEPTH;
    	}
    }
    
    protected List<Quad> transform(List<BakedQuad> quads) {
    	List<Quad> outQuads = new ArrayList<Quad>();
    	for (BakedQuad bakedQuad : quads) {
    		//outQuads.add(transform(bakedQuad));
    	}
    	return outQuads;
    }
    
    public VertexFormat getVertexFormat() {
		return _vertexFormat;
	}
    
    public int getCbMetadata() {
		return _cbMetadata;
	}

	public BlockState getBlockState() {
		return _blockState;
	}

	public BlockPos getBlockPos() {
		return _blockPos;
	}
    
    public static RenderPkg get() {
    	return THREAD_LOCAL_RENDER_PKG.get();
    }
    
}
