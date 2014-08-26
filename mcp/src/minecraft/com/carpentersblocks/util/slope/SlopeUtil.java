package com.carpentersblocks.util.slope;

import net.minecraftforge.common.ForgeDirection;

import com.carpentersblocks.data.Slope;
import com.carpentersblocks.util.registry.FeatureRegistry;

public class SlopeUtil {

    /**
     * Return number of boxes that need to be constructed for slope per pass.
     */
    public int getNumBoxesPerPass(Slope slope)
    {
        switch (slope.type)
        {
            case PRISM:
            case PRISM_1P:
            case PRISM_2P:
            case PRISM_3P:
            case PRISM_4P:
                return FeatureRegistry.slopeSmoothness / 2;
            default:
                return FeatureRegistry.slopeSmoothness;
        }
    }

    /**
     * Return number of passes required for slope box generation.
     */
    public int getNumPasses(Slope slope)
    {
        switch (slope.type)
        {
            case OBLIQUE_EXT:
                return getNumBoxesPerPass(slope);
            case OBLIQUE_INT:
                return 3;
            case WEDGE_INT:
                return 2;
            case PRISM_WEDGE:
                return 2;
            case PRISM:
            case PRISM_1P:
            case PRISM_2P:
            case PRISM_3P:
            case PRISM_4P:
                return 5;
            default:
                return 1;
        }
    }

    /**
     * Will return slope boundaries for all slopes
     */
    public float[] genBounds(Slope slope, int slice, int precision, int pass)
    {
        ++pass;

        // For oblique exterior corners
        float zeroPassOffset = (float) (pass - 1) / getNumPasses(slope);
        float onePassOffset = (float) pass / getNumPasses(slope);

        // Includes 0.0F -> 0.99_F
        float zeroOffset = (float) slice / (float) precision;

        // Includes 0.01_F -> 1.0F
        float oneOffset = (float) (slice + 1) / (float) precision;

        switch (slope.slopeID)
        {
            case Slope.ID_WEDGE_NW:
                return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
            case Slope.ID_WEDGE_SW:
                return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
            case Slope.ID_WEDGE_NE:
                return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
            case Slope.ID_WEDGE_SE:
                return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
            case Slope.ID_WEDGE_POS_N:
                return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
            case Slope.ID_WEDGE_POS_W:
                return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
            case Slope.ID_WEDGE_POS_E:
                return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
            case Slope.ID_WEDGE_POS_S:
                return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
            case Slope.ID_WEDGE_NEG_N:
                return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
            case Slope.ID_WEDGE_NEG_W:
                return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
            case Slope.ID_WEDGE_NEG_E:
                return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
            case Slope.ID_WEDGE_NEG_S:
                return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
            case Slope.ID_WEDGE_INT_POS_NW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
                }
            case Slope.ID_WEDGE_INT_POS_SW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
                }
            case Slope.ID_WEDGE_INT_POS_NE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
                }
            case Slope.ID_WEDGE_INT_POS_SE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
                }
            case Slope.ID_WEDGE_INT_NEG_NW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
                }
            case Slope.ID_WEDGE_INT_NEG_SW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
                }
            case Slope.ID_WEDGE_INT_NEG_NE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
                }
            case Slope.ID_WEDGE_INT_NEG_SE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
                }
            case Slope.ID_WEDGE_EXT_POS_NW:
                return new float[] { zeroOffset, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
            case Slope.ID_WEDGE_EXT_POS_SW:
                return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F - zeroOffset };
            case Slope.ID_WEDGE_EXT_POS_NE:
                return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F - zeroOffset, oneOffset, 1.0F };
            case Slope.ID_WEDGE_EXT_POS_SE:
                return new float[] { 0.0F, 0.0F, 0.0F, 1.0F - zeroOffset, oneOffset, 1.0F - zeroOffset };
            case Slope.ID_WEDGE_EXT_NEG_NW:
                return new float[] { zeroOffset, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
            case Slope.ID_WEDGE_EXT_NEG_SW:
                return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F - zeroOffset };
            case Slope.ID_WEDGE_EXT_NEG_NE:
                return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F - zeroOffset, 1.0F, 1.0F };
            case Slope.ID_WEDGE_EXT_NEG_SE:
                return new float[] { 0.0F, 1.0F - oneOffset, 0.0F, 1.0F - zeroOffset, 1.0F, 1.0F - zeroOffset };
            case Slope.ID_OBL_EXT_POS_NW:
                return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 0.0F, 1.0F - oneOffset * (1.0F - zeroPassOffset), 1.0F, onePassOffset, 1.0F };
            case Slope.ID_OBL_EXT_POS_SW:
                return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 0.0F, 0.0F, 1.0F, onePassOffset, oneOffset * (1.0F - zeroPassOffset) };
            case Slope.ID_OBL_EXT_POS_NE:
                return new float[] { 0.0F, 0.0F, zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), oneOffset * (1.0F - zeroPassOffset), onePassOffset, 1.0F };
            case Slope.ID_OBL_EXT_POS_SE:
                return new float[] { 0.0F, 0.0F, 0.0F, oneOffset * (1.0F - zeroPassOffset), onePassOffset, 1.0F - zeroPassOffset - zeroOffset * (1.0F - zeroPassOffset), };
            case Slope.ID_OBL_INT_POS_NW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
                }
                break;
            case Slope.ID_OBL_INT_POS_SW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
                    case 2:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
                }
                break;
            case Slope.ID_OBL_INT_POS_NE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
                }
                break;
            case Slope.ID_OBL_INT_POS_SE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
                }
                break;
            case Slope.ID_OBL_EXT_NEG_NW:
                return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 1.0F - onePassOffset, 1.0F - oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F, 1.0F };
            case Slope.ID_OBL_EXT_NEG_SW:
                return new float[] { zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), 1.0F - onePassOffset, 0.0F, 1.0F, 1.0F, oneOffset * (1.0F - zeroPassOffset) };
            case Slope.ID_OBL_EXT_NEG_NE:
                return new float[] { 0.0F, 1.0F - onePassOffset, zeroPassOffset + zeroOffset * (1.0F - zeroPassOffset), oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F };
            case Slope.ID_OBL_EXT_NEG_SE:
                return new float[] { 0.0F, 1.0F - onePassOffset, 0.0F, oneOffset * (1.0F - zeroPassOffset), 1.0F, 1.0F - zeroPassOffset - zeroOffset * (1.0F - zeroPassOffset) };
            case Slope.ID_OBL_INT_NEG_NW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 1.0F - oneOffset, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Slope.ID_OBL_INT_NEG_SW:
                switch (pass) {
                    case 1:
                        return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, 1.0F, oneOffset };
                    case 2:
                        return new float[] { zeroOffset, 1.0F - oneOffset, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
                }
                break;
            case Slope.ID_OBL_INT_NEG_NE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, zeroOffset, oneOffset, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 1.0F - oneOffset, zeroOffset, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Slope.ID_OBL_INT_NEG_SE:
                switch (pass) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F, 1.0F - zeroOffset };
                    case 2:
                        return new float[] { 0.0F, zeroOffset, 0.0F, oneOffset, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, zeroOffset, 0.0F, 1.0F, 1.0F, oneOffset };
                }
                break;
            case Slope.ID_PRISM_POS:
                return new float[] { zeroOffset * 0.5F, 0.0F, zeroOffset * 0.5F, 1.0F - zeroOffset * 0.5F, oneOffset * 0.5F, 1.0F - zeroOffset * 0.5F };
            case Slope.ID_PRISM_NEG:
                return new float[] { zeroOffset * 0.5F, 1.0F - oneOffset * 0.5F, zeroOffset * 0.5F, 1.0F - zeroOffset * 0.5F, 1.0F, 1.0F - zeroOffset * 0.5F };
            default: {

                boolean renderPrism = getNumPasses(slope) / precision < 2.0F;

                switch (slope.slopeID) {
                    case Slope.ID_PRISM_WEDGE_POS_N:
                        switch (pass) {
                            case 1:
                                return new float[] { 0.0F, 0.0F, zeroOffset, 1.0F, oneOffset, 1.0F };
                            case 2:
                                if (renderPrism) {
                                    return new float[] { zeroOffset * 0.5F, 0.0F, 0.0F, 1.0F - zeroOffset * 0.5F, oneOffset * 0.5F, 0.5F };
                                }
                        }
                        break;
                    case Slope.ID_PRISM_WEDGE_POS_S:
                        switch (pass) {
                            case 1:
                                return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F - zeroOffset, oneOffset };
                            case 2:
                                if (renderPrism) {
                                    return new float[] { zeroOffset * 0.5F, 0.0F, 0.5F, 1.0F - zeroOffset * 0.5F, oneOffset * 0.5F, 1.0F };
                                }
                        }
                        break;
                    case Slope.ID_PRISM_WEDGE_POS_W:
                        switch (pass) {
                            case 1:
                                return new float[] { zeroOffset, 0.0F, 0.0F, 1.0F, oneOffset, 1.0F };
                            case 2:
                                if (renderPrism) {
                                    return new float[] { 0.0F, 0.0F, zeroOffset * 0.5F, 0.5F, oneOffset * 0.5F, 1.0F - zeroOffset * 0.5F };
                                }
                        }
                        break;
                    case Slope.ID_PRISM_WEDGE_POS_E:
                        switch (pass) {
                            case 1:
                                return new float[] { 0.0F, 0.0F, 0.0F, oneOffset, 1.0F - zeroOffset, 1.0F };
                            case 2:
                                if (renderPrism) {
                                    return new float[] { 0.5F, 0.0F, zeroOffset * 0.5F, 1.0F, oneOffset * 0.5F, 1.0F - zeroOffset * 0.5F };
                                }
                        }
                        break;
                    default:

                        switch (pass) {
                            case 1:
                                if (!slope.equals(Slope.PRISM_4P_POS)) {
                                    return genBounds(Slope.PRISM_POS, slice, precision, pass);
                                } else {
                                    return null;
                                }
                            case 2:
                                if (slope.facings.contains(ForgeDirection.NORTH)) {
                                    return new float[] { zeroOffset * 0.5F, 0.0F, 0.0F, 1.0F - zeroOffset * 0.5F, oneOffset * 0.5F, 0.5F };
                                }
                                break;
                            case 3:
                                if (slope.facings.contains(ForgeDirection.SOUTH)) {
                                    return new float[] { zeroOffset * 0.5F, 0.0F, 0.5F, 1.0F - zeroOffset * 0.5F, oneOffset * 0.5F, 1.0F };
                                }
                                break;
                            case 4:
                                if (slope.facings.contains(ForgeDirection.WEST)) {
                                    return new float[] { 0.0F, 0.0F, zeroOffset * 0.5F, 0.5F, oneOffset * 0.5F, 1.0F - zeroOffset * 0.5F };
                                }
                                break;
                            case 5:
                                if (slope.facings.contains(ForgeDirection.EAST)) {
                                    return new float[] { 0.5F, 0.0F, zeroOffset * 0.5F, 1.0F, oneOffset * 0.5F, 1.0F - zeroOffset * 0.5F };
                                }
                                break;
                        }
                }

            }
        }

        return null;
    }

}
