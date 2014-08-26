package com.carpentersblocks.util.slope;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import com.carpentersblocks.block.BlockCarpentersSlope;
import com.carpentersblocks.data.Slope;
import com.carpentersblocks.data.Slope.Face;
import com.carpentersblocks.data.Slope.Type;
import com.carpentersblocks.tileentity.TEBase;
import com.carpentersblocks.util.BlockProperties;

public class SlopeTransform {

    /**
     * Transforms wedge slope to connect with adjacent wedge slopes.
     */
    public static int transformWedge(World world, int slopeID, int x, int y, int z)
    {
        Block block_XN = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block block_XP = Block.blocksList[world.getBlockId(x + 1, y, z)];
        Block block_YN = Block.blocksList[world.getBlockId(x, y - 1, z)];
        Block block_YP = Block.blocksList[world.getBlockId(x, y + 1, z)];
        Block block_ZN = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block block_ZP = Block.blocksList[world.getBlockId(x, y, z + 1)];

        Slope slope_XN = block_XN != null && block_XN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x - 1, y, z))] : null;
        Slope slope_XP = block_XP != null && block_XP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x + 1, y, z))] : null;
        Slope slope_YN = block_YN != null && block_YN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y - 1, z))] : null;
        Slope slope_YP = block_YP != null && block_YP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y + 1, z))] : null;
        Slope slope_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z - 1))] : null;
        Slope slope_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z + 1))] : null;

        /* Transform into horizontal wedge. */

        int face_bias = slope_YP != null && slope_YP.getFace(ForgeDirection.DOWN).equals(Face.WEDGE) ? slope_YP.getFaceBias(ForgeDirection.DOWN) : slope_YN != null && slope_YN.getFace(ForgeDirection.UP).equals(Face.WEDGE) ? slope_YN.getFaceBias(ForgeDirection.UP) : 0;

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

        Slope slope = Slope.slopesList[slopeID];
        Type primary_type = slope.getPrimaryType();

        if (primary_type.equals(Type.WEDGE)) {

            if (slope_ZN != null) {

                if (slope_XN != null) {
                    if (slope_ZN.facings.contains(ForgeDirection.WEST) && slope_XN.facings.contains(ForgeDirection.NORTH)) {
                        return slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.EAST) && slope_XN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_XN.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                }

                if (slope_XP != null) {
                    if (slope_ZN.facings.contains(ForgeDirection.EAST) && slope_XP.facings.contains(ForgeDirection.NORTH)) {
                        return slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.WEST) && slope_XP.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_XP.isPositive && slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                }

            }

            if (slope_ZP != null) {

                if (slope_XN != null) {
                    if (slope_ZP.facings.contains(ForgeDirection.WEST) && slope_XN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.EAST) && slope_XN.facings.contains(ForgeDirection.NORTH)) {
                        return slope_XN.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                }

                if (slope_XP != null) {
                    if (slope_ZP.facings.contains(ForgeDirection.EAST) && slope_XP.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.WEST) && slope_XP.facings.contains(ForgeDirection.NORTH)) {
                        return slope_XP.isPositive && slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

            }

            if (slope_XN != null) {

                if (slope.facings.contains(ForgeDirection.WEST)) {
                    if (slope_XN.facings.contains(ForgeDirection.SOUTH) && !slope_XN.facings.contains(ForgeDirection.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW;
                    }
                    if (slope_XN.facings.contains(ForgeDirection.NORTH) && !slope_XN.facings.contains(ForgeDirection.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                }

                if (slope.facings.contains(ForgeDirection.EAST)) {
                    if (slope_XN.facings.contains(ForgeDirection.SOUTH) && !slope_XN.facings.contains(ForgeDirection.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                    if (slope_XN.facings.contains(ForgeDirection.NORTH) && !slope_XN.facings.contains(ForgeDirection.EAST)) {
                        return slope_XN.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                }

            }

            if (slope_XP != null) {

                if (slope.facings.contains(ForgeDirection.WEST)) {
                    if (slope_XP.facings.contains(ForgeDirection.SOUTH) && !slope_XP.facings.contains(ForgeDirection.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                    if (slope_XP.facings.contains(ForgeDirection.NORTH) && !slope_XP.facings.contains(ForgeDirection.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

                if (slope.facings.contains(ForgeDirection.EAST)) {
                    if (slope_XP.facings.contains(ForgeDirection.SOUTH) && !slope_XP.facings.contains(ForgeDirection.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_XP.facings.contains(ForgeDirection.NORTH) && !slope_XP.facings.contains(ForgeDirection.WEST)) {
                        return slope_XP.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                }

            }

            if (slope_ZN != null) {

                if (slope.facings.contains(ForgeDirection.NORTH)) {
                    if (slope_ZN.facings.contains(ForgeDirection.EAST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE;
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.WEST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW;
                    }
                }

                if (slope.facings.contains(ForgeDirection.SOUTH)) {
                    if (slope_ZN.facings.contains(ForgeDirection.EAST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE;
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.WEST) && !slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        return slope_ZN.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW;
                    }
                }

            }

            if (slope_ZP != null) {

                if (slope.facings.contains(ForgeDirection.NORTH)) {
                    if (slope_ZP.facings.contains(ForgeDirection.EAST) && !slope_ZP.facings.contains(ForgeDirection.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE;
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.WEST) && !slope_ZP.facings.contains(ForgeDirection.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW;
                    }
                }

                if (slope.facings.contains(ForgeDirection.SOUTH)) {
                    if (slope_ZP.facings.contains(ForgeDirection.EAST) && !slope_ZP.facings.contains(ForgeDirection.NORTH)) {
                        return slope_ZP.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE;
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.WEST) && !slope_ZP.facings.contains(ForgeDirection.NORTH)) {
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
    public static void transformAdjacentWedges(World world, int slopeID, int x, int y, int z)
    {
        Block block_XN = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block block_XP = Block.blocksList[world.getBlockId(x + 1, y, z)];
        Block block_ZN = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block block_ZP = Block.blocksList[world.getBlockId(x, y, z + 1)];

        Slope slope_XN = block_XN != null && block_XN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x - 1, y, z))] : null;
        Slope slope_XP = block_XP != null && block_XP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x + 1, y, z))] : null;
        Slope slope_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z - 1))] : null;
        Slope slope_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z + 1))] : null;

        Slope slope = Slope.slopesList[slopeID];
        Type primary_type = slope.getPrimaryType();

        if (primary_type.equals(Type.WEDGE)) {

            TEBase TE_XN = slope_XN != null ? (TEBase) world.getBlockTileEntity(x - 1, y, z) : null;
            TEBase TE_XP = slope_XP != null ? (TEBase) world.getBlockTileEntity(x + 1, y, z) : null;
            TEBase TE_ZN = slope_ZN != null ? (TEBase) world.getBlockTileEntity(x, y, z - 1) : null;
            TEBase TE_ZP = slope_ZP != null ? (TEBase) world.getBlockTileEntity(x, y, z + 1) : null;

            if (slope.facings.contains(ForgeDirection.WEST)) {

                if (slope_ZN != null && primary_type.equals(slope_ZN.getPrimaryType()) && slope.isPositive == slope_ZN.isPositive) {
                    if (slope_ZN.facings.contains(ForgeDirection.NORTH)) {
                        BlockProperties.setMetadata(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        BlockProperties.setMetadata(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
                    }
                }

                if (slope_ZP != null && primary_type.equals(slope_ZP.getPrimaryType()) && slope.isPositive == slope_ZP.isPositive) {
                    if (slope_ZP.facings.contains(ForgeDirection.SOUTH)) {
                        BlockProperties.setMetadata(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.NORTH)) {
                        BlockProperties.setMetadata(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
                    }
                }

            }

            if (slope.facings.contains(ForgeDirection.EAST)) {

                if (slope_ZN != null && primary_type.equals(slope_ZN.getPrimaryType()) && slope.isPositive == slope_ZN.isPositive) {
                    if (slope_ZN.facings.contains(ForgeDirection.NORTH)) {
                        BlockProperties.setMetadata(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
                    }
                    if (slope_ZN.facings.contains(ForgeDirection.SOUTH)) {
                        BlockProperties.setMetadata(TE_ZN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
                    }
                }

                if (slope_ZP != null && primary_type.equals(slope_ZP.getPrimaryType()) && slope.isPositive == slope_ZP.isPositive) {
                    if (slope_ZP.facings.contains(ForgeDirection.SOUTH)) {
                        BlockProperties.setMetadata(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
                    }
                    if (slope_ZP.facings.contains(ForgeDirection.NORTH)) {
                        BlockProperties.setMetadata(TE_ZP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
                    }
                }

            }

            if (slope.facings.contains(ForgeDirection.NORTH)) {

                if (slope_XN != null && primary_type.equals(slope_XN.getPrimaryType()) && slope.isPositive == slope_XN.isPositive) {
                    if (slope_XN.facings.contains(ForgeDirection.WEST)) {
                        BlockProperties.setMetadata(TE_XN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NW : Slope.ID_WEDGE_EXT_NEG_NW);
                    }
                    if (slope_XN.facings.contains(ForgeDirection.EAST)) {
                        BlockProperties.setMetadata(TE_XN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NE : Slope.ID_WEDGE_INT_NEG_NE);
                    }
                }

                if (slope_XP != null && primary_type.equals(slope_XP.getPrimaryType()) && slope.isPositive == slope_XP.isPositive) {
                    if (slope_XP.facings.contains(ForgeDirection.EAST)) {
                        BlockProperties.setMetadata(TE_XP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_NE : Slope.ID_WEDGE_EXT_NEG_NE);
                    }
                    if (slope_XP.facings.contains(ForgeDirection.WEST)) {
                        BlockProperties.setMetadata(TE_XP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_NW : Slope.ID_WEDGE_INT_NEG_NW);
                    }
                }

            }

            if (slope.facings.contains(ForgeDirection.SOUTH)) {

                if (slope_XN != null && primary_type.equals(slope_XN.getPrimaryType()) && slope.isPositive == slope_XN.isPositive) {
                    if (slope_XN.facings.contains(ForgeDirection.WEST)) {
                        BlockProperties.setMetadata(TE_XN, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SW : Slope.ID_WEDGE_EXT_NEG_SW);
                    }
                    if (slope_XN.facings.contains(ForgeDirection.EAST)) {
                        BlockProperties.setMetadata(TE_XN, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SE : Slope.ID_WEDGE_INT_NEG_SE);
                    }
                }

                if (slope_XP != null && primary_type.equals(slope_XP.getPrimaryType()) && slope.isPositive == slope_XP.isPositive) {
                    if (slope_XP.facings.contains(ForgeDirection.EAST)) {
                        BlockProperties.setMetadata(TE_XP, slope.isPositive ? Slope.ID_WEDGE_EXT_POS_SE : Slope.ID_WEDGE_EXT_NEG_SE);
                    }
                    if (slope_XP.facings.contains(ForgeDirection.WEST)) {
                        BlockProperties.setMetadata(TE_XP, slope.isPositive ? Slope.ID_WEDGE_INT_POS_SW : Slope.ID_WEDGE_INT_NEG_SW);
                    }
                }

            }

        }
    }

    /**
     * Transforms adjacent prism slopes to connect to source prism slope.
     */
    public static void transformAdjacentPrisms(World world, int x, int y, int z)
    {
        Block block_XN = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block block_XP = Block.blocksList[world.getBlockId(x + 1, y, z)];
        Block block_ZN = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block block_ZP = Block.blocksList[world.getBlockId(x, y, z + 1)];

        Slope slope_XN = block_XN != null && block_XN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x - 1, y, z))] : null;
        Slope slope_XP = block_XP != null && block_XP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x + 1, y, z))] : null;
        Slope slope_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z - 1))] : null;
        Slope slope_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z + 1))] : null;

        if (slope_XN != null && slope_XN.getPrimaryType().equals(Type.PRISM)) {
            BlockProperties.setMetadata((TEBase) world.getBlockTileEntity(x - 1, y, z), transformPrism(world, slope_XN.slopeID, x - 1, y, z));
        }
        if (slope_XP != null && slope_XP.getPrimaryType().equals(Type.PRISM)) {
            BlockProperties.setMetadata((TEBase) world.getBlockTileEntity(x + 1, y, z), transformPrism(world, slope_XP.slopeID, x + 1, y, z));
        }
        if (slope_ZN != null && slope_ZN.getPrimaryType().equals(Type.PRISM)) {
            BlockProperties.setMetadata((TEBase) world.getBlockTileEntity(x, y, z - 1), transformPrism(world, slope_ZN.slopeID, x, y, z - 1));
        }
        if (slope_ZP != null && slope_ZP.getPrimaryType().equals(Type.PRISM)) {
            BlockProperties.setMetadata((TEBase) world.getBlockTileEntity(x, y, z + 1), transformPrism(world, slope_ZP.slopeID, x, y, z + 1));
        }
    }

    /**
     * Transforms prism slope to connect with adjacent prism slopes.
     */
    public static int transformPrism(World world, int slopeID, int x, int y, int z)
    {
        Block block_XN = Block.blocksList[world.getBlockId(x - 1, y, z)];
        Block block_XP = Block.blocksList[world.getBlockId(x + 1, y, z)];
        Block block_ZN = Block.blocksList[world.getBlockId(x, y, z - 1)];
        Block block_ZP = Block.blocksList[world.getBlockId(x, y, z + 1)];

        Slope slope_XN = block_XN != null && block_XN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x - 1, y, z))] : null;
        Slope slope_XP = block_XP != null && block_XP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x + 1, y, z))] : null;
        Slope slope_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z - 1))] : null;
        Slope slope_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersSlope ? Slope.slopesList[BlockProperties.getMetadata((TEBase) world.getBlockTileEntity(x, y, z + 1))] : null;

        boolean prism_XN = slope_XN != null && (slope_XN.getPrimaryType().equals(Type.PRISM) || slope_XN.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_XN.facings.contains(ForgeDirection.EAST ));
        boolean prism_XP = slope_XP != null && (slope_XP.getPrimaryType().equals(Type.PRISM) || slope_XP.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_XP.facings.contains(ForgeDirection.WEST ));
        boolean prism_ZN = slope_ZN != null && (slope_ZN.getPrimaryType().equals(Type.PRISM) || slope_ZN.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_ZN.facings.contains(ForgeDirection.SOUTH));
        boolean prism_ZP = slope_ZP != null && (slope_ZP.getPrimaryType().equals(Type.PRISM) || slope_ZP.getPrimaryType().equals(Type.PRISM_WEDGE) && slope_ZP.facings.contains(ForgeDirection.NORTH));

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
