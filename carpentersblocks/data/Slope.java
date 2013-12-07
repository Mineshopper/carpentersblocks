package carpentersblocks.data;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.EAST;
import static net.minecraftforge.common.ForgeDirection.NORTH;
import static net.minecraftforge.common.ForgeDirection.SOUTH;
import static net.minecraftforge.common.ForgeDirection.UP;
import static net.minecraftforge.common.ForgeDirection.WEST;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;

public class Slope {

	/**
	 * 16-bit data components:
	 *
	 *	[0000000000000000]
	 *  slopeID
	 */

	/*
	 * Slope IDs.
	 * 
	 * IDs 0 - 11 are set arbitrarily when
	 * block is placed in world.
	 * 
	 * IDs 12 - 45 are mixed to retain compatibility
	 * with older versions.
	 */

	public final static byte ID_WEDGE_SE = 0;
	public final static byte ID_WEDGE_NW = 1;
	public final static byte ID_WEDGE_NE = 2;
	public final static byte ID_WEDGE_SW = 3;
	public final static byte ID_WEDGE_NEG_N = 4;
	public final static byte ID_WEDGE_NEG_S = 5;
	public final static byte ID_WEDGE_NEG_W = 6;
	public final static byte ID_WEDGE_NEG_E = 7;
	public final static byte ID_WEDGE_POS_N = 8;
	public final static byte ID_WEDGE_POS_S = 9;
	public final static byte ID_WEDGE_POS_W = 10;
	public final static byte ID_WEDGE_POS_E = 11;
	public final static byte ID_WEDGE_INT_NEG_SE = 19;
	public final static byte ID_WEDGE_INT_NEG_NW = 15;
	public final static byte ID_WEDGE_INT_NEG_NE = 13;
	public final static byte ID_WEDGE_INT_NEG_SW = 17;
	public final static byte ID_WEDGE_INT_POS_SE = 18;
	public final static byte ID_WEDGE_INT_POS_NW = 14;
	public final static byte ID_WEDGE_INT_POS_NE = 12;
	public final static byte ID_WEDGE_INT_POS_SW = 16;
	public final static byte ID_WEDGE_EXT_NEG_SE = 23;
	public final static byte ID_WEDGE_EXT_NEG_NW = 27;
	public final static byte ID_WEDGE_EXT_NEG_NE = 25;
	public final static byte ID_WEDGE_EXT_NEG_SW = 21;
	public final static byte ID_WEDGE_EXT_POS_SE = 22;
	public final static byte ID_WEDGE_EXT_POS_NW = 26;
	public final static byte ID_WEDGE_EXT_POS_NE = 24;
	public final static byte ID_WEDGE_EXT_POS_SW = 20;
	public final static byte ID_OBL_INT_NEG_SE = 35;
	public final static byte ID_OBL_INT_NEG_NW = 31;
	public final static byte ID_OBL_INT_NEG_NE = 29;
	public final static byte ID_OBL_INT_NEG_SW = 33;
	public final static byte ID_OBL_INT_POS_SE = 34;
	public final static byte ID_OBL_INT_POS_NW = 30;
	public final static byte ID_OBL_INT_POS_NE = 28;
	public final static byte ID_OBL_INT_POS_SW = 32;
	public final static byte ID_OBL_EXT_NEG_SE = 39;
	public final static byte ID_OBL_EXT_NEG_NW = 43;
	public final static byte ID_OBL_EXT_NEG_NE = 41;
	public final static byte ID_OBL_EXT_NEG_SW = 37;
	public final static byte ID_OBL_EXT_POS_SE = 38;
	public final static byte ID_OBL_EXT_POS_NW = 42;
	public final static byte ID_OBL_EXT_POS_NE = 40;
	public final static byte ID_OBL_EXT_POS_SW = 36;
	public final static byte ID_PYR_HALF_NEG = 45;
	public final static byte ID_PYR_HALF_POS = 44;

	public enum SlopeType
	{
		WEDGE_XZ,
		WEDGE_Y,
		WEDGE_INT,
		WEDGE_EXT,
		OBLIQUE_INT,
		OBLIQUE_EXT,
		PYRAMID
	}

	/** Array containing registered slopes. */
	public static final Slope[] slopesList = new Slope[46];

	/** ID of the slope. */
	public final int slopeID;

	/** Slope type. */
	public final SlopeType slopeType;

	/**
	 * Holds slope face shape.
	 * 
	 * [DOWN, UP, NORTH, SOUTH, WEST, EAST]
	 */
	private final int[] faceShape;

	/**
	 * If faceShape indicates side is WEDGE,
	 * this will indicate which corner of face
	 * is the "corner."
	 * 
	 * This is used for side render checks.
	 * 
	 * Stores MIN/MAX of wedge corner.
	 * Stored as [X/Y], [Z/Y], or [X/Z].
	 * 
	 * [DOWN, UP, NORTH, SOUTH, WEST, EAST]
	 */
	private final int[] wedgeCorner;

	/**
	 * Is the slope facing up.
	 * A quick reference since it's called often.
	 */
	public final boolean isPositive;

	/** Slope directions. */
	public final List<ForgeDirection> facings;

	public static final byte NO_FACE = 0;
	public static final byte FULL = 1;
	public static final byte WEDGE = 2;

	public static final byte MIN_MIN = 1;
	public static final byte MIN_MAX = 2;
	public static final byte MAX_MIN = 3;
	public static final byte MAX_MAX = 4;

	public Slope(int slopeID, SlopeType slopeType, ForgeDirection[] facings, int[] faceShape, int[] wedgeCorner)
	{
		this.slopeID = slopeID;
		slopesList[slopeID] = this;
		this.slopeType = slopeType;
		this.faceShape = faceShape;
		this.wedgeCorner = wedgeCorner;

		this.facings = new ArrayList<ForgeDirection>();

		for (ForgeDirection face : facings) {
			this.facings.add(face);
		}

		isPositive = this.facings.contains(UP);
	}

	public static final Slope WEDGE_NW = new Slope(ID_WEDGE_NW, SlopeType.WEDGE_XZ, new ForgeDirection[] { NORTH, WEST }, new int[] { WEDGE, WEDGE, NO_FACE, FULL, NO_FACE, FULL }, new int[] { MAX_MAX, MAX_MAX, 0, 0, 0, 0 });
	public static final Slope WEDGE_NE = new Slope(ID_WEDGE_NE, SlopeType.WEDGE_XZ, new ForgeDirection[] { NORTH, EAST }, new int[] { WEDGE, WEDGE, NO_FACE, FULL, FULL, NO_FACE }, new int[] { MIN_MAX, MIN_MAX, 0, 0, 0, 0 });
	public static final Slope WEDGE_SW = new Slope(ID_WEDGE_SW, SlopeType.WEDGE_XZ, new ForgeDirection[] { SOUTH, WEST }, new int[] { WEDGE, WEDGE, FULL, NO_FACE, NO_FACE, FULL }, new int[] { MAX_MIN, MAX_MIN, 0, 0, 0, 0 });
	public static final Slope WEDGE_SE = new Slope(ID_WEDGE_SE, SlopeType.WEDGE_XZ, new ForgeDirection[] { SOUTH, EAST }, new int[] { WEDGE, WEDGE, FULL, NO_FACE, FULL, NO_FACE }, new int[] { MIN_MIN, MIN_MIN, 0, 0, 0, 0 });
	public static final Slope WEDGE_NEG_N = new Slope(ID_WEDGE_NEG_N, SlopeType.WEDGE_Y, new ForgeDirection[] { DOWN, NORTH }, new int[] { NO_FACE, FULL, NO_FACE, FULL, WEDGE, WEDGE }, new int[] { 0, 0, 0, 0, MAX_MAX, MAX_MAX });
	public static final Slope WEDGE_NEG_S = new Slope(ID_WEDGE_NEG_S, SlopeType.WEDGE_Y, new ForgeDirection[] { DOWN, SOUTH }, new int[] { NO_FACE, FULL, FULL, NO_FACE, WEDGE, WEDGE }, new int[] { 0, 0, 0, 0, MIN_MAX, MIN_MAX });
	public static final Slope WEDGE_NEG_W = new Slope(ID_WEDGE_NEG_W, SlopeType.WEDGE_Y, new ForgeDirection[] { DOWN, WEST }, new int[] { NO_FACE, FULL, WEDGE, WEDGE, NO_FACE, FULL }, new int[] { 0, 0, MAX_MAX, MAX_MAX, 0, 0 });
	public static final Slope WEDGE_NEG_E = new Slope(ID_WEDGE_NEG_E, SlopeType.WEDGE_Y, new ForgeDirection[] { DOWN, EAST }, new int[] { NO_FACE, FULL, WEDGE, WEDGE, FULL, NO_FACE }, new int[] { 0, 0, MIN_MAX, MIN_MAX, 0, 0 });
	public static final Slope WEDGE_POS_N = new Slope(ID_WEDGE_POS_N, SlopeType.WEDGE_Y, new ForgeDirection[] { UP, NORTH }, new int[] { FULL, NO_FACE, NO_FACE, FULL, WEDGE, WEDGE }, new int[] { 0, 0, 0, 0, MAX_MIN, MAX_MIN });
	public static final Slope WEDGE_POS_S = new Slope(ID_WEDGE_POS_S, SlopeType.WEDGE_Y, new ForgeDirection[] { UP, SOUTH }, new int[] { FULL, NO_FACE, FULL, NO_FACE, WEDGE, WEDGE }, new int[] { 0, 0, 0, 0, MIN_MIN, MIN_MIN });
	public static final Slope WEDGE_POS_W = new Slope(ID_WEDGE_POS_W, SlopeType.WEDGE_Y, new ForgeDirection[] { UP, WEST }, new int[] { FULL, NO_FACE, WEDGE, WEDGE, NO_FACE, FULL }, new int[] { 0, 0, MAX_MIN, MAX_MIN, 0, 0 });
	public static final Slope WEDGE_POS_E = new Slope(ID_WEDGE_POS_E, SlopeType.WEDGE_Y, new ForgeDirection[] { UP, EAST }, new int[] { FULL, NO_FACE, WEDGE, WEDGE, FULL, NO_FACE }, new int[] { 0, 0, MIN_MIN, MIN_MIN, 0, 0 });
	public static final Slope WEDGE_INT_NEG_NW = new Slope(ID_WEDGE_INT_NEG_NW, SlopeType.WEDGE_INT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { NO_FACE, FULL, WEDGE, FULL, WEDGE, FULL }, new int[] { 0, 0, MAX_MAX, 0, MAX_MAX, 0 });
	public static final Slope WEDGE_INT_NEG_NE = new Slope(ID_WEDGE_INT_NEG_NE, SlopeType.WEDGE_INT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { NO_FACE, FULL, WEDGE, FULL, FULL, WEDGE }, new int[] { 0, 0, MIN_MAX, 0, 0, MAX_MAX });
	public static final Slope WEDGE_INT_NEG_SW = new Slope(ID_WEDGE_INT_NEG_SW, SlopeType.WEDGE_INT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { NO_FACE, FULL, FULL, WEDGE, WEDGE, FULL }, new int[] { 0, 0, 0, MAX_MAX, MIN_MAX, 0 });
	public static final Slope WEDGE_INT_NEG_SE = new Slope(ID_WEDGE_INT_NEG_SE, SlopeType.WEDGE_INT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { NO_FACE, FULL, FULL, WEDGE, FULL, WEDGE }, new int[] { 0, 0, 0, MIN_MAX, 0, MIN_MAX });
	public static final Slope WEDGE_INT_POS_NW = new Slope(ID_WEDGE_INT_POS_NW, SlopeType.WEDGE_INT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { FULL, NO_FACE, WEDGE, FULL, WEDGE, FULL }, new int[] { 0, 0, MAX_MIN, 0, MAX_MIN, 0 });
	public static final Slope WEDGE_INT_POS_NE = new Slope(ID_WEDGE_INT_POS_NE, SlopeType.WEDGE_INT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { FULL, NO_FACE, WEDGE, FULL, FULL, WEDGE }, new int[] { 0, 0, MIN_MIN, 0, 0, MAX_MIN });
	public static final Slope WEDGE_INT_POS_SW = new Slope(ID_WEDGE_INT_POS_SW, SlopeType.WEDGE_INT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { FULL, NO_FACE, FULL, WEDGE, WEDGE, FULL }, new int[] { 0, 0, 0, MAX_MIN, MIN_MIN, 0 });
	public static final Slope WEDGE_INT_POS_SE = new Slope(ID_WEDGE_INT_POS_SE, SlopeType.WEDGE_INT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { FULL, NO_FACE, FULL, WEDGE, FULL, WEDGE }, new int[] { 0, 0, 0, MIN_MIN, 0, MIN_MIN });
	public static final Slope WEDGE_EXT_NEG_NW = new Slope(ID_WEDGE_EXT_NEG_NW, SlopeType.WEDGE_EXT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { NO_FACE, FULL, NO_FACE, WEDGE, NO_FACE, WEDGE }, new int[] { 0, 0, 0, MAX_MAX, 0, MAX_MAX });
	public static final Slope WEDGE_EXT_NEG_NE = new Slope(ID_WEDGE_EXT_NEG_NE, SlopeType.WEDGE_EXT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { NO_FACE, FULL, NO_FACE, WEDGE, WEDGE, NO_FACE }, new int[] { 0, 0, 0, MIN_MAX, MAX_MAX, 0 });
	public static final Slope WEDGE_EXT_NEG_SW = new Slope(ID_WEDGE_EXT_NEG_SW, SlopeType.WEDGE_EXT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { NO_FACE, FULL, WEDGE, NO_FACE, NO_FACE, WEDGE }, new int[] { 0, 0, MAX_MAX, 0, 0, MIN_MAX });
	public static final Slope WEDGE_EXT_NEG_SE = new Slope(ID_WEDGE_EXT_NEG_SE, SlopeType.WEDGE_EXT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { NO_FACE, FULL, WEDGE, NO_FACE, WEDGE, NO_FACE }, new int[] { 0, 0, MIN_MAX, 0, MIN_MAX, 0 });
	public static final Slope WEDGE_EXT_POS_NW = new Slope(ID_WEDGE_EXT_POS_NW, SlopeType.WEDGE_EXT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { FULL, NO_FACE, NO_FACE, WEDGE, NO_FACE, WEDGE }, new int[] { 0, 0, 0, MAX_MIN, 0, MAX_MIN });
	public static final Slope WEDGE_EXT_POS_NE = new Slope(ID_WEDGE_EXT_POS_NE, SlopeType.WEDGE_EXT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { FULL, NO_FACE, NO_FACE, WEDGE, WEDGE, NO_FACE }, new int[] { 0, 0, 0, MIN_MIN, MAX_MIN, 0 });
	public static final Slope WEDGE_EXT_POS_SW = new Slope(ID_WEDGE_EXT_POS_SW, SlopeType.WEDGE_EXT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { FULL, NO_FACE, WEDGE, NO_FACE, NO_FACE, WEDGE }, new int[] { 0, 0, MAX_MIN, 0, 0, MIN_MIN });
	public static final Slope WEDGE_EXT_POS_SE = new Slope(ID_WEDGE_EXT_POS_SE, SlopeType.WEDGE_EXT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { FULL, NO_FACE, WEDGE, NO_FACE, WEDGE, NO_FACE }, new int[] { 0, 0, MIN_MIN, 0, MIN_MIN, 0 });
	public static final Slope OBL_INT_NEG_NW = new Slope(ID_OBL_INT_NEG_NW, SlopeType.OBLIQUE_INT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { WEDGE, FULL, WEDGE, FULL, WEDGE, FULL }, new int[] { MAX_MAX, 0, MAX_MAX, 0, MAX_MAX, 0 });
	public static final Slope OBL_INT_NEG_NE = new Slope(ID_OBL_INT_NEG_NE, SlopeType.OBLIQUE_INT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { WEDGE, FULL, WEDGE, FULL, FULL, WEDGE }, new int[] { MIN_MAX, 0, MIN_MAX, 0, 0, MAX_MAX });
	public static final Slope OBL_INT_NEG_SW = new Slope(ID_OBL_INT_NEG_SW, SlopeType.OBLIQUE_INT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { WEDGE, FULL, FULL, WEDGE, WEDGE, FULL }, new int[] { MAX_MIN, 0, 0, MAX_MAX, MIN_MAX, 0 });
	public static final Slope OBL_INT_NEG_SE = new Slope(ID_OBL_INT_NEG_SE, SlopeType.OBLIQUE_INT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { WEDGE, FULL, FULL, WEDGE, FULL, WEDGE }, new int[] { MIN_MIN, 0, 0, MIN_MAX, 0, MIN_MAX });
	public static final Slope OBL_INT_POS_NW = new Slope(ID_OBL_INT_POS_NW, SlopeType.OBLIQUE_INT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { FULL, WEDGE, WEDGE, FULL, WEDGE, FULL }, new int[] { 0, MAX_MAX, MAX_MIN, 0, MAX_MIN, 0 });
	public static final Slope OBL_INT_POS_NE = new Slope(ID_OBL_INT_POS_NE, SlopeType.OBLIQUE_INT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { FULL, WEDGE, WEDGE, FULL, FULL, WEDGE }, new int[] { 0, MIN_MAX, MIN_MIN, 0, 0, MAX_MIN });
	public static final Slope OBL_INT_POS_SW = new Slope(ID_OBL_INT_POS_SW, SlopeType.OBLIQUE_INT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { FULL, WEDGE, FULL, WEDGE, WEDGE, FULL }, new int[] { 0, MAX_MIN, 0, MAX_MIN, MIN_MIN, 0 });
	public static final Slope OBL_INT_POS_SE = new Slope(ID_OBL_INT_POS_SE, SlopeType.OBLIQUE_INT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { FULL, WEDGE, FULL, WEDGE, FULL, WEDGE }, new int[] { 0, MIN_MIN, 0, MIN_MIN, 0, MIN_MIN });
	public static final Slope OBL_EXT_NEG_NW = new Slope(ID_OBL_EXT_NEG_NW, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { NO_FACE, WEDGE, NO_FACE, WEDGE, NO_FACE, WEDGE }, new int[] { 0, MAX_MAX, 0, MAX_MAX, 0, MAX_MAX });
	public static final Slope OBL_EXT_NEG_NE = new Slope(ID_OBL_EXT_NEG_NE, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { NO_FACE, WEDGE, NO_FACE, WEDGE, WEDGE, NO_FACE }, new int[] { 0, MIN_MAX, 0, MIN_MAX, MAX_MAX, 0 });
	public static final Slope OBL_EXT_NEG_SW = new Slope(ID_OBL_EXT_NEG_SW, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { NO_FACE, WEDGE, WEDGE, NO_FACE, NO_FACE, WEDGE }, new int[] { 0, MAX_MIN, MAX_MAX, 0, 0, MIN_MAX });
	public static final Slope OBL_EXT_NEG_SE = new Slope(ID_OBL_EXT_NEG_SE, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { NO_FACE, WEDGE, WEDGE, NO_FACE, WEDGE, NO_FACE }, new int[] { 0, MIN_MIN, MIN_MAX, 0, MIN_MAX, 0 });
	public static final Slope OBL_EXT_POS_NW = new Slope(ID_OBL_EXT_POS_NW, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { WEDGE, NO_FACE, NO_FACE, WEDGE, NO_FACE, WEDGE }, new int[] { MAX_MAX, 0, 0, MAX_MIN, 0, MAX_MIN });
	public static final Slope OBL_EXT_POS_NE = new Slope(ID_OBL_EXT_POS_NE, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { WEDGE, NO_FACE, NO_FACE, WEDGE, WEDGE, NO_FACE }, new int[] { MIN_MAX, 0, 0, MIN_MIN, MAX_MIN, 0 });
	public static final Slope OBL_EXT_POS_SW = new Slope(ID_OBL_EXT_POS_SW, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { WEDGE, NO_FACE, WEDGE, NO_FACE, NO_FACE, WEDGE }, new int[] { MAX_MIN, 0, MAX_MIN, 0, 0, MIN_MIN });
	public static final Slope OBL_EXT_POS_SE = new Slope(ID_OBL_EXT_POS_SE, SlopeType.OBLIQUE_EXT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { WEDGE, NO_FACE, WEDGE, NO_FACE, WEDGE, NO_FACE }, new int[] { MIN_MIN, 0, MIN_MIN, 0, MIN_MIN, 0 });
	public static final Slope PYR_HALF_NEG = new Slope(ID_PYR_HALF_NEG, SlopeType.PYRAMID, new ForgeDirection[] { DOWN, NORTH, SOUTH, WEST, EAST }, new int[] { NO_FACE, FULL, NO_FACE, NO_FACE, NO_FACE, NO_FACE }, new int[] { 0, 0, 0, 0, 0, 0 });
	public static final Slope PYR_HALF_POS = new Slope(ID_PYR_HALF_POS, SlopeType.PYRAMID, new ForgeDirection[] { UP, NORTH, SOUTH, WEST, EAST }, new int[] { FULL, NO_FACE, NO_FACE, NO_FACE, NO_FACE, NO_FACE }, new int[] { 0, 0, 0, 0, 0, 0 });

	public boolean isFaceFull(ForgeDirection side)
	{
		return faceShape[side.ordinal()] == FULL;
	}

	public boolean hasSide(ForgeDirection side)
	{
		return faceShape[side.ordinal()] != NO_FACE;
	}

	public int wedgeOrientation(ForgeDirection side)
	{
		return wedgeCorner[side.ordinal()];
	}

}
