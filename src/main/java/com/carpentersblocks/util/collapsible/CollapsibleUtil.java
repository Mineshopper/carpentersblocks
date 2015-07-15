package com.carpentersblocks.util.collapsible;

import com.carpentersblocks.data.Collapsible;
import com.carpentersblocks.tileentity.TEBase;

public class CollapsibleUtil {

    private static Collapsible data = new Collapsible();

    public static double CENTER_YMAX;

    public static double offset_XZNN;
    public static double offset_XZNP;
    public static double offset_XZPN;
    public static double offset_XZPP;

    /**
     * Returns true if fully collapsed.
     */
    public static boolean isMin(TEBase TE)
    {
        for (int quad = 0; quad < 4; quad++) {
            if (data.getQuadDepth(TE, quad) > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if a full cube.
     */
    public static boolean isMax(TEBase TE)
    {
        for (int quad = 0; quad < 4; quad++) {
            if (data.getQuadDepth(TE, quad) < 16) {
                return false;
            }
        }

        return true;
    }

    /**
     * Fills Y-offsets for each corner and center of block for rendering purposes.
     */
    public static void computeOffsets(TEBase TE)
    {
        double BIAS = isMin(TE) ? 1.0D / 1024.0D : 0.0D; /* small offset to prevent Z-fighting at depth 0 */

        offset_XZNN = Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNN) / 16.0D + BIAS;
        offset_XZNP = Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNP) / 16.0D + BIAS;
        offset_XZPN = Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPN) / 16.0D + BIAS;
        offset_XZPP = Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPP) / 16.0D + BIAS;

        /* Find primary corners and set center yMax offset. */

        double NW_SE = Math.abs(offset_XZNN - offset_XZPP);
        double NE_SW = Math.abs(offset_XZPN - offset_XZNP);

        /*
         * Changing this to NW_SE > NE_SW will change how slopes are split.
         * It's really a matter of personal preference.
         */
        if (NW_SE < NE_SW) {
            CENTER_YMAX = (offset_XZPN + offset_XZNP) / 2.0F;
        } else {
            CENTER_YMAX = (offset_XZNN + offset_XZPP) / 2.0F;
        }
    }

    /**
     * Returns block depth determined by the largest quadrant.
     */
    public static float getBoundsMaxDepth(TEBase TE)
    {
        float maxDepth = 0.0F;

        for (int quad = 0; quad < 4; ++quad) {
            float depth = Collapsible.getQuadDepth(TE, quad) / 16.0F;
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }

        return maxDepth;
    }

    /**
     * Will generate four boxes with max height represented by largest quadrant depth.
     */
    public static float[] genBounds(TEBase TE, int quad)
    {
        float xMin = 0.0F;
        float zMin = 0.0F;
        float xMax = 1.0F;
        float zMax = 1.0F;

        switch (quad)
        {
            case Collapsible.QUAD_XZNN:
                xMax = 0.5F;
                zMax = 0.5F;
                break;
            case Collapsible.QUAD_XZNP:
                xMax = 0.5F;
                zMin = 0.5F;
                break;
            case Collapsible.QUAD_XZPN:
                xMin = 0.5F;
                zMax = 0.5F;
                break;
            case Collapsible.QUAD_XZPP:
                xMin = 0.5F;
                zMin = 0.5F;
                break;
        }

        float maxDepth = getBoundsMaxDepth(TE);
        float depth = Collapsible.getQuadDepth(TE, quad) / 16.0F;

        // Make quads stagger no more than 0.5F so player can always walk across them
        if (data.isPositive(TE)) {
            if (maxDepth - depth > 0.5F) {
                depth = maxDepth - 0.5F;
            }
            return new float[] { xMin, 0.0F, zMin, xMax, depth, zMax };
        } else {
            return new float[] { xMin, 1.0F - depth, zMin, xMax, 1.0F, zMax };
        }
    }

}
