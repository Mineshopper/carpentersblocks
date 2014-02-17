package carpentersblocks.util.stairs;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.block.BlockCarpentersStairs;
import carpentersblocks.data.Stairs;
import carpentersblocks.data.Stairs;
import carpentersblocks.data.Stairs.Type;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class StairsTransform {
    
    /**
     * Transforms stairs to connect with adjacent stairs.
     */
    public static int transformStairs(World world, int stairsID, int x, int y, int z)
    {
        Block block_XN = world.getBlock(x - 1, y, z);
        Block block_XP = world.getBlock(x + 1, y, z);
        Block block_YN = world.getBlock(x, y - 1, z);
        Block block_YP = world.getBlock(x, y + 1, z);
        Block block_ZN = world.getBlock(x, y, z - 1);
        Block block_ZP = world.getBlock(x, y, z + 1);
        
        Stairs stairs_XN = block_XN != null && block_XN instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x - 1, y, z))] : null;
        Stairs stairs_XP = block_XP != null && block_XP instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x + 1, y, z))] : null;
        Stairs stairs_YN = block_YN != null && block_YN instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y - 1, z))] : null;
        Stairs stairs_YP = block_YP != null && block_YP instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y + 1, z))] : null;
        Stairs stairs_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y, z - 1))] : null;
        Stairs stairs_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y, z + 1))] : null;
        
        /* Transform into normal side. */
        
        if (stairs_YN != null) {
            if (stairs_YN.stairsType.equals(Type.NORMAL_SIDE)) {
                return stairs_YN.stairsID;
            }
        }
        if (stairs_YP != null) {
            if (stairs_YP.stairsType.equals(Type.NORMAL_SIDE)) {
                return stairs_YP.stairsID;
            }
        }
        
        /* Transform into normal corner. */
        
        Stairs stairs = Stairs.stairsList[stairsID];
        
        if (stairs_ZN != null) {
            
            if (stairs_XN != null) {
                if (stairs_ZN.facings.contains(ForgeDirection.WEST) && stairs_XN.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_XN.isPositive && stairs_ZN.isPositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
                }
                if (stairs_ZN.facings.contains(ForgeDirection.EAST) && stairs_XN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_XN.isPositive && stairs_ZN.isPositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
                }
            }
            
            if (stairs_XP != null) {
                if (stairs_ZN.facings.contains(ForgeDirection.EAST) && stairs_XP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_XP.isPositive && stairs_ZN.isPositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
                }
                if (stairs_ZN.facings.contains(ForgeDirection.WEST) && stairs_XP.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_XP.isPositive && stairs_ZN.isPositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
                }
            }
            
        }
        
        if (stairs_ZP != null) {
            
            if (stairs_XN != null) {
                if (stairs_ZP.facings.contains(ForgeDirection.WEST) && stairs_XN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_XN.isPositive && stairs_ZP.isPositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
                }
                if (stairs_ZP.facings.contains(ForgeDirection.EAST) && stairs_XN.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_XN.isPositive && stairs_ZP.isPositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
                }
            }
            
            if (stairs_XP != null) {
                if (stairs_ZP.facings.contains(ForgeDirection.EAST) && stairs_XP.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_XP.isPositive && stairs_ZP.isPositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
                }
                if (stairs_ZP.facings.contains(ForgeDirection.WEST) && stairs_XP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_XP.isPositive && stairs_ZP.isPositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
                }
            }
            
        }
        
        if (stairs_XN != null) {
            
            if (stairs.facings.contains(ForgeDirection.WEST)) {
                if (stairs_XN.facings.contains(ForgeDirection.SOUTH) && !stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    return stairs_XN.isPositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
                }
                if (stairs_XN.facings.contains(ForgeDirection.NORTH) && !stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    return stairs_XN.isPositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
                }
            }
            
            if (stairs.facings.contains(ForgeDirection.EAST)) {
                if (stairs_XN.facings.contains(ForgeDirection.SOUTH) && !stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    return stairs_XN.isPositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
                }
                if (stairs_XN.facings.contains(ForgeDirection.NORTH) && !stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    return stairs_XN.isPositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
                }
            }
            
        }
        
        if (stairs_XP != null) {
            
            if (stairs.facings.contains(ForgeDirection.WEST)) {
                if (stairs_XP.facings.contains(ForgeDirection.SOUTH) && !stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    return stairs_XP.isPositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
                }
                if (stairs_XP.facings.contains(ForgeDirection.NORTH) && !stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    return stairs_XP.isPositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
                }
            }
            
            if (stairs.facings.contains(ForgeDirection.EAST)) {
                if (stairs_XP.facings.contains(ForgeDirection.SOUTH) && !stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    return stairs_XP.isPositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
                }
                if (stairs_XP.facings.contains(ForgeDirection.NORTH) && !stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    return stairs_XP.isPositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
                }
            }
            
        }
        
        if (stairs_ZN != null) {
            
            if (stairs.facings.contains(ForgeDirection.NORTH)) {
                if (stairs_ZN.facings.contains(ForgeDirection.EAST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_ZN.isPositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE;
                }
                if (stairs_ZN.facings.contains(ForgeDirection.WEST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_ZN.isPositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW;
                }
            }
            
            if (stairs.facings.contains(ForgeDirection.SOUTH)) {
                if (stairs_ZN.facings.contains(ForgeDirection.EAST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_ZN.isPositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE;
                }
                if (stairs_ZN.facings.contains(ForgeDirection.WEST) && !stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    return stairs_ZN.isPositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW;
                }
            }
            
        }
        
        if (stairs_ZP != null) {
            
            if (stairs.facings.contains(ForgeDirection.NORTH)) {
                if (stairs_ZP.facings.contains(ForgeDirection.EAST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_ZP.isPositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE;
                }
                if (stairs_ZP.facings.contains(ForgeDirection.WEST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_ZP.isPositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW;
                }
            }
            
            if (stairs.facings.contains(ForgeDirection.SOUTH)) {
                if (stairs_ZP.facings.contains(ForgeDirection.EAST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_ZP.isPositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE;
                }
                if (stairs_ZP.facings.contains(ForgeDirection.WEST) && !stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    return stairs_ZP.isPositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW;
                }
            }
            
        }
        
        return stairsID;
    }
    
    /**
     * Transforms adjacent stairs to connect to source stairs.
     */
    public static void transformAdjacentStairs(World world, int stairsID, int x, int y, int z)
    {
        Block block_XN = world.getBlock(x - 1, y, z);
        Block block_XP = world.getBlock(x + 1, y, z);
        Block block_ZN = world.getBlock(x, y, z - 1);
        Block block_ZP = world.getBlock(x, y, z + 1);
        
        Stairs stairs_XN = block_XN != null && block_XN instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x - 1, y, z))] : null;
        Stairs stairs_XP = block_XP != null && block_XP instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x + 1, y, z))] : null;
        Stairs stairs_ZN = block_ZN != null && block_ZN instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y, z - 1))] : null;
        Stairs stairs_ZP = block_ZP != null && block_ZP instanceof BlockCarpentersStairs ? Stairs.stairsList[BlockProperties.getData((TEBase) world.getTileEntity(x, y, z + 1))] : null;
        
        Stairs stairs = Stairs.stairsList[stairsID];
        
        TEBase TE_XN = stairs_XN != null ? (TEBase) world.getTileEntity(x - 1, y, z) : null;
        TEBase TE_XP = stairs_XP != null ? (TEBase) world.getTileEntity(x + 1, y, z) : null;
        TEBase TE_ZN = stairs_ZN != null ? (TEBase) world.getTileEntity(x, y, z - 1) : null;
        TEBase TE_ZP = stairs_ZP != null ? (TEBase) world.getTileEntity(x, y, z + 1) : null;
        
        if (stairs.facings.contains(ForgeDirection.WEST)) {
            
            if (stairs_ZN != null && stairs.isPositive == stairs_ZN.isPositive) {
                if (stairs_ZN.facings.contains(ForgeDirection.NORTH)) {
                    BlockProperties.setData(TE_ZN, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
                }
                if (stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    BlockProperties.setData(TE_ZN, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
                }
            }
            
            if (stairs_ZP != null && stairs.isPositive == stairs_ZP.isPositive) {
                if (stairs_ZP.facings.contains(ForgeDirection.SOUTH)) {
                    BlockProperties.setData(TE_ZP, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
                }
                if (stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    BlockProperties.setData(TE_ZP, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
                }
            }
            
        }
        
        if (stairs.facings.contains(ForgeDirection.EAST)) {
            
            if (stairs_ZN != null && stairs.isPositive == stairs_ZN.isPositive) {
                if (stairs_ZN.facings.contains(ForgeDirection.NORTH)) {
                    BlockProperties.setData(TE_ZN, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
                }
                if (stairs_ZN.facings.contains(ForgeDirection.SOUTH)) {
                    BlockProperties.setData(TE_ZN, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
                }
            }
            
            if (stairs_ZP != null && stairs.isPositive == stairs_ZP.isPositive) {
                if (stairs_ZP.facings.contains(ForgeDirection.SOUTH)) {
                    BlockProperties.setData(TE_ZP, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
                }
                if (stairs_ZP.facings.contains(ForgeDirection.NORTH)) {
                    BlockProperties.setData(TE_ZP, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
                }
            }
            
        }
        
        if (stairs.facings.contains(ForgeDirection.NORTH)) {
            
            if (stairs_XN != null && stairs.isPositive == stairs_XN.isPositive) {
                if (stairs_XN.facings.contains(ForgeDirection.WEST)) {
                    BlockProperties.setData(TE_XN, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_NW : Stairs.ID_NORMAL_EXT_NEG_NW);
                }
                if (stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    BlockProperties.setData(TE_XN, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_NE : Stairs.ID_NORMAL_INT_NEG_NE);
                }
            }
            
            if (stairs_XP != null && stairs.isPositive == stairs_XP.isPositive) {
                if (stairs_XP.facings.contains(ForgeDirection.EAST)) {
                    BlockProperties.setData(TE_XP, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_NE : Stairs.ID_NORMAL_EXT_NEG_NE);
                }
                if (stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    BlockProperties.setData(TE_XP, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_NW : Stairs.ID_NORMAL_INT_NEG_NW);
                }
            }
            
        }
        
        if (stairs.facings.contains(ForgeDirection.SOUTH)) {
            
            if (stairs_XN != null && stairs.isPositive == stairs_XN.isPositive) {
                if (stairs_XN.facings.contains(ForgeDirection.WEST)) {
                    BlockProperties.setData(TE_XN, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_SW : Stairs.ID_NORMAL_EXT_NEG_SW);
                }
                if (stairs_XN.facings.contains(ForgeDirection.EAST)) {
                    BlockProperties.setData(TE_XN, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_SE : Stairs.ID_NORMAL_INT_NEG_SE);
                }
            }
            
            if (stairs_XP != null && stairs.isPositive == stairs_XP.isPositive) {
                if (stairs_XP.facings.contains(ForgeDirection.EAST)) {
                    BlockProperties.setData(TE_XP, stairs.isPositive ? Stairs.ID_NORMAL_EXT_POS_SE : Stairs.ID_NORMAL_EXT_NEG_SE);
                }
                if (stairs_XP.facings.contains(ForgeDirection.WEST)) {
                    BlockProperties.setData(TE_XP, stairs.isPositive ? Stairs.ID_NORMAL_INT_POS_SW : Stairs.ID_NORMAL_INT_NEG_SW);
                }
            }
            
        }
    }
    
}
