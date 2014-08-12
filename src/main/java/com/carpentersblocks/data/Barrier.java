package com.carpentersblocks.data;

import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class Barrier {

    /**
     * 16-bit data components:
     *
     * [00000000000]  [0]   [0000]
     * Unused         Post  Type
     */

    public final static byte TYPE_VANILLA    = 0;
    public final static byte TYPE_VANILLA_X1 = 1;
    public final static byte TYPE_VANILLA_X2 = 2;
    public final static byte TYPE_VANILLA_X3 = 3;
    public final static byte TYPE_PICKET     = 4;
    public final static byte TYPE_SHADOWBOX  = 5;
    public final static byte TYPE_WALL       = 6;

    public final static byte NO_POST  = 0;
    public final static byte HAS_POST = 1;

    /**
     * Returns data.
     */
    public static int getType(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0xf;
    }

    /**
     * Sets data (vanilla, picket, etc).
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff0;
        temp |= type;

        BlockProperties.setMetadata(TE, temp);
    }

    /**
     * Returns post bit from data.
     */
    public static int getPost(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) >> 4;
    }

    /**
     * Sets post bit.
     */
    public static void setPost(TEBase TE, int post)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffef;
        temp |= post << 4;

        BlockProperties.setMetadata(TE, temp);
    }

}
