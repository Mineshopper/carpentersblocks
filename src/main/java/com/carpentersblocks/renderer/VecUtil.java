 package com.carpentersblocks.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.carpentersblocks.util.attribute.EnumAttributeLocation;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class VecUtil {
	
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

	public static Vec3d[] buildVecs(EnumFacing facing, Vec3d[] vecs) {
		Set<Vec3d> set = new HashSet<Vec3d>(Arrays.asList(vecs));
		if (set.size() == 3) {
			return sortTriangle(facing, set.toArray(new Vec3d[set.size()]));
		} else if (set.size() == 4) {
			return sortQuad(facing, vecs);
		} else {
			return null;
		}
	}
	
	private static Vec3d[] sortQuad(EnumFacing facing, Vec3d[] vecs) {
		List<Vec3d> consumables = new LinkedList<Vec3d>(Arrays.asList(vecs));
		Vec3d[] newVecs = { vecs[0], vecs[1], vecs[2], vecs[3] };
		Vec2l[] bounds = getBoundingPlane(facing, vecs);
		for (int i = 0; i < bounds.length; ++i) {
			Vec3d closest = null;
			long minDist = 0;
			for (Vec3d Vec3d : consumables) {
				long dist = squareDistanceTo(facing, bounds[i], Vec3d);
				if (closest == null || dist < minDist) {
					minDist = dist;
					closest = Vec3d;
				}
			}
			consumables.remove(closest);
			newVecs[i] = closest;
		}
		return newVecs;
	}
	
	private static Vec3d[] sortTriangle(EnumFacing facing, Vec3d[] inVecs) {
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
	
	public static UV[] getUV(Quad quad, boolean floatY, EnumAttributeLocation location) {
		if (quad.isObliqueSlope()) {
			return getUVObliqueSlope(quad, floatY, location);
		} else if (quad.getFacing().ordinal() != location.ordinal() && !location.equals(EnumAttributeLocation.HOST)) {
			return getUVSideCover(quad, floatY, location);
		} else {
			return getUV(quad, floatY);
		}
	}

	// TODO: Work on this; need to adjust for depth
	public static UV[] getUVObliqueSlope(Quad quad, boolean floatY, EnumAttributeLocation location) {
		Vec3d[] vecs = quad.getVecs();
		
		// Needs to be selected based on oblique properties:
		//	1. Hard-coded slope oblique ext/int
		//	2. Dynamic collapsible oblique (not sure how to map UV)
		
		// Set midpoint corner
		boolean[] midPt = new boolean[4];
		if (vecs[0].equals(vecs[1])) {
			if (Quad.compare(vecs[0].y, vecs[3].y) < 0) {
				midPt[3] = true;
			} else if (Quad.compare(vecs[0].y, vecs[2].y) > 0) {
				midPt[2] = true;
			}
		} else { // (vecs[2].equals(vecs[3])) {
			if (Quad.compare(vecs[0].y, vecs[3].y) > 0) {
				midPt[0] = true;
			} else if (Quad.compare(vecs[1].y, vecs[2].y) < 0) {
				midPt[1] = true;
			}
		}
		
		// If side location, translate on y-axis back to [0,1] bounds
		if (!EnumAttributeLocation.HOST.equals(location)) {
			quad = new Quad(quad);
			vecs = quad.getVecs();
			double translation;
			if (EnumFacing.DOWN.equals(quad.getYSlope())) {
				translation = -1 * Math.min(vecs[1].y, vecs[2].y);
			} else {
				translation = -1 * (Math.max(vecs[0].y, vecs[3].y) - 1.0D);
			}
			if (Double.compare(0.0D, translation) != 0) {
				for (int i = 0; i < vecs.length; ++i) {
					vecs[i] = vecs[i].addVector(0, translation, 0);
				}
			}
		}
		
		switch (quad.getCardinalFacing()) {
    		case NORTH:
    			// Midpoint is characteristic of sorting algorithm for triangles; covers built-in cases
    			if (floatY && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D).invertU(),
        				new UV(vecs[1].x, vecs[0].y - vecs[1].y).invertU(),
        				new UV(vecs[2].x, vecs[0].y - vecs[2].y).invertU(),
        				new UV(vecs[3].x, vecs[0].y - vecs[3].y).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : vecs[0].x, vecs[0].y).invertUV(),
        				new UV(midPt[1] ? 0.5D : vecs[1].x, vecs[1].y).invertUV(),
        				new UV(midPt[2] ? 0.5D : vecs[2].x, vecs[2].y).invertUV(),
        				new UV(midPt[3] ? 0.5D : vecs[3].x, vecs[3].y).invertUV()
        			};
    			}
    		case SOUTH:
    			if (floatY && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D),
        				new UV(vecs[1].x, vecs[0].y - vecs[1].y),
        				new UV(vecs[2].x, vecs[0].y - vecs[2].y),
        				new UV(vecs[3].x, vecs[0].y - vecs[3].y)
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : vecs[0].x, vecs[0].y).invertV(),
        				new UV(midPt[1] ? 0.5D : vecs[1].x, vecs[1].y).invertV(),
        				new UV(midPt[2] ? 0.5D : vecs[2].x, vecs[2].y).invertV(),
        				new UV(midPt[3] ? 0.5D : vecs[3].x, vecs[3].y).invertV()
        			};
    			}
    		case WEST:
    			if (floatY && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D),
        				new UV(vecs[1].z, vecs[0].y - vecs[1].y),
        				new UV(vecs[2].z, vecs[0].y - vecs[2].y),
        				new UV(vecs[3].z, vecs[0].y - vecs[3].y)
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : vecs[0].z, vecs[0].y).invertV(),
        				new UV(midPt[1] ? 0.5D : vecs[1].z, vecs[1].y).invertV(),
        				new UV(midPt[2] ? 0.5D : vecs[2].z, vecs[2].y).invertV(),
        				new UV(midPt[3] ? 0.5D : vecs[3].z, vecs[3].y).invertV()
        			};
    			}
    		default: // EAST
    			if (floatY && midPt[0]) {
    				return new UV[] {
        				new UV(0.5D, 0.0D).invertU(),
        				new UV(vecs[1].z, vecs[0].y - vecs[1].y).invertU(),
        				new UV(vecs[2].z, vecs[0].y - vecs[2].y).invertU(),
        				new UV(vecs[3].z, vecs[0].y - vecs[3].y).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(midPt[0] ? 0.5D : vecs[0].z, vecs[0].y).invertUV(),
        				new UV(midPt[1] ? 0.5D : vecs[1].z, vecs[1].y).invertUV(),
        				new UV(midPt[2] ? 0.5D : vecs[2].z, vecs[2].y).invertUV(),
        				new UV(midPt[3] ? 0.5D : vecs[3].z, vecs[3].y).invertUV()
        			};
    			}
		}
	}
	
	public static UV[] getUV(Quad quad, boolean floatY) {
		Vec3d[] vecs = quad.getVecs();
		switch (quad.getFacing()) {
    		case DOWN:
    			return new UV[] {
	    			new UV(vecs[0].x, vecs[0].z).invertV(),
	    			new UV(vecs[1].x, vecs[1].z).invertV(),
	    			new UV(vecs[2].x, vecs[2].z).invertV(),
	    			new UV(vecs[3].x, vecs[3].z).invertV()
    			};
    		case UP:
				return new UV[] {
    				new UV(vecs[0].x, vecs[0].z),
    				new UV(vecs[1].x, vecs[1].z),
    				new UV(vecs[2].x, vecs[2].z),
    				new UV(vecs[3].x, vecs[3].z)
    			};
    		case NORTH:
    			if (floatY) {
    				return new UV[] {
        				new UV(vecs[0].x, 0.0D).invertU(),
        				new UV(vecs[1].x, vecs[0].y - vecs[1].y).invertU(),
        				new UV(vecs[2].x, vecs[3].y - vecs[2].y).invertU(),
        				new UV(vecs[3].x, 0.0D).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(vecs[0].x, vecs[0].y).invertUV(),
        				new UV(vecs[1].x, vecs[1].y).invertUV(),
        				new UV(vecs[2].x, vecs[2].y).invertUV(),
        				new UV(vecs[3].x, vecs[3].y).invertUV()
        			};
    			}
    		case SOUTH:
    			if (floatY) {
    				return new UV[] {
        				new UV(vecs[0].x, 0.0D),
        				new UV(vecs[1].x, vecs[0].y - vecs[1].y),
        				new UV(vecs[2].x, vecs[3].y - vecs[2].y),
        				new UV(vecs[3].x, 0.0D)
        			};
    			} else {
    				return new UV[] {
        				new UV(vecs[0].x, vecs[0].y).invertV(),
        				new UV(vecs[1].x, vecs[1].y).invertV(),
        				new UV(vecs[2].x, vecs[2].y).invertV(),
        				new UV(vecs[3].x, vecs[3].y).invertV()
        			};
    			}
    		case WEST:
    			if (floatY) {
    				return new UV[] {
        				new UV(vecs[0].z, 0.0D),
        				new UV(vecs[1].z, vecs[0].y - vecs[1].y),
        				new UV(vecs[2].z, vecs[3].y - vecs[2].y),
        				new UV(vecs[3].z, 0.0D)
        			};
    			} else {
    				return new UV[] {
        				new UV(vecs[0].z, vecs[0].y).invertV(),
        				new UV(vecs[1].z, vecs[1].y).invertV(),
        				new UV(vecs[2].z, vecs[2].y).invertV(),
        				new UV(vecs[3].z, vecs[3].y).invertV()
        			};
    			}
    		case EAST:
    			if (floatY) {
    				return new UV[] {
        				new UV(vecs[0].z, 0.0D).invertU(),
        				new UV(vecs[1].z, vecs[0].y - vecs[1].y).invertU(),
        				new UV(vecs[2].z, vecs[3].y - vecs[2].y).invertU(),
        				new UV(vecs[3].z, 0.0D).invertU()
        			};
    			} else {
    				return new UV[] {
        				new UV(vecs[0].z, vecs[0].y).invertUV(),
        				new UV(vecs[1].z, vecs[1].y).invertUV(),
        				new UV(vecs[2].z, vecs[2].y).invertUV(),
        				new UV(vecs[3].z, vecs[3].y).invertUV()
        			};
    			}
		}
		return null;
	}
	
	private static UV[] getUVSideCover(Quad quad, boolean floatY, EnumAttributeLocation location) {
		Vec3d[] vecs = quad.getVecs();
		double depth = 0.0625D;
		switch (quad.getFacing()) {
    		case DOWN:
    			switch (location) {
	    			case NORTH:
	    				// Offset -Z
	    				depth = vecs[0].z - vecs[1].z;
	    				return new UV[] {
	    					new UV(vecs[0].x,         1.0D).invertV(),
	    					new UV(vecs[1].x, 1.0D - depth).invertV(),
	    					new UV(vecs[2].x, 1.0D - depth).invertV(),
	    					new UV(vecs[3].x,         1.0D).invertV()
	    				};
	    			case SOUTH:
	    				// Offset +Z
	    				depth = vecs[0].z - vecs[1].z;
	    				return new UV[] {
	    					new UV(vecs[0].x, depth).invertV(),
	    					new UV(vecs[1].x,  0.0D).invertV(),
	    					new UV(vecs[2].x,  0.0D).invertV(),
	    					new UV(vecs[3].x, depth).invertV()
	    				};
	    			case WEST:
	    				// Offset -X
	    				depth = vecs[3].x - vecs[0].x;
	    				return new UV[] {
	    					new UV(1.0D - depth, vecs[0].z).invertV(),
	    					new UV(1.0D - depth, vecs[1].z).invertV(),
	    					new UV(1.0D,         vecs[2].z).invertV(),
	    					new UV(1.0D,         vecs[3].z).invertV()
	    				};
	    			default: //case EAST:
	    				// Offset +X
	    				depth = vecs[3].x - vecs[0].x;
	    				return new UV[] {
	    					new UV( 0.0D, vecs[0].z).invertV(),
	    					new UV( 0.0D, vecs[1].z).invertV(),
	    					new UV(depth, vecs[2].z).invertV(),
	    					new UV(depth, vecs[3].z).invertV()
	    				};
    			}
    		case UP:
    			switch (location) {
	    			case NORTH:
	    				// Offset -Z
	    				depth = vecs[1].z - vecs[0].z;
	    				return new UV[] {
	    					new UV(vecs[0].x, depth).invertV(),
	    					new UV(vecs[1].x,  0.0D).invertV(),
	    					new UV(vecs[2].x,  0.0D).invertV(),
	    					new UV(vecs[3].x, depth).invertV()
	    				};
	    			case SOUTH:
	    				// Offset +Z
	    				depth = vecs[1].z - vecs[0].z;
	    				return new UV[] {
	    					new UV(vecs[0].x,         1.0D).invertV(),
	    					new UV(vecs[1].x, 1.0D - depth).invertV(),
	    					new UV(vecs[2].x, 1.0D - depth).invertV(),
	    					new UV(vecs[3].x,         1.0D).invertV()
	    				};
	    			case WEST:
	    				// Offset -X
	    				depth = vecs[3].x - vecs[0].x;
	    				return new UV[] {
	    					new UV(1.0D - depth, vecs[0].z),
	    					new UV(1.0D - depth, vecs[1].z),
	    					new UV(1.0D,         vecs[2].z),
	    					new UV(1.0D,         vecs[3].z)
	    				};
	    			default: //case EAST:
	    				// Offset +X
	    				depth = vecs[3].x - vecs[0].x;
	    				return new UV[] {
	    					new UV( 0.0D, vecs[0].z),
	    					new UV( 0.0D, vecs[1].z),
	    					new UV(depth, vecs[2].z),
	    					new UV(depth, vecs[3].z)
	    				};
				}
    		case NORTH:
    			switch (location) {
	    			case DOWN:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D).invertU(),
		    					new UV(vecs[1].x, depth).invertU(),
		    					new UV(vecs[2].x, depth).invertU(),
		    					new UV(vecs[3].x,  0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D).invertU(),
		    					new UV(vecs[1].x, depth).invertU(),
		    					new UV(vecs[2].x, depth).invertU(),
		    					new UV(vecs[3].x,  0.0D).invertU()
		    				};
	    				}
	    			case UP:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D).invertU(),
		    					new UV(vecs[1].x, depth).invertU(),
		    					new UV(vecs[2].x, depth).invertU(),
		    					new UV(vecs[3].x,  0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
	    						new UV(vecs[0].x, depth).invertUV(),
		    					new UV(vecs[1].x,  0.0D).invertUV(),
		    					new UV(vecs[2].x,  0.0D).invertUV(),
		    					new UV(vecs[3].x, depth).invertUV()
		    				};
	    				}
	    			case WEST:
	    				depth = vecs[0].x - vecs[3].x;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV( 0.0D, 0.0D),
		    					new UV( 0.0D, vecs[0].y - vecs[1].y),
		    					new UV(depth, vecs[3].y - vecs[2].y),
		    					new UV(depth, 0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV( 0.0D, vecs[0].y).invertV(),
		    					new UV( 0.0D, vecs[1].y).invertV(),
		    					new UV(depth, vecs[2].y).invertV(),
		    					new UV(depth, vecs[3].y).invertV()
		    				};
	    				}
	    			default: //case EAST:
	    				depth = vecs[0].x - vecs[3].x;
	    				if (floatY) {
		    				return new UV[] {
	            				new UV(depth, 0.0D).invertU(),
	            				new UV(depth, vecs[0].y - vecs[1].y).invertU(),
	            				new UV( 0.0D, vecs[3].y - vecs[2].y).invertU(),
	            				new UV( 0.0D, 0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(depth, vecs[0].y).invertUV(),
		    					new UV(depth, vecs[1].y).invertUV(),
		    					new UV( 0.0D, vecs[2].y).invertUV(),
		    					new UV( 0.0D, vecs[3].y).invertUV()
		    				};
	    				}
    			}
    		case SOUTH:
    			switch (location) {
	    			case DOWN:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D),
		    					new UV(vecs[1].x, depth),
		    					new UV(vecs[2].x, depth),
		    					new UV(vecs[3].x,  0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D),
		    					new UV(vecs[1].x, depth),
		    					new UV(vecs[2].x, depth),
		    					new UV(vecs[3].x,  0.0D)
		    				};
	    				}
	    			case UP:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].x,  0.0D),
		    					new UV(vecs[1].x, depth),
		    					new UV(vecs[2].x, depth),
		    					new UV(vecs[3].x,  0.0D)
		    				};
	    				} else {
		    				return new UV[] {
	    						new UV(vecs[0].x, depth).invertV(),
		    					new UV(vecs[1].x,  0.0D).invertV(),
		    					new UV(vecs[2].x,  0.0D).invertV(),
		    					new UV(vecs[3].x, depth).invertV()
		    				};
	    				}
	    			case WEST:
	    				depth = vecs[0].x - vecs[3].x;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(depth, 0.0D).invertU(),
		    					new UV(depth, vecs[0].y - vecs[1].y).invertU(),
		    					new UV( 0.0D, vecs[3].y - vecs[2].y).invertU(),
		    					new UV( 0.0D, 0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(depth, vecs[0].y).invertUV(),
		    					new UV(depth, vecs[1].y).invertUV(),
		    					new UV( 0.0D, vecs[2].y).invertUV(),
		    					new UV( 0.0D, vecs[3].y).invertUV()
		    				};
	    				}
	    			default: //case EAST:
	    				depth = vecs[0].x - vecs[3].x;
	    				if (floatY) {
		    				return new UV[] {
	            				new UV( 0.0D, 0.0D),
	            				new UV( 0.0D, vecs[0].y - vecs[1].y),
	            				new UV(depth, vecs[3].y - vecs[2].y),
	            				new UV(depth, 0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV( 0.0D, vecs[0].y).invertV(),
		    					new UV( 0.0D, vecs[1].y).invertV(),
		    					new UV(depth, vecs[2].y).invertV(),
		    					new UV(depth, vecs[3].y).invertV()
		    				};
	    				}
				}
    		case WEST:
    			switch (location) {
	    			case DOWN:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D),
		    					new UV(vecs[1].z, depth),
		    					new UV(vecs[2].z, depth),
		    					new UV(vecs[3].z,  0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D),
		    					new UV(vecs[1].z, depth),
		    					new UV(vecs[2].z, depth),
		    					new UV(vecs[3].z,  0.0D)
		    				};
	    				}
	    			case UP:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D),
		    					new UV(vecs[1].z, depth),
		    					new UV(vecs[2].z, depth),
		    					new UV(vecs[3].z,  0.0D)
		    				};
	    				} else {
		    				return new UV[] {
	    						new UV(vecs[0].z, depth).invertV(),
		    					new UV(vecs[1].z,  0.0D).invertV(),
		    					new UV(vecs[2].z,  0.0D).invertV(),
		    					new UV(vecs[3].z, depth).invertV()
		    				};
	    				}
	    			case NORTH:
	    				depth = vecs[3].z - vecs[0].z;
	    				if (floatY) {
		    				return new UV[] {
	            				new UV(depth, 0.0D).invertU(),
	            				new UV(depth, vecs[0].y - vecs[1].y).invertU(),
	            				new UV( 0.0D, vecs[3].y - vecs[2].y).invertU(),
	            				new UV( 0.0D, 0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(depth, vecs[0].y).invertUV(),
		    					new UV(depth, vecs[1].y).invertUV(),
		    					new UV( 0.0D, vecs[2].y).invertUV(),
		    					new UV( 0.0D, vecs[3].y).invertUV()
		    				};
	    				}
	    			default: //case SOUTH:
	    				depth = vecs[3].z - vecs[0].z;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV( 0.0D, 0.0D),
		    					new UV( 0.0D, vecs[0].y - vecs[1].y),
		    					new UV(depth, vecs[3].y - vecs[2].y),
		    					new UV(depth, 0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV( 0.0D, vecs[0].y).invertV(),
		    					new UV( 0.0D, vecs[1].y).invertV(),
		    					new UV(depth, vecs[2].y).invertV(),
		    					new UV(depth, vecs[3].y).invertV()
		    				};
	    				}
			}
    		default: //case EAST:
    			switch (location) {
	    			case DOWN:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D).invertU(),
		    					new UV(vecs[1].z, depth).invertU(),
		    					new UV(vecs[2].z, depth).invertU(),
		    					new UV(vecs[3].z,  0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D).invertU(),
		    					new UV(vecs[1].z, depth).invertU(),
		    					new UV(vecs[2].z, depth).invertU(),
		    					new UV(vecs[3].z,  0.0D).invertU()
		    				};
	    				}
	    			case UP:
	    				depth = vecs[0].y - vecs[1].y;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV(vecs[0].z,  0.0D).invertU(),
		    					new UV(vecs[1].z, depth).invertU(),
		    					new UV(vecs[2].z, depth).invertU(),
		    					new UV(vecs[3].z,  0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
	    						new UV(vecs[0].z, depth).invertUV(),
		    					new UV(vecs[1].z,  0.0D).invertUV(),
		    					new UV(vecs[2].z,  0.0D).invertUV(),
		    					new UV(vecs[3].z, depth).invertUV()
		    				};
	    				}
	    			case NORTH:
	    				depth = vecs[0].z - vecs[3].z;
	    				if (floatY) {
		    				return new UV[] {
		    					new UV( 0.0D, 0.0D),
		    					new UV( 0.0D, vecs[0].y - vecs[1].y),
		    					new UV(depth, vecs[3].y - vecs[2].y),
		    					new UV(depth, 0.0D)
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV( 0.0D, vecs[0].y).invertV(),
		    					new UV( 0.0D, vecs[1].y).invertV(),
		    					new UV(depth, vecs[2].y).invertV(),
		    					new UV(depth, vecs[3].y).invertV()
		    				};
	    				}
	    			default: //case SOUTH:
	    				depth = vecs[0].z - vecs[3].z;
	    				if (floatY) {
		    				return new UV[] {
	            				new UV(depth, 0.0D).invertU(),
	            				new UV(depth, vecs[0].y - vecs[1].y).invertU(),
	            				new UV( 0.0D, vecs[3].y - vecs[2].y).invertU(),
	            				new UV( 0.0D, 0.0D).invertU()
		    				};
	    				} else {
		    				return new UV[] {
		    					new UV(depth, vecs[0].y).invertUV(),
		    					new UV(depth, vecs[1].y).invertUV(),
		    					new UV( 0.0D, vecs[2].y).invertUV(),
		    					new UV( 0.0D, vecs[3].y).invertUV()
		    				};
	    				}
    			}
		}
	}
	
	public static List<Quad> getPerpendicularQuads(Quad quad, double depth) {
		List<Quad> list = new ArrayList<Quad>();
		Vec3d[] vecs = quad.getVecs();
		switch (quad.getSideCoverOffset()) {
			case DOWN:
				// NORTH
				list.add(Quad.getQuad(
						EnumFacing.NORTH,
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(0, -depth, 0),
						vecs[1].addVector(0, -depth, 0),
						vecs[1].addVector(0, 0, 0)));
				// SOUTH
				list.add(Quad.getQuad(
						EnumFacing.SOUTH,
						vecs[0].addVector(0, 0, 0),
						vecs[0].addVector(0, -depth, 0),
						vecs[3].addVector(0, -depth, 0),
						vecs[3].addVector(0, 0, 0)));
				// WEST
				list.add(Quad.getQuad(
						EnumFacing.WEST,
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(0, -depth, 0),
						vecs[0].addVector(0, -depth, 0),
						vecs[0].addVector(0, 0, 0)));
				// EAST
				list.add(Quad.getQuad(
						EnumFacing.EAST,
						vecs[3].addVector(0, 0, 0),
						vecs[3].addVector(0, -depth, 0),
						vecs[2].addVector(0, -depth, 0),
						vecs[2].addVector(0, 0, 0)));
				break;
			case UP:
				// NORTH
				list.add(Quad.getQuad(
						EnumFacing.NORTH,
						vecs[3].addVector(0, depth, 0),
						vecs[3].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, 0),
						vecs[0].addVector(0, depth, 0)));
				// SOUTH
				list.add(Quad.getQuad(
						EnumFacing.SOUTH,
						vecs[1].addVector(0, depth, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(0, depth, 0)));
				// WEST
				list.add(Quad.getQuad(
						EnumFacing.WEST,
						vecs[0].addVector(0, depth, 0),
						vecs[0].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(0, depth, 0)));
				// EAST
				list.add(Quad.getQuad(
						EnumFacing.EAST,
						vecs[2].addVector(0, depth, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[3].addVector(0, 0, 0),
						vecs[3].addVector(0, depth, 0)));
				break;
			case NORTH:
				// DOWN
				list.add(Quad.getQuad(
						EnumFacing.DOWN,
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, -depth),
						vecs[1].addVector(0, 0, -depth),
						vecs[1].addVector(0, 0, 0)));
				// UP
				list.add(Quad.getQuad(
						EnumFacing.UP,
						vecs[3].addVector(0, 0, -depth),
						vecs[3].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, -depth)));
				// WEST
				list.add(Quad.getQuad(
						EnumFacing.WEST,
						vecs[3].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, -depth),
						vecs[3].addVector(0, 0, -depth)));
				// EAST
				list.add(Quad.getQuad(
						EnumFacing.EAST,
						vecs[0].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, -depth),
						vecs[0].addVector(0, 0, -depth)));
				break;
			case SOUTH:
				// DOWN
				list.add(Quad.getQuad(
						EnumFacing.DOWN,
						vecs[1].addVector(0, 0, depth),
						vecs[1].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, depth)));
				// UP
				list.add(Quad.getQuad(
						EnumFacing.UP,
						vecs[0].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, depth),
						vecs[3].addVector(0, 0, depth),
						vecs[3].addVector(0, 0, 0)));
				// WEST
				list.add(Quad.getQuad(
						EnumFacing.WEST,
						vecs[0].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, depth),
						vecs[0].addVector(0, 0, depth)));
				// EAST
				list.add(Quad.getQuad(
						EnumFacing.EAST,
						vecs[3].addVector(0, 0, depth),
						vecs[2].addVector(0, 0, depth),
						vecs[2].addVector(0, 0, 0),
						vecs[3].addVector(0, 0, 0)));
				break;
			case WEST:
				// DOWN
				list.add(Quad.getQuad(
						EnumFacing.DOWN,
						vecs[2].addVector(-depth, 0, 0),
						vecs[1].addVector(-depth, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, 0)));
				// UP
				list.add(Quad.getQuad(
						EnumFacing.UP,
						vecs[0].addVector(-depth, 0, 0),
						vecs[3].addVector(-depth, 0, 0),
						vecs[3].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, 0)));
				// NORTH
				list.add(Quad.getQuad(
						EnumFacing.NORTH,
						vecs[0].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(-depth, 0, 0),
						vecs[0].addVector(-depth, 0, 0)));
				// SOUTH
				list.add(Quad.getQuad(
						EnumFacing.SOUTH,
						vecs[3].addVector(-depth, 0, 0),
						vecs[2].addVector(-depth, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[3].addVector(0, 0, 0)));
				break;
			case EAST: // Top, Down, South render reverse
				// DOWN
				list.add(Quad.getQuad(
						EnumFacing.DOWN,
						vecs[1].addVector(0, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[2].addVector(depth, 0, 0),
						vecs[1].addVector(depth, 0, 0)));
				// UP
				list.add(Quad.getQuad(
						EnumFacing.UP,
						vecs[3].addVector(0, 0, 0),
						vecs[0].addVector(0, 0, 0),
						vecs[0].addVector(depth, 0, 0),
						vecs[3].addVector(depth, 0, 0)));
				// NORTH
				list.add(Quad.getQuad(
						EnumFacing.NORTH,
						vecs[3].addVector(depth, 0, 0),
						vecs[2].addVector(depth, 0, 0),
						vecs[2].addVector(0, 0, 0),
						vecs[3].addVector(0, 0, 0)));
				// SOUTH
				list.add(Quad.getQuad(
						EnumFacing.SOUTH,
						vecs[0].addVector(0, 0, 0),
						vecs[1].addVector(0, 0, 0),
						vecs[1].addVector(depth, 0, 0),
						vecs[0].addVector(depth, 0, 0)));
				break;
		}
		while (list.remove(null));
		return list;
	}
	
}
