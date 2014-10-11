package com.carpentersblocks.data;

import com.carpentersblocks.tileentity.TEBase;

public class FlowerPot {

    /**
     * 16-bit data components:
     *
     * [0000] [0000] [00000000]
     * Unused Angle  Unused
     */

    public final static byte COLOR_NATURAL  = 0;
    public final static byte COLOR_ENRICHED = 1;

    /**
     * Returns angle as value from 0 to 15.
     */
    public static int getAngle(TEBase TE)
    {
        return (TE.getData() & 0xf00) >> 8;
    }

    /**
     * Sets angle as value from 0 to 15.
     */
    public static void setAngle(TEBase TE, int angle)
    {
        int temp = (TE.getData() & ~0xf00) | (angle << 8);
        TE.setData(temp);
    }

}
