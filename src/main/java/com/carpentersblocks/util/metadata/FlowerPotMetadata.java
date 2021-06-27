package com.carpentersblocks.util.metadata;

import com.carpentersblocks.nbt.CbTileEntity;

public class FlowerPotMetadata {

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
    public static int getAngle(CbTileEntity cbTileEntity) {
        return (cbTileEntity.getCbMetadata() & 0xf00) >> 8;
    }

    /**
     * Sets angle as value from 0 to 15.
     */
    public static void setAngle(CbTileEntity cbTileEntity, int angle) {
        int temp = (cbTileEntity.getCbMetadata() & ~0xf00) | (angle << 8);
        cbTileEntity.setCbMetadata(temp);
    }
	
}
