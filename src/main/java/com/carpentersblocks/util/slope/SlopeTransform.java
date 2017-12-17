package com.carpentersblocks.util.slope;

import com.carpentersblocks.block.BlockCarpentersSlope;
import com.carpentersblocks.block.data.Slope;
import com.carpentersblocks.block.data.Slope.Face;
import com.carpentersblocks.block.data.Slope.Type;
import com.carpentersblocks.tileentity.CbTileEntity;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SlopeTransform {

    /**
     * Transforms wedge slope to connect with adjacent wedge slopes.
     */
    public static int transformWedge(World world, int slopeID, BlockPos blockPos) {
    	BlockPos blockPos_XN = blockPos.west();
    	BlockPos blockPos_XP = blockPos.east();
    	BlockPos blockPos_YN = blockPos.down();
    	BlockPos blockPos_YP = blockPos.up();
    	BlockPos blockPos_ZN = blockPos.north();
    	BlockPos blockPos_ZP = blockPos.south();
        Slope slope_XN = world.getBlockState(blockPos_XN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XN))) : null;
        Slope slope_XP = world.getBlockState(blockPos_XP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XP))) : null;
        Slope slope_YN = world.getBlockState(blockPos_YN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_YN))) : null;
        Slope slope_YP = world.getBlockState(blockPos_YP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_YP))) : null;
        Slope slope_ZN = world.getBlockState(blockPos_ZN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZN))) : null;
        Slope slope_ZP = world.getBlockState(blockPos_ZP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZP))) : null;

        /* Transform into horizontal wedge. */

        int face_bias = slope_YP != null && slope_YP.getFace(EnumFacing.DOWN).equals(Face.WEDGE) ? slope_YP.getFaceBias(EnumFacing.DOWN) : slope_YN != null && slope_YN.getFace(EnumFacing.UP).equals(Face.WEDGE) ? slope_YN.getFaceBias(EnumFacing.UP) : 0;

        switch (face_bias) {
            case Slope.XYNN:
                return Slope.ID_WEDGE_SE;
            case Slope.XYNP:
                return Slope.ID_WEDGE_NE;
            case Slope.XYPN:
                return Slope.ID_WEDGE_SW;
            case Slope.XYPP:
                return Slope.ID_WEDGE_NW;
        }

        /* Transform into corner wedge. */

        Slope slope = Slope.getSlopeById(slopeID);
        Type primary_type = slope.getPrimaryType();

        if (primary_type.equals(Type.WEDGE)) {

            if (slope_ZN != null) {

                if (slope_XN != null) {
                    if (slope_ZN.facings.contains(EnumFacing.WEST) && slope_XN.facings.contains(EnumFacing.NORTH)) {
                        return slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                    if (slope_ZN.facings.contains(EnumFacing.EAST) && slope_XN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                }

                if (slope_XP != null) {
                    if (slope_ZN.facings.contains(EnumFacing.EAST) && slope_XP.facings.contains(EnumFacing.NORTH)) {
                        return slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                    if (slope_ZN.facings.contains(EnumFacing.WEST) && slope_XP.facings.contains(EnumFacing.SOUTH)) {
                        return slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                }

            }

            if (slope_ZP != null) {

                if (slope_XN != null) {
                    if (slope_ZP.facings.contains(EnumFacing.WEST) && slope_XN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
                    }
                    if (slope_ZP.facings.contains(EnumFacing.EAST) && slope_XN.facings.contains(EnumFacing.NORTH)) {
                        return slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                }

                if (slope_XP != null) {
                    if (slope_ZP.facings.contains(EnumFacing.EAST) && slope_XP.facings.contains(EnumFacing.SOUTH)) {
                        return slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_ZP.facings.contains(EnumFacing.WEST) && slope_XP.facings.contains(EnumFacing.NORTH)) {
                        return slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

            }

            if (slope_XN != null) {

                if (slope.facings.contains(EnumFacing.WEST)) {
                    if (slope_XN.facings.contains(EnumFacing.SOUTH) && !slope_XN.facings.contains(EnumFacing.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
                    }
                    if (slope_XN.facings.contains(EnumFacing.NORTH) && !slope_XN.facings.contains(EnumFacing.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                }

                if (slope.facings.contains(EnumFacing.EAST)) {
                    if (slope_XN.facings.contains(EnumFacing.SOUTH) && !slope_XN.facings.contains(EnumFacing.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                    if (slope_XN.facings.contains(EnumFacing.NORTH) && !slope_XN.facings.contains(EnumFacing.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                }

            }

            if (slope_XP != null) {

                if (slope.facings.contains(EnumFacing.WEST)) {
                    if (slope_XP.facings.contains(EnumFacing.SOUTH) && !slope_XP.facings.contains(EnumFacing.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                    if (slope_XP.facings.contains(EnumFacing.NORTH) && !slope_XP.facings.contains(EnumFacing.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

                if (slope.facings.contains(EnumFacing.EAST)) {
                    if (slope_XP.facings.contains(EnumFacing.SOUTH) && !slope_XP.facings.contains(EnumFacing.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_XP.facings.contains(EnumFacing.NORTH) && !slope_XP.facings.contains(EnumFacing.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                }

            }

            if (slope_ZN != null) {

                if (slope.facings.contains(EnumFacing.NORTH)) {
                    if (slope_ZN.facings.contains(EnumFacing.EAST) && !slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                    if (slope_ZN.facings.contains(EnumFacing.WEST) && !slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                }

                if (slope.facings.contains(EnumFacing.SOUTH)) {
                    if (slope_ZN.facings.contains(EnumFacing.EAST) && !slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                    if (slope_ZN.facings.contains(EnumFacing.WEST) && !slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                }

            }

            if (slope_ZP != null) {

                if (slope.facings.contains(EnumFacing.NORTH)) {
                    if (slope_ZP.facings.contains(EnumFacing.EAST) && !slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                    if (slope_ZP.facings.contains(EnumFacing.WEST) && !slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

                if (slope.facings.contains(EnumFacing.SOUTH)) {
                    if (slope_ZP.facings.contains(EnumFacing.EAST) && !slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_ZP.facings.contains(EnumFacing.WEST) && !slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
                    }
                }

            }

        }

        return slopeID;
    }

    /**
     * Transforms adjacent wedge slopes to connect to source wedge slopeID.
     */
    public static void transformAdjacentWedges(World world, int slopeID, BlockPos blockPos) {
    	BlockPos blockPos_XN = blockPos.west();
    	BlockPos blockPos_XP = blockPos.east();
    	BlockPos blockPos_ZN = blockPos.north();
    	BlockPos blockPos_ZP = blockPos.south();
        Slope slope_XN = world.getBlockState(blockPos_XN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XN))) : null;
        Slope slope_XP = world.getBlockState(blockPos_XP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XP))) : null;
        Slope slope_ZN = world.getBlockState(blockPos_ZN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZN))) : null;
        Slope slope_ZP = world.getBlockState(blockPos_ZP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZP))) : null;

        Slope slope = Slope.getSlopeById(slopeID);
        Type primary_type = slope.getPrimaryType();

        if (primary_type.equals(Type.WEDGE)) {

            CbTileEntity TE_XN = slope_XN != null ? (CbTileEntity) world.getTileEntity(blockPos_XN) : null;
            CbTileEntity TE_XP = slope_XP != null ? (CbTileEntity) world.getTileEntity(blockPos_XP) : null;
            CbTileEntity TE_ZN = slope_ZN != null ? (CbTileEntity) world.getTileEntity(blockPos_ZN) : null;
            CbTileEntity TE_ZP = slope_ZP != null ? (CbTileEntity) world.getTileEntity(blockPos_ZP) : null;

            if (slope.facings.contains(EnumFacing.WEST)) {

                if (slope_ZN != null && primary_type.equals(slope_ZN.getPrimaryType()) && slope.isPositive == slope_ZN.isPositive) {
                    if (slope_ZN.facings.contains(EnumFacing.NORTH)) {
                        TE_ZN.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
                    }
                    if (slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        TE_ZN.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
                    }
                }

                if (slope_ZP != null && primary_type.equals(slope_ZP.getPrimaryType()) && slope.isPositive == slope_ZP.isPositive) {
                    if (slope_ZP.facings.contains(EnumFacing.SOUTH)) {
                        TE_ZP.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
                    }
                    if (slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        TE_ZP.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
                    }
                }

            }

            if (slope.facings.contains(EnumFacing.EAST)) {

                if (slope_ZN != null && primary_type.equals(slope_ZN.getPrimaryType()) && slope.isPositive == slope_ZN.isPositive) {
                    if (slope_ZN.facings.contains(EnumFacing.NORTH)) {
                        TE_ZN.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
                    }
                    if (slope_ZN.facings.contains(EnumFacing.SOUTH)) {
                        TE_ZN.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
                    }
                }

                if (slope_ZP != null && primary_type.equals(slope_ZP.getPrimaryType()) && slope.isPositive == slope_ZP.isPositive) {
                    if (slope_ZP.facings.contains(EnumFacing.SOUTH)) {
                        TE_ZP.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
                    }
                    if (slope_ZP.facings.contains(EnumFacing.NORTH)) {
                        TE_ZP.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
                    }
                }

            }

            if (slope.facings.contains(EnumFacing.NORTH)) {

                if (slope_XN != null && primary_type.equals(slope_XN.getPrimaryType()) && slope.isPositive == slope_XN.isPositive) {
                    if (slope_XN.facings.contains(EnumFacing.WEST)) {
                        TE_XN.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
                    }
                    if (slope_XN.facings.contains(EnumFacing.EAST)) {
                        TE_XN.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
                    }
                }

                if (slope_XP != null && primary_type.equals(slope_XP.getPrimaryType()) && slope.isPositive == slope_XP.isPositive) {
                    if (slope_XP.facings.contains(EnumFacing.EAST)) {
                        TE_XP.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
                    }
                    if (slope_XP.facings.contains(EnumFacing.WEST)) {
                        TE_XP.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
                    }
                }

            }

            if (slope.facings.contains(EnumFacing.SOUTH)) {

                if (slope_XN != null && primary_type.equals(slope_XN.getPrimaryType()) && slope.isPositive == slope_XN.isPositive) {
                    if (slope_XN.facings.contains(EnumFacing.WEST)) {
                        TE_XN.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
                    }
                    if (slope_XN.facings.contains(EnumFacing.EAST)) {
                        TE_XN.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
                    }
                }

                if (slope_XP != null && primary_type.equals(slope_XP.getPrimaryType()) && slope.isPositive == slope_XP.isPositive) {
                    if (slope_XP.facings.contains(EnumFacing.EAST)) {
                        TE_XP.setData(slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
                    }
                    if (slope_XP.facings.contains(EnumFacing.WEST)) {
                        TE_XP.setData(slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
                    }
                }

            }

        }
    }

    /**
     * Transforms adjacent prism slopes to connect to source prism slope.
     */
    public static void transformAdjacentPrisms(World world, BlockPos blockPos) {
    	BlockPos blockPos_XN = blockPos.west();
    	BlockPos blockPos_XP = blockPos.east();
    	BlockPos blockPos_ZN = blockPos.north();
    	BlockPos blockPos_ZP = blockPos.south();
        Slope slope_XN = world.getBlockState(blockPos_XN) instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XN))) : null;
        Slope slope_XP = world.getBlockState(blockPos_XP) instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XP))) : null;
        Slope slope_ZN = world.getBlockState(blockPos_ZN) instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZN))) : null;
        Slope slope_ZP = world.getBlockState(blockPos_ZP) instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZP))) : null;

        if (slope_XN != null && slope_XN.getPrimaryType().equals(Type.PRISM)) {
            ((CbTileEntity) world.getTileEntity(blockPos_XN)).setData(transformPrism(world, slope_XN.slopeID, blockPos_XN));
        }
        if (slope_XP != null && slope_XP.getPrimaryType().equals(Type.PRISM)) {
            ((CbTileEntity) world.getTileEntity(blockPos_XP)).setData(transformPrism(world, slope_XP.slopeID, blockPos_XP));
        }
        if (slope_ZN != null && slope_ZN.getPrimaryType().equals(Type.PRISM)) {
            ((CbTileEntity) world.getTileEntity(blockPos_ZN)).setData(transformPrism(world, slope_ZN.slopeID, blockPos_ZN));
        }
        if (slope_ZP != null && slope_ZP.getPrimaryType().equals(Type.PRISM)) {
            ((CbTileEntity) world.getTileEntity(blockPos_ZP)).setData(transformPrism(world, slope_ZP.slopeID, blockPos_ZP));
        }
    }

    /**
     * Transforms prism slope to connect with adjacent prism slopes.
     */
    public static int transformPrism(World world, int slopeID, BlockPos blockPos) {
    	BlockPos blockPos_XN = blockPos.west();
    	BlockPos blockPos_XP = blockPos.east();
    	BlockPos blockPos_ZN = blockPos.north();
    	BlockPos blockPos_ZP = blockPos.south();
        Slope slope_XN = world.getBlockState(blockPos_XN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XN))) : null;
        Slope slope_XP = world.getBlockState(blockPos_XP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_XP))) : null;
        Slope slope_ZN = world.getBlockState(blockPos_ZN).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZN))) : null;
        Slope slope_ZP = world.getBlockState(blockPos_ZP).getBlock() instanceof BlockCarpentersSlope ? Slope.getSlope(((CbTileEntity) world.getTileEntity(blockPos_ZP))) : null;

        boolean prism_XN = slope_XN != null && (slope_XN.getPrimaryType().equals(Type.PRISM) || slope_XN.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_XN.facings.contains(EnumFacing.EAST ));
        boolean prism_XP = slope_XP != null && (slope_XP.getPrimaryType().equals(Type.PRISM) || slope_XP.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_XP.facings.contains(EnumFacing.WEST ));
        boolean prism_ZN = slope_ZN != null && (slope_ZN.getPrimaryType().equals(Type.PRISM) || slope_ZN.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_ZN.facings.contains(EnumFacing.SOUTH));
        boolean prism_ZP = slope_ZP != null && (slope_ZP.getPrimaryType().equals(Type.PRISM) || slope_ZP.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_ZP.facings.contains(EnumFacing.NORTH));

        int prism_set = 0x0000;

        if (prism_XN) {
            prism_set |= 0x0100;
        }
        if (prism_XP) {
            prism_set |= 0x1000;
        }
        if (prism_ZN) {
            prism_set |= 0x0001;
        }
        if (prism_ZP) {
            prism_set |= 0x0010;
        }

        switch (prism_set) {
            case 0x0001:
                return Slope.ID_PRISM_1P_POS_N;
            case 0x0010:
                return Slope.ID_PRISM_1P_POS_S;
            case 0x0100:
                return Slope.ID_PRISM_1P_POS_W;
            case 0x1000:
                return Slope.ID_PRISM_1P_POS_E;
            case 0x0011:
                return Slope.ID_PRISM_2P_POS_NS;
            case 0x0101:
                return Slope.ID_PRISM_2P_POS_NW;
            case 0x0110:
                return Slope.ID_PRISM_2P_POS_SW;
            case 0x1001:
                return Slope.ID_PRISM_2P_POS_NE;
            case 0x1010:
                return Slope.ID_PRISM_2P_POS_SE;
            case 0x1100:
                return Slope.ID_PRISM_2P_POS_WE;
            case 0x0111:
                return Slope.ID_PRISM_3P_POS_NSW;
            case 0x1011:
                return Slope.ID_PRISM_3P_POS_NSE;
            case 0x1101:
                return Slope.ID_PRISM_3P_POS_NWE;
            case 0x1110:
                return Slope.ID_PRISM_3P_POS_SWE;
            case 0x1111:
                return Slope.ID_PRISM_POS_4P;
        }

        return slopeID;
    }

}
