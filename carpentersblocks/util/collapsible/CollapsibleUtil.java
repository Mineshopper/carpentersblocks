package carpentersblocks.util.collapsible;

import carpentersblocks.data.Collapsible;
import carpentersblocks.tileentity.TEBase;

public class CollapsibleUtil {

    public static double CENTER_YMAX;

    public static double offset_XZNN;
    public static double offset_XZNP;
    public static double offset_XZPN;
    public static double offset_XZPP;

    /**
     * Fills Y-offsets for each corner and center of block for rendering purposes.
     */
    public static void computeOffsets(TEBase TE)
    {
        offset_XZNN = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) - 1.0D) / 15.0D;
        offset_XZNP = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) - 1.0D) / 15.0D;
        offset_XZPN = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) - 1.0D) / 15.0D;
        offset_XZPP = (Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) - 1.0D) / 15.0D;

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

}
