package com.carpentersblocks.data;

import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.common.util.ForgeDirection;

public class Slope {

    /**
     * 16-bit data components:
     *
     * [0000000000000000]
     * slopeID
     */

    public final static byte ID_WEDGE_SE          = 0;
    public final static byte ID_WEDGE_NW          = 1;
    public final static byte ID_WEDGE_NE          = 2;
    public final static byte ID_WEDGE_SW          = 3;
    public final static byte ID_WEDGE_NEG_N       = 4;
    public final static byte ID_WEDGE_NEG_S       = 5;
    public final static byte ID_WEDGE_NEG_W       = 6;
    public final static byte ID_WEDGE_NEG_E       = 7;
    public final static byte ID_WEDGE_POS_N       = 8;
    public final static byte ID_WEDGE_POS_S       = 9;
    public final static byte ID_WEDGE_POS_W       = 10;
    public final static byte ID_WEDGE_POS_E       = 11;
    public final static byte ID_WEDGE_INT_NEG_SE  = 12;
    public final static byte ID_WEDGE_INT_NEG_NW  = 13;
    public final static byte ID_WEDGE_INT_NEG_NE  = 14;
    public final static byte ID_WEDGE_INT_NEG_SW  = 15;
    public final static byte ID_WEDGE_INT_POS_SE  = 16;
    public final static byte ID_WEDGE_INT_POS_NW  = 17;
    public final static byte ID_WEDGE_INT_POS_NE  = 18;
    public final static byte ID_WEDGE_INT_POS_SW  = 19;
    public final static byte ID_WEDGE_EXT_NEG_SE  = 20;
    public final static byte ID_WEDGE_EXT_NEG_NW  = 21;
    public final static byte ID_WEDGE_EXT_NEG_NE  = 22;
    public final static byte ID_WEDGE_EXT_NEG_SW  = 23;
    public final static byte ID_WEDGE_EXT_POS_SE  = 24;
    public final static byte ID_WEDGE_EXT_POS_NW  = 25;
    public final static byte ID_WEDGE_EXT_POS_NE  = 26;
    public final static byte ID_WEDGE_EXT_POS_SW  = 27;
    public final static byte ID_OBL_INT_NEG_SE    = 28;
    public final static byte ID_OBL_INT_NEG_NW    = 29;
    public final static byte ID_OBL_INT_NEG_NE    = 30;
    public final static byte ID_OBL_INT_NEG_SW    = 31;
    public final static byte ID_OBL_INT_POS_SE    = 32;
    public final static byte ID_OBL_INT_POS_NW    = 33;
    public final static byte ID_OBL_INT_POS_NE    = 34;
    public final static byte ID_OBL_INT_POS_SW    = 35;
    public final static byte ID_OBL_EXT_NEG_SE    = 36;
    public final static byte ID_OBL_EXT_NEG_NW    = 37;
    public final static byte ID_OBL_EXT_NEG_NE    = 38;
    public final static byte ID_OBL_EXT_NEG_SW    = 39;
    public final static byte ID_OBL_EXT_POS_SE    = 40;
    public final static byte ID_OBL_EXT_POS_NW    = 41;
    public final static byte ID_OBL_EXT_POS_NE    = 42;
    public final static byte ID_OBL_EXT_POS_SW    = 43;
    public final static byte ID_PRISM_NEG         = 44;
    public final static byte ID_PRISM_POS         = 45;
    public final static byte ID_PRISM_1P_POS_N    = 46;
    public final static byte ID_PRISM_1P_POS_S    = 47;
    public final static byte ID_PRISM_1P_POS_W    = 48;
    public final static byte ID_PRISM_1P_POS_E    = 49;
    public final static byte ID_PRISM_2P_POS_NS   = 50;
    public final static byte ID_PRISM_2P_POS_WE   = 51;
    public final static byte ID_PRISM_2P_POS_SE   = 52;
    public final static byte ID_PRISM_2P_POS_NW   = 53;
    public final static byte ID_PRISM_2P_POS_NE   = 54;
    public final static byte ID_PRISM_2P_POS_SW   = 55;
    public final static byte ID_PRISM_3P_POS_NWE  = 56;
    public final static byte ID_PRISM_3P_POS_SWE  = 57;
    public final static byte ID_PRISM_3P_POS_NSW  = 58;
    public final static byte ID_PRISM_3P_POS_NSE  = 59;
    public final static byte ID_PRISM_POS_4P      = 60;
    public final static byte ID_PRISM_WEDGE_POS_N = 61;
    public final static byte ID_PRISM_WEDGE_POS_S = 62;
    public final static byte ID_PRISM_WEDGE_POS_W = 63;
    public final static byte ID_PRISM_WEDGE_POS_E = 64;

    public enum Type
    {
        WEDGE_SIDE,
        WEDGE,
        WEDGE_INT,
        WEDGE_EXT,
        OBLIQUE_INT,
        OBLIQUE_EXT,
        PRISM,
        PRISM_1P,
        PRISM_2P,
        PRISM_3P,
        PRISM_4P,
        PRISM_WEDGE
    }

    public enum Face
    {
        NONE,
        FULL,
        WEDGE,
        TRIANGLE
    }

    /*
     * These represent the primary corner of the face shape.
     * 0 means no face.
     */

    public final static byte XYNN = 1;
    public final static byte XYNP = 2;
    public final static byte XYPN = 3;
    public final static byte XYPP = 4;

    /** Array containing registered slopes. */
    public static final Slope[] slopesList = new Slope[65];

    /** ID of the slope. */
    public final int slopeID;

    /** Class SlopeType **/
    public final SlopeType slopeType;
    
    /** Slope type. */
    public final Type type;

    /**
     * Holds slope face shape.
     *
     * [DOWN, UP, NORTH, SOUTH, WEST, EAST]
     */
    private final Face[] face;

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
    private final int[] faceBias;

    /**
     * Is the slope facing up.
     * A quick reference since it's called often.
     */
    public final boolean isPositive;

    /**
     * For most slopes, this aids in auto-transformation code.
     * For prism slopes, this aids in rendering the slopes.
     */
    public final List<ForgeDirection> facings;

    public Slope(int slopeID, Type slopeType, ForgeDirection[] facings, Face[] faceShape, int[] faceBias)
    {
        this.slopeID = slopeID;
        slopesList[slopeID] = this;
        type = slopeType;
        this.slopeType = SlopeType.getFromType(type);
        face = faceShape;
        this.faceBias = faceBias;

        this.facings = new ArrayList<ForgeDirection>();

        for (ForgeDirection face : facings) {
            this.facings.add(face);
        }

        isPositive = this.facings.contains(UP);
    }

    public static final Slope WEDGE_NW = new Slope(ID_WEDGE_NW, Type.WEDGE_SIDE, new ForgeDirection[] { NORTH, WEST }, new Face[] { Face.WEDGE, Face.WEDGE, Face.NONE, Face.FULL, Face.NONE, Face.FULL }, new int[] { XYPP, XYPP, 0, 0, 0, 0 });
    public static final Slope WEDGE_NE = new Slope(ID_WEDGE_NE, Type.WEDGE_SIDE, new ForgeDirection[] { NORTH, EAST }, new Face[] { Face.WEDGE, Face.WEDGE, Face.NONE, Face.FULL, Face.FULL, Face.NONE }, new int[] { XYNP, XYNP, 0, 0, 0, 0 });
    public static final Slope WEDGE_SW = new Slope(ID_WEDGE_SW, Type.WEDGE_SIDE, new ForgeDirection[] { SOUTH, WEST }, new Face[] { Face.WEDGE, Face.WEDGE, Face.FULL, Face.NONE, Face.NONE, Face.FULL }, new int[] { XYPN, XYPN, 0, 0, 0, 0 });
    public static final Slope WEDGE_SE = new Slope(ID_WEDGE_SE, Type.WEDGE_SIDE, new ForgeDirection[] { SOUTH, EAST }, new Face[] { Face.WEDGE, Face.WEDGE, Face.FULL, Face.NONE, Face.FULL, Face.NONE }, new int[] { XYNN, XYNN, 0, 0, 0, 0 });
    public static final Slope WEDGE_NEG_N = new Slope(ID_WEDGE_NEG_N, Type.WEDGE, new ForgeDirection[] { DOWN, NORTH }, new Face[] { Face.NONE, Face.FULL, Face.NONE, Face.FULL, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYPP, XYPP });
    public static final Slope WEDGE_NEG_S = new Slope(ID_WEDGE_NEG_S, Type.WEDGE, new ForgeDirection[] { DOWN, SOUTH }, new Face[] { Face.NONE, Face.FULL, Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYNP, XYNP });
    public static final Slope WEDGE_NEG_W = new Slope(ID_WEDGE_NEG_W, Type.WEDGE, new ForgeDirection[] { DOWN, WEST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.WEDGE, Face.NONE, Face.FULL }, new int[] { 0, 0, XYPP, XYPP, 0, 0 });
    public static final Slope WEDGE_NEG_E = new Slope(ID_WEDGE_NEG_E, Type.WEDGE, new ForgeDirection[] { DOWN, EAST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL, Face.NONE }, new int[] { 0, 0, XYNP, XYNP, 0, 0 });
    public static final Slope WEDGE_POS_N = new Slope(ID_WEDGE_POS_N, Type.WEDGE, new ForgeDirection[] { UP, NORTH }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.FULL, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYPN, XYPN });
    public static final Slope WEDGE_POS_S = new Slope(ID_WEDGE_POS_S, Type.WEDGE, new ForgeDirection[] { UP, SOUTH }, new Face[] { Face.FULL, Face.NONE, Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYNN, XYNN });
    public static final Slope WEDGE_POS_W = new Slope(ID_WEDGE_POS_W, Type.WEDGE, new ForgeDirection[] { UP, WEST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE, Face.FULL }, new int[] { 0, 0, XYPN, XYPN, 0, 0 });
    public static final Slope WEDGE_POS_E = new Slope(ID_WEDGE_POS_E, Type.WEDGE, new ForgeDirection[] { UP, EAST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE, Face.FULL, Face.NONE }, new int[] { 0, 0, XYNN, XYNN, 0, 0 });
    public static final Slope WEDGE_INT_NEG_NW = new Slope(ID_WEDGE_INT_NEG_NW, Type.WEDGE_INT, new ForgeDirection[] { DOWN, NORTH, WEST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL }, new int[] { 0, 0, XYPP, 0, XYPP, 0 });
    public static final Slope WEDGE_INT_NEG_NE = new Slope(ID_WEDGE_INT_NEG_NE, Type.WEDGE_INT, new ForgeDirection[] { DOWN, NORTH, EAST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE }, new int[] { 0, 0, XYNP, 0, 0, XYPP });
    public static final Slope WEDGE_INT_NEG_SW = new Slope(ID_WEDGE_INT_NEG_SW, Type.WEDGE_INT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new Face[] { Face.NONE, Face.FULL, Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL }, new int[] { 0, 0, 0, XYPP, XYNP, 0 });
    public static final Slope WEDGE_INT_NEG_SE = new Slope(ID_WEDGE_INT_NEG_SE, Type.WEDGE_INT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new Face[] { Face.NONE, Face.FULL, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE }, new int[] { 0, 0, 0, XYNP, 0, XYNP });
    public static final Slope WEDGE_INT_POS_NW = new Slope(ID_WEDGE_INT_POS_NW, Type.WEDGE_INT, new ForgeDirection[] { UP, NORTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL }, new int[] { 0, 0, XYPN, 0, XYPN, 0 });
    public static final Slope WEDGE_INT_POS_NE = new Slope(ID_WEDGE_INT_POS_NE, Type.WEDGE_INT, new ForgeDirection[] { UP, NORTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE }, new int[] { 0, 0, XYNN, 0, 0, XYPN });
    public static final Slope WEDGE_INT_POS_SW = new Slope(ID_WEDGE_INT_POS_SW, Type.WEDGE_INT, new ForgeDirection[] { UP, SOUTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL }, new int[] { 0, 0, 0, XYPN, XYNN, 0 });
    public static final Slope WEDGE_INT_POS_SE = new Slope(ID_WEDGE_INT_POS_SE, Type.WEDGE_INT, new ForgeDirection[] { UP, SOUTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE }, new int[] { 0, 0, 0, XYNN, 0, XYNN });
    public static final Slope WEDGE_EXT_NEG_NW = new Slope(ID_WEDGE_EXT_NEG_NW, Type.WEDGE_EXT, new ForgeDirection[] { DOWN, NORTH, WEST }, new Face[] { Face.NONE, Face.FULL, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE }, new int[] { 0, 0, 0, XYPP, 0, XYPP });
    public static final Slope WEDGE_EXT_NEG_NE = new Slope(ID_WEDGE_EXT_NEG_NE, Type.WEDGE_EXT, new ForgeDirection[] { DOWN, NORTH, EAST }, new Face[] { Face.NONE, Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE }, new int[] { 0, 0, 0, XYNP, XYPP, 0 });
    public static final Slope WEDGE_EXT_NEG_SW = new Slope(ID_WEDGE_EXT_NEG_SW, Type.WEDGE_EXT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE }, new int[] { 0, 0, XYPP, 0, 0, XYNP });
    public static final Slope WEDGE_EXT_NEG_SE = new Slope(ID_WEDGE_EXT_NEG_SE, Type.WEDGE_EXT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new Face[] { Face.NONE, Face.FULL, Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE }, new int[] { 0, 0, XYNP, 0, XYNP, 0 });
    public static final Slope WEDGE_EXT_POS_NW = new Slope(ID_WEDGE_EXT_POS_NW, Type.WEDGE_EXT, new ForgeDirection[] { UP, NORTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE }, new int[] { 0, 0, 0, XYPN, 0, XYPN });
    public static final Slope WEDGE_EXT_POS_NE = new Slope(ID_WEDGE_EXT_POS_NE, Type.WEDGE_EXT, new ForgeDirection[] { UP, NORTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE }, new int[] { 0, 0, 0, XYNN, XYPN, 0 });
    public static final Slope WEDGE_EXT_POS_SW = new Slope(ID_WEDGE_EXT_POS_SW, Type.WEDGE_EXT, new ForgeDirection[] { UP, SOUTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE }, new int[] { 0, 0, XYPN, 0, 0, XYNN });
    public static final Slope WEDGE_EXT_POS_SE = new Slope(ID_WEDGE_EXT_POS_SE, Type.WEDGE_EXT, new ForgeDirection[] { UP, SOUTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE }, new int[] { 0, 0, XYNN, 0, XYNN, 0 });
    public static final Slope OBL_INT_NEG_NW = new Slope(ID_OBL_INT_NEG_NW, Type.OBLIQUE_INT, new ForgeDirection[] { DOWN, NORTH, WEST }, new Face[] { Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL }, new int[] { XYPP, 0, XYPP, 0, XYPP, 0 });
    public static final Slope OBL_INT_NEG_NE = new Slope(ID_OBL_INT_NEG_NE, Type.OBLIQUE_INT, new ForgeDirection[] { DOWN, NORTH, EAST }, new Face[] { Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE }, new int[] { XYNP, 0, XYNP, 0, 0, XYPP });
    public static final Slope OBL_INT_NEG_SW = new Slope(ID_OBL_INT_NEG_SW, Type.OBLIQUE_INT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new Face[] { Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL }, new int[] { XYPN, 0, 0, XYPP, XYNP, 0 });
    public static final Slope OBL_INT_NEG_SE = new Slope(ID_OBL_INT_NEG_SE, Type.OBLIQUE_INT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new Face[] { Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE }, new int[] { XYNN, 0, 0, XYNP, 0, XYNP });
    public static final Slope OBL_INT_POS_NW = new Slope(ID_OBL_INT_POS_NW, Type.OBLIQUE_INT, new ForgeDirection[] { UP, NORTH, WEST }, new Face[] { Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL }, new int[] { 0, XYPP, XYPN, 0, XYPN, 0 });
    public static final Slope OBL_INT_POS_NE = new Slope(ID_OBL_INT_POS_NE, Type.OBLIQUE_INT, new ForgeDirection[] { UP, NORTH, EAST }, new Face[] { Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL, Face.FULL, Face.WEDGE }, new int[] { 0, XYNP, XYNN, 0, 0, XYPN });
    public static final Slope OBL_INT_POS_SW = new Slope(ID_OBL_INT_POS_SW, Type.OBLIQUE_INT, new ForgeDirection[] { UP, SOUTH, WEST }, new Face[] { Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE, Face.WEDGE, Face.FULL }, new int[] { 0, XYPN, 0, XYPN, XYNN, 0 });
    public static final Slope OBL_INT_POS_SE = new Slope(ID_OBL_INT_POS_SE, Type.OBLIQUE_INT, new ForgeDirection[] { UP, SOUTH, EAST }, new Face[] { Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE, Face.FULL, Face.WEDGE }, new int[] { 0, XYNN, 0, XYNN, 0, XYNN });
    public static final Slope OBL_EXT_NEG_NW = new Slope(ID_OBL_EXT_NEG_NW, Type.OBLIQUE_EXT, new ForgeDirection[] { DOWN, NORTH, WEST }, new Face[] { Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE }, new int[] { 0, XYPP, 0, XYPP, 0, XYPP });
    public static final Slope OBL_EXT_NEG_NE = new Slope(ID_OBL_EXT_NEG_NE, Type.OBLIQUE_EXT, new ForgeDirection[] { DOWN, NORTH, EAST }, new Face[] { Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE }, new int[] { 0, XYNP, 0, XYNP, XYPP, 0 });
    public static final Slope OBL_EXT_NEG_SW = new Slope(ID_OBL_EXT_NEG_SW, Type.OBLIQUE_EXT, new ForgeDirection[] { DOWN, SOUTH, WEST }, new Face[] { Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE }, new int[] { 0, XYPN, XYPP, 0, 0, XYNP });
    public static final Slope OBL_EXT_NEG_SE = new Slope(ID_OBL_EXT_NEG_SE, Type.OBLIQUE_EXT, new ForgeDirection[] { DOWN, SOUTH, EAST }, new Face[] { Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE }, new int[] { 0, XYNN, XYNP, 0, XYNP, 0 });
    public static final Slope OBL_EXT_POS_NW = new Slope(ID_OBL_EXT_POS_NW, Type.OBLIQUE_EXT, new ForgeDirection[] { UP, NORTH, WEST }, new Face[] { Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE }, new int[] { XYPP, 0, 0, XYPN, 0, XYPN });
    public static final Slope OBL_EXT_POS_NE = new Slope(ID_OBL_EXT_POS_NE, Type.OBLIQUE_EXT, new ForgeDirection[] { UP, NORTH, EAST }, new Face[] { Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE, Face.WEDGE, Face.NONE }, new int[] { XYNP, 0, 0, XYNN, XYPN, 0 });
    public static final Slope OBL_EXT_POS_SW = new Slope(ID_OBL_EXT_POS_SW, Type.OBLIQUE_EXT, new ForgeDirection[] { UP, SOUTH, WEST }, new Face[] { Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE, Face.NONE, Face.WEDGE }, new int[] { XYPN, 0, XYPN, 0, 0, XYNN });
    public static final Slope OBL_EXT_POS_SE = new Slope(ID_OBL_EXT_POS_SE, Type.OBLIQUE_EXT, new ForgeDirection[] { UP, SOUTH, EAST }, new Face[] { Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE, Face.WEDGE, Face.NONE }, new int[] { XYNN, 0, XYNN, 0, XYNN, 0 });
    public static final Slope PRISM_NEG = new Slope(ID_PRISM_NEG, Type.PRISM, new ForgeDirection[] { DOWN }, new Face[] { Face.NONE, Face.FULL, Face.NONE, Face.NONE, Face.NONE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_POS = new Slope(ID_PRISM_POS, Type.PRISM, new ForgeDirection[] { UP }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.NONE, Face.NONE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_1P_POS_N = new Slope(ID_PRISM_1P_POS_N, Type.PRISM_1P, new ForgeDirection[] { UP, NORTH }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.NONE, Face.NONE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_1P_POS_S = new Slope(ID_PRISM_1P_POS_S, Type.PRISM_1P, new ForgeDirection[] { UP, SOUTH }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.TRIANGLE, Face.NONE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_1P_POS_W = new Slope(ID_PRISM_1P_POS_W, Type.PRISM_1P, new ForgeDirection[] { UP, WEST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.NONE, Face.TRIANGLE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_1P_POS_E = new Slope(ID_PRISM_1P_POS_E, Type.PRISM_1P, new ForgeDirection[] { UP, EAST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.NONE, Face.NONE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_NS = new Slope(ID_PRISM_2P_POS_NS, Type.PRISM_2P, new ForgeDirection[] { UP, NORTH, SOUTH }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.NONE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_WE = new Slope(ID_PRISM_2P_POS_WE, Type.PRISM_2P, new ForgeDirection[] { UP, WEST, EAST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.NONE, Face.TRIANGLE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_SE = new Slope(ID_PRISM_2P_POS_SE, Type.PRISM_2P, new ForgeDirection[] { UP, SOUTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.TRIANGLE, Face.NONE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_NW = new Slope(ID_PRISM_2P_POS_NW, Type.PRISM_2P, new ForgeDirection[] { UP, NORTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.NONE, Face.TRIANGLE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_NE = new Slope(ID_PRISM_2P_POS_NE, Type.PRISM_2P, new ForgeDirection[] { UP, NORTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.NONE, Face.NONE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_2P_POS_SW = new Slope(ID_PRISM_2P_POS_SW, Type.PRISM_2P, new ForgeDirection[] { UP, SOUTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_3P_POS_WEN = new Slope(ID_PRISM_3P_POS_NWE, Type.PRISM_3P, new ForgeDirection[] { UP, NORTH, WEST, EAST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.NONE, Face.TRIANGLE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_3P_POS_WES = new Slope(ID_PRISM_3P_POS_SWE, Type.PRISM_3P, new ForgeDirection[] { UP, SOUTH, WEST, EAST }, new Face[] { Face.FULL, Face.NONE, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_3P_POS_NSW = new Slope(ID_PRISM_3P_POS_NSW, Type.PRISM_3P, new ForgeDirection[] { UP, NORTH, SOUTH, WEST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.TRIANGLE, Face.NONE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_3P_POS_NSE = new Slope(ID_PRISM_3P_POS_NSE, Type.PRISM_3P, new ForgeDirection[] { UP, NORTH, SOUTH, EAST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.NONE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_4P_POS = new Slope(ID_PRISM_POS_4P, Type.PRISM_4P, new ForgeDirection[] { UP, NORTH, SOUTH, WEST, EAST }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.TRIANGLE, Face.TRIANGLE, Face.TRIANGLE }, new int[] { 0, 0, 0, 0, 0, 0 });
    public static final Slope PRISM_WEDGE_POS_N = new Slope(ID_PRISM_WEDGE_POS_N, Type.PRISM_WEDGE, new ForgeDirection[] { UP, NORTH }, new Face[] { Face.FULL, Face.NONE, Face.TRIANGLE, Face.FULL, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYPN, XYPN });
    public static final Slope PRISM_WEDGE_POS_S = new Slope(ID_PRISM_WEDGE_POS_S, Type.PRISM_WEDGE, new ForgeDirection[] { UP, SOUTH }, new Face[] { Face.FULL, Face.NONE, Face.FULL, Face.TRIANGLE, Face.WEDGE, Face.WEDGE }, new int[] { 0, 0, 0, 0, XYNN, XYNN });
    public static final Slope PRISM_WEDGE_POS_W = new Slope(ID_PRISM_WEDGE_POS_W, Type.PRISM_WEDGE, new ForgeDirection[] { UP, WEST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE, Face.TRIANGLE, Face.FULL }, new int[] { 0, 0, XYPN, XYPN, 0, 0 });
    public static final Slope PRISM_WEDGE_POS_E = new Slope(ID_PRISM_WEDGE_POS_E, Type.PRISM_WEDGE, new ForgeDirection[] { UP, EAST }, new Face[] { Face.FULL, Face.NONE, Face.WEDGE, Face.WEDGE, Face.FULL, Face.TRIANGLE }, new int[] { 0, 0, XYNN, XYNN, 0, 0 });

    /**
     * Returns primary slope type.
     * Used when slopes share common attributes.
     */
    public Type getPrimaryType()
    {
        switch (type) {
            case PRISM:
            case PRISM_1P:
            case PRISM_2P:
            case PRISM_3P:
            case PRISM_4P:
                return Type.PRISM;
            default:
                return type;
        }
    }

    public Face getFace(ForgeDirection side)
    {
        return face[side.ordinal()];
    }

    public boolean isFaceFull(ForgeDirection side)
    {
        return face[side.ordinal()] == Face.FULL;
    }

    public boolean hasSide(ForgeDirection side)
    {
        return face[side.ordinal()] != Face.NONE;
    }

    public int getFaceBias(ForgeDirection side)
    {
        return faceBias[side.ordinal()];
    }

}
