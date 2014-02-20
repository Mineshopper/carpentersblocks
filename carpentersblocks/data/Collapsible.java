package carpentersblocks.data;

import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;

public class Collapsible {
    
    /**
     * 16-bit data components:
     *
     * [0000]  [0000]  [0000]  [0000]
     * XZNN    XZNP    XZPN    XZPP
     */
    
    public final static int QUAD_XZNN = 0;
    public final static int QUAD_XZNP = 1;
    public final static int QUAD_XZPN = 2;
    public final static int QUAD_XZPP = 3;
    
    /**
     * Returns corner number.
     */
    public static int getQuad(double hitX, double hitZ)
    {
        int xOffset = (int) Math.round(hitX);
        int zOffset = (int) Math.round(hitZ);
        
        if (xOffset == 0) {
            if (zOffset == 0) {
                return QUAD_XZNN;
            } else {
                return QUAD_XZNP;
            }
        } else {
            if (zOffset == 0) {
                return QUAD_XZPN;
            } else {
                return QUAD_XZPP;
            }
        }
    }
    
    /**
     * Sets height of corner as value from 1 to 16.
     * Will correct out-of-range values automatically, and won't cause block update if height doesn't change.
     */
    public static void setQuadHeight(TEBase TE, int corner, int height)
    {
        int data = BlockProperties.getMetadata(TE);
        --height;
        
        if (height >= 0 && height < 16)
        {
            switch (corner) {
                case QUAD_XZNN:
                    data &= 0xfff;
                    data |= 15 - height << 12;
                    break;
                case QUAD_XZNP:
                    data &= 0xf0ff;
                    data |= 15 - height << 8;
                    break;
                case QUAD_XZPN:
                    data &= 0xff0f;
                    data |= 15 - height << 4;
                    break;
                case QUAD_XZPP:
                    data &= 0xfff0;
                    data |= 15 - height;
                    break;
            }
            
            if (BlockProperties.getMetadata(TE) != data) {
                BlockProperties.setMetadata(TE, data);
            }
        }
    }
    
    /**
     * Returns height of corner as value from 1 to 16.
     */
    public static int getQuadHeight(final TEBase TE, int corner)
    {
        int data = BlockProperties.getMetadata(TE);
        
        switch (corner) {
            case QUAD_XZNN:
                data &= 0xf000;
                return 16 - (data >> 12);
            case QUAD_XZNP:
                data &= 0x0f00;
                return 16 - (data >> 8);
            case QUAD_XZPN:
                data &= 0x00f0;
                return 16 - (data >> 4);
            case QUAD_XZPP:
                return 16 - (data &= 0xf);
            default:
                return 16;
        }
    }
    
}
