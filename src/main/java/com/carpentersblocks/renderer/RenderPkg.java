package com.carpentersblocks.renderer;

import static net.minecraft.util.BlockRenderLayer.CUTOUT_MIPPED;
import static net.minecraft.util.BlockRenderLayer.TRANSLUCENT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.tileentity.CbTileEntity;
import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.RotationUtil.Rotation;
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
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class RenderPkg {

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
    private final static double SIDE_DEPTH = 1/16D;
    private final static double SNOW_SIDE_DEPTH = 1/8D;
    
    public RenderPkg(VertexFormat vertexFormat, IBlockState blockState, EnumFacing facing, long rand) {
    	_rand = rand;
    	_blockState = blockState;
    	_vertexFormat = vertexFormat;
    	_cbMetadata = ((IExtendedBlockState)blockState).getValue(Property.CB_METADATA);
    	_blockPos = ((IExtendedBlockState)blockState).getValue(Property.BLOCK_POS);
    	_renderFace = ((IExtendedBlockState)blockState).getValue(Property.RENDER_FACE);
    	_state = ((IExtendedBlockState)blockState).getValue(Property.CB_STATE);
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute>) ((IExtendedBlockState)blockState).getValue(Property.ATTR_MAP));
    	_uncoveredRenderLayer = Minecraft.getMinecraft().world.getBlockState(_blockPos).getBlock().getBlockLayer();
    	_quadContainer = new QuadContainer(vertexFormat, EnumAttributeLocation.HOST);
    }
    
    public AbstractState getState() {
    	return _state;
    }
    
	public void addAll(Collection<Quad> collection) {
		_quadContainer.addAll(collection);
	}
    
    public void add(Quad quad) {
    	_quadContainer.add(quad);
    }
    
	/**
	 * Rotates quads about facing axis.
	 * 
	 * @param facing defines the axis of rotation
	 * @param rotation the rotation enum
	 */
	public void rotate(Rotation rotation) {
		_quadContainer.rotate(rotation);
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
    
    /**
     * Gets a final list of baked quads for rendering.
     * 
     * @return a list of baked quads
     */
    public List<BakedQuad> getQuads() {
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
	        
	    	// Set side cover depth
	        if (!EnumAttributeLocation.HOST.equals(location)) {
		    	_sideDepth = getSideCoverDepth(location);
		    	quadContainer = _quadContainer.toSideLocation(null, location, _sideDepth);
	        }
	        
	        Map<EnumFacing, List<Quad>> coverQuadMap = getCoveredQuads(quadContainer, location, renderLayer);
	        
	        // Find required render layers for cover
	    	boolean renderAttribute = false;
	    	IBlockState attributeState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
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
    
    private Map<EnumFacing, List<Quad>> getCoveredQuads(QuadContainer quadContainer, EnumAttributeLocation location, BlockRenderLayer renderLayer) {
    	Map<EnumFacing, List<Quad>> map = new HashMap<EnumFacing, List<Quad>>();
    	boolean canRenderCover = false;
    	boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
    	int dyeRgb = 0;
    	if (hasDye) {
    		dyeRgb = DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel());
    	}
    	boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    	BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
    	IBlockState blockState = null;
    	
    	// Discover cover quads to donate texture and tint color
    	Map<EnumFacing, List<BakedQuad>> bakedQuads = new HashMap<EnumFacing, List<BakedQuad>>();
    	if (hasCover) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(coverStack);
		    	blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
		    	canRenderCover = blockState.getBlock().canRenderInLayer(blockState, renderLayer);
	    		for (EnumFacing facing : EnumFacing.values()) {
		    		bakedQuads.put(facing, itemModel.getQuads(blockState, facing, _rand));
		    	}
	    	}
    	}
    	
    	int attrRgb = -1;
    	if (blockState != null) {
    		//quadContainer.transformForBlockState(blockState);
    		attrRgb = blockColors.colorMultiplier(blockState, Minecraft.getMinecraft().world, _blockPos, ForgeHooksClient.getWorldRenderPass());
    	}
    	
    	// Add quads
    	for (EnumFacing facing : EnumFacing.values()) {
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
    	    					newQuad.setSprite(bakedQuad.getSprite());
								map.get(facing).add(newQuad);
	    					}
	    				}
	    				continue;
	    			}
	    		}
	    		if (quad.getRenderLayer().equals(renderLayer)) {
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
    	return "minecraft:blocks/grass_top".equalsIgnoreCase(sprite.getIconName()) || "minecraft:blocks/grass_side_overlay".equalsIgnoreCase(sprite.getIconName());
    }
    
    private List<BakedQuad> getQuadsForSide(QuadContainer quadContainer, EnumFacing facing, TextureAtlasSprite spriteOverride, Integer rgbOverride, boolean isCover) {
    	List<Quad> srcQuads = quadContainer.getQuads(null, facing);
    	List<Quad> destQuads = new ArrayList<Quad>(srcQuads.size());
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
    
    private double getSideCoverDepth(EnumAttributeLocation location) {
    	IBlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
    	if (EnumAttributeLocation.UP.equals(location) && blockState != null && isSnowState(blockState)) {
    		return SNOW_SIDE_DEPTH;
    	} else {
    		return SIDE_DEPTH;
    	}
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
	
}
