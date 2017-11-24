package com.carpentersblocks.renderer;

import com.carpentersblocks.util.IConstants;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.registry.SpriteRegistry;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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
	
	private Quad() { }
	
	private Quad(EnumFacing facing, Vec3d ... vecs) {
		_facing = facing;
		_vecs = vecs;
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
	}
	
	public static Quad getQuad(EnumFacing facing, Vec3d ... inVecs) {
		return getQuad(facing, SpriteRegistry.sprite_uncovered_full, inVecs);
	}
	
	public static Quad getQuad(EnumFacing facing, TextureAtlasSprite sprite, Vec3d ... inVecs) {
		Vec3d[] vec3ds = VecUtil.buildVecs(facing, inVecs);
		if (vec3ds == null || vec3ds.length != 4) {
			return null;
		}
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
		for (int i = 0; i < _vecs.length; ++i) {
			_vecs[i] = _vecs[i].addVector(x, y, z);
		}
		return this;
	}
	
	public EnumFacing getFacing() {
		return _facing;
	}
	
	public void setFacing(EnumFacing facing) {
		_facing = facing;
	}
	
	public Vec3d[] getVecs() {
		return _vecs;
	}
	
	private void setVecs(Vec3d[] vecs) {
		_vecs = vecs;
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
	
	public BakedQuad bake(VertexFormat vertexFormat, EnumAttributeLocation location) {
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(vertexFormat);
        builder.setTexture(_sprite);
        Vec3d normal = VecUtil.getNormal(this);
        boolean floatY = _sprite.getIconName().equals(GRASS_SIDE_OVERLAY) || _sprite.getIconName().contains("overlay/overlay_") && _sprite.getIconName().endsWith("_side");
        UV[] uv = VecUtil.getUV(this, floatY, location);
        for (int i = 0; i < 4; ++i) {
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
	                case COLOR:
	                	builder.put(
	                		idx,
	                		((_rgb & 0xff0000) >> 16) / 255.0f,
	                		((_rgb & 0xff00) >> 8) / 255.0f,
	                		(_rgb & 0xff) / 255.0f, 1.0f
	                	);
	                    break;
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
	                		(float) normal.x,
	                		(float) normal.y,
	                		(float) normal.z,
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
	
}
