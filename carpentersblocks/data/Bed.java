package carpentersblocks.data;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import carpentersblocks.tileentity.TEBase;
import carpentersblocks.util.BlockProperties;
import carpentersblocks.util.registry.BlockRegistry;

public class Bed {
    
    /**
     * 16-bit data components:
     *
     * [0]     [00]       [00000000]  [0]         [0000]
     * isHead  Direction  Design      isOccupied  Type
     */
    
    public final static byte TYPE_NORMAL = 0;
    
    /**
     * Returns type.
     */
    public final static int getType(TEBase TE)
    {
        return BlockProperties.getMetadata(TE) & 0xf;
    }
    
    /**
     * Sets type.
     */
    public static void setType(TEBase TE, int type)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xfff0;
        temp |= type;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns design.
     */
    public static int getDesign(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x1fe0;
        return temp >> 5;
    }
    
    /**
     * Sets design.
     */
    public static void setDesign(TEBase TE, int design)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xe01f;
        temp |= design << 5;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns whether bed is occupied.
     */
    public static boolean isOccupied(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x10;
        
        return temp != 0;
    }
    
    /**
     * Sets occupation.
     */
    public static void setOccupied(TEBase TE, boolean isOccupied)
    {
        int temp = BlockProperties.getMetadata(TE) & 0xffef;
        
        if (isOccupied) {
            temp |= 1 << 4;
        }
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns TE for opposite piece.
     * Will return null if opposite piece doesn't exist (when creating or destroying block, for instance).
     */
    public static TEBase getOppositeTE(TEBase TE)
    {
        ForgeDirection dir = getDirection(TE);
        int x = TE.xCoord;
        int z = TE.zCoord;
        
        if (isHeadOfBed(TE)) {
            x = TE.xCoord + dir.offsetX;
            z = TE.zCoord + dir.offsetZ;
        } else {
            x = TE.xCoord - dir.offsetX;
            z = TE.zCoord - dir.offsetZ;
        }
        
        World world = TE.getWorldObj();
        
        if (world.getBlock(x, TE.yCoord, z).equals(BlockRegistry.blockCarpentersBed)) {
            return (TEBase) world.getTileEntity(x, TE.yCoord, z);
        } else {
            return null;
        }
    }
    
    /**
     * Returns whether block is head of bed.
     */
    public static boolean isHeadOfBed(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x8000;
        
        return temp != 0;
    }
    
    /**
     * Sets block as head of bed.
     */
    public static void setHeadOfBed(TEBase TE)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x7fff;
        temp |= 1 << 15;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
    /**
     * Returns direction of bed piece.
     */
    public static ForgeDirection getDirection(TEBase TE)
    {
        int facing = BlockProperties.getMetadata(TE) & 0x6000;
        
        return BlockProperties.getDirectionFromFacing(facing >> 13);
    }
    
    /**
     * Sets direction of bed piece.
     * Stored as player facing from 0 to 3.
     */
    public static void setDirection(TEBase TE, int facing)
    {
        int temp = BlockProperties.getMetadata(TE) & 0x9fff;
        temp |= facing << 13;
        
        BlockProperties.setMetadata(TE, temp);
    }
    
}
