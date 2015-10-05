package com.carpentersblocks.data;

import net.minecraftforge.common.util.ForgeDirection;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.collapsible.CollapsibleUtil;

public class Collapsible implements ISided {

    /**
     * 32-bit data components:
     *
     * [000000000] [00000] [00000] [00000] [00000] [000]
     * Unused      XZNN    XZNP    XZPN    XZPP    Dir
     */

    public final static Collapsible INSTANCE = new Collapsible();

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
     * Sets quad depth as value from 0 to 16.
     */
    public static void setQuadDepth(TEBase TE, int quad, int depth, boolean markDirty)
    {
        if (depth < 0 || depth > 16) {
            return;
        }

        int data = TE.getData();
        switch (quad) {
            case QUAD_XZNN:
                data &= ~0x7c0000;
                data |= depth << 18;
                break;
            case QUAD_XZNP:
                data &= ~0x3e000;
                data |= depth << 13;
                break;
            case QUAD_XZPN:
                data &= ~0x1f00;
                data |= depth << 8;
                break;
            case QUAD_XZPP:
                data &= ~0xf8;
                data |= depth << 3;
                break;
        }

        if (TE.getData() != data) {
            TE.setData(data);
            if (markDirty) {
                TE.markDirty();
            }
        }
    }

    /**
     * Returns quad depth as value from 0 to 16.
     */
    public static int getQuadDepth(final TEBase TE, int quad)
    {
        int steps = 0;

        int data = TE.getData();
        switch (quad) {
            case QUAD_XZNN:
                data &= 0x7c0000;
                steps = data >> 18;
                break;
            case QUAD_XZNP:
                data &= 0x3e000;
                steps = data >> 13;
                break;
            case QUAD_XZPN:
                data &= 0x1f00;
                steps = data >> 8;
                break;
            case QUAD_XZPP:
                data &= 0xf8;
                steps = data >> 3;
                break;
        }

        return steps > 16 ? 16 : steps;
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
                return Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNN) + Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPN) == 32;
            case SOUTH:
                return Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPP) == 32;
            case WEST:
                return Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNP) + Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZNN) == 32;
            case EAST:
                return Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPN) + Collapsible.getQuadDepth(TE, Collapsible.QUAD_XZPP) == 32;
            default:
                return true;
        }
    }

    public boolean match(TEBase TE_1, TEBase TE_2)
    {
        return isPositive(TE_1) == isPositive(TE_2);
    }

    @Override
    public boolean setDirection(TEBase TE, ForgeDirection dir)
    {
        int temp = (TE.getData() & ~0x7) | dir.ordinal();
        return TE.setData(temp);
    }

    @Override
    public ForgeDirection getDirection(TEBase TE)
    {
        return ForgeDirection.getOrientation(TE.getData() & 0x7);
    }

    public boolean isPositive(TEBase TE)
    {
        return getDirection(TE).equals(ForgeDirection.UP);
    }

}
