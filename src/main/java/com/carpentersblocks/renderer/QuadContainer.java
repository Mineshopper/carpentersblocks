package com.carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.List;

import com.carpentersblocks.util.attribute.EnumAttributeLocation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

public class QuadContainer {

	private static final String GRASS_SIDE_OVERLAY = "minecraft:blocks/grass_side_overlay";
	private List<Quad> _quads;
	private VertexFormat _format;
	private boolean _sideCover;
	private EnumAttributeLocation _location;
	
	public QuadContainer(VertexFormat format, EnumAttributeLocation location, boolean sideCover) {
		_quads = new ArrayList<Quad>();
		_format = format;
		_sideCover = sideCover;
		_location = location;
	}
	
	public List<Quad> getQuads(EnumFacing facing) {
		List<Quad> quads = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (facing.equals(quad.getFacing())) {
				quads.add(quad);
			}
		}
		return quads;
	}
	
	public void add(Quad quad) {
		if (VecUtil.isValid(quad)) {
			_quads.add(quad);
		}
	}

	public QuadContainer toSideLocation(EnumAttributeLocation location, double depth) {
		QuadContainer quadContainer = new QuadContainer(_format, location, true);
		EnumFacing facing = EnumFacing.getFront(location.ordinal());
	    List<Quad> quads = getQuads(facing);
	    for (Quad quad : quads) {
	    	//quadContainer.add(new Quad(quad).setFacing(facing.getOpposite())); // Should only add this if face is visible (if host is translucent/mipped)
	    	Quad sideQuad = Quad.getQuad(facing, quad.getVecs());
	    	if (sideQuad != null) {
	    		quadContainer.add(sideQuad.offset(facing.getFrontOffsetX() * depth, facing.getFrontOffsetY() * depth, facing.getFrontOffsetZ() * depth));
		    	for (Quad perpQuad : VecUtil.getPerpendicularQuads(quad, depth)) {
		    		quadContainer.add(perpQuad);
		    	}
	    	}
	    }
	    return quadContainer;
	}
	
	public List<BakedQuad> getBakedQuads(EnumFacing facing, TextureAtlasSprite sprite, int rgb) {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : _quads) {
			if (facing.equals(quad.getFacing())) {
				UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(_format);
		        builder.setTexture(sprite);
		        Vec3d normal = VecUtil.getNormal(quad);
		        boolean floatY = sprite.getIconName().equals(GRASS_SIDE_OVERLAY) || sprite.getIconName().contains("overlay/overlay_") && sprite.getIconName().endsWith("_side");
		        UV[] uv = VecUtil.getUV(quad, floatY, _location);
		        Vec3d[] vecs = quad.getVecs();
		        putVertex(builder, normal, vecs[0].xCoord, vecs[0].yCoord, vecs[0].zCoord, uv[0].getU(), uv[0].getV(), sprite, rgb); // 0xff0000 Testing
		        putVertex(builder, normal, vecs[1].xCoord, vecs[1].yCoord, vecs[1].zCoord, uv[1].getU(), uv[1].getV(), sprite, rgb); // 0x00ff00 Testing
		        putVertex(builder, normal, vecs[2].xCoord, vecs[2].yCoord, vecs[2].zCoord, uv[2].getU(), uv[2].getV(), sprite, rgb); // 0x0000ff Testing
		        putVertex(builder, normal, vecs[3].xCoord, vecs[3].yCoord, vecs[3].zCoord, uv[3].getU(), uv[3].getV(), sprite, rgb); // 0xffff00 Testing
		        list.add(builder.build());
			}
		}
		return list;
	}
	
    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite, int rgb) {
    	for (int idx = 0; idx < _format.getElementCount(); idx++) {
            switch (_format.getElement(idx).getUsage()) {
                case POSITION:
                    builder.put(idx, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                	builder.put(idx, ((rgb & 0xff0000) >>> 16) / 255.0f, ((rgb & 0xff00) >>> 8) / 255.0f, (rgb & 0xff) / 255.0f, 1.0f);
                    break;
                case UV:
                    if (_format.getElement(idx).getIndex() == 0) {
                        builder.put(idx, sprite.getInterpolatedU(u), sprite.getInterpolatedV(v), 0f, 1f);
                    }
                    break;
                case NORMAL:
                    builder.put(idx, (float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord, 0f);
                    break;
                default:
                    builder.put(idx);
                    break;
            }
        }
    }
	
}
