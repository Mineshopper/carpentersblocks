package com.carpentersblocks.client.renderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.carpentersblocks.block.AbstractCoverableBlock;
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
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.handler.DesignHandler;
import com.carpentersblocks.util.handler.DyeHandler;
import com.carpentersblocks.util.handler.OverlayHandler;
import com.carpentersblocks.util.handler.OverlayHandler.Overlay;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
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
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.data.IModelData;

public class RenderPkg {
	
	/** Used to provide render properties to render helper classes. */
	public static ThreadLocal<RenderPkg> THREAD_LOCAL_RENDER_PKG = new ThreadLocal<>();
	private VertexFormat _vertexFormat;
    private Random _rand;
    private AttributeHelper _cbAttrHelper;
    private BlockState _blockState;
	private BlockPos _blockPos;
    private int _cbMetadata;
    private ReferenceQuads _referenceQuads;
    
    /**
     * Overloaded constructor that handles rendering block.
     * <p>
     * Used for rendering block in world.
     * 
     * @param vertexFormat the vertex format
     * @param extraData extra data passed from tile entity
     * @param blockState the block state
     * @param random a random instance
     */
    public RenderPkg(VertexFormat vertexFormat, IModelData extraData, BlockState blockState, Random random) {
    	this(vertexFormat, random);
    	_blockPos = extraData.getData(CbTileEntity.MODEL_BLOCK_POS);
    	_cbMetadata = extraData.getData(CbTileEntity.MODEL_METADATA);
    	_cbAttrHelper = new AttributeHelper((Map<Key, AbstractAttribute<?>>) extraData.getData(CbTileEntity.MODEL_ATTRIBUTES));
    	_blockState = blockState;
    }
    
    /**
     * Overloaded constructor that handles rendering block item.
     * <p>
     * Used for rendering block in inventory.
     * 
     * @param vertexFormat the vertex format
     * @param random a random instance
     */
	public RenderPkg(VertexFormat vertexFormat, Random random) {
		_vertexFormat = vertexFormat;
    	_rand = random;
    	_referenceQuads = new ReferenceQuads();
    }
    
    /**
     * Gets list of block quads without rendering attributes
     * or other block features.
     * <p>
     * Used for inventory rendering.
     * 
     * @return a list of baked quads
     */
    public List<BakedQuad> getBlockItemBakedQuads() {
    	return bake(_referenceQuads);
    }
    
    /**
     * Gets a final list of baked quads for rendering.
     * <p>
     * Used for rendering blocks in the world.
     * 
     * @return a list of baked quads
     */
    public List<BakedQuad> getBlockBakedQuads() {
        List<BakedQuad> bakedQuads = new ArrayList<>();
    	for (Entry<EnumAttributeLocation, List<Quad>> entry : getLocationQuads().entrySet()) {
    		for (Quad quad : entry.getValue()) {
    			EnumAttributeLocation location = entry.getKey();
    			List<Quad> outputQuads = new ArrayList<>();
    			Direction blockStateSensitiveDirection;
    			
    			// get cover quads using block state sensitive direction
    			{
    				BlockState coverBlockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
    				if (coverBlockState == null) {
    					coverBlockState = _blockState;
    				}
    				blockStateSensitiveDirection = getBlockStateSensitiveDirection(quad, coverBlockState);
    				Quad quadCopy = new Quad(quad);
    				quadCopy.setDirection(blockStateSensitiveDirection);
    				outputQuads.addAll(getCoverQuads(location, quadCopy, coverBlockState));
    			}
    			
		        // add chisel quad
		        if (_cbAttrHelper.hasAttribute(location, EnumAttributeType.DESIGN_CHISEL)) {
	        		String design = ((AttributeString)_cbAttrHelper.getAttribute(location, EnumAttributeType.DESIGN_CHISEL)).getModel();
	        		TextureAtlasSprite chiselSprite = TextureAtlasSprites.sprite_design_chisel.get(DesignHandler.listChisel.indexOf(design));
	        		Quad chiselQuad = new Quad(quad).setTextureAtlasSprite(chiselSprite);
	        		chiselQuad.setDirection(blockStateSensitiveDirection);
	        		outputQuads.add(chiselQuad);
		        }
		        
		        // add overlay quad
		        if (_cbAttrHelper.hasAttribute(location, EnumAttributeType.OVERLAY)) {
		        	Overlay overlay = OverlayHandler.getOverlayType(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.OVERLAY)).getModel());
		        	BlockState overlayBlockState = overlay.getBlockState();
		        	Direction direction = getBlockStateSensitiveDirection(quad, overlayBlockState);
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(overlay, direction);
	        		if (overlaySprite != null) {
	        			int overlayColor = RenderConstants.DEFAULT_RGB;
			        	if (Overlay.grass.equals(overlay)) {
			        		overlayColor = Minecraft.getInstance().getBlockColors().getColor(overlayBlockState, Minecraft.getInstance().level, _blockPos, 0);
			        	}
	        			Quad newQuad = new Quad(quad)
	        					.setRgb(overlayColor)
	        					.setRenderType(RenderType.cutoutMipped())
        						.setTextureAtlasSprite(overlaySprite);
	        			newQuad.setDirection(blockStateSensitiveDirection);
	        			outputQuads.add(newQuad);
	        		}
		        }
		        
		        // add snow overlay if side is underneath snow
		        Key upKey = AbstractAttribute.generateKey(EnumAttributeLocation.UP, EnumAttributeType.COVER);
		        boolean isHostAndSnowAbove = location.isHost()
		        		&& isSnow(BlockUtil.getAttributeBlockState(_cbAttrHelper, upKey.getLocation(), upKey.getType()));
		        boolean isSideAndSnowAbove = location.isCardinalSide()
		        		&& isSnow(Minecraft.getInstance().level.getBlockState(location.offset(_blockPos)));
		        if (isHostAndSnowAbove || isSideAndSnowAbove) {
		        	Direction direction = getBlockStateSensitiveDirection(quad, Overlay.snow.getBlockState());
	        		TextureAtlasSprite overlaySprite = OverlayHandler.getOverlaySprite(Overlay.snow, direction);
	        		if (overlaySprite != null) {
	        			Quad newQuad = new Quad(quad).setTextureAtlasSprite(overlaySprite);
	        			newQuad.setDirection(blockStateSensitiveDirection);
	        			outputQuads.add(newQuad);
	        		}
		        }
		        
		        // remove occluded quads
		        /*
		        Map<Integer, List<Quad>> map = new HashMap<>();
	    		for (Quad outputQuad : outputQuads) {
	    			int hash = Objects.hash(outputQuad.getVec3ds()[0], outputQuad.getVec3ds()[1], outputQuad.getVec3ds()[2], outputQuad.getVec3ds()[3]);
	    			if (!map.containsKey(hash)) {
	    				map.put(hash, new ArrayList<>());
	    			}
	    			// quads LIFO sorted
	    			map.get(hash).add(0, outputQuad);
	    		}
	    		for (Entry<Integer, List<Quad>> listEntry : map.entrySet()) {
	    			ListIterator<Quad> iter = listEntry.getValue().listIterator(listEntry.getValue().size());
	    	        boolean removeRemainder = false;
	    	        while (iter.hasPrevious()) {
	    	        	Quad theQuad = iter.previous();
	    	        	if (removeRemainder) {
	    	        		iter.remove();
	    	        		continue;
	    	        	}
	    	        	//if (RenderType.solid().equals(theQuad.getRenderType())) {
	    	        	//	removeRemainder = true;
	    	        	//}
	    	        }
	    		}
	    		*/
	    		
		        // force quads in alpha render pass if any quad is alpha layer to control z-fighting
		        boolean forceAlpha = outputQuads.stream().anyMatch(q -> RenderType.translucent().equals(q.getRenderType()));
		        if (forceAlpha) {
		        	outputQuads.stream().forEach(q -> q.setRenderType(RenderType.translucent()));
		        }
		        
		        // post-process output quad properties
		        {
		        	Direction bakeDirection = quad.getDirection();
		        	if (QuadUtil.isSloped(quad, Axis.Y)) {
		        		if (Direction.UP.equals(QuadUtil.getYSlope(quad))) {
		        			bakeDirection = Direction.UP;
		        		} else { // Direction.DOWN
		        			bakeDirection = Direction.DOWN;
		        		}
		        	}
			        // bake direction needs to match for all quads
		        	for (Quad outputQuad : outputQuads) {
		        		outputQuad.setBakeDirection(bakeDirection);
		        	}
		        }
		        
		        // bake quads and add to baked quad list
		        outputQuads
		        		.stream()
		        		.filter(q -> MinecraftForgeClient.getRenderLayer().equals(q.getRenderType()))
		        		.forEach(q -> bakedQuads.add(q.bake()));
    		}
    	}
    	
        return bakedQuads;
    }
    
    /**
     * Gets render direction for given quad and block state.
     * <p>
     * This gives sloped quads the ability to set their own
     * render direction based on block state properties.
     * 
     * @param quad the quad
     * @param blockState the block state
     * @return a direction
     */
    private Direction getBlockStateSensitiveDirection(Quad quad, BlockState blockState) {
    	if (QuadUtil.isSloped(quad, Axis.Y)) {
			double maxUpSlope = RenderConstants.MAX_UP_SLOPE;
			double maxSideSlope = RenderConstants.MAX_SIDE_SLOPE;
			if (blockState != null && !(blockState.getBlock() instanceof AbstractCoverableBlock)) {
				if (OverlayHandler.isFloatingSpriteBlockState(blockState)) {
					maxUpSlope = RenderConstants.MAX_UP_SLOPE_GRASS;
					maxSideSlope = RenderConstants.MAX_SIDE_SLOPE_GRASS;
				}
			}
			if (QuadUtil.compare(quad.getNormal().y, maxUpSlope) >= 0) {
				return Direction.UP;
			} else if (QuadUtil.compare(quad.getNormal().y, maxSideSlope) >= 0) {
				return QuadUtil.getCardinalFacing(quad);
			} else {
				return Direction.DOWN;
			}
		}
    	return quad.getDirection();
    }
    
    /**
     * Gets whether block state belongs to {@link Blocks#SNOW_BLOCK SNOW_BLOCK}
     * or {@link Blocks#SNOW SNOW TOPPER}.
     * 
     * @param blockState the block state
     * @return <code>true</code> if block state represents snow
     */
    private boolean isSnow(@Nullable BlockState blockState) {
    	return blockState != null &&
    			(blockState.getBlock().equals(Blocks.SNOW) || blockState.getBlock().equals(Blocks.SNOW_BLOCK));
    }
    
    /**
     * Uses the attributes stored in tile entity to construct
     * quads using the quad container and stored cover properties.
     * 
     * @param quadContainer the quad container
     * @return a map of quad lists for each direction
     */
    private List<Quad> getCoverQuads(EnumAttributeLocation location, final Quad quad, BlockState blockState) {
    	List<Quad> quads = new ArrayList<>();
    	
    	if (!RenderTypeLookup.canRenderInLayer(blockState, MinecraftForgeClient.getRenderLayer())) {
    		return quads;
    	}
    	
    	boolean hasDye = _cbAttrHelper.hasAttribute(location, EnumAttributeType.DYE);
    	final int dyeRgb = hasDye ?
    			DyeHandler.getColor(((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.DYE)).getModel()) : 0;
    	boolean hasCover = _cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER);
    	BlockColors blockColors = Minecraft.getInstance().getBlockColors();
    	
    	// discover cover quads to donate texture and tint index
    	Map<Direction, List<BakedQuad>> bakedQuads = new HashMap<Direction, List<BakedQuad>>();
    	if (hasCover) {
	    	ItemStack coverStack = ((AttributeItemStack)_cbAttrHelper.getAttribute(location, EnumAttributeType.COVER)).getModel();
	    	if (coverStack != null) {
		    	IBakedModel itemModel = Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(coverStack);
		    	blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
	    		for (Direction facing : Direction.values()) {
		    		bakedQuads.put(facing, itemModel.getQuads(blockState, facing, _rand));
		    	}
	    	}
    	}
    	
    	// gather tint index for block state
    	final int tintIndex = blockState != null ? blockColors.getColor(blockState, Minecraft.getInstance().level, _blockPos, 0) : -1;
    	
    	// add quads
		if (quad.canCover()
				&& hasCover
				&& bakedQuads.get(quad.getDirection()) != null) {
			for (BakedQuad bakedQuad : bakedQuads.get(quad.getDirection())) {
				Quad newQuad = new Quad(quad);
				if (Blocks.GRASS_BLOCK.equals(blockState.getBlock())) {
					if (isTintedGrassSprite(bakedQuad.getSprite())) {
						newQuad.setRgb(tintIndex);
					} else if (hasDye) {
						newQuad.setRgb(dyeRgb);
					}
				} else if (bakedQuad.getTintIndex() > -1) { // non-grass block with tint
					if (hasDye) {
						newQuad.setRgb(dyeRgb);
					} else {
						newQuad.setRgb(tintIndex);
					}
		    	} else if (hasDye) { // dye overridden by attribute
		    		newQuad.setRgb(dyeRgb);
		    	}
				newQuad.setTextureAtlasSprite(bakedQuad.getSprite());
				newQuad.setRenderType(MinecraftForgeClient.getRenderLayer());
				quads.add(newQuad);
			}
		} else {
			// uncovered quad
			quads.add(new Quad(quad));
		}
		if (hasDye) {
			quads.forEach(q -> q.setRgb(dyeRgb));
		}
    
    	return quads;
    }
    
    /**
     * Returns <code>true</code> if sprite belongs to {@link GrassBlock}
     * and has a tint index.
     * 
     * @param sprite the texture atlas sprite
     * @return <code>true</code> if a tinted grass sprite
     */
    private boolean isTintedGrassSprite(TextureAtlasSprite sprite) {
    	return TextureAtlasSprites.sprite_grass_tinted_side.get().equals(sprite)
    			|| TextureAtlasSprites.sprite_grass_top.get().equals(sprite);
    }
    

    
    /**
     * Gets map of attribute locations and associated list
     * of quads.
     * 
     * @return a map of attribute locations and quads
     */
    public Map<EnumAttributeLocation, List<Quad>> getLocationQuads() {
    	Map<EnumAttributeLocation, List<Quad>> map = new HashMap<>();
    	for (EnumAttributeLocation location : EnumAttributeLocation.values()) {
    		if (location.isHost()) {
    			map.put(location, _referenceQuads.get());
    		} else if (_cbAttrHelper.hasAttribute(location, EnumAttributeType.COVER)) {
    			double sideDepth;
	        	BlockState blockState = BlockUtil.getAttributeBlockState(_cbAttrHelper, location, EnumAttributeType.COVER);
	        	if (EnumAttributeLocation.UP.equals(location) && isSnow(blockState)) {
	        		sideDepth = 0.125D;
	        	} else {
	        		sideDepth = 0.0625D;
	        	}
	        	map.put(location, _referenceQuads.get(location, sideDepth));
    		}
    	}
    	return map;
    }
    
    /**
	 * Bakes quads.
	 * 
	 * @param quads a list of quads
	 * @return a list of baked quads
	 */
	public List<BakedQuad> bake(ReferenceQuads referenceQuads) {
		return referenceQuads
				.get()
				.stream()
				.map(q -> new Quad(q).bake())
				.collect(Collectors.toList());
	}
    
	/**
	 * Gets quad container for HOST location.
	 * 
	 * @return the host quad container
	 */
	public ReferenceQuads getReferenceQuads() {
		return _referenceQuads;
	}
	
	/**
	 * Marks reference quads as locked, indicating all
	 * quads have been filled from source block model.
	 */
    public void lockReferenceQuads() {
    	_referenceQuads.lockQuads();
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
    
	/**
	 * Gets thread local instance of {@link RenderPkg}.
	 * 
	 * @return a render package
	 */
    public static RenderPkg get() {
    	return THREAD_LOCAL_RENDER_PKG.get();
    }
    
}
