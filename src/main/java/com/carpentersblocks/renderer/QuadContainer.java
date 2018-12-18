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

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class QuadContainer {
	
	private List<Quad> _quads;
	private EnumAttributeLocation _location;
	private final static double MAX_UP_SLOPE = Math.sin(35 * Math.PI / 180);
	private final static double MAX_SIDE_SLOPE = -Math.sin(55 * Math.PI / 180);
	
	public QuadContainer(EnumAttributeLocation location) {
		_quads = new ArrayList<Quad>();
		_location = location;
	}
	
	public List<BakedQuad> bakeQuads() {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : _quads) {
			Quad cpyQuad = new Quad(quad);
			list.add(cpyQuad.bake(_location));
		}
		return list;
	}
	
	public List<Quad> getQuads(IBlockState blockState, EnumFacing ... facings) {
		transformForBlockState(false, blockState);
		List<Quad> quads = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (facings.length == 0) {
				quads.add(new Quad(quad));
			} else {
				for (EnumFacing facing : facings) {
					if (facing.equals(quad.getFacing())) {
						quads.add(new Quad(quad));
					}
				}
			}
		}
		return quads;
	}
	
	public void addAll(Collection<Quad> collection) {
		_quads.addAll(collection);
	}
	
	public void add(Quad quad) {
		if (QuadUtil.isValid(quad)) {
			_quads.add(quad);
		}
	}

	public QuadContainer toSideLocation(IBlockState blockState, EnumAttributeLocation location, double depth) {
		QuadContainer quadContainer = new QuadContainer(location);
		EnumFacing facing = EnumFacing.getFront(location.ordinal());
	    for (Quad quad : _quads) {
	    	EnumFacing offsetFacing = quad.getSideCoverAltFacing(); // Sloped faces should be UP/DOWN
	    	if (facing.equals(offsetFacing)) { // Side cover direction matches offset facing
		    	//quadContainer.add(new Quad(quad).setFacing(facing.getOpposite())); // Should only add this if face is visible (if host is translucent/mipped)
		    	Quad offsetQuad = new Quad(quad);
		    	offsetQuad.applyFacing(offsetFacing);
	    		quadContainer.addAll(QuadUtil.getPerpendicularQuads(offsetQuad, depth));
	    		quadContainer.add(quad.offset(offsetFacing.getFrontOffsetX() * depth, offsetFacing.getFrontOffsetY() * depth, offsetFacing.getFrontOffsetZ() * depth));
	    	}
	    }
	    
	    // Remove duplicate quads with matching vector coordinates
	    if (quadContainer._quads.size() > 6) {
			List<Quad> list = new ArrayList<Quad>(quadContainer._quads);
			QuadComparator comparator = new QuadComparator();
			Iterator<Quad> iter = quadContainer._quads.iterator();
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
			list.add(cpyQuad.bake(_location));
		}
		return list;
	}
	
	public List<BakedQuad> getBakedQuads(EnumFacing facing) {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : _quads) {
			list.add(quad.bake(_location));
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
	
	/**
	 * For sloped quads, maps correct facing for quad
	 * based on UP, DOWN, or side facing orientation
	 * and block type.
	 * 
	 * @param blockState cover blockstate
	 */
	public void transformForBlockState(boolean isSideCover, IBlockState blockState) {
		if (!isSideCover && blockState == null) {
			return;
		}
		List<Quad> list = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (quad.isSloped(Axis.Y)) {
				if (isSideCover) {
					if (!Axis.Y.equals(quad.getFacing())) {
						Quad newQuad = new Quad(quad);
						newQuad.applyFacing(quad.getNormal().y > 0.0D ? EnumFacing.UP : EnumFacing.DOWN);
						list.add(newQuad);
					} else {
						list.add(new Quad(quad));
					}
				} else {
					if (Blocks.GRASS.equals(blockState.getBlock())) {
						Quad newQuad = new Quad(quad);
						if (QuadUtil.compare(quad.getNormal().y, MAX_UP_SLOPE) >= 0) {
							if (!EnumFacing.UP.equals(quad.getFacing())) {
								newQuad.applyFacing(EnumFacing.UP);
							}
						} else if (QuadUtil.compare(quad.getNormal().y, MAX_SIDE_SLOPE) >= 0) {
							if (!quad.getCardinalFacing().equals(quad.getFacing())) {
								newQuad.applyFacing(quad.getCardinalFacing());
							}
						} else {
							if (!EnumFacing.DOWN.equals(quad.getFacing())) {
								newQuad.applyFacing(EnumFacing.DOWN);
							}
						}
						list.add(newQuad);
					} else {
						Quad newQuad = new Quad(quad);
						if (!quad.getCardinalFacing().equals(quad.getFacing())) {
							newQuad.applyFacing(quad.getCardinalFacing());
						}
						list.add(newQuad);
					}
				}
			} else {
				list.add(new Quad(quad));
			}
		}
		_quads.clear();
		_quads.addAll(list);
	}
	
}
