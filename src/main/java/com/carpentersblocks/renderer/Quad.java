package com.carpentersblocks.renderer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

import com.carpentersblocks.block.data.SlopeData;
import com.carpentersblocks.block.state.Property;
import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.ModLogger;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.registry.BlockRegistry;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class Quad {

	private static final float FULL_BRIGHT = (float) (15 * 0x20) / 0xffff;
	private static final String GRASS_SIDE_OVERLAY = "minecraft:blocks/grass_side_overlay";
	
   	private Vec3d[] _vecs;
	private EnumFacing _facing;
	private TextureAtlasSprite _sprite;
	private int _rgb;
	private boolean _maxBrightness;
	private BlockRenderLayer _renderLayer;
	private Vec3d _normal;
	boolean _isOblique;
	
	private Quad() { }
	
	private Quad(EnumFacing facing, Vec3d ... vecs) {
		_vecs = vecs;
		applyFacing(facing);
	}
	
	public enum Rotation {
		
		QUARTER(Math.PI / 2),
		HALF(Math.PI),
		THREE_QUARTER(Math.PI * 3 / 2);
		
		private double _value;
		
		Rotation(double value) {
			_value = value;
		}
		
		public double getValue() {
			return _value;
		}
		
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param srcQuad the source quad
	 */
	public Quad(Quad srcQuad) {
		Vec3d[] srcVecs = srcQuad.getVecs();
		Vec3d[] vecs = new Vec3d[srcVecs.length];
		for (int i = 0; i < srcVecs.length; i++) {
			vecs[i] = new Vec3d(srcVecs[i].x, srcVecs[i].y, srcVecs[i].z);
		}
		_vecs = vecs;
		_facing = srcQuad.getFacing();
		_maxBrightness = srcQuad.isMaxBrightness();
		_renderLayer = srcQuad.getRenderLayer();
		_rgb = srcQuad.getRgb();
		_sprite = srcQuad.getSprite();
		_normal = new Vec3d(srcQuad.getNormal().x, srcQuad.getNormal().y, srcQuad.getNormal().z);
		_isOblique = srcQuad.isObliqueSlope();
	}
	
	public static Quad getQuad(EnumFacing facing, Vec3d ... inVecs) {
		return getQuad(facing, SpriteRegistry.sprite_uncovered_full, inVecs);
	}
	
	public static Quad getQuad(EnumFacing facing, TextureAtlasSprite sprite, Vec3d ... inVecs) {
		Vec3d[] vec3ds = VecUtil.buildVecs(facing, inVecs);
		return getQuad(facing, sprite, IConstants.DEFAULT_RGB, false, BlockRenderLayer.CUTOUT_MIPPED, inVecs);
	}
	
	public static Quad getQuad(EnumFacing facing, TextureAtlasSprite sprite, int rgb, Vec3d ... inVecs) {
		Vec3d[] vec3ds = VecUtil.buildVecs(facing, inVecs);
		if (vec3ds == null || vec3ds.length != 4) {
			return null;
		}
		return getQuad(facing, sprite, rgb, false, BlockRenderLayer.CUTOUT_MIPPED, inVecs);
	}
	
	public static Quad getQuad(EnumFacing facing, TextureAtlasSprite sprite, int rgb, boolean maxBrightness, BlockRenderLayer renderLayer, Vec3d ... inVecs) {
		Vec3d[] vec3ds = VecUtil.buildVecs(facing, inVecs);
		if (vec3ds == null || vec3ds.length != 4) {
			return null;
		}
		Quad quad = new Quad(facing, vec3ds);
		quad.setSprite(sprite);
		quad.setMaxBrightness(maxBrightness);
		quad.setRenderLayer(renderLayer);
		quad.setRgb(rgb);
		return quad;
	}
	
	public Quad offset(double x, double y, double z) {
		Quad quad = new Quad(this);
		for (int i = 0; i < quad._vecs.length; ++i) {
			quad._vecs[i] = quad._vecs[i].addVector(x, y, z);
		}
		return quad;
	}
	
	public EnumFacing getFacing() {
		return _facing;
	}
	
	public Vec3d[] getVecs() {
		return _vecs;
	}
	
	public TextureAtlasSprite getSprite() {
		return _sprite;
	}
	
	public void setSprite(TextureAtlasSprite sprite) {
		_sprite = sprite;
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

	public BlockRenderLayer getRenderLayer() {
		return _renderLayer;
	}

	public void setRenderLayer(BlockRenderLayer renderLayer) {
		_renderLayer = renderLayer;
	}
	
	public boolean canCover() {
		return getSprite().getIconName().contains("uncovered_");
	}
	
	public BakedQuad bake(EnumAttributeLocation location) {
		VertexFormat vertexFormat = RenderPkg._threadLocalVertexFormat.get();
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(vertexFormat);
        builder.setTexture(_sprite);        
        
        // If 
       // applyFacing(this.getCardinalFacing());
        // Oblique sloped quads always render using cardinal facing
        //if (this._isOblique && Double.compare(Math.abs(_normal.x), Math.abs(_normal.z)) == 0) {
        //	applyFacing(this.getCardinalFacing());
        //}
        
        boolean floatY = _sprite.getIconName().equals(GRASS_SIDE_OVERLAY) || _sprite.getIconName().contains("overlay/overlay_") && _sprite.getIconName().endsWith("_side");

        // TODO: Maybe clean this up if rendering in inventory
        Block block = (Block) RenderPkg.getThreadedProperty(Property.BLOCK_TYP);
        Integer cbMetadata = (Integer) RenderPkg.getThreadedProperty(Property.CB_METADATA);
        
        if (BlockRegistry.blockCarpentersSlope.equals(block) && _isOblique) {
        	switch (SlopeData.getType(cbMetadata)) {
	        	case OBLIQUE_EXTERIOR:
	        		// Split into two faces (not sure if possible from here)
	        		break;
	        	case OBLIQUE_INTERIOR:
	        		// One face
	        		break;
        		default: {}
        	}
        } else if (BlockRegistry.blockCarpentersCollapsibleBlock.equals(block) && (isSloped(Axis.X) || isSloped(Axis.Y) || isSloped(Axis.Z))) {
        	// Find rotated facing and render using it
        	this._facing = (EnumFacing) RenderPkg.getThreadedProperty(Property.FACING);
        	this._isOblique = false;
        	floatY = false;
        }
        
        UV[] uv = VecUtil.getUV(this, floatY, location);
        for (int i = 0; i < 4; ++i) {
        	//_rgb = i == 0 ? 0xff0000 : i == 1 ? 0x00ff00 : i == 2 ? 0x0000ff : 0xffff00; // Debug
	        for (int idx = 0; idx < vertexFormat.getElementCount(); idx++) {
	            switch (vertexFormat.getElement(idx).getUsage()) {
	                case POSITION:
	                    builder.put(
	                    	idx,
	                    	(float) _vecs[i].x,
	                    	(float) _vecs[i].y,
	                    	(float) _vecs[i].z,
	                    	1f
	                    );
	                    break;
	                case COLOR: {
	                	builder.put(
	                		idx,
	                		((_rgb & 0xff0000) >> 16) / 255.0f,
	                		((_rgb & 0xff00) >> 8) / 255.0f,
	                		(_rgb & 0xff) / 255.0f, 1.0f
	                	);
	                    break;
	                }
	                case UV:
	                    if (vertexFormat.getElement(idx).getIndex() == 0) {
	                        builder.put(
	                        	idx,
	                        	_sprite.getInterpolatedU(uv[i].getU()),
	                        	_sprite.getInterpolatedV(uv[i].getV()),
	                        	0f,
	                        	1f
	                        );
	                    } else if (vertexFormat.getElement(idx).getIndex() == 1) {
	                    	float brightness = _maxBrightness ? FULL_BRIGHT : 0f;
							builder.put(
								idx,
								brightness,
								brightness
							);
	                    }
	                    break;
	                case NORMAL:
	                	builder.put(
	                		idx,
	                		(float) _normal.x,
	                		(float) _normal.y,
	                		(float) _normal.z,
	                		0f);
	                    break;
	                default:
	                    builder.put(idx);
	                    break;
	            }
	        }
        }
		return builder.build();
	}
	
	/**
	 * Rotates quads about axis. Positive axis is clockwise rotation.
	 * Negative axis is counter-clockwise rotation.
	 * 
	 * @param axis the axis of rotation
	 * @param rotation the rotation enum
	 */
	public void rotate(com.carpentersblocks.util.RotationUtil.Rotation rotation) {
		Vec3d[] newVecs = new Vec3d[_vecs.length];
		EnumFacing newFacing = rotation.getRotatedFacing(_facing);
		for (int i = 0; i < _vecs.length; ++i) {
			Vec3d vec3d = _vecs[i];
			Vec3d vec3dRot = vec3d.add(Vec3d.ZERO);
			if (rotation.getRadians(Axis.X) > 0) {
				vec3dRot = rotateAroundX(vec3dRot, rotation.getRadians(Axis.X));
			}
			if (rotation.getRadians(Axis.Y) > 0) {
				vec3dRot = rotateAroundY(vec3dRot, rotation.getRadians(Axis.Y));
			}
			if (rotation.getRadians(Axis.Z) > 0) {
				vec3dRot = rotateAroundZ(vec3dRot, rotation.getRadians(Axis.Z));
			}
			newVecs[i] = vec3dRot;
		}
		_vecs = newVecs;
		applyFacing(newFacing);
	}
	
	private Vec3d rotateAroundX(Vec3d vec3d, double radians) {
    	double y = 0.5D + (vec3d.z - 0.5D) * Math.sin(radians) + (vec3d.y - 0.5D) * Math.cos(radians);
    	double z = 0.5D + (vec3d.z - 0.5D) * Math.cos(radians) - (vec3d.y - 0.5D) * Math.sin(radians);
    	return new Vec3d(vec3d.x, y, z);
    }
	
	private Vec3d rotateAroundY(Vec3d vec3d, double radians) {
    	double z = 0.5D + (vec3d.x - 0.5D) * Math.sin(radians) + (vec3d.z - 0.5D) * Math.cos(radians);
    	double x = 0.5D + (vec3d.x - 0.5D) * Math.cos(radians) - (vec3d.z - 0.5D) * Math.sin(radians);
    	return new Vec3d(x, vec3d.y, z);
    }
    
	private Vec3d rotateAroundZ(Vec3d vec3d, double radians) {
    	double x = 0.5D + (vec3d.y - 0.5D) * Math.sin(radians) + (vec3d.x - 0.5D) * Math.cos(radians);
    	double y = 0.5D + (vec3d.y - 0.5D) * Math.cos(radians) - (vec3d.x - 0.5D) * Math.sin(radians);
    	return new Vec3d(x, y, vec3d.z);
    }
	
	public EnumFacing getYSlope() {
		return _normal.y > 0.0D ? EnumFacing.UP : EnumFacing.DOWN;
	}
	
	public boolean isSloped(Axis axis) {
		double axis1 = Axis.X.equals(axis) ? _normal.x : Axis.Y.equals(axis) ? _normal.y : _normal.z;
		return compare(axis1, -1.0D) > 0 &&
				compare(axis1, 0.0D) != 0 &&
				compare(axis1, 1.0D) < 0;
	}
	
	/**
	 * Return quad facing as NWSE component
	 * 
	 * @return
	 */
	public EnumFacing getCardinalFacing() {
		if (!isSloped(Axis.Y)) {
			ModLogger.warning("getCardinalFacing called for non-sloping Y-axis");
		}
		return EnumFacing.getFacingFromVector((float)_normal.x, 0, (float)_normal.z);
	}
	
	/**
	 * Applies facing and calculates properties.
	 * <p>
	 * Quad must be rotated to match facing prior to calling this
	 * or runtime exception may occur.
	 * 
	 * @param facing the new facing
	 */
	public void applyFacing(EnumFacing facing) {
		_facing = facing;
		_vecs = VecUtil.buildVecs(facing, _vecs);
		Vec3d[] vecs1 = new LinkedHashSet<Vec3d>(Arrays.asList(this.getVecs())).toArray(new Vec3d[this.getVecs().length]);
		_normal = (vecs1[1].subtract(vecs1[0])).crossProduct(vecs1[2].subtract(vecs1[1])).normalize();
		_isOblique = isSloped(Axis.X) && isSloped(Axis.Y) && isSloped(Axis.Z);
	}
	
	public static int compare(double d1, double d2) {
		double epsilon = 0.0001;
		double diff = d1 - d2;
		if (diff < -epsilon) {
			return -1;
		} else if (diff > epsilon) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public Vec3d getNormal() {
		return _normal;
	}
	
	public boolean isObliqueSlope() {
		return _isOblique;
	}
	
	private boolean isTriangle() {
		return new HashSet<Vec3d>(Arrays.asList(this.getVecs())).size() < 4;
	}
	
	public EnumFacing getSideCoverOffset() {
		if (isSloped(Axis.Y)) {
			if (_normal.y > 0.0D) {
				return EnumFacing.UP;
			} else {
				return EnumFacing.DOWN;
			}
		} else {
			return _facing;
		}
	}
	
}
