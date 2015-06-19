package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.collapsible.CollapsibleUtil;

public class Collapsible {

    /**
     * 32-bit data components:
     *
     * [000000000000] [00000] [00000] [00000] [00000]
     * Unused         XZNN    XZNP    XZPN    XZPP
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
     * Sets height of corner as value from 0 to 16.
     */
    public static void setQuadHeight(TEBase TE, int corner, int height)
    {
        if (height < 0 || height > 16) {
            return;
        }

        int data = TE.getData();
        switch (corner) {
            case QUAD_XZNN:
                data &= ~0xf8000;
                data |= height << 15;
                break;
            case QUAD_XZNP:
                data &= ~0x7c00;
                data |= height << 10;
                break;
            case QUAD_XZPN:
                data &= ~0x3e0;
                data |= height << 5;
                break;
            case QUAD_XZPP:
                data &= ~0x1f;
                data |= height;
                break;
        }

        if (TE.getData() != data) {
            TE.setData(data);
        }
    }

    /**
     * Returns height of corner as value from 0 to 16.
     */
    public static int getQuadHeight(final TEBase TE, int corner)
    {
        int height = 0;

        int data = TE.getData();
        switch (corner) {
            case QUAD_XZNN:
                data &= 0xf8000;
                height = data >> 15;
                break;
            case QUAD_XZNP:
                data &= 0x7c00;
                height = data >> 10;
                break;
            case QUAD_XZPN:
                data &= 0x3e0;
                height = data >> 5;
                break;
            case QUAD_XZPP:
                height = data & 0x1f;
                break;
        }

        return height > 16 ? 16 : height;
    }

    public static boolean isSideSolid(final TEBase TE, ForgeDirection side)
    {
        switch (side)
        {
            case DOWN:
                return true;
            case UP:
                return CollapsibleUtil.isMax(TE);
            case NORTH:
                return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) == 32;
            case SOUTH:
                return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) == 32;
            case WEST:
                return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZNN) == 32;
            case EAST:
                return Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPN) + Collapsible.getQuadHeight(TE, Collapsible.QUAD_XZPP) == 32;
            default:
                return true;
        }
    }

}
