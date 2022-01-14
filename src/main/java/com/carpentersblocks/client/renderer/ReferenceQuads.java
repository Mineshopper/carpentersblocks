package com.carpentersblocks.client.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.ModLogger;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.QuadUtil;
import com.carpentersblocks.util.RotationUtil.CbRotation;

import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Holds list of raw quads for current block.
 */
public class ReferenceQuads {
	
	private LockableQuadsSource _lockableQuads;
	
	/**
	 * Default constructor that accepts a location.
	 * <p>
	 * This is generally used for generating an initial list of
	 * quads for the {@link EnumAttributeLocation.HOST HOST}
	 * location.
	 * 
	 * @param location a location
	 */
	public ReferenceQuads() {
		_lockableQuads = new LockableQuadsSource();
	}
	
	/**
	 * Adds all quads.
	 * 
	 * @param quads a list of quads
	 */
	public void addAll(List<Quad> quads) {
		quads.forEach(q -> add(q));
	}
	
	/**
	 * Adds quad.
	 * 
	 * @param quad a quad
	 */
	public void add(Quad quad) {
		if (quad != null && QuadUtil.isValid(quad.getVec3ds())) {
			_lockableQuads.add(quad);
		}
	}
	
	/**
	 * Rotates quads using input rotation.
	 * 
	 * @param rotation the rotation
	 */
	public void rotate(CbRotation rotation) {
		_lockableQuads.rotate(rotation);
	}
	
	/**
	 * Gets quads for host location.
	 * 
	 * @return a list of quads
	 */
	public List<Quad> get() {
		return _lockableQuads.copy();
	}
	
	/**
	 * Gets quads generated for side locations with a given thickness.
	 * 
	 * @param location the location
	 * @param depth the depth of side
	 * @return a list of quads
	 */
	public List<Quad> get(EnumAttributeLocation location, double depth) {
		if (location.isHost()) {
			ModLogger.log(Level.WARN, "Attempted to get side quads for HOST location");
			return new ArrayList<>();
		}
		List<Quad> quads = new ArrayList<>();
		Direction facing = Direction.from3DDataValue(location.ordinal());
	    for (Quad quad : _lockableQuads.copy()) {
	    	Direction offsetFacing = QuadUtil.getSideCoverRenderDirection(quad); // sloped faces offset up/down only
	    	if (facing.equals(offsetFacing)) { // side location direction matches offset facing
		    	//quadContainer.add(new Quad(quad).setFacing(facing.getOpposite())); // should only add this if face is visible (if host is translucent/mipped)
		    	Quad offsetQuad = new Quad(quad);
		    	offsetQuad.setDirection(offsetFacing);
	    		quads.addAll(QuadUtil.getPerpendicularQuads(offsetQuad, depth));
	    		quads.add(quad.offset(offsetFacing.getStepX() * depth, offsetFacing.getStepY() * depth, offsetFacing.getStepZ() * depth));
	    	}
	    }
	    
	    // remove duplicate quads with matching vector coordinates
	    if (quads.size() > 6) {
			List<Quad> list = new ArrayList<Quad>(_lockableQuads.size());
			QuadComparator comparator = new QuadComparator();
			Iterator<Quad> iter = quads.iterator();
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
	    return quads;
	}
	
	/**
	 * Comparator used for sorting quads.
	 */
	private class QuadComparator implements Comparator<Quad> {
		
		@Override
		public int compare(Quad q1, Quad q2) {
			List<Vector3d> tempList = new ArrayList<Vector3d>();
			tempList.addAll(Arrays.asList(q1.getVec3ds()));
			Iterator<Vector3d> iter = tempList.iterator();
			while (iter.hasNext()) {
				Vector3d v1 = iter.next();
				for (Vector3d v2 : q2.getVec3ds()) {
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
	 * Locks source quads after filling is complete.
	 */
	public void lockQuads() {
		_lockableQuads.lock();
	}
	
	/**
	 * Provides a list of quads that, once initialized and locked,
	 * can be copied freely without affecting state of the initial
	 * list of quads.
	 */
	private class LockableQuadsSource {
		
		private boolean _locked;
		private List<Quad> _list;
		
		public LockableQuadsSource() {
			_list = new ArrayList<Quad>();
		}
		
		public void add(Quad quad) {
			if (_locked) {
				ModLogger.log(Level.DEBUG, "Attempted to add quad to locked list; expect render abnormalities");
				return;
			}
			_list.add(quad);
		}
		
		public List<Quad> copy() {
			if (!_locked) {
				ModLogger.log(Level.DEBUG, "Attempted to copy quads, but object is not locked; expect render abnormalities");
				return new ArrayList<>();
			}
			return _list.stream().map(q -> new Quad(q)).collect(Collectors.toList());
		}
		
		public void rotate(CbRotation rotation) {
			if (_locked) {
				ModLogger.log(Level.DEBUG, "Attempted to rotate quads in locked list; expect render abnormalities");
				return;
			}
			_list.forEach(q -> q.rotate(rotation));
		}
		
		public void lock() {
			if (_locked) {
				ModLogger.log(Level.DEBUG, "Attempted to lock quad list more than once; expect render abnormalities");
				return;
			}
			_locked = true;
		}
		
		public int size() {
			return _list.size();
		}
		
	}
	
}
