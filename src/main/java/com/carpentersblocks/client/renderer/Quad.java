package com.carpentersblocks.client.renderer;

import java.math.BigDecimal;
import java.util.Arrays;

import com.carpentersblocks.client.TextureAtlasSprites;
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.RotationUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;
import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;

public class Quad {
	
   	private Vector3d[] _vec3ds;
	private Direction _direction;
	private TextureAtlasSprite _textureAtlasSprite;
	private int _rgb;
	private boolean _maxBrightness;
	private RenderType _renderType;
	private Vector3d _normal;
	private boolean _diffuseLighting;
	private double _offset;
	private Vector3d _uvOffset;
	private Direction _uvFloat;
	private Direction _bakeDirection;
	
	private Quad() { }
			
	/**
	 * Copy constructor
	 * 
	 * @param src the source quad
	 */
	public Quad(Quad src) {
		_vec3ds = Arrays.copyOf(src.getVec3ds(), src.getVec3ds().length);
		_direction = src.getDirection();
		_maxBrightness = src.isMaxBrightness();
		_renderType = src.getRenderType();
		_rgb = src.getRgb();
		_textureAtlasSprite = src.getTextureAtlasSprite();
		_normal = new Vector3d(src.getNormal().x, src.getNormal().y, src.getNormal().z);
		_diffuseLighting = src.hasDiffuseLighting();
		_uvFloat = src.getUVFloat();
		_uvOffset = new Vector3d(src.getUVOffset().x, src.getUVOffset().y, src.getUVOffset().z);
	}
	
	/**
	 * Gets a basic quad requiring minimum details.
	 * <p>
	 * This is used by the mod's built-in block state system which
	 * constructs shapes using custom json files.
	 * 
	 * @param direction the direction
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction direction, Vector3d ... vec3ds) {
		return getQuad(
				direction,
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
	 * @param direction the direction
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getUnsortedQuad(Direction direction, Vector3d ... vec3ds) {
		return getQuad(
				direction,
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
	 * @param direction the direction
	 * @param sprite the sprite
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction direction, TextureAtlasSprite sprite, Vector3d ... vec3ds) {
		return getQuad(
				direction,
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
	 * @param direction the direction
	 * @param sprite the sprite
	 * @param tintIdx the tint index (rgb)
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(Direction direction, TextureAtlasSprite sprite, int tintIdx, Vector3d ... vec3ds) {
		return getQuad(
				direction,
				sprite,
				tintIdx,
				false,
				true,
				RenderType.cutoutMipped(),
				vec3ds);
	}
	
	/**
	 * Handles the final step of quad creation for internal methods using all arguments.
	 * 
	 * @param direction the direction
	 * @param sprite the sprite
	 * @param tintIdx the tint index (rgb)
	 * @param maxBrightness the max brightness
	 * @param renderType the render layer
	 * @param vec3ds the quad vectors
	 * @return a new quad
	 */
	public static Quad getQuad(
			Direction direction,
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
		if (!quad.setDirection(direction)) {
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
	
	/**
	 * Bakes quad.
	 * 
	 * @param location the location
	 * @return a baked quad
	 */
	public BakedQuad bake() {
		RenderPkg renderPkg = RenderPkg.get();
        UV[] uv = QuadUtil.getUV(this);
        BakedQuadBuilder builder = new BakedQuadBuilder(getTextureAtlasSprite());
        builder.setQuadOrientation(getBakeDirection());
        for (int vertex = 0; vertex < 4; vertex++) {
        	if (DefaultVertexFormats.BLOCK.equals(renderPkg.getVertexFormat())) {
        		putBlockVertex(
        				builder,
	            		getRgb(),
	            		(float) quantizedVector3d(getVec3ds()[vertex]).add(getNormal().multiply(getOffset(), getOffset(), getOffset())).x,
	            		(float) quantizedVector3d(getVec3ds()[vertex]).add(getNormal().multiply(getOffset(), getOffset(), getOffset())).y,
	            		(float) quantizedVector3d(getVec3ds()[vertex]).add(getNormal().multiply(getOffset(), getOffset(), getOffset())).z,
	            		getTextureAtlasSprite().getU(uv[vertex].getU()),
	            		getTextureAtlasSprite().getV(uv[vertex].getV())
        		);
        	} else if (DefaultVertexFormats.NEW_ENTITY.equals(renderPkg.getVertexFormat())) {
        		putItemVertex(
        				builder,
	            		(float) getVec3ds()[vertex].x,
	            		(float) getVec3ds()[vertex].y,
	            		(float) getVec3ds()[vertex].z,
	            		getTextureAtlasSprite().getU(uv[vertex].getU()),
	            		getTextureAtlasSprite().getV(uv[vertex].getV())
        		);
        	}
        }
        return builder.build();
	}
	
	/**
	 * Rotated blocks accumulate precision errors that don't
	 * work well with ambient occlusion calculations.
	 * <p>
	 * This will quantize 3d vector coordinates into the
	 * nearest 1/16th increment to accommodate side cover
	 * depths, providing aligned values that work in harmony
	 * with the lighting system.
	 * 
	 * @param value the input 3d vector
	 * @return a quantized 3d vector to nearest 1/16th coordinates
	 */
	private Vector3d quantizedVector3d(Vector3d vec3d) {
		double[] values = { vec3d.x(), vec3d.y(), vec3d.z() };
		double[] adjusted = new double[3];
		for (int i = 0; i < values.length; ++i) {
			BigDecimal bigDecimal = new BigDecimal(String.valueOf(values[i]));
			int intValue = bigDecimal.intValue();
			float floatValue = Math.abs(bigDecimal.subtract(new BigDecimal(intValue)).floatValue());
			Float decimalValue = null;
			for (int counter = 16; counter >= 0; --counter) {
				float fractional = counter / 16.0f;
				if (decimalValue == null || MathHelper.abs(floatValue - decimalValue) > MathHelper.abs(fractional - floatValue)) {
					decimalValue = fractional;
				}
			}
			adjusted[i] = values[i] > 0 ? intValue + decimalValue : intValue - decimalValue;
		}
		return new Vector3d(adjusted[0], adjusted[1], adjusted[2]);
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
	                consumer.put(e, r, g, b, 1.0f);
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
		setDirection(rotation.getRotatedDirection(_direction));
	}
	
	/**
	 * Sorts vec3ds for given direction.
	 * <p>
	 * If sorting fails, will return <code>false</code>
	 * and vec3ds will remain unmodified.  This is kept
	 * separate from {@link #setDirection(Direction)} so
	 * that vec3d order can be rotated to be compatible
	 * with baking orientation for lighting purposes.
	 * 
	 * @param direction a direction
	 * @return <code>true</code> if sorted successfully
	 */
	public boolean sortVec3ds(Direction direction) {
		Vector3d[] sortedVec3ds = QuadUtil.sortVector3dsByDirection(direction, _vec3ds);
		if (!QuadUtil.isValid(sortedVec3ds)) {
			return false;
		}
		_vec3ds = sortedVec3ds;
		return true;
	}
	
	/**
	 * Sets a new direction for quad.
	 * <p>
	 * If direction is compatible, will sort points and
	 * calculate a new normal if direction flips.
	 * 
	 * @param direction the new direction
	 * @return <code>true</code> if set successfully
	 */
	public boolean setDirection(Direction direction) {
		if (!sortVec3ds(direction)) {
			return false;
		}
		_direction = direction;
		_normal = QuadUtil.getNormal(_vec3ds);
		return true;
	}
	
	public Direction getDirection() {
		return _direction;
	}
	
	public Vector3d[] getVec3ds() {
		return _vec3ds;
	}
	
	public TextureAtlasSprite getTextureAtlasSprite() {
		return _textureAtlasSprite;
	}
	
	public Quad setTextureAtlasSprite(TextureAtlasSprite sprite) {
		_textureAtlasSprite = sprite;
		return this;
	}
	
	public int getRgb() {
		return _rgb;
	}

	public Quad setRgb(int rgb) {
		_rgb = rgb;
		return this;
	}

	public boolean isMaxBrightness() {
		return _maxBrightness;
	}

	public Quad setMaxBrightness(boolean maxBrightness) {
		_maxBrightness = maxBrightness;
		return this;
	}

	public RenderType getRenderType() {
		return _renderType;
	}

	public Quad setRenderType(RenderType renderType) {
		_renderType = renderType;
		return this;
	}
	
	public boolean canCover() {
		return _textureAtlasSprite.getName().getPath().contains("uncovered_");
	}
		
	public Vector3d getNormal() {
		return _normal;
	}
	
	public boolean hasDiffuseLighting() {
		return _diffuseLighting;
	}

	public Quad setDiffuseLighting(boolean _diffuseLighting) {
		this._diffuseLighting = _diffuseLighting;
		return this;
	}
	
	public double getOffset() {
		return _offset;
	}

	public Quad setOffset(double _offset) {
		this._offset = _offset;
		return this;
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
	
	public Direction getBakeDirection() {
		if (_bakeDirection == null) {
			return _direction;
		}
		return _bakeDirection;
	}
	
	public void setBakeDirection(Direction bakeDirection) {
		this._bakeDirection = bakeDirection;
	}
	
}