package carpentersblocks.util.stairs;

import carpentersblocks.data.Stairs;

public class StairsUtil {

    /**
     * Will return stairs boundaries.
     */
    public float[] genBounds(int box, Stairs stairs)
    {
        ++box;

        switch (stairs.stairsID)
        {
            case Stairs.ID_NORMAL_SW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_NW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_NE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_SE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_POS_N:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_POS_W:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_POS_E:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_POS_S:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_NEG_N:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_NEG_W:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_NEG_E:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_NEG_S:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.0F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_NW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_SW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_NE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_INT_POS_SE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 3:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_NW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_SW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
                    case 3:
                        return new float[] { 0.0F, 0.5F, 0.5F, 0.5F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_NE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_INT_NEG_SE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 3:
                        return new float[] { 0.5F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_NW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_SW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_NE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_POS_SE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_NW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_SW:
                switch (box) {
                    case 1:
                        return new float[] { 0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.5F, 0.5F, 0.5F, 1.0F, 1.0F, 1.0F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_NE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.5F, 0.5F, 1.0F, 1.0F };
                    case 2:
                        return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.5F, 0.0F, 0.5F, 1.0F, 0.5F };
                }
                break;
            case Stairs.ID_NORMAL_EXT_NEG_SE:
                switch (box) {
                    case 1:
                        return new float[] { 0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 0.5F };
                    case 2:
                        return new float[] { 0.5F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F };
                    case 3:
                        return new float[] { 0.0F, 0.5F, 0.5F, 0.5F, 1.0F, 1.0F };
                }
                break;
        }

        return null;
    }

}
