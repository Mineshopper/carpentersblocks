 package com.carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.carpentersblocks.util.attribute.EnumAttributeLocation;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class QuadUtil {
	
	public static class Vec2l {
		
		private long _u;
		private long _v;
		
		public Vec2l(long u, long v) {
			_u = u;
			_v = v;
		}
		
		public Vec2l(EnumFacing facing, Vec3d vec3d) {
			switch (facing.getAxis()) {
				case X:
					_u = Math.round(vec3d.z * 16);
					_v = Math.round(vec3d.y * 16);
					break;
				case Y:
	                _u = Math.round(vec3d.x * 16);
	                _v = Math.round(vec3d.z * 16);
					break;
				case Z:
					_u = Math.round(vec3d.x * 16);
					_v = Math.round(vec3d.y * 16);
					break;
			}
		}
		
		public long getU() {
			return _u;
		}
		
		public long getV() {
			return _v;
		}
		
	}
	
	public static Vec2l getVec2l(EnumFacing facing, Vec3d Vec3d) {
		return new Vec2l(facing, Vec3d);
	}
	
	private static Vec2l[] getBoundingPlane(EnumFacing facing, Vec3d[] vecs) {
		long minU = Long.MAX_VALUE;
		long maxU = Long.MIN_VALUE;
		long minV = Long.MAX_VALUE;
		long maxV = Long.MIN_VALUE;
		for (Vec3d vec : vecs) {
			Vec2l vec2l = new Vec2l(facing, vec);
			minU = Math.min(minU, vec2l.getU());
			maxU = Math.max(maxU, vec2l.getU());
			minV = Math.min(minV, vec2l.getV());
			maxV = Math.max(maxV, vec2l.getV());
		}
		switch (facing) {
			case DOWN:
				return new Vec2l[] { new Vec2l(minU, maxV), new Vec2l(minU, minV), new Vec2l(maxU, minV), new Vec2l(maxU, maxV) };
			case UP:
				return new Vec2l[] { new Vec2l(minU, minV), new Vec2l(minU, maxV), new Vec2l(maxU, maxV), new Vec2l(maxU, minV) };
			case NORTH:
				return new Vec2l[] { new Vec2l(maxU, maxV), new Vec2l(maxU, minV), new Vec2l(minU, minV), new Vec2l(minU, maxV) };
			case SOUTH:
				return new Vec2l[] { new Vec2l(minU, maxV), new Vec2l(minU, minV), new Vec2l(maxU, minV), new Vec2l(maxU, maxV) };
			case WEST:
				return new Vec2l[] { new Vec2l(minU, maxV), new Vec2l(minU, minV), new Vec2l(maxU, minV), new Vec2l(maxU, maxV) };
			case EAST:
				return new Vec2l[] { new Vec2l(maxU, maxV), new Vec2l(maxU, minV), new Vec2l(minU, minV), new Vec2l(minU, maxV) };
			default:
				return null;
		}
	}
	
	/**
	 * Check for malformed quad vertices.
	 * 
	 * @param quad the quad
	 * @return <code>true</code> if quad is valid<br><code>false</code> if quad is invalid
	 */
	public static boolean isValid(Quad quad) {
		if (quad == null || quad.getVecs() == null || quad.getVecs().length != 4) {
			return false;
		}
		Vec3d[] vecs = quad.getVecs();
		Comparator<Vec3d> comparator = new Comparator<Vec3d>() {
			@Override
			public int compare(Vec3d vec1, Vec3d vec2) {
				if (MathHelper.epsilonEquals((float) vec1.x, (float) vec2.x)
						&& MathHelper.epsilonEquals((float) vec1.y, (float) vec2.y)
						&& MathHelper.epsilonEquals((float) vec1.z, (float) vec2.z)) {
					return 0;
				}
				return 1;
			}
		};
		Set<Vec3d> set = new HashSet<Vec3d>(Arrays.asList(vecs));
		return set.size() >= 3; // Need 3 unique to calculate normal
	}

	private static long squareDistanceTo(EnumFacing facing, Vec2l vec2l, Vec3d Vec3d) {
		Vec2l vec2l2 = new Vec2l(facing, Vec3d);
        long u = vec2l2.getU() - vec2l.getU();
        long v = vec2l2.getV() - vec2l.getV();
        return u * u + v * v;
	}

	public static Vec3d[] sortVec3dsByFacing(EnumFacing facing, Vec3d[] inVecs) {
		Set<Vec3d> set = new HashSet<Vec3d>(Arrays.asList(inVecs));
		if (!(set.size() > 2 || set.size() < 5)) {
			return null;
		}
		
		List<Vec3d> vecs = new ArrayList<Vec3d>(Arrays.asList(inVecs));
		List[] cornerList = {
				new ArrayList<Vec3d>(),
				new ArrayList<Vec3d>(),
				new ArrayList<Vec3d>(),
				new ArrayList<Vec3d>() };
		
		// Generate bounds
		Vec2l[] bounds = getBoundingPlane(facing, inVecs);
		
		// Generate corner lists with closest vectors
		for (int i = 0; i < bounds.length; ++i) {
			SortedMap<Long,List<Vec3d>> map = new TreeMap<Long,List<Vec3d>>();
			for (Vec3d vec3d : vecs) {
				long dist = squareDistanceTo(facing, bounds[i], vec3d);
				if (!map.containsKey(dist)) {
					map.put(dist, new ArrayList<Vec3d>());
				}
				map.get(dist).add(vec3d);
			}
			cornerList[i].addAll(map.get(map.firstKey()));
		}
		
		// Save unique Vec3ds for removal from ambiguous points
		List<Vec3d> removeList = new ArrayList<Vec3d>();
		for (int i = 0; i < cornerList.length; ++i) {
			if (cornerList[i] != null && cornerList[i].size() == 1) {
				removeList.add((Vec3d) cornerList[i].get(0));
			}
		}
		
		// Remove standalone points from other matches
		for (int i = 0; i < cornerList.length; ++i) {
			if (cornerList[i] != null && cornerList[i].size() > 1) {
				cornerList[i].removeAll(removeList);
			}
		}
		
		// Map corners to finalVecs
		Vec3d[] finalVecs = new Vec3d[4];
		for (int i = 0; i < cornerList.length; ++i) {
			List<Vec3d> list = cornerList[i];
			if (list.size() > 0) {
				finalVecs[i] = list.get(0);
			}
		}
		
		// Count null points; collapsible may be two, slopes should only ever be one
		int emptyCorner = 0;
		for (int i = 0; i < cornerList.length; ++i) {
			if (cornerList[i].isEmpty()) {
				++emptyCorner;
			}
		}
		
		// Fill missing corners
		if (emptyCorner > 0) {
			if (finalVecs[0] == null) {
				finalVecs[0] = finalVecs[1];
			} else if (finalVecs[1] == null) {
				finalVecs[1] = finalVecs[0];
			}
			if (finalVecs[2] == null) {
				finalVecs[2] = finalVecs[3];
			} else if (finalVecs[3] == null) {
				finalVecs[3] = finalVecs[2];
			}
		}
		
		return finalVecs;
	}
	
	public static UV[] getUV(Quad quad, boolean isFloatingOverlay, EnumAttributeLocation location) {
		if (quad.isObliqueSlope()) {
			return getUVObliqueSlope(quad, isFloatingOverlay, location);
		} else {
			return getUV(quad, isFloatingOverlay);
		}
	}

	// TODO: Work on this; need to adjust for depth
	public static UV[] getUVObliqueSlope(Quad quad, boolean isFloatingOverlay, EnumAttributeLocation location) {
		Vec3d TL = quad.getVecs()[0];
		Vec3d BL = quad.getVecs()[1];
		Vec3d BR = quad.getVecs()[2];
		Vec3d TR = quad.getVecs()[3];
		
		// Set midpoint corner
		boolean[] midPt = new boolean[4];
		if (TL.equals(BL)) {
			if (compare(TL.y, TR.y) < 0) {
				midPt[3] = true;
			} else if (compare(TL.y, BR.y) > 0) {
				midPt[2] = true;
			}
		} else { // (BR.equals(TR)) {
			if (compare(TL.y, TR.y) > 0) {
				midPt[0] = true;
			} else if (compare(BL.y, BR.y) < 0) {
				midPt[1] = true;
			}
		}
		
		// If side location, translate on y-axis back to [0,1] bounds
		if (!EnumAttributeLocation.HOST.equals(location)) {
			quad = new Quad(quad);
			Vec3d[] vecs = quad.getVecs();
			double translation;
			if (EnumFacing.DOWN.equals(quad.getYSlope())) {
				translation = -1 * Math.min(BL.y, BR.y);
			} else {
				translation = -1 * (Math.max(TL.y, TR.y) - 1.0D);
			}
			if (Double.compare(0.0D, translation) != 0) {
				TL = TL.addVector(0, translation, 0);
				BL = BL.addVector(0, translation, 0);
				BR = BR.addVector(0, translation, 0);
				TR = TR.addVector(0, translation, 0);
			}
		}
		
		switch (quad.getCardinalFacing()) {
    		case NORTH:
    			// Midpoint is characteristic of sorting algorithm for triangles; covers built-in cases
    			if (isFloatingOverlay && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D).invertU(),
        				new UV(BL.x, TL.y - BL.y).invertU(),
        				new UV(BR.x, TL.y - BR.y).invertU(),
        				new UV(TR.x, TL.y - TR.y).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : TL.x, TL.y).invertUV(),
        				new UV(midPt[1] ? 0.5D : BL.x, BL.y).invertUV(),
        				new UV(midPt[2] ? 0.5D : BR.x, BR.y).invertUV(),
        				new UV(midPt[3] ? 0.5D : TR.x, TR.y).invertUV()
        			};
    			}
    		case SOUTH:
    			if (isFloatingOverlay && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D),
        				new UV(BL.x, TL.y - BL.y),
        				new UV(BR.x, TL.y - BR.y),
        				new UV(TR.x, TL.y - TR.y)
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : TL.x, TL.y).invertV(),
        				new UV(midPt[1] ? 0.5D : BL.x, BL.y).invertV(),
        				new UV(midPt[2] ? 0.5D : BR.x, BR.y).invertV(),
        				new UV(midPt[3] ? 0.5D : TR.x, TR.y).invertV()
        			};
    			}
    		case WEST:
    			if (isFloatingOverlay && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D),
        				new UV(BL.z, TL.y - BL.y),
        				new UV(BR.z, TL.y - BR.y),
        				new UV(TR.z, TL.y - TR.y)
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : TL.z, TL.y).invertV(),
        				new UV(midPt[1] ? 0.5D : BL.z, BL.y).invertV(),
        				new UV(midPt[2] ? 0.5D : BR.z, BR.y).invertV(),
        				new UV(midPt[3] ? 0.5D : TR.z, TR.y).invertV()
        			};
    			}
    		default: // EAST
    			if (isFloatingOverlay && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D).invertU(),
        				new UV(BL.z, TL.y - BL.y).invertU(),
        				new UV(BR.z, TL.y - BR.y).invertU(),
        				new UV(TR.z, TL.y - TR.y).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : TL.z, TL.y).invertUV(),
        				new UV(midPt[1] ? 0.5D : BL.z, BL.y).invertUV(),
        				new UV(midPt[2] ? 0.5D : BR.z, BR.y).invertUV(),
        				new UV(midPt[3] ? 0.5D : TR.z, TR.y).invertUV()
        			};
    			}
		}
	}
	
	public static UV[] getUV(Quad quad, boolean isFloatingOverlay) {
		Vec3d TL = quad.getVecs()[0];
		Vec3d BL = quad.getVecs()[1];
		Vec3d BR = quad.getVecs()[2];
		Vec3d TR = quad.getVecs()[3];
		switch (quad.getFacing()) {
			case DOWN:
				if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case NORTH:
							return new UV[] {
	            				new UV(TL.x, 1.0D),
	            				new UV(BL.x, TL.z - BL.z).invertV(),
	            				new UV(BR.x, TR.z - BR.z).invertV(),
	            				new UV(TR.x, 1.0D)
	            			};
						case SOUTH:
							return new UV[] {
	            				new UV(TL.x, TL.z - BL.z),
	            				new UV(BL.x, 0.0D),
	            				new UV(BR.x, 0.0D),
	            				new UV(TR.x, TR.z - BR.z)
	            			};
						case WEST:
							return new UV[] {
	            				new UV(TR.x - TL.x, TL.z).invertV(),
	            				new UV(BR.x - BL.x, BL.z).invertV(),
	            				new UV(0.0D, BR.z).invertV(),
	            				new UV(0.0D, TR.z).invertV()
	            			};
						default: // EAST
							return new UV[] {
	            				new UV(1.0D, TL.z).invertV(),
	            				new UV(1.0D, BL.z).invertV(),
	            				new UV(BR.x - BL.x, BR.z).invertUV(),
	            				new UV(TR.x - TL.x, TR.z).invertUV()
	            			};
					}
				} else {
					return new UV[] {
		    			new UV(TL.x + quad.getUVOffset().x, TL.z + quad.getUVOffset().z).invertV(),
		    			new UV(BL.x + quad.getUVOffset().x, BL.z + quad.getUVOffset().z).invertV(),
		    			new UV(BR.x + quad.getUVOffset().x, BR.z + quad.getUVOffset().z).invertV(),
		    			new UV(TR.x + quad.getUVOffset().x, TR.z + quad.getUVOffset().z).invertV()
	    			};
				}
			case UP:
				if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case NORTH:
		    				return new UV[] {
	            				new UV(TL.x, 0.0D),
	            				new UV(BL.x, BL.z - TL.z),
	            				new UV(BR.x, BR.z - TR.z),
	            				new UV(TR.x, 0.0D)
	            			};
						case SOUTH:
							return new UV[] {
								new UV(TL.x, 1.0D),
	            				new UV(BL.x, BL.z - TL.z).invertV(),
	            				new UV(BR.x, BR.z - TR.z).invertV(),
	            				new UV(TR.x, 1.0D)
	            			};
						case WEST:
							return new UV[] {
								new UV(TR.x - TL.x, TL.z),
	            				new UV(BR.x - BL.x, BL.z),
	            				new UV(0.0D, BR.z),
	            				new UV(0.0D, TR.z)
	            			};
						default: // EAST
							return new UV[] {
								new UV(1.0D, TL.z),
	            				new UV(TR.x - TL.x, BL.z).invertU(),
	            				new UV(BR.x - BL.x, BR.z).invertU(),
	            				new UV(1.0D, TR.z)
	            			};
					}
				} else {
					return new UV[] {
						new UV(TL.x + quad.getUVOffset().x, TL.z + quad.getUVOffset().z),
	    				new UV(BL.x + quad.getUVOffset().x, BL.z + quad.getUVOffset().z),
	    				new UV(BR.x + quad.getUVOffset().x, BR.z + quad.getUVOffset().z),
	    				new UV(TR.x + quad.getUVOffset().x, TR.z + quad.getUVOffset().z)
	    			};
				}
    		case NORTH:
    			if (isFloatingOverlay) {
    				return new UV[] {
        				new UV(TL.x, 0.0D).invertU(),
        				new UV(BL.x, TL.y - BL.y).invertU(),
        				new UV(BR.x, TR.y - BR.y).invertU(),
        				new UV(TR.x, 0.0D).invertU()
        			};
    			} else if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case DOWN:
		    				return new UV[] {
	            				new UV(TL.x, 1.0D).invertU(),
	            				new UV(BL.x, TL.y - BL.y).invertUV(),
	            				new UV(BR.x, TR.y - BR.y).invertUV(),
	            				new UV(TR.x, 1.0D).invertU()
	            			};
						case UP:
							return new UV[] {
	            				new UV(TL.x, TL.y - BL.y).invertU(),
	            				new UV(BL.x, 0.0D).invertU(),
	            				new UV(BR.x, 0.0D).invertU(),
	            				new UV(TR.x, TR.y - BR.y).invertU()
	            			};
						case WEST:
							return new UV[] {
	            				new UV(1.0D, TL.y).invertV(),
	            				new UV(1.0D, BL.y).invertV(),
	            				new UV(BL.x - BR.x, BR.y).invertUV(),
	            				new UV(TL.x - TR.x, TR.y).invertUV()
	            			};
						default: // EAST
		    				return new UV[] {
	            				new UV(TL.x - TR.x, TL.y).invertV(),
	            				new UV(BL.x - BR.x, BL.y).invertV(),
	            				new UV(0.0D, BR.y).invertV(),
	            				new UV(0.0D, TR.y).invertV()
	            			};
					}
				} else {
    				return new UV[] {
        				new UV(TL.x + quad.getUVOffset().x, TL.y + quad.getUVOffset().y).invertUV(),
        				new UV(BL.x + quad.getUVOffset().x, BL.y + quad.getUVOffset().y).invertUV(),
        				new UV(BR.x + quad.getUVOffset().x, BR.y + quad.getUVOffset().y).invertUV(),
        				new UV(TR.x + quad.getUVOffset().x, TR.y + quad.getUVOffset().y).invertUV()
        			};
				}
    		case SOUTH:
    			if (isFloatingOverlay) {
					return new UV[] {
        				new UV(TL.x, 0.0D),
        				new UV(BL.x, TL.y - BL.y),
        				new UV(BR.x, TR.y - BR.y),
        				new UV(TR.x, 0.0D)
        			};
				} else if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case DOWN:
		    				return new UV[] {
	            				new UV(TL.x, 1.0D),
	            				new UV(BL.x, TL.y - BL.y).invertV(),
	            				new UV(BR.x, TR.y - BR.y).invertV(),
	            				new UV(TR.x, 1.0D)
	            			};
						case UP:
							return new UV[] {
	            				new UV(TL.x, TL.y - BL.y),
	            				new UV(BL.x, 0.0D),
	            				new UV(BR.x, 0.0D),
	            				new UV(TR.x, TR.y - BR.y)
	            			};
						case WEST:
							return new UV[] {
	            				new UV(0.0D, TL.y).invertV(),
	            				new UV(0.0D, BL.y).invertV(),
	            				new UV(BR.x - BL.x, BR.y).invertV(),
	            				new UV(TR.x - TL.x, TR.y).invertV()
	            			};
						default: // EAST
							return new UV[] {
	            				new UV(TR.x - TL.x, TL.y).invertUV(),
	            				new UV(BR.x - BL.x, BL.y).invertUV(),
	            				new UV(1.0D, BR.y).invertV(),
	            				new UV(1.0D, TR.y).invertV()
	            			};
					}
				} else {
					return new UV[] {
        				new UV(TL.x + quad.getUVOffset().x, TL.y + quad.getUVOffset().y).invertV(),
        				new UV(BL.x + quad.getUVOffset().x, BL.y + quad.getUVOffset().y).invertV(),
        				new UV(BR.x + quad.getUVOffset().x, BR.y + quad.getUVOffset().y).invertV(),
        				new UV(TR.x + quad.getUVOffset().x, TR.y + quad.getUVOffset().y).invertV()
        			};
				}
    		case WEST:
    			if (isFloatingOverlay) {
					return new UV[] {
        				new UV(TL.z, 0.0D),
        				new UV(BL.z, TL.y - BL.y),
        				new UV(BR.z, TR.y - BR.y),
        				new UV(TR.z, 0.0D)
        			};
				} else if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case DOWN:
		    				return new UV[] {
	            				new UV(TL.z, 1.0D),
	            				new UV(BL.z, TL.y - BL.y).invertV(),
	            				new UV(BR.z, TR.y - BR.y).invertV(),
	            				new UV(TR.z, 1.0D)
	            			};
						case UP:
							return new UV[] {
	            				new UV(TL.z, TL.y - BL.y),
	            				new UV(BL.z, 0.0D),
	            				new UV(BR.z, 0.0D),
	            				new UV(TR.z, TR.y - BR.y)
	            			};
						case NORTH:
							return new UV[] {
	            				new UV(0.0D, TL.y).invertV(),
	            				new UV(0.0D, BL.y).invertV(),
	            				new UV(BR.z - BL.z, BR.y).invertV(),
	            				new UV(TR.z - TL.z, TR.y).invertV()
	            			};
						default: // SOUTH
							return new UV[] {
	            				new UV(TR.z - TL.z, TL.y).invertUV(),
	            				new UV(BR.z - BL.z, BL.y).invertUV(),
	            				new UV(1.0D, BR.y).invertV(),
	            				new UV(1.0D, TR.y).invertV()
	            			};
					}
				} else {
					return new UV[] {
						new UV(TL.z + quad.getUVOffset().z, TL.y + quad.getUVOffset().y).invertV(),
        				new UV(BL.z + quad.getUVOffset().z, BL.y + quad.getUVOffset().y).invertV(),
        				new UV(BR.z + quad.getUVOffset().z, BR.y + quad.getUVOffset().y).invertV(),
        				new UV(TR.z + quad.getUVOffset().z, TR.y + quad.getUVOffset().y).invertV()
        			};	
				}
    		case EAST:
    			if (isFloatingOverlay) {
    				return new UV[] {
        				new UV(TL.z, 0.0D).invertU(),
        				new UV(BL.z, TL.y - BL.y).invertU(),
        				new UV(BR.z, TR.y - BR.y).invertU(),
        				new UV(TR.z, 0.0D).invertU()
        			};
				} else if (quad.hasFloatingUV()) {
					switch (quad.getUVFloat()) {
						case DOWN:
		    				return new UV[] {
	            				new UV(TL.z, 1.0D).invertU(),
	            				new UV(BL.z, TL.y - BL.y).invertUV(),
	            				new UV(BR.z, TR.y - BR.y).invertUV(),
	            				new UV(TR.z, 1.0D).invertU()
	            			};
						case UP:
							return new UV[] {
	            				new UV(TL.z, TL.y - BL.y).invertU(),
	            				new UV(BL.z, 0.0D).invertU(),
	            				new UV(BR.z, 0.0D).invertU(),
	            				new UV(TR.z, TR.y - BR.y).invertU()
	            			};
						case NORTH:
							return new UV[] {
	            				new UV(1.0D, TL.y).invertV(),
	            				new UV(1.0D, BL.y).invertV(),
	            				new UV(BL.z - BR.z, BR.y).invertUV(),
	            				new UV(TL.z - TR.z, TR.y).invertUV()
	            			};
						default: // SOUTH
		    				return new UV[] {
	            				new UV(TL.z - TR.z, TL.y).invertV(),
	            				new UV(BL.z - BR.z, BL.y).invertV(),
	            				new UV(0.0D, BR.y).invertV(),
	            				new UV(0.0D, TR.y).invertV()
	            			};
					}
				} else {
					return new UV[] {
        				new UV(TL.z + quad.getUVOffset().z, TL.y + quad.getUVOffset().y).invertUV(),
        				new UV(BL.z + quad.getUVOffset().z, BL.y + quad.getUVOffset().y).invertUV(),
        				new UV(BR.z + quad.getUVOffset().z, BR.y + quad.getUVOffset().y).invertUV(),
        				new UV(TR.z + quad.getUVOffset().z, TR.y + quad.getUVOffset().y).invertUV()
        			};
				}
			default:
				return null;
		}
	}
	
	public static List<Quad> getPerpendicularQuads(Quad quad, double depth) {
		List<Quad> list = new ArrayList<Quad>();
		Vec3d[] vecs = quad.getVecs();
		switch (quad.getSideCoverAltFacing()) {
			case DOWN:
				list.add(Quad.getQuad(
					EnumFacing.NORTH,
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(0, -depth, 0),
					vecs[1].addVector(0, -depth, 0),
					vecs[1].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.SOUTH,
					vecs[0].addVector(0, 0, 0),
					vecs[0].addVector(0, -depth, 0),
					vecs[3].addVector(0, -depth, 0),
					vecs[3].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.WEST,
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(0, -depth, 0),
					vecs[0].addVector(0, -depth, 0),
					vecs[0].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.EAST,
					vecs[3].addVector(0, 0, 0),
					vecs[3].addVector(0, -depth, 0),
					vecs[2].addVector(0, -depth, 0),
					vecs[2].addVector(0, 0, 0)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.DOWN));
				break;
			case UP:
				list.add(Quad.getQuad(
					EnumFacing.NORTH,
					vecs[3].addVector(0, depth, 0),
					vecs[3].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, 0),
					vecs[0].addVector(0, depth, 0)));
				list.add(Quad.getQuad(
					EnumFacing.SOUTH,
					vecs[1].addVector(0, depth, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(0, depth, 0)));
				list.add(Quad.getQuad(
					EnumFacing.WEST,
					vecs[0].addVector(0, depth, 0),
					vecs[0].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(0, depth, 0)));
				list.add(Quad.getQuad(
					EnumFacing.EAST,
					vecs[2].addVector(0, depth, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[3].addVector(0, 0, 0),
					vecs[3].addVector(0, depth, 0)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.UP));
				break;
			case NORTH:
				list.add(Quad.getQuad(
					EnumFacing.DOWN,
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, -depth),
					vecs[1].addVector(0, 0, -depth),
					vecs[1].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.UP,
					vecs[3].addVector(0, 0, -depth),
					vecs[3].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, -depth)));
				list.add(Quad.getQuad(
					EnumFacing.WEST,
					vecs[3].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, -depth),
					vecs[3].addVector(0, 0, -depth)));
				list.add(Quad.getQuad(
					EnumFacing.EAST,
					vecs[0].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, -depth),
					vecs[0].addVector(0, 0, -depth)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.NORTH));
				break;
			case SOUTH:
				list.add(Quad.getQuad(
					EnumFacing.DOWN,
					vecs[1].addVector(0, 0, depth),
					vecs[1].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, depth)));
				list.add(Quad.getQuad(
					EnumFacing.UP,
					vecs[0].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, depth),
					vecs[3].addVector(0, 0, depth),
					vecs[3].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.WEST,
					vecs[0].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, depth),
					vecs[0].addVector(0, 0, depth)));
				list.add(Quad.getQuad(
					EnumFacing.EAST,
					vecs[3].addVector(0, 0, depth),
					vecs[2].addVector(0, 0, depth),
					vecs[2].addVector(0, 0, 0),
					vecs[3].addVector(0, 0, 0)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.SOUTH));
				break;
			case WEST:
				list.add(Quad.getQuad(
					EnumFacing.DOWN,
					vecs[2].addVector(-depth, 0, 0),
					vecs[1].addVector(-depth, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.UP,
					vecs[0].addVector(-depth, 0, 0),
					vecs[3].addVector(-depth, 0, 0),
					vecs[3].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.NORTH,
					vecs[0].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(-depth, 0, 0),
					vecs[0].addVector(-depth, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.SOUTH,
					vecs[3].addVector(-depth, 0, 0),
					vecs[2].addVector(-depth, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[3].addVector(0, 0, 0)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.WEST));
				break;
			case EAST:
				list.add(Quad.getQuad(
					EnumFacing.DOWN,
					vecs[1].addVector(0, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[2].addVector(depth, 0, 0),
					vecs[1].addVector(depth, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.UP,
					vecs[3].addVector(0, 0, 0),
					vecs[0].addVector(0, 0, 0),
					vecs[0].addVector(depth, 0, 0),
					vecs[3].addVector(depth, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.NORTH,
					vecs[3].addVector(depth, 0, 0),
					vecs[2].addVector(depth, 0, 0),
					vecs[2].addVector(0, 0, 0),
					vecs[3].addVector(0, 0, 0)));
				list.add(Quad.getQuad(
					EnumFacing.SOUTH,
					vecs[0].addVector(0, 0, 0),
					vecs[1].addVector(0, 0, 0),
					vecs[1].addVector(depth, 0, 0),
					vecs[0].addVector(depth, 0, 0)));
				list.removeIf(Objects::isNull);
				list.forEach(q -> q.setUVFloat(EnumFacing.EAST));
				break;
		}
		return list;
	}
	
	/**
	 * Compares {@link Double} values using epsilon of 1/16, since
	 * Minecraft blocks are 16 pixels across.
	 * 
	 * @param d1 first double
	 * @param d2 second double
	 * @return 	<ul>
	 * 				<li>-1 if first value is less than second value</li>
	 * 				<li>0 if both values are equal</li>
	 * 				<li>1 if first value is greater than second value</li>
	 * 			</ul>
	 */
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
	
}
