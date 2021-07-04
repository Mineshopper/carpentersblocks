package com.carpentersblocks.client.renderer;

import java.util.Arrays;
import java.util.LinkedHashSet;

import com.carpentersblocks.block.CbBlocks;
import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.RotationUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.google.common.collect.ImmutableList;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

public class Quad {

	private static final float TINT_IDX_FULL_BRIGHT = (float) (15 * 0x20) / 0xffff;
	private static final String GRASS_SIDE_OVERLAY = "minecraft:block/grass_block_side_overlay";
	
	private static final float[] quantizedSixteenths = {
			-0.0625f, -0.125f, 0.0f, 0.0625f, 0.125f, 0.1875f, 0.25f, 0.3125f, 0.375f,
			0.4375f, 0.5f, 0.5625f, 0.625f, 0.6875f, 0.75f, 0.8125f,
			0.875f, 0.9375f, 1.0f, 1.0625f, 1.125f
	};
	
   	private Vector3d[] _vec3ds;
	private Direction _direction;
	private TextureAtlasSprite _textureAtlasSprite;
	private int _rgb;
	private boolean _maxBrightness;
	private RenderType _renderType;
	private Vector3d _normal;
	private boolean _isOblique;
	private Vector3d _uvOffset;
	private Direction _uvFloat;
	private boolean _diffuseLighting;
	
	private Quad() { }
			
	/**
	 * Copy constructor
	 * 
	 * @param src the source quad
	 */
	public Quad(Quad src) {
		_vec3ds = Arrays.copyOf(src.getVecs(), src.getVecs().length);
		_direction = src.getDirection();
		_maxBrightness = src.isMaxBrightness();
		_renderType = src.getRenderType();
		_rgb = src.getRgb();
		_textureAtlasSprite = src.getTextureAtlasSprite();
		_normal = new Vector3d(src.getNormal().x, src.getNormal().y, src.getNormal().z);
		_isOblique = src.isObliqueSlope();
		_uvFloat = src.getUVFloat();
		_uvOffset = src.getUVOffset();
		_diffuseLighting = src.hasDiffuseLighting();
	}
	
	/**
	 * Gets a basic quad requiring minimum details.
	 * <p>
	 * This is used by the mod's built-in block state system which
	 * constructs shapes using custom json files.
	 * 
	 * @param facing the facing
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction facing, Vector3d ... vec3ds) {
		return getQuad(
				facing,
				true,
				TextureAtlasSprites.sprite_uncovered_full,
				RenderConstants.DEFAULT_RGB,
				false,
				true,
				RenderType.cutoutMipped(),
				vec3ds);
	}
	
	/**
	 * Gets quad that does not automatically sort the input
	 * vectors based on the facing.
	 * <p>
	 * This is used when generating side covers, since they are
	 * generated from pre-sorted quads.
	 * 
	 * @param facing the facing
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getUnsortedQuad(Direction facing, Vector3d ... vec3ds) {
		return getQuad(
				facing,
				false,
				TextureAtlasSprites.sprite_uncovered_full,
				RenderConstants.DEFAULT_RGB,
				false,
				true,
				RenderType.cutoutMipped(),
				vec3ds);
	}
	
	/**
	 * Gets quad using provided sprite.
	 * <p>
	 * This is used when first generating quads for rendering.
	 * 
	 * @param facing the facing
	 * @param sprite the sprite
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction facing, TextureAtlasSprite sprite, Vector3d ... vec3ds) {
		return getQuad(
				facing,
				true,
				sprite,
				RenderConstants.DEFAULT_RGB,
				false,
				true,
				RenderType.cutoutMipped(),
				vec3ds);
	}
	
	/**
	 * Gets quad using provided sprite and tint index (rgb).
	 * <p>
	 * This is used when transforming BakedQuads into Quads.
	 * 
	 * @param facing the facing
	 * @param sprite the sprite
	 * @param tintIdx the tint index (rgb)
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction facing, TextureAtlasSprite sprite, int tintIdx, Vector3d ... vec3ds) {
		return getQuad(
				facing,
				true,
				sprite,
				tintIdx,
				false,
				true,
				RenderType.cutoutMipped(),
				vec3ds);
	}
	
	/**
	 * Handles the final step of quad creation for internal methods using all parameters.
	 * 
	 * @param facing the facing
	 * @param sortVecs whether input vectors need to be sorted based on facing
	 * @param sprite the sprite
	 * @param tintIdx the tint index (rgb)
	 * @param maxBrightness the max brightness
	 * @param renderType the render layer
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(
			Direction facing,
			boolean sortVecs,
			TextureAtlasSprite sprite,
			int tintIdx,
			boolean maxBrightness,
			boolean diffuseLighting,
			RenderType renderType,
			Vector3d ... vec3ds) {
		Quad quad = new Quad();
		quad._vec3ds = Arrays.copyOf(vec3ds, vec3ds.length);
		quad.setTextureAtlasSprite(sprite);
		quad.setMaxBrightness(maxBrightness);
		quad.setDiffuseLighting(diffuseLighting);
		quad.setRenderType(renderType);
		quad.setRgb(tintIdx);
		if (!quad.applyFacing(sortVecs, facing)) {
			return null;
		}
		return quad;
	}
	
	/**
	 * Copy constructor with offset.
	 * 
	 * @param x the x offset
	 * @param y the y offset
	 * @param z the z offset
	 * @return
	 */
	public Quad offset(double x, double y, double z) {
		Quad quad = new Quad(this);
		for (int i = 0; i < quad._vec3ds.length; ++i) {
			quad._vec3ds[i] = quad._vec3ds[i].add(x, y, z);
		}
		quad.setUVOffset(new Vector3d(-x, -y, -z));
		return quad;
	}
	
	public BakedQuad bake(EnumAttributeLocation location) {
		RenderPkg renderPkg = RenderPkg.get();
		
        // inventory rendering ignores these
        boolean isFloatingOverlay = false;
        if (DefaultVertexFormats.BLOCK.equals(renderPkg.getVertexFormat())) {
	        isFloatingOverlay = _textureAtlasSprite.getName().toString().equals(GRASS_SIDE_OVERLAY)
	        	|| _textureAtlasSprite.getName().getPath().contains("overlay/overlay_")
	        	&& _textureAtlasSprite.getName().getPath().endsWith("_side");
	        Block block = renderPkg.getBlockState().getBlock();
	        if (CbBlocks.blockCollapsibleBlock.equals(block) && (isSloped(Axis.X) || isSloped(Axis.Y) || isSloped(Axis.Z))) {
	        	this._isOblique = false;
	        	isFloatingOverlay = false;
	        }
        }
        
        boolean debug = false;
        if (debug) {
	        switch (this._direction) {
		        case DOWN:
		        	_rgb = 0xffffff; // white
		        	break;
		        case UP:
		        	_rgb = 0x000000; // black
		        	break;
		        case NORTH:
		        	_rgb = 0xff0000; // red
		        	break;
		        case SOUTH:
		        	_rgb = 0x00ff00; // green
		        	break;
		        case WEST:
		        	_rgb = 0x0000ff; // blue
		        	break;
		        case EAST:
		        	_rgb = 0xffff00; // yellow
		        	break;
	        }
        }
        
        UV[] uv = QuadUtil.getUV(this, isFloatingOverlay, location);
        BakedQuadBuilder builder = new BakedQuadBuilder(_textureAtlasSprite);
        builder.setQuadOrientation(_direction);
        
        for (int vertex = 0; vertex < 4; vertex++) {
        	if (DefaultVertexFormats.BLOCK.equals(renderPkg.getVertexFormat())) {
        		putBlockVertex(
        				builder,
	            		_rgb,
	            		quantizeToHexFractional(_vec3ds[vertex].x),
	            		quantizeToHexFractional(_vec3ds[vertex].y),
	            		quantizeToHexFractional(_vec3ds[vertex].z),
	            		_textureAtlasSprite.getU(uv[vertex].getU()),
	            		_textureAtlasSprite.getV(uv[vertex].getV())
        		);
        	} else if (DefaultVertexFormats.NEW_ENTITY.equals(renderPkg.getVertexFormat())) {
        		putItemVertex(
        				builder,
	            		(float) _vec3ds[vertex].x,
	            		(float) _vec3ds[vertex].y,
	            		(float) _vec3ds[vertex].z,
	            		_textureAtlasSprite.getU(uv[vertex].getU()),
	            		_textureAtlasSprite.getV(uv[vertex].getV())
        		);
        	}
        }
        return builder.build();
	}
	
	/**
	 * Rotated blocks accumulate precision errors that break
	 * vanilla and non-experimental Forge lighting systems.
	 * <p>
	 * This will quantize a coordinate value into 1/16th
	 * increments from 0.0f to 1.0f, providing aligned values
	 * that work in harmony with occlusion and lighting systems.
	 * 
	 * @param value the input value
	 * @return a quantized value to nearest 1/16th increment
	 * between 0.0f and 1.0f
	 */
	private float quantizeToHexFractional(double value) {
		Float closest = null;
		for (float quantization : quantizedSixteenths) {
			if (closest == null || MathHelper.abs((float) value - closest) > MathHelper.abs(quantization - (float) value)) {
				closest = quantization;
			}
		}
		return closest;
	}
	
	private void putItemVertex(IVertexConsumer consumer, float x, float y, float z, float u, float v) {
        VertexFormat format = consumer.getVertexFormat();
        for (int e = 0; e < format.getElements().size(); e++) {
            VertexFormatElement element = format.getElements().get(e);
            switch(element.getUsage()) {
	            case POSITION:
	                consumer.put(e, x, y, z, 1f);
	                break;
	            case COLOR:
	                consumer.put(e, 1f, 1f, 1f, 1f);
	                break;
	            case NORMAL:
	            	consumer.put(
	                		e,
	                		(float) _normal.x,
	                		(float) _normal.y,
	                		(float) _normal.z,
	                		0f);
	                break;
	            case UV:
	                if (element.getIndex() == 0) {
	                    consumer.put(e, u, v, 0f, 1f);
	                    break;
	                }
	            default:
	                consumer.put(e);
	                break;
            }
        }
    }
	
	private void putBlockVertex(IVertexConsumer consumer, int color, float x, float y, float z, float u, float v) {
        ImmutableList<VertexFormatElement> elements = DefaultVertexFormats.BLOCK.getElements();
        for (int e = 0; e < elements.size(); e++) {
            switch (elements.get(e).getUsage()) {
	            case POSITION:
	                consumer.put(e, x, y, z, 1f);
	                break;
	            case COLOR:
	                float r = ((color >> 16) & 0xFF) / 255f;
	                float g = ((color >>  8) & 0xFF) / 255f;
	                float b = ( color        & 0xFF) / 255f;
	                float a = ((color >> 24) & 0xFF) / 255f; // used?
	                consumer.put(e, r, g, b, a);
	                break;
	            case NORMAL:
	            	consumer.put(
	                		e,
	                		(float) _normal.x,
	                		(float) _normal.y,
	                		(float) _normal.z,
	                		0f);
	                break;
	            case UV:
                    if (elements.get(e).getIndex() == 0) {
                        consumer.put(e, u, v, 0f, 1f);
                        break;
                    }
	            default:
	                consumer.put(e);
	                break;
            }
        }
    }
	
	/**
	 * Rotates quad.
	 * 
	 * @param rotation the rotation
	 */
	public void rotate(CbRotation rotation) {
		_vec3ds = RotationUtil.rotate(_vec3ds, rotation);
		this.applyFacing(true, rotation.getRotatedDirection(_direction));
	}
	
	/**
	 * Gets whether quad is sloped.
	 * 
	 * @param axis the axis to check for sloping
	 * @return <code>true</code> if sloped on axis
	 */
	public boolean isSloped(Axis axis) {
		double axis1 = Axis.X.equals(axis) ? _normal.x : Axis.Y.equals(axis) ? _normal.y : _normal.z;
		return QuadUtil.compare(axis1, -1.0D) > 0 &&
				QuadUtil.compare(axis1, 0.0D) != 0 &&
				QuadUtil.compare(axis1, 1.0D) < 0;
	}
	
	/**
	 * Applies direction and calculates properties.
	 * <p>
	 * Quad must be reoriented to match facing prior to calling this
	 * or runtime exception may occur.
	 * 
	 * @param sortVecs whether to sort vectors using new direction
	 * @param direction the new direction
	 * @return <code>true</code> if transformation succeeds, <code>false</code> if it fails.
	 */
	public boolean applyFacing(boolean sortVecs, Direction direction) {
		if (sortVecs) {
			_vec3ds = QuadUtil.sortVector3dsByDirection(direction, _vec3ds);
		}
		if (!QuadUtil.isValid(this)) {
			return false;
		}
		_direction = direction;
		Vector3d[] vecs1 = new LinkedHashSet<Vector3d>(Arrays.asList(this.getVecs())).toArray(new Vector3d[this.getVecs().length]);
		_normal = QuadUtil.getNormal(vecs1);
		_isOblique = isSloped(Axis.X) && isSloped(Axis.Y) && isSloped(Axis.Z);
		return true;
	}
	
	public Direction getYSlope() {
		return getNormal().y > 0.0D ? Direction.UP : Direction.DOWN;
	}
	
	public Direction getDirection() {
		return _direction;
	}
	
	public Vector3d[] getVecs() {
		return _vec3ds;
	}
	
	public TextureAtlasSprite getTextureAtlasSprite() {
		return _textureAtlasSprite;
	}
	
	public void setTextureAtlasSprite(TextureAtlasSprite sprite) {
		_textureAtlasSprite = sprite;
	}
	
	public int getRgb() {
		return _rgb;
	}

	public void setRgb(int rgb) {
		_rgb = rgb;
	}

	public boolean isMaxBrightness() {
		return _maxBrightness;
	}

	public void setMaxBrightness(boolean maxBrightness) {
		_maxBrightness = maxBrightness;
	}

	public RenderType getRenderType() {
		return _renderType;
	}

	public void setRenderType(RenderType renderType) {
		_renderType = renderType;
	}
	
	public boolean canCover() {
		return _textureAtlasSprite.getName().getPath().contains("uncovered_");
	}
		
	public Vector3d getNormal() {
		return _normal;
	}
	
	public boolean isObliqueSlope() {
		return _isOblique;
	}

	public Vector3d getUVOffset() {
		if (_uvOffset == null) {
			return Vector3d.ZERO;
		}
		return _uvOffset;
	}

	public void setUVOffset(Vector3d _uvOffset) {
		this._uvOffset = _uvOffset;
	}

	public boolean hasFloatingUV() {
		return this._uvFloat != null;
	}
	
	public Direction getUVFloat() {
		return _uvFloat;
	}

	public Quad setUVFloat(Direction _uvFloat) {
		this._uvFloat = _uvFloat;
		return this;
	}

	public boolean hasDiffuseLighting() {
		return _diffuseLighting;
	}

	public void setDiffuseLighting(boolean _diffuseLighting) {
		this._diffuseLighting = _diffuseLighting;
	}
	
}