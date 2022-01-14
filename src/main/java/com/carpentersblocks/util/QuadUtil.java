package com.carpentersblocks.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;

import com.carpentersblocks.ModLogger;
import com.carpentersblocks.client.renderer.Quad;
import com.carpentersblocks.client.renderer.RenderPkg;
import com.carpentersblocks.client.renderer.UV;
import com.carpentersblocks.nbt.attribute.EnumAttributeLocation;
import com.carpentersblocks.util.handler.OverlayHandler;

import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.vector.Vector3d;

public class QuadUtil {
	
	/**
	 * Reduces vec3d down to point2d representation.
	 * <p>
	 * Used to calculate distances between quad boundaries and
	 * the corners of quad.
	 * 
	 * @param facing the quad facing
	 * @param vec3d the vec3d to convert
	 * @return point representation of vec3d based on facing
	 */
	public static Point2d vec3dToPoint2d(Direction facing, Vector3d vec3d) {
		switch (facing.getAxis()) {
			case X:
				return new Point2d(vec3d.z, vec3d.y);
			case Y:
				return new Point2d(vec3d.x, vec3d.z);
			default: // case Z:
				return new Point2d(vec3d.x, vec3d.y);
		}
	}
	
	/**
	 * Gathers minimum and maximum boundaries generated using input vec3ds
	 * and (0,0) -> (1,1) block dimensions.
	 * 
	 * @param facing the quad facing
	 * @param vec3ds the vec3ds
	 * @return an array of points defining boundary
	 */
	private static Point2d[] getBoundingPlane(Direction facing, Set<Point2d> points) {
		double minU = Double.MAX_VALUE;
		double maxU = Double.MIN_VALUE;
		double minV = Double.MAX_VALUE;
		double maxV = Double.MIN_VALUE;
		for (Point2d pt : points) {
			minU = Math.min(minU, pt.getU());
			maxU = Math.max(maxU, pt.getU());
			minV = Math.min(minV, pt.getV());
			maxV = Math.max(maxV, pt.getV());
		}
		switch (facing) {
			case DOWN:
				return new Point2d[] { new Point2d(minU, maxV), new Point2d(minU, minV), new Point2d(maxU, minV), new Point2d(maxU, maxV) };
			case UP:
				return new Point2d[] { new Point2d(minU, minV), new Point2d(minU, maxV), new Point2d(maxU, maxV), new Point2d(maxU, minV) };
			case NORTH:
				return new Point2d[] { new Point2d(maxU, maxV), new Point2d(maxU, minV), new Point2d(minU, minV), new Point2d(minU, maxV) };
			case SOUTH:
				return new Point2d[] { new Point2d(minU, maxV), new Point2d(minU, minV), new Point2d(maxU, minV), new Point2d(maxU, maxV) };
			case WEST:
				return new Point2d[] { new Point2d(minU, maxV), new Point2d(minU, minV), new Point2d(maxU, minV), new Point2d(maxU, maxV) };
			default: // case EAST:
				return new Point2d[] { new Point2d(maxU, maxV), new Point2d(maxU, minV), new Point2d(minU, minV), new Point2d(minU, maxV) };
		}
	}
	
	/**
	 * Check for malformed quad vertices.
	 * 
	 * @param vec3ds the quad points
	 * @return <code>true</code> if quad is valid<br><code>false</code> if quad is invalid
	 */
	public static boolean isValid(Vector3d[] vec3ds) {
		if (vec3ds == null || vec3ds.length != 4) {
			return false;
		}
		Set<Vector3d> set = new HashSet<Vector3d>(Arrays.asList(vec3ds));
		return set.size() >= 3; // Need 3 unique to calculate normal
	}
	
	/**
	 * Used for traditional quads and triangles where corners are uniquely identifiable.
	 * <p>
	 * These types of sorted quads are required for supporting side covers.
	 * 
	 * @param facing quad facing
	 * @param bounds quad point boundary
	 * @param vec3ds list of vec3ds
	 * @return array of mappable corners
	 */
	private static Vector3d[] getCornersUsingUniqueness(Direction facing, Point2d[] bounds, List<Vector3d> vec3ds) {
		
		Vector3d[] corners = new Vector3d[4];
		
		if (vec3ds.size() == 4) {
			// standard quad
			Vector3dDirectionSortComparator uComparator = new Vector3dDirectionSortComparator(facing, true);
			Vector3dDirectionSortComparator vComparator = new Vector3dDirectionSortComparator(facing, false);
			
			List<Vector3d> uSorted = vec3ds
					.stream()
					.sorted(uComparator)
					.collect(Collectors.toList());
			
			// set left corners
			if (vComparator.compare(uSorted.get(0),  uSorted.get(1)) > 0) {
				corners[0] = uSorted.get(0);
				corners[1] = uSorted.get(1);
			} else {
				corners[0] = uSorted.get(1);
				corners[1] = uSorted.get(0);
			}
			
			// set right corners
			if (vComparator.compare(uSorted.get(2),  uSorted.get(3)) > 0) {
				corners[3] = uSorted.get(2);
				corners[2] = uSorted.get(3);
			} else {
				corners[3] = uSorted.get(3);
				corners[2] = uSorted.get(2);
			}
			
			return corners;
		}
		
		// legacy sorting as fallback
		for (int i = 0; i < bounds.length; ++i) {
			
			// Gather vector distances to corner
			double[] proximity = new double[vec3ds.size()];
			for (int j = 0; j < vec3ds.size(); ++j) {
				Point2d pt = vec3dToPoint2d(facing, vec3ds.get(j));
				proximity[j] = Math.ceil(pt.distance(bounds[i]) * 10000) / 10000;
			}
			
			// Ensure closest corner is unique
			SortedMap<Double,List<Vector3d>> map = new TreeMap<Double,List<Vector3d>>();
			for (int j = 0; j < proximity.length; ++j) {
				if (!map.containsKey(proximity[j])) {
					map.put(proximity[j], new ArrayList<Vector3d>());
				}
				map.get(proximity[j]).add(vec3ds.get(j));
			}
			
			// Corner is unique; get corner assignment
			if (map.get(map.firstKey()).size() == 1) {
				// Add closest corner
				for (int j = 0; j < proximity.length; ++j) {
					if (proximity[j] == map.firstKey()) {
						corners[i] = map.get(map.firstKey()).get(0);
						break;
					}
				}
			}
		}
		
		// Resolve missing corners
		List<Vector3d> unassigned = new ArrayList<Vector3d>(vec3ds);
		unassigned.removeAll(Arrays.asList(corners));
		if (!unassigned.isEmpty()) {
			for (int i = 0; i < corners.length; ++i) {
				if (corners[i] == null) {
					corners[i] = unassigned.get(0);
				}
			}
		}
		
		return corners;
	}
	
	private static class Vector3dDirectionSortComparator implements Comparator<Vector3d> {
		
		private Direction direction;
		private boolean compareU;
		
		public Vector3dDirectionSortComparator(Direction direction, boolean compareU) {
			this.direction = direction;
			this.compareU = compareU;
		}

		@Override
		public int compare(Vector3d v1, Vector3d v2) {
			Point2d p1 = QuadUtil.vec3dToPoint2d(direction, v1);
			Point2d p2 = QuadUtil.vec3dToPoint2d(direction, v2);
			if (compareU) {
				if (Direction.NORTH.equals(direction) || Direction.EAST.equals(direction)) {
					return QuadUtil.compare(1.0D - p1.getU(), 1.0D - p2.getU());
				} else {
					return QuadUtil.compare(p1.getU(), p2.getU());
				}
			} else {
				if (Direction.UP.equals(direction)) {
					return QuadUtil.compare(1.0D - p1.getV(), 1.0D - p2.getV());
				} else {
					return QuadUtil.compare(p1.getV(), p2.getV());
				}
			}
		}
		
	}
	
	/**
	 * Handles vec3d sorting for a majority of rendering tasks.
	 * <p>
	 * This ensures vec3ds are in proper order for the given direction,
	 * and can be used as a source for generating side covers
	 * for perpendicular faces.
	 * 
	 * @param direction quad direction
	 * @param inVecs source vec3ds
	 * @return a sorted array of vec3ds
	 */
	public static Vector3d[] sortVector3dsByDirection(Direction direction, Vector3d[] inVecs) {
		Set<Vector3d> inVecsNew = new HashSet<>(Arrays.asList(inVecs));
		Vector3d[] cornerList = null;
		Set<Point2d> set = new HashSet<Point2d>();
		for (Vector3d vec3d : inVecsNew) {
			set.add(vec3dToPoint2d(direction, vec3d));
		}
		if (set.size() > 2) {
			Point2d[] bounds = getBoundingPlane(direction, set);
			cornerList = getCornersUsingUniqueness(direction, bounds, new ArrayList<>(inVecsNew));
			int cornerCount = 0;
			for (int i = 0; i < cornerList.length; ++i) {
				if (cornerList[i] != null) {
					++cornerCount;
				}
			}
			if (cornerCount >= 3) {
				if (cornerList[0] == null) {
					cornerList[0] = cornerList[1];
				} else if (cornerList[1] == null) {
					cornerList[1] = cornerList[0];
				} else if (cornerList[2] == null) {
					cornerList[2] = cornerList[3];
				} else if (cornerList[3] == null) {
					cornerList[3] = cornerList[2];
				}
			}
		}
		return cornerList;
	}
	
	/**
	 * Gets UV mapping for oblique sloped faces.
	 * <p>
	 * TODO: requires work to match up textures
	 * 
	 * @param quad the quad
	 * @param isFloatingOverlay whether texture has floating properties, like grass side
	 * @param location the location of resource on block (e.g. side cover, host)
	 * @return array of UV coordinates
	 */
	public static UV[] getUVObliqueSlope(Quad quad, boolean isFloatingOverlay, EnumAttributeLocation location) {
		Vector3d TL = quad.getVec3ds()[0];
		Vector3d BL = quad.getVec3ds()[1];
		Vector3d BR = quad.getVec3ds()[2];
		Vector3d TR = quad.getVec3ds()[3];
		
		// Set midpoint corner
		boolean[] midPt = new boolean[4];
		if (compare(TL, BL) == 0) {
			if (compare(TL.y, TR.y) < 0) {
				midPt[2] = true;
			} else if (compare(TL.y, BR.y) > 0) {
				midPt[2] = true;
			}
		} else { // (compare(BR, TR) == 0) {
			if (compare(TL.y, TR.y) > 0) {
				midPt[0] = true;
			} else if (compare(BL.y, BR.y) < 0) {
				midPt[1] = true;
			}
		}
		
		// If side location, translate on y-axis back to [0,1] bounds
		if (!EnumAttributeLocation.HOST.equals(location)) {
			quad = new Quad(quad);
			double translation;
			if (Direction.DOWN.equals(getYSlope(quad))) {
				translation = -1 * Math.min(BL.y, BR.y);
			} else {
				translation = -1 * (Math.max(TL.y, TR.y) - 1.0D);
			}
			if (Double.compare(0.0D, translation) != 0) {
				TL = TL.add(0, translation, 0);
				BL = BL.add(0, translation, 0);
				BR = BR.add(0, translation, 0);
				TR = TR.add(0, translation, 0);
			}
		}
		
		switch (QuadUtil.getCardinalFacing(quad)) {
    		case NORTH:
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
	
	/**
	 * Gets whether quad is sloped.
	 * 
	 * @param axis the axis to check for sloping
	 * @return <code>true</code> if sloped on axis
	 */
	public static boolean isSloped(Quad quad, Axis axis) {
		double axis1 = Axis.X.equals(axis) ? quad.getNormal().x() : Axis.Y.equals(axis) ? quad.getNormal().y() : quad.getNormal().z();
		return QuadUtil.compare(axis1, -1.0D) > 0 &&
				QuadUtil.compare(axis1, 0.0D) != 0 &&
				QuadUtil.compare(axis1, 1.0D) < 0;
	}
	
	/**
	 * Gets Y-direction for quad, if direction is compatible
	 * with quad points -- otherwise returns null.
	 * 
	 * @param quad a quad
	 * @return a direction or null
	 */
	public static Direction getYSlope(Quad quad) {
		if (isSloped(quad, Axis.Y)) {
			if (compare(quad.getNormal().y(), 0.0D) > 0) {
				return Direction.UP;
			} else {
				return Direction.DOWN;
			}
		}
		return null;
	}
	
	/**
	 * Gets UV mapping for ordinary (non-triangle shaped) quads.
	 * 
	 * @param quad the quad
	 * @param isFloatingOverlay whether texture has floating properties, like grass side
	 * @return array of UV coordinates
	 */
	public static UV[] getUV(Quad quad) {
		Vector3d TL = quad.getVec3ds()[0];
		Vector3d BL = quad.getVec3ds()[1];
		Vector3d BR = quad.getVec3ds()[2];
		Vector3d TR = quad.getVec3ds()[3];
		
		boolean isFloating = DefaultVertexFormats.BLOCK.equals(RenderPkg.get().getVertexFormat())
				&& OverlayHandler.isFloatingSprite(quad.getTextureAtlasSprite());
		
		switch (quad.getDirection()) {
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
			if (isFloating) {
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
			if (isFloating) {
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
			if (isFloating) {
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
			if (isFloating) {
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
		Vector3d[] vecs = quad.getVec3ds();
		switch (getSideCoverRenderDirection(quad)) {
			case DOWN:
				list.add(Quad.getUnsortedQuad(
					Direction.NORTH,
					vecs[2].add(0, 0, 0),
					vecs[2].add(0, -depth, 0),
					vecs[1].add(0, -depth, 0),
					vecs[1].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.SOUTH,
					vecs[0].add(0, 0, 0),
					vecs[0].add(0, -depth, 0),
					vecs[3].add(0, -depth, 0),
					vecs[3].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.WEST,
					vecs[1].add(0, 0, 0),
					vecs[1].add(0, -depth, 0),
					vecs[0].add(0, -depth, 0),
					vecs[0].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.EAST,
					vecs[3].add(0, 0, 0),
					vecs[3].add(0, -depth, 0),
					vecs[2].add(0, -depth, 0),
					vecs[2].add(0, 0, 0)));
				preparePerdendicularQuads(list, Direction.DOWN);
				break;
			case UP:
				list.add(Quad.getUnsortedQuad(
					Direction.NORTH,
					vecs[3].add(0, depth, 0),
					vecs[3].add(0, 0, 0),
					vecs[0].add(0, 0, 0),
					vecs[0].add(0, depth, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.SOUTH,
					vecs[1].add(0, depth, 0),
					vecs[1].add(0, 0, 0),
					vecs[2].add(0, 0, 0),
					vecs[2].add(0, depth, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.WEST,
					vecs[0].add(0, depth, 0),
					vecs[0].add(0, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[1].add(0, depth, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.EAST,
					vecs[2].add(0, depth, 0),
					vecs[2].add(0, 0, 0),
					vecs[3].add(0, 0, 0),
					vecs[3].add(0, depth, 0)));
				preparePerdendicularQuads(list, Direction.UP);
				break;
			case NORTH:
				list.add(Quad.getUnsortedQuad(
					Direction.DOWN,
					vecs[2].add(0, 0, 0),
					vecs[2].add(0, 0, -depth),
					vecs[1].add(0, 0, -depth),
					vecs[1].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.UP,
					vecs[3].add(0, 0, -depth),
					vecs[3].add(0, 0, 0),
					vecs[0].add(0, 0, 0),
					vecs[0].add(0, 0, -depth)));
				list.add(Quad.getUnsortedQuad(
					Direction.WEST,
					vecs[3].add(0, 0, -depth),
					vecs[2].add(0, 0, -depth),
					vecs[2].add(0, 0, 0),
					vecs[3].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.EAST,
					vecs[0].add(0, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[1].add(0, 0, -depth),
					vecs[0].add(0, 0, -depth)));
				preparePerdendicularQuads(list, Direction.NORTH);
				break;
			case SOUTH:
				list.add(Quad.getUnsortedQuad(
					Direction.DOWN,
					vecs[1].add(0, 0, depth),
					vecs[1].add(0, 0, 0),
					vecs[2].add(0, 0, 0),
					vecs[2].add(0, 0, depth)));
				list.add(Quad.getUnsortedQuad(
					Direction.UP,
					vecs[0].add(0, 0, 0),
					vecs[0].add(0, 0, depth),
					vecs[3].add(0, 0, depth),
					vecs[3].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.WEST,
					vecs[0].add(0, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[1].add(0, 0, depth),
					vecs[0].add(0, 0, depth)));
				list.add(Quad.getUnsortedQuad(
					Direction.EAST,
					vecs[3].add(0, 0, depth),
					vecs[2].add(0, 0, depth),
					vecs[2].add(0, 0, 0),
					vecs[3].add(0, 0, 0)));
				preparePerdendicularQuads(list, Direction.SOUTH);
				break;
			case WEST:
				list.add(Quad.getUnsortedQuad(
					Direction.DOWN,
					vecs[2].add(-depth, 0, 0),
					vecs[1].add(-depth, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[2].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.UP,
					vecs[0].add(-depth, 0, 0),
					vecs[3].add(-depth, 0, 0),
					vecs[3].add(0, 0, 0),
					vecs[0].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.NORTH,
					vecs[0].add(0, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[1].add(-depth, 0, 0),
					vecs[0].add(-depth, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.SOUTH,
					vecs[3].add(-depth, 0, 0),
					vecs[2].add(-depth, 0, 0),
					vecs[2].add(0, 0, 0),
					vecs[3].add(0, 0, 0)));
				preparePerdendicularQuads(list, Direction.WEST);
				break;
			case EAST:
				list.add(Quad.getUnsortedQuad(
					Direction.DOWN,
					vecs[1].add(0, 0, 0),
					vecs[2].add(0, 0, 0),
					vecs[2].add(depth, 0, 0),
					vecs[1].add(depth, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.UP,
					vecs[3].add(0, 0, 0),
					vecs[0].add(0, 0, 0),
					vecs[0].add(depth, 0, 0),
					vecs[3].add(depth, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.NORTH,
					vecs[3].add(depth, 0, 0),
					vecs[2].add(depth, 0, 0),
					vecs[2].add(0, 0, 0),
					vecs[3].add(0, 0, 0)));
				list.add(Quad.getUnsortedQuad(
					Direction.SOUTH,
					vecs[0].add(0, 0, 0),
					vecs[1].add(0, 0, 0),
					vecs[1].add(depth, 0, 0),
					vecs[0].add(depth, 0, 0)));
				preparePerdendicularQuads(list, Direction.EAST);
				break;
		}
		return list;
	}
	
	/**
	 * Cleans up any null quads and sets UV float orientation
	 * for perpendicular quads.
	 * 
	 * @param list a list of quads
	 * @param facing the quad facing
	 */
	private static void preparePerdendicularQuads(List<Quad> list, Direction facing) {
		Iterator<Quad> iter = list.iterator();
		while (iter.hasNext()) {
			Quad quad = iter.next();
			if (quad == null) {
				iter.remove();
				continue;
			}
			quad.setUVFloat(facing);
		}
	}
	
	/**
	 * Gets alternate facing for side cover sloped face.
	 * <p>
	 * Side covers on sloped faces will protrude either UP or DOWN,
	 * and never to NSWE directions. This method returns whether
	 * the slope should be UP or DOWN facing for Y-sloping quads.
	 * 
	 * @return the alternate facing
	 */
	public static Direction getSideCoverRenderDirection(Quad quad) {
		if (isSloped(quad, Axis.Y)) {
			if (Double.compare(quad.getNormal().y, 0.0D) > 0) {
				return Direction.UP;
			} else {
				return Direction.DOWN;
			}
		} else {
			return quad.getDirection();
		}
	}
	
	/**
	 * Return quad facing as NWSE component.
	 * 
	 * @param quad the quad
	 * @return the quad facing
	 */
	public static Direction getCardinalFacing(Quad quad) {
		if (!isSloped(quad, Axis.Y)) {
			ModLogger.log(Level.WARN, "getCardinalFacing called for non-sloping Y-axis");
		}
		return Direction.getNearest(quad.getNormal().x, 0, quad.getNormal().z);
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
	
	/**
	 * Compares {@link Vector3d} values using epsilon of 1/16, since
	 * Minecraft blocks are 16 pixels across.
	 * 
	 * @param v1 first vector
	 * @param v2 second vector
	 * @return 	<ul>
	 * 				<li>-1 if first value is less than second value</li>
	 * 				<li>0 if both values are equal</li>
	 * 				<li>1 if first value is greater than second value</li>
	 * 			</ul>
	 */
	public static int compare(Vector3d v1, Vector3d v2) {		
		return compare(v1.x, v2.x) + compare(v1.y, v2.y) + compare(v1.z, v2.z);
	}
	
	/**
	 * Gets normal for vec3d array.
	 * 
	 * @param vecs the quad vec3ds
	 * @return the normal {@link Vector3d}
	 */
	public static Vector3d getNormal(final Vector3d[] vecs) {
		Vector3d[] noDuplicateVec3ds = new LinkedHashSet<Vector3d>(Arrays.asList(vecs)).toArray(new Vector3d[vecs.length]);
		return (noDuplicateVec3ds[1].subtract(noDuplicateVec3ds[0]))
				.cross(noDuplicateVec3ds[2].subtract(noDuplicateVec3ds[1]))
				.normalize();
	}
	
}