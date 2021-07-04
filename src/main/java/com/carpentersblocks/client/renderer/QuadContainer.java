package com.carpentersblocks.client.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

public class QuadContainer {
	
	private List<Quad> _quads;
	private EnumAttributeLocation _location;
	private final static double MAX_UP_SLOPE_GRASS = Math.sin(35 * Math.PI / 180);
	private final static double MAX_SIDE_SLOPE_GRASS = -Math.sin(55 * Math.PI / 180);
	private final static double MAX_UP_SLOPE = Math.sin(46 * Math.PI / 180);
	private final static double MAX_SIDE_SLOPE = -Math.sin(44 * Math.PI / 180);
	
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
	
	public List<Quad> getQuads(BlockState blockState, Direction ... facings) {
		transformForBlockState(false, blockState);
		List<Quad> quads = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (facings.length == 0) {
				quads.add(new Quad(quad));
			} else {
				for (Direction facing : facings) {
					if (facing.equals(quad.getDirection())) {
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

	public QuadContainer toSideLocation(BlockState blockState, EnumAttributeLocation location, double depth) {
		QuadContainer quadContainer = new QuadContainer(location);
		Direction facing = Direction.from3DDataValue(location.ordinal());
	    for (Quad quad : _quads) {
	    	Direction offsetFacing = QuadUtil.getSideCoverRenderDirection(quad); // Sloped faces should be UP/DOWN
	    	if (facing.equals(offsetFacing)) { // Side cover direction matches offset facing
		    	//quadContainer.add(new Quad(quad).setFacing(facing.getOpposite())); // Should only add this if face is visible (if host is translucent/mipped)
		    	Quad offsetQuad = new Quad(quad);
		    	
		    	offsetQuad.applyFacing(true, offsetFacing);
	    		quadContainer.addAll(QuadUtil.getPerpendicularQuads(offsetQuad, depth));
	    		quadContainer.add(quad.offset(offsetFacing.getStepX() * depth, offsetFacing.getStepY() * depth, offsetFacing.getStepZ() * depth));
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
	    transformForBlockState(true, blockState);
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
	
	public List<BakedQuad> getBakedQuads(Direction facing) {
		List<BakedQuad> list = new ArrayList<BakedQuad>();
		for (Quad quad : _quads) {
			list.add(quad.bake(_location));
		}
		return list;
	}
	
	public Set<RenderType> getRenderTypes(boolean renderAttribute) {
		Set<RenderType> set = new HashSet<RenderType>();
		for (Quad quad : _quads) {
			set.add(quad.getRenderType());
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
	
	public boolean isCoverable(RenderType renderType) {
		for (Quad quad : _quads) {
			if (quad.canCover() && renderType.equals(quad.getRenderType())) {
				return true;
			}
		}
		return false;
	}
	
	private class QuadComparator implements Comparator<Quad> {
		
		@Override
		public int compare(Quad q1, Quad q2) {
			List<Vector3d> tempList = new ArrayList<Vector3d>();
			tempList.addAll(Arrays.asList(q1.getVecs()));
			Iterator<Vector3d> iter = tempList.iterator();
			while (iter.hasNext()) {
				Vector3d v1 = iter.next();
				for (Vector3d v2 : q2.getVecs()) {
					if (MathHelper.equal((float) v1.x, (float) v2.x)
							&& MathHelper.equal((float) v1.y, (float) v2.y)
							&& MathHelper.equal((float) v1.z, (float) v2.z)) {
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
	public void rotate(CbRotation rotation) {
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
	public void transformForBlockState(boolean isSideCover, BlockState blockState) {
		if (!isSideCover && blockState == null) {
			return;
		}
		List<Quad> list = new ArrayList<Quad>();
		for (Quad quad : _quads) {
			if (quad.isSloped(Axis.Y)) {
				if (isSideCover) {
					if (!Axis.Y.equals(quad.getDirection().getAxis())) {
						Quad newQuad = new Quad(quad);
						newQuad.applyFacing(true, quad.getNormal().y > 0.0D ? Direction.UP : Direction.DOWN);
						list.add(newQuad);
					} else {
						list.add(new Quad(quad));
					}
				} else {
					double MAX_UP_SLOPE = QuadContainer.MAX_UP_SLOPE;
					double MAX_SIDE_SLOPE = QuadContainer.MAX_SIDE_SLOPE;
					if (Blocks.GRASS_BLOCK.equals(blockState.getBlock()) || Blocks.MYCELIUM.equals(blockState.getBlock())) {
						MAX_UP_SLOPE = MAX_UP_SLOPE_GRASS;
						MAX_SIDE_SLOPE = MAX_SIDE_SLOPE_GRASS;
					}
					Quad newQuad = new Quad(quad);
					if (QuadUtil.compare(quad.getNormal().y, MAX_UP_SLOPE) >= 0) {
						if (!Direction.UP.equals(quad.getDirection())) {
							newQuad.applyFacing(true, Direction.UP);
						}
					} else if (QuadUtil.compare(quad.getNormal().y, MAX_SIDE_SLOPE) >= 0) {
						if (!QuadUtil.getCardinalFacing(quad).equals(quad.getDirection())) {
							newQuad.applyFacing(true, QuadUtil.getCardinalFacing(quad));
						}
					} else {
						if (!Direction.DOWN.equals(quad.getDirection())) {
							newQuad.applyFacing(true, Direction.DOWN);
						}
					}
					list.add(newQuad);
				}
			} else {
				list.add(new Quad(quad));
			}
		}
		_quads.clear();
		_quads.addAll(list);
	}
	
}
