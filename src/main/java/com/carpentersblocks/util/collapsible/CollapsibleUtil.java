package com.carpentersblocks.util.collapsible;

import com.carpentersblocks.data.Collapsible;
import com.carpentersblocks.tileentity.TEBase;

public class CollapsibleUtil {

    public static double CENTER_YMAX;

    public static double offset_XZNN;
    public static double offset_XZNP;
    public static double offset_XZPN;
    public static double offset_XZPP;

    public static boolean isFullyCollapsed(TEBase TE)
    {
        int combinedHeight = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP);

        return combinedHeight == 16 * 4;
    }

    public static boolean isFullHeight(TEBase TE)
    {
        int combinedHeight = Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) +
                             Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP);

        return combinedHeight == 0;
    }

    /**
     * Fills Y-offsets for each corner and center of block for rendering purposes.
     */
    public static void computeOffsets(TEBase TE)
    {
        double BIAS = isFullyCollapsed(TE) ? 0.0D : 1.0D / 1024.0D; /* small offset to prevent Z-fighting at height 1 */

        offset_XZNN = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) - 1.0D) / 15.0D + BIAS;
        offset_XZNP = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) - 1.0D) / 15.0D + BIAS;
        offset_XZPN = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) - 1.0D) / 15.0D + BIAS;
        offset_XZPP = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) - 1.0D) / 15.0D + BIAS;

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
     * Returns block height determined by the highest quadrant.
     */
    public static float getBoundsMaxHeight(TEBase TE)
    {
        float maxHeight = 0.0F;

        for (int quadrant = 0; quadrant < 4; ++quadrant) {
            float quadHeight = (Collapsible.getQuadHeight(TE, quadrant) - 1.0F) / 15.0F;
            if (quadHeight > maxHeight) {
                maxHeight = quadHeight;
            }
        }

        return maxHeight;
    }

    /**
     * Will generate four boxes with max height represented by quadrant height.
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

        float maxHeight = getBoundsMaxHeight(TE);
        float height = (Collapsible.getQuadHeight(TE, quad) - 1.0F) / 15.0F;

        /* Make quads stagger no more than 0.5F so player can always walk across them. */
        if (maxHeight - height > 0.5F) {
            height = maxHeight - 0.5F;
        }

        float[] finalBounds = { xMin, 0.0F, zMin, xMax, height, zMax };

        return finalBounds;
    }

}
