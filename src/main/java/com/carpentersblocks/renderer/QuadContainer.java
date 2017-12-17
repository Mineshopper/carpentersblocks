package com.carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.carpentersblocks.util.RotationUtil.Rotation;
import com.carpentersblocks.util.attribute.EnumAttributeLocation;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class QuadContainer {
	
	private List<Quad> _quads;
	private VertexFormat _vertexFormat;
	private boolean _sideCover;
	private EnumAttributeLocation _location;
	private boolean _stateful;
	
	public QuadContainer(VertexFormat vertexFormat, EnumAttributeLocation location, boolean sideCover) {
		_quads = new ArrayList<Quad>();
		_vertexFormat = vertexFormat;
		_sideCover = sideCover;
		_location = location;
	}
	
	public List<Quad> getQuads(EnumFacing facing) {
		List<Quad> quads = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (facing.equals(quad.getFacing())) {
				quads.add(new Quad(quad));
			}
		}
		return quads;
	}
	
	public List<Quad> getQuads() {
		return _quads;
	}
	
	public void addAll(Collection<Quad> collection) {
		_quads.addAll(collection);
	}
	
	public void add(Quad quad) {
		if (VecUtil.isValid(quad)) {
			_quads.add(quad);
		}
	}

	public QuadContainer toSideLocation(EnumAttributeLocation location, double depth) {
		QuadContainer quadContainer = new QuadContainer(_vertexFormat, location, true);
		EnumFacing facing = EnumFacing.getFront(location.ordinal());
	    List<Quad> quads = getQuads(facing);
	    for (Quad quad : quads) {
	    	//quadContainer.add(new Quad(quad).setFacing(facing.getOpposite())); // Should only add this if face is visible (if host is translucent/mipped)
	    	Quad sideQuad = Quad.getQuad(facing, quad.getVecs());
	    	if (sideQuad != null) {
	    		quadContainer.add(sideQuad.offset(facing.getFrontOffsetX() * depth, facing.getFrontOffsetY() * depth, facing.getFrontOffsetZ() * depth));
		    	for (Quad perpQuad : VecUtil.getPerpendicularQuads(quad, depth)) {
		    		quadContainer.add(new Quad(perpQuad));
		    	}
	    	}
	    }
	    // Remove duplicate quads with matching vector coordinates
	    if (quadContainer.getQuads().size() > 6) {
			List<Quad> list = new ArrayList<Quad>(quadContainer.getQuads());
			QuadComparator comparator = new QuadComparator();
			Iterator<Quad> iter = quadContainer.getQuads().iterator();
			while (iter.hasNext()) {
				Quad q1 = iter.next();
				for (Quad q2 : list) {
					if (q1 != q2 && comparator.compare(q1, q2) == 0) {
						iter.remove();
						break;
					}
				}
			}
		}
	    return quadContainer;
	}
	
	public List<BakedQuad> bakeQuads(List<Quad> quads) {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : quads) {
			Quad cpyQuad = new Quad(quad);
			list.add(cpyQuad.bake(_vertexFormat, _location));
		}
		return list;
	}
	
	public List<BakedQuad> getBakedQuads(EnumFacing facing) {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : _quads) {
			list.add(quad.bake(_vertexFormat, _location));
		}
		return list;
	}
	
	public Set<BlockRenderLayer> getRenderLayers(boolean renderAttribute) {
		Set<BlockRenderLayer> set = new HashSet<BlockRenderLayer>();
		for (Quad quad : _quads) {
			set.add(quad.getRenderLayer());
		}
		return set;
	}
	
	public boolean hasCoverOverride() {
		for (Quad quad : _quads) {
			if (!quad.canCover()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean getIsStateful() {
		return _stateful;
	}
	
	public void setStateful(boolean value) {
		_stateful = value;
	}
	
	public boolean isCoverable(BlockRenderLayer renderLayer) {
		for (Quad quad : _quads) {
			if (quad.canCover() && renderLayer.equals(quad.getRenderLayer())) {
				return true;
			}
		}
		return false;
	}
	
	private class QuadComparator implements Comparator<Quad> {
		
		@Override
		public int compare(Quad q1, Quad q2) {
			List<Vec3d> tempList = new ArrayList<Vec3d>();
			tempList.addAll(Arrays.asList(q1.getVecs()));
			Iterator<Vec3d> iter = tempList.iterator();
			while (iter.hasNext()) {
				Vec3d v1 = iter.next();
				for (Vec3d v2 : q2.getVecs()) {
					if (MathHelper.epsilonEquals((float) v1.x, (float) v2.x)
							&& MathHelper.epsilonEquals((float) v1.y, (float) v2.y)
							&& MathHelper.epsilonEquals((float) v1.z, (float) v2.z)) {
						iter.remove();
						break;
					}
				}
			}
			if (tempList.isEmpty()) {
				return 0;
			} else {
				return -1;
			}
		}
		
	}
	
	/**
	 * Rotates quads about facing axis.
	 * 
	 * @param facing defines the axis of rotation
	 * @param rotation the rotation enum
	 */
	public void rotate(Rotation rotation) {
		for (Quad quad : _quads) {
			quad.rotate(rotation);
		}
	}
	
}
