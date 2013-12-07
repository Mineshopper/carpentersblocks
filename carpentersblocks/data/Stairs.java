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

public class Stairs {

	/**
	 * 16-bit data components:
	 *
	 *	[0000000000000000]
	 *  stairsID
	 */

	/*
	 * Stairs IDs.
	 * 
	 * IDs 0 - 11 are set arbitrarily when
	 * block is placed in world.
	 * 
	 * IDs 12 - 27 are mixed to retain compatibility
	 * with older versions.
	 */

	public final static byte ID_NORMAL_SE = 0;
	public final static byte ID_NORMAL_NW = 1;
	public final static byte ID_NORMAL_NE = 2;
	public final static byte ID_NORMAL_SW = 3;
	public final static byte ID_NORMAL_NEG_N = 4;
	public final static byte ID_NORMAL_NEG_S = 5;
	public final static byte ID_NORMAL_NEG_W = 6;
	public final static byte ID_NORMAL_NEG_E = 7;
	public final static byte ID_NORMAL_POS_N = 8;
	public final static byte ID_NORMAL_POS_S = 9;
	public final static byte ID_NORMAL_POS_W = 10;
	public final static byte ID_NORMAL_POS_E = 11;
	public final static byte ID_NORMAL_INT_NEG_SE = 19;
	public final static byte ID_NORMAL_INT_NEG_NW = 15;
	public final static byte ID_NORMAL_INT_NEG_NE = 13;
	public final static byte ID_NORMAL_INT_NEG_SW = 17;
	public final static byte ID_NORMAL_INT_POS_SE = 18;
	public final static byte ID_NORMAL_INT_POS_NW = 14;
	public final static byte ID_NORMAL_INT_POS_NE = 12;
	public final static byte ID_NORMAL_INT_POS_SW = 16;
	public final static byte ID_NORMAL_EXT_NEG_SE = 23;
	public final static byte ID_NORMAL_EXT_NEG_NW = 27;
	public final static byte ID_NORMAL_EXT_NEG_NE = 25;
	public final static byte ID_NORMAL_EXT_NEG_SW = 21;
	public final static byte ID_NORMAL_EXT_POS_SE = 22;
	public final static byte ID_NORMAL_EXT_POS_NW = 26;
	public final static byte ID_NORMAL_EXT_POS_NE = 24;
	public final static byte ID_NORMAL_EXT_POS_SW = 20;

	public enum StairsType
	{
		NORMAL_XZ,
		NORMAL_Y,
		NORMAL_INT,
		NORMAL_EXT
	}

	/** Array containing registered stairs. */
	public static final Stairs[] stairsList = new Stairs[28];

	/** ID of the stairs. */
	public final int stairsID;

	/** Stairs type. */
	public final StairsType stairsType;

	/**
	 * Holds stairs face shape.
	 * 
	 * [DOWN, UP, NORTH, SOUTH, WEST, EAST]
	 */
	private final int[] faceShape;

	/**
	 * If faceShape indicates side is STAGGERED,
	 * this will indicate which corner of face
	 * is the "corner."
	 * 
	 * Not currently used.
	 * 
	 * Stores MIN/MAX of staggered corner.
	 * Stored as [X/Y], [Z/Y], or [X/Z].
	 * 
	 * [DOWN, UP, NORTH, SOUTH, WEST, EAST]
	 */
	private final int[] staggeredCorner;

	/**
	 * Are the stairs facing up.
	 * A quick reference since it's called often.
	 */
	public final boolean arePositive;

	/** Stairs direction. */
	public final List<ForgeDirection> facings;

	public static final byte NO_FACE = 0;
	public static final byte FULL = 1;
	public static final byte STAGGERED = 2;

	public static final byte MIN_MIN = 1;
	public static final byte MIN_MAX = 2;
	public static final byte MAX_MIN = 3;
	public static final byte MAX_MAX = 4;

	public Stairs(int stairsID, StairsType stairsType, ForgeDirection[] facings, int[] faceShape, int[] staggeredCorner)
	{
		this.stairsID = stairsID;
		stairsList[stairsID] = this;
		this.stairsType = stairsType;
		this.faceShape = faceShape;
		this.staggeredCorner = staggeredCorner;

		this.facings = new ArrayList<ForgeDirection>();

		for (ForgeDirection face : facings) {
			this.facings.add(face);
		}

		arePositive = this.facings.contains(UP);
	}

	public static final Stairs NORMAL_NW = new Stairs(ID_NORMAL_NW, StairsType.NORMAL_XZ, new ForgeDirection[] { NORTH, WEST }, new int[] { STAGGERED, STAGGERED, NO_FACE, FULL, NO_FACE, FULL }, new int[] { MAX_MAX, MAX_MAX, 0, 0, 0, 0 });
	public static final Stairs NORMAL_NE = new Stairs(ID_NORMAL_NE, StairsType.NORMAL_XZ, new ForgeDirection[] { NORTH, EAST }, new int[] { STAGGERED, STAGGERED, NO_FACE, FULL, FULL, NO_FACE }, new int[] { MIN_MAX, MIN_MAX, 0, 0, 0, 0 });
	public static final Stairs NORMAL_SW = new Stairs(ID_NORMAL_SW, StairsType.NORMAL_XZ, new ForgeDirection[] { SOUTH, WEST }, new int[] { STAGGERED, STAGGERED, FULL, NO_FACE, NO_FACE, FULL }, new int[] { MAX_MIN, MAX_MIN, 0, 0, 0, 0 });
	public static final Stairs NORMAL_SE = new Stairs(ID_NORMAL_SE, StairsType.NORMAL_XZ, new ForgeDirection[] { SOUTH, EAST }, new int[] { STAGGERED, STAGGERED, FULL, NO_FACE, FULL, NO_FACE }, new int[] { MIN_MIN, MIN_MIN, 0, 0, 0, 0 });
	public static final Stairs NORMAL_NEG_N = new Stairs(ID_NORMAL_NEG_N, StairsType.NORMAL_Y, new ForgeDirection[] { DOWN, NORTH }, new int[] { NO_FACE, FULL, NO_FACE, FULL, STAGGERED, STAGGERED }, new int[] { 0, 0, 0, 0, MAX_MAX, MAX_MAX });
	public static final Stairs NORMAL_NEG_S = new Stairs(ID_NORMAL_NEG_S, StairsType.NORMAL_Y, new ForgeDirection[] { DOWN, SOUTH }, new int[] { NO_FACE, FULL, FULL, NO_FACE, STAGGERED, STAGGERED }, new int[] { 0, 0, 0, 0, MIN_MAX, MIN_MAX });
	public static final Stairs NORMAL_NEG_W = new Stairs(ID_NORMAL_NEG_W, StairsType.NORMAL_Y, new ForgeDirection[] { DOWN, WEST }, new int[] { NO_FACE, FULL, STAGGERED, STAGGERED, NO_FACE, FULL }, new int[] { 0, 0, MAX_MAX, MAX_MAX, 0, 0 });
	public static final Stairs NORMAL_NEG_E = new Stairs(ID_NORMAL_NEG_E, StairsType.NORMAL_Y, new ForgeDirection[] { DOWN, EAST }, new int[] { NO_FACE, FULL, STAGGERED, STAGGERED, FULL, NO_FACE }, new int[] { 0, 0, MIN_MAX, MIN_MAX, 0, 0 });
	public static final Stairs NORMAL_POS_N = new Stairs(ID_NORMAL_POS_N, StairsType.NORMAL_Y, new ForgeDirection[] { UP, NORTH }, new int[] { FULL, NO_FACE, NO_FACE, FULL, STAGGERED, STAGGERED }, new int[] { 0, 0, 0, 0, MAX_MIN, MAX_MIN });
	public static final Stairs NORMAL_POS_S = new Stairs(ID_NORMAL_POS_S, StairsType.NORMAL_Y, new ForgeDirection[] { UP, SOUTH }, new int[] { FULL, NO_FACE, FULL, NO_FACE, STAGGERED, STAGGERED }, new int[] { 0, 0, 0, 0, MIN_MIN, MIN_MIN });
	public static final Stairs NORMAL_POS_W = new Stairs(ID_NORMAL_POS_W, StairsType.NORMAL_Y, new ForgeDirection[] { UP, WEST }, new int[] { FULL, NO_FACE, STAGGERED, STAGGERED, NO_FACE, FULL }, new int[] { 0, 0, MAX_MIN, MAX_MIN, 0, 0 });
	public static final Stairs NORMAL_POS_E = new Stairs(ID_NORMAL_POS_E, StairsType.NORMAL_Y, new ForgeDirection[] { UP, EAST }, new int[] { FULL, NO_FACE, STAGGERED, STAGGERED, FULL, NO_FACE }, new int[] { 0, 0, MIN_MIN, MIN_MIN, 0, 0 });
	public static final Stairs NORMAL_INT_NEG_NW = new Stairs(ID_NORMAL_INT_NEG_NW, StairsType.NORMAL_INT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { NO_FACE, FULL, STAGGERED, FULL, STAGGERED, FULL }, new int[] { 0, 0, MAX_MAX, 0, MAX_MAX, 0 });
	public static final Stairs NORMAL_INT_NEG_NE = new Stairs(ID_NORMAL_INT_NEG_NE, StairsType.NORMAL_INT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { NO_FACE, FULL, STAGGERED, FULL, FULL, STAGGERED }, new int[] { 0, 0, MIN_MAX, 0, 0, MAX_MAX });
	public static final Stairs NORMAL_INT_NEG_SW = new Stairs(ID_NORMAL_INT_NEG_SW, StairsType.NORMAL_INT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { NO_FACE, FULL, FULL, STAGGERED, STAGGERED, FULL }, new int[] { 0, 0, 0, MAX_MAX, MIN_MAX, 0 });
	public static final Stairs NORMAL_INT_NEG_SE = new Stairs(ID_NORMAL_INT_NEG_SE, StairsType.NORMAL_INT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { NO_FACE, FULL, FULL, STAGGERED, FULL, STAGGERED }, new int[] { 0, 0, 0, MIN_MAX, 0, MIN_MAX });
	public static final Stairs NORMAL_INT_POS_NW = new Stairs(ID_NORMAL_INT_POS_NW, StairsType.NORMAL_INT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { FULL, NO_FACE, STAGGERED, FULL, STAGGERED, FULL }, new int[] { 0, 0, MAX_MIN, 0, MAX_MIN, 0 });
	public static final Stairs NORMAL_INT_POS_NE = new Stairs(ID_NORMAL_INT_POS_NE, StairsType.NORMAL_INT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { FULL, NO_FACE, STAGGERED, FULL, FULL, STAGGERED }, new int[] { 0, 0, MIN_MIN, 0, 0, MAX_MIN });
	public static final Stairs NORMAL_INT_POS_SW = new Stairs(ID_NORMAL_INT_POS_SW, StairsType.NORMAL_INT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { FULL, NO_FACE, FULL, STAGGERED, STAGGERED, FULL }, new int[] { 0, 0, 0, MAX_MIN, MIN_MIN, 0 });
	public static final Stairs NORMAL_INT_POS_SE = new Stairs(ID_NORMAL_INT_POS_SE, StairsType.NORMAL_INT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { FULL, NO_FACE, FULL, STAGGERED, FULL, STAGGERED }, new int[] { 0, 0, 0, MIN_MIN, 0, MIN_MIN });
	public static final Stairs NORMAL_EXT_NEG_NW = new Stairs(ID_NORMAL_EXT_NEG_NW, StairsType.NORMAL_EXT, new ForgeDirection[] { DOWN, NORTH, WEST }, new int[] { NO_FACE, FULL, NO_FACE, STAGGERED, NO_FACE, STAGGERED }, new int[] { 0, 0, 0, MAX_MAX, 0, MAX_MAX });
	public static final Stairs NORMAL_EXT_NEG_NE = new Stairs(ID_NORMAL_EXT_NEG_NE, StairsType.NORMAL_EXT, new ForgeDirection[] { DOWN, NORTH, EAST }, new int[] { NO_FACE, FULL, NO_FACE, STAGGERED, STAGGERED, NO_FACE }, new int[] { 0, 0, 0, MIN_MAX, MAX_MAX, 0 });
	public static final Stairs NORMAL_EXT_NEG_SW = new Stairs(ID_NORMAL_EXT_NEG_SW, StairsType.NORMAL_EXT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new int[] { NO_FACE, FULL, STAGGERED, NO_FACE, NO_FACE, STAGGERED }, new int[] { 0, 0, MAX_MAX, 0, 0, MIN_MAX });
	public static final Stairs NORMAL_EXT_NEG_SE = new Stairs(ID_NORMAL_EXT_NEG_SE, StairsType.NORMAL_EXT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new int[] { NO_FACE, FULL, STAGGERED, NO_FACE, STAGGERED, NO_FACE }, new int[] { 0, 0, MIN_MAX, 0, MIN_MAX, 0 });
	public static final Stairs NORMAL_EXT_POS_NW = new Stairs(ID_NORMAL_EXT_POS_NW, StairsType.NORMAL_EXT, new ForgeDirection[] { UP, NORTH, WEST }, new int[] { FULL, NO_FACE, NO_FACE, STAGGERED, NO_FACE, STAGGERED }, new int[] { 0, 0, 0, MAX_MIN, 0, MAX_MIN });
	public static final Stairs NORMAL_EXT_POS_NE = new Stairs(ID_NORMAL_EXT_POS_NE, StairsType.NORMAL_EXT, new ForgeDirection[] { UP, NORTH, EAST }, new int[] { FULL, NO_FACE, NO_FACE, STAGGERED, STAGGERED, NO_FACE }, new int[] { 0, 0, 0, MIN_MIN, MAX_MIN, 0 });
	public static final Stairs NORMAL_EXT_POS_SW = new Stairs(ID_NORMAL_EXT_POS_SW, StairsType.NORMAL_EXT, new ForgeDirection[] { UP, SOUTH, WEST }, new int[] { FULL, NO_FACE, STAGGERED, NO_FACE, NO_FACE, STAGGERED }, new int[] { 0, 0, MAX_MIN, 0, 0, MIN_MIN });
	public static final Stairs NORMAL_EXT_POS_SE = new Stairs(ID_NORMAL_EXT_POS_SE, StairsType.NORMAL_EXT, new ForgeDirection[] { UP, SOUTH, EAST }, new int[] { FULL, NO_FACE, STAGGERED, NO_FACE, STAGGERED, NO_FACE }, new int[] { 0, 0, MIN_MIN, 0, MIN_MIN, 0 });

	public boolean isFaceFull(ForgeDirection side)
	{
		return faceShape[side.ordinal()] == FULL;
	}

	public int staggeredOrientation(ForgeDirection side)
	{
		return staggeredCorner[side.ordinal()];
	}

}
